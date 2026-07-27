<template>
  <div class="order-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
        </div>
      </template>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="订单 ID" width="100" />
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="userId" label="用户 ID" />
        <el-table-column prop="totalAmount" label="订单金额" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">详情</el-button>
            <el-button size="small" type="primary" @click="handleUpdateStatus(scope.row)">更新状态</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderList, updateOrderStatus } from '@/api/order'

const tableData = ref([])
const statusDialogVisible = ref(false)

const statusForm = reactive({
  orderId: null,
  status: ''
})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  try {
    const res = await getOrderList({ page: 1, size: 10 })
    tableData.value = res.data?.list || []
  } catch (error) {
    console.error(error)
  }
}

const getStatusType = (status) => {
  const types = {
    PENDING: 'warning',
    PAID: 'primary',
    SHIPPED: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    PENDING: '待支付',
    PAID: '已支付',
    SHIPPED: '已发货',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return texts[status] || status
}

const handleView = (row) => {
  ElMessage.info('查看订单详情：' + row.orderNo)
}

const handleUpdateStatus = (row) => {
  statusForm.orderId = row.id
  statusForm.status = row.status
  statusDialogVisible.value = true
}

const handleSubmitStatus = async () => {
  try {
    await updateOrderStatus(statusForm.orderId, statusForm.status)
    ElMessage.success('更新成功')
    statusDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.order-manage {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
