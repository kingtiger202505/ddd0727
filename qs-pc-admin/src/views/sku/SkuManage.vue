<template>
  <div class="sku-manage-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>商品 SKU 管理</span>
          <el-button type="primary" @click="openSkuDialog()">新增 SKU</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.productName" placeholder="输入商品名称" />
        </el-form-item>
        <el-form-item label="SKU 编码">
          <el-input v-model="searchForm.skuCode" placeholder="输入 SKU 编码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadSkuList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- SKU 列表 -->
      <el-table :data="skuList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="productName" label="商品名称" min-width="150" />
        <el-table-column prop="skuCode" label="SKU 编码" width="150" />
        <el-table-column label="规格详情" min-width="200">
          <template #default="{ row }">
            <el-tag v-for="(value, key) in row.specs" :key="key" size="small" style="margin-right: 5px;">
              {{ key }}:{{ value }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="售价 (元)" width="100" />
        <el-table-column prop="stock" label="库存" width="100">
          <template #default="{ row }">
            <span :class="{ 'stock-warning': row.stock < 10 }">{{ row.stock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="editSku(row)">编辑</el-button>
            <el-button link type="warning" size="small" @click="adjustStock(row)">调库存</el-button>
            <el-button link type="danger" size="small" @click="deleteSku(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="loadSkuList"
          @current-change="loadSkuList"
        />
      </div>
    </el-card>

    <!-- 新增/编辑 SKU 对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="skuForm" :rules="skuRules" ref="skuFormRef" label-width="100px">
        <el-form-item label="所属商品" prop="productId">
          <el-select v-model="skuForm.productId" placeholder="选择商品" style="width: 100%" @change="onProductChange">
            <el-option v-for="item in productOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="SKU 编码" prop="skuCode">
          <el-input v-model="skuForm.skuCode" placeholder="自动生成或手动输入" />
        </el-form-item>

        <el-form-item label="规格组合" prop="specsJson">
          <div v-for="(spec, index) in specInputs" :key="index" class="spec-item">
            <el-input v-model="spec.name" placeholder="规格名 (如颜色)" style="width: 120px; margin-right: 10px;" />
            <el-input v-model="spec.value" placeholder="规格值 (如红色)" style="width: 120px; margin-right: 10px;" />
            <el-button type="danger" icon="Delete" circle size="small" @click="removeSpec(index)" v-if="index > 0"/>
          </div>
          <el-button type="primary" link @click="addSpec" style="margin-top: 10px;">+ 添加规格项</el-button>
        </el-form-item>

        <el-form-item label="售价" prop="price">
          <el-input-number v-model="skuForm.price" :min="0" :precision="2" :step="0.1" />
        </el-form-item>

        <el-form-item label="初始库存" prop="stock" v-if="!skuForm.id">
          <el-input-number v-model="skuForm.stock" :min="0" :step="1" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="skuForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitSkuForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 库存调整对话框 -->
    <el-dialog v-model="stockDialogVisible" title="调整库存" width="400px">
      <el-form :model="stockForm" label-width="80px">
        <el-form-item label="当前库存">
          <span>{{ currentSku.stock }}</span>
        </el-form-item>
        <el-form-item label="调整数量" prop="changeNum">
          <el-input-number v-model="stockForm.changeNum" :min="-currentSku.stock" placeholder="正数入库，负数出库" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="stockForm.remark" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stockDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStockAdjust">确认调整</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';
import productApi from '@/api/product';
import skuApi from '@/api/sku';
import inventoryApi from '@/api/inventory';

// 状态定义
const loading = ref(false);
const dialogVisible = ref(false);
const stockDialogVisible = ref(false);
const dialogTitle = ref('新增 SKU');
const skuFormRef = ref(null);
const currentSku = ref({});

const searchForm = reactive({
  productName: '',
  skuCode: ''
});

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

const skuList = ref([]);
const productOptions = ref([]);

const skuForm = reactive({
  id: null,
  productId: null,
  skuCode: '',
  specsJson: '{}',
  price: 0,
  stock: 0,
  status: 1
});

const stockForm = reactive({
  skuId: null,
  changeNum: 0,
  remark: ''
});

const specInputs = ref([{ name: '', value: '' }]);

const skuRules = {
  productId: [{ required: true, message: '请选择商品', trigger: 'change' }],
  skuCode: [{ required: true, message: '请输入 SKU 编码', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
};

// 方法定义
const loadSkuList = async () => {
  loading.value = true;
  try {
    const res = await skuApi.getList({
      page: pagination.currentPage,
      size: pagination.pageSize,
      ...searchForm
    });
    skuList.value = res.data.records || [];
    pagination.total = res.data.total || 0;
    
    // 解析规格 JSON 用于显示
    skuList.value.forEach(item => {
      if (item.specsJson) {
        item.specs = JSON.parse(item.specsJson);
      } else {
        item.specs = {};
      }
    });
  } catch (error) {
    ElMessage.error('加载 SKU 列表失败');
  } finally {
    loading.value = false;
  }
};

const loadProducts = async () => {
  try {
    const res = await productApi.getList({ size: 1000 }); // 获取所有商品
    productOptions.value = res.data.records || [];
  } catch (error) {
    ElMessage.error('加载商品列表失败');
  }
};

const resetSearch = () => {
  searchForm.productName = '';
  searchForm.skuCode = '';
  loadSkuList();
};

const openSkuDialog = () => {
  dialogTitle.value = '新增 SKU';
  resetSkuForm();
  dialogVisible.value = true;
};

const editSku = (row) => {
  dialogTitle.value = '编辑 SKU';
  Object.assign(skuForm, row);
  
  // 解析规格回显
  if (row.specsJson) {
    const specs = JSON.parse(row.specsJson);
    specInputs.value = Object.keys(specs).map(key => ({
      name: key,
      value: specs[key]
    }));
  } else {
    specInputs.value = [{ name: '', value: '' }];
  }
  
  dialogVisible.value = true;
};

const resetSkuForm = () => {
  if (skuFormRef.value) skuFormRef.value.resetFields();
  skuForm.id = null;
  skuForm.productId = null;
  skuForm.skuCode = '';
  skuForm.specsJson = '{}';
  skuForm.price = 0;
  skuForm.stock = 0;
  skuForm.status = 1;
  specInputs.value = [{ name: '', value: '' }];
};

const addSpec = () => {
  specInputs.value.push({ name: '', value: '' });
};

const removeSpec = (index) => {
  specInputs.value.splice(index, 1);
};

const onProductChange = (productId) => {
  // 可选：根据商品 ID 自动获取默认规格模板
  const product = productOptions.value.find(p => p.id === productId);
  if (product && !skuForm.skuCode) {
    // 简单生成规则：SPU 代码 + 时间戳
    skuForm.skuCode = `${product.code}-${Date.now().toString().slice(-6)}`;
  }
};

const submitSkuForm = async () => {
  if (!skuFormRef.value) return;
  
  await skuFormRef.value.validate(async (valid) => {
    if (!valid) return;

    // 组装规格 JSON
    const specsObj = {};
    specInputs.value.forEach(item => {
      if (item.name && item.value) {
        specsObj[item.name] = item.value;
      }
    });
    skuForm.specsJson = JSON.stringify(specsObj);

    try {
      if (skuForm.id) {
        await skuApi.update(skuForm);
        ElMessage.success('更新成功');
      } else {
        await skuApi.create(skuForm);
        ElMessage.success('创建成功');
      }
      dialogVisible.value = false;
      loadSkuList();
    } catch (error) {
      ElMessage.error(error.response?.data?.msg || '操作失败');
    }
  });
};

const adjustStock = (row) => {
  currentSku.value = row;
  stockForm.skuId = row.id;
  stockForm.changeNum = 0;
  stockForm.remark = '';
  stockDialogVisible.value = true;
};

const submitStockAdjust = async () => {
  if (stockForm.changeNum === 0) {
    ElMessage.warning('调整数量不能为 0');
    return;
  }

  try {
    await inventoryApi.adjustStock({
      skuId: stockForm.skuId,
      changeNum: stockForm.changeNum,
      remark: stockForm.remark
    });
    ElMessage.success('库存调整成功');
    stockDialogVisible.value = false;
    loadSkuList();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '库存调整失败');
  }
};

const deleteSku = (id) => {
  ElMessageBox.confirm('确定要删除该 SKU 吗？删除后无法恢复', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await skuApi.delete(id);
      ElMessage.success('删除成功');
      loadSkuList();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

onMounted(() => {
  loadSkuList();
  loadProducts();
});
</script>

<style scoped>
.sku-manage-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.stock-warning {
  color: #f56c6c;
  font-weight: bold;
}
.spec-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
</style>
