<template>
  <div class="inventory-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>库存管理</span>
        </div>
      </template>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="skuId" label="SKU ID" />
        <el-table-column prop="warehouseName" label="仓库名称" />
        <el-table-column prop="quantity" label="库存数量" />
        <el-table-column prop="lockQuantity" label="锁定数量" />
        <el-table-column prop="availableQuantity" label="可用数量" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleAdjust(scope.row)">调整库存</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="调整库存" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="SKU ID">
          <el-input v-model="form.skuId" disabled />
        </el-form-item>
        <el-form-item label="仓库">
          <el-input v-model="form.warehouseName" disabled />
        </el-form-item>
        <el-form-item label="调整数量">
          <el-input-number v-model="form.changeQty" :min="-9999" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getInventoryList, updateInventory } from '@/api/inventory'

const tableData = ref([])
const dialogVisible = ref(false)

const form = reactive({
  id: null,
  skuId: null,
  warehouseName: '',
  changeQty: 0,
  remark: ''
})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  try {
    const res = await getInventoryList({ page: 1, size: 10 })
    tableData.value = res.data?.list || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdjust = (row) => {
  form.id = row.id
  form.skuId = row.skuId
  form.warehouseName = row.warehouseName
  form.changeQty = 0
  form.remark = ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await updateInventory({
      inventoryId: form.id,
      changeQty: form.changeQty,
      remark: form.remark
    })
    ElMessage.success('调整成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.inventory-manage {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
