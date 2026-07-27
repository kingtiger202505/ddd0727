<template>
  <div class="product-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
          <el-button type="primary" @click="handleAdd">新增商品</el-button>
        </div>
      </template>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="basePrice" label="基础价格" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
        <el-form-item label="基础价格">
          <el-input-number v-model="form.basePrice" :min="0" />
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
import { getProductList, createProduct, updateProduct, deleteProduct } from '@/api/product'

const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: '',
  description: '',
  basePrice: 0
})

onMounted(() => {
  loadData()
})

const loadData = async () => {
  try {
    const res = await getProductList({ page: 1, size: 10 })
    tableData.value = res.data?.list || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增商品'
  form.id = null
  form.name = ''
  form.description = ''
  form.basePrice = 0
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑商品'
  form.id = row.id
  form.name = row.name
  form.description = row.description
  form.basePrice = row.basePrice
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await deleteProduct(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await updateProduct(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createProduct(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.product-manage {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
