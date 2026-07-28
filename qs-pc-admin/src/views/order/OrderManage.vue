<template>
  <div class="order-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
        </div>
      </template>

      <!-- 多条件筛选搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 140px;">
            <el-option label="待支付" value="PENDING" />
            <el-option label="已支付" value="PAID" />
            <el-option label="已发货" value="SHIPPED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="买家 ID">
          <el-input v-model="searchForm.buyerId" placeholder="买家 ID" clearable style="width: 120px;" />
        </el-form-item>
        <el-form-item label="卖家 ID">
          <el-input v-model="searchForm.sellerId" placeholder="卖家 ID" clearable style="width: 120px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 订单表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="订单 ID" width="90" />
        <el-table-column prop="orderNo" label="订单号" min-width="180" />
        <el-table-column prop="buyerId" label="买家 ID" width="100" />
        <el-table-column prop="sellerId" label="卖家 ID" width="100" />
        <el-table-column prop="totalAmount" label="订单金额" width="120" />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="170" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">详情</el-button>
            <el-button size="small" type="primary" @click="handleUpdateStatus(scope.row)">更新状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 订单状态变更弹窗 -->
    <el-dialog v-model="statusDialogVisible" title="更新订单状态" width="400px">
      <el-form :model="statusForm" label-width="80px">
        <el-form-item label="订单状态">
          <el-select v-model="statusForm.status" placeholder="请选择状态">
            <el-option label="待支付" value="PENDING" />
            <el-option label="已支付" value="PAID" />
            <el-option label="已发货" value="SHIPPED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitStatus">确定</el-button>
      </template>
    </el-dialog>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="600px">
      <el-descriptions :column="2" border v-if="currentOrder">
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)">{{ getStatusText(currentOrder.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="买家 ID">{{ currentOrder.buyerId }}</el-descriptions-item>
        <el-descriptions-item label="卖家 ID">{{ currentOrder.sellerId }}</el-descriptions-item>
        <el-descriptions-item label="总金额">￥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentOrder.createdAt }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderList, updateOrderStatus } from '@/api/order'

const tableData = ref([])
const statusDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentOrder = ref(null)

const searchForm = reactive({
  orderNo: '',
  status: '',
  buyerId: '',
  sellerId: ''
})

const statusForm = reactive({
  orderId: null,
  status: ''
})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  try {
    const params = {
      orderNo: searchForm.orderNo || undefined,
      status: searchForm.status || undefined,
      buyerId: searchForm.buyerId || undefined,
      sellerId: searchForm.sellerId || undefined
    }
    const res = await getOrderList(params)
    tableData.value = res.data || []
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  }
}

const handleSearch = () => {
  loadData()
}

const handleReset = () => {
  searchForm.orderNo = ''
  searchForm.status = ''
  searchForm.buyerId = ''
  searchForm.sellerId = ''
  loadData()
}

const handleView = (row) => {
  currentOrder.value = row
  detailDialogVisible.value = true
}

const handleUpdateStatus = (row) => {
  statusForm.orderId = row.id
  statusForm.status = row.status
  statusDialogVisible.value = true
}

const handleSubmitStatus = async () => {
  try {
    await updateOrderStatus(statusForm.orderId, statusForm.status)
    ElMessage.success('状态更新成功')
    statusDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const getStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    PAID: 'success',
    SHIPPED: 'info',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    PENDING: '待支付',
    PAID: '已支付',
    SHIPPED: '已发货',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}
</script>

<style scoped>
.search-form {
  margin-bottom: 16px;
}
</style>
