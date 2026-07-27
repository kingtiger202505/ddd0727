<template>
  <div class="quotation-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报价审批</span>
        </div>
      </template>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="报价 ID" width="100" />
        <el-table-column prop="quotationNo" label="报价单号" />
        <el-table-column prop="userId" label="用户 ID" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="requestPrice" label="请求价格" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">详情</el-button>
            <el-button size="small" type="success" @click="handleApprove(scope.row, true)">通过</el-button>
            <el-button size="small" type="danger" @click="handleApprove(scope.row, false)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="counterPriceDialogVisible" title="还价审批" width="400px">
      <el-form :model="counterPriceForm" label-width="80px">
        <el-form-item label="还价价格">
          <el-input-number v-model="counterPriceForm.counterPrice" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="counterPriceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitCounterPrice">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getQuotationList, approveQuotation } from '@/api/order'

const tableData = ref([])
const counterPriceDialogVisible = ref(false)

const counterPriceForm = reactive({
  quotationId: null,
  counterPrice: 0
})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  try {
    const res = await getQuotationList({ page: 1, size: 10 })
    tableData.value = res.data?.list || []
  } catch (error) {
    console.error(error)
  }
}

const getStatusType = (status) => {
  const types = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger',
    COUNTER_OFFER: 'primary'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    PENDING: '待审批',
    APPROVED: '已通过',
    REJECTED: '已拒绝',
    COUNTER_OFFER: '已还价'
  }
  return texts[status] || status
}

const handleView = (row) => {
  ElMessage.info('查看报价详情：' + row.quotationNo)
}

const handleApprove = (row, approved) => {
  if (!approved) {
    counterPriceForm.quotationId = row.id
    counterPriceForm.counterPrice = 0
    counterPriceDialogVisible.value = true
  } else {
    submitApprove(row.id, true, 0)
  }
}

const handleSubmitCounterPrice = () => {
  submitApprove(counterPriceForm.quotationId, false, counterPriceForm.counterPrice)
}

const submitApprove = async (id, approved, counterPrice) => {
  try {
    await approveQuotation(id, approved, counterPrice)
    ElMessage.success(approved ? '审批通过' : '已还价')
    counterPriceDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.quotation-manage {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
