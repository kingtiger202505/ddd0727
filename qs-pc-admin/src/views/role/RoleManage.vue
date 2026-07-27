<template>
  <div class="role-manage-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>角色权限管理</span>
          <el-button type="primary" @click="openRoleDialog()">新增角色</el-button>
        </div>
      </template>

      <!-- 角色列表 -->
      <el-table :data="roleList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="editRole(row)">编辑</el-button>
            <el-button link type="warning" size="small" @click="openPermissionDialog(row)">分配权限</el-button>
            <el-button link type="danger" size="small" @click="deleteRole(row.id)">删除</el-button>
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
          @size-change="loadRoleList"
          @current-change="loadRoleList"
        />
      </div>
    </el-card>

    <!-- 新增/编辑角色对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="roleForm" :rules="roleRules" ref="roleFormRef" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码 (如：ADMIN)" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRoleForm">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog v-model="permissionDialogVisible" title="分配权限" width="600px">
      <el-form>
        <el-form-item label="当前角色">
          <span>{{ currentRole.roleName }}</span>
        </el-form-item>
        <el-form-item label="权限配置">
          <el-tree
            ref="permissionTreeRef"
            :data="permissionTree"
            :props="{ children: 'children', label: 'permissionName' }"
            show-checkbox
            node-key="id"
            default-expand-all
            :default-checked-keys="checkedPermissionIds"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPermissions">保存权限</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import roleApi from '@/api/role';
import permissionApi from '@/api/permission';

// 状态定义
const loading = ref(false);
const dialogVisible = ref(false);
const permissionDialogVisible = ref(false);
const dialogTitle = ref('新增角色');
const roleFormRef = ref(null);
const permissionTreeRef = ref(null);
const currentRole = ref({});

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

const roleList = ref([]);
const permissionTree = ref([]);
const checkedPermissionIds = ref([]);

const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  status: 1
});

const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
};

// 方法定义
const loadRoleList = async () => {
  loading.value = true;
  try {
    const res = await roleApi.getList({
      page: pagination.currentPage,
      size: pagination.pageSize
    });
    roleList.value = res.data.records || [];
    pagination.total = res.data.total || 0;
  } catch (error) {
    ElMessage.error('加载角色列表失败');
  } finally {
    loading.value = false;
  }
};

const loadPermissions = async () => {
  try {
    const res = await permissionApi.getTree();
    permissionTree.value = res.data || [];
  } catch (error) {
    ElMessage.error('加载权限树失败');
  }
};

const openRoleDialog = () => {
  dialogTitle.value = '新增角色';
  resetRoleForm();
  dialogVisible.value = true;
};

const editRole = (row) => {
  dialogTitle.value = '编辑角色';
  Object.assign(roleForm, row);
  dialogVisible.value = true;
};

const resetRoleForm = () => {
  if (roleFormRef.value) roleFormRef.value.resetFields();
  roleForm.id = null;
  roleForm.roleName = '';
  roleForm.roleCode = '';
  roleForm.description = '';
  roleForm.status = 1;
};

const submitRoleForm = async () => {
  if (!roleFormRef.value) return;
  
  await roleFormRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      if (roleForm.id) {
        await roleApi.update(roleForm);
        ElMessage.success('更新成功');
      } else {
        await roleApi.create(roleForm);
        ElMessage.success('创建成功');
      }
      dialogVisible.value = false;
      loadRoleList();
    } catch (error) {
      ElMessage.error(error.response?.data?.msg || '操作失败');
    }
  });
};

const openPermissionDialog = async (row) => {
  currentRole.value = row;
  permissionDialogVisible.value = true;
  checkedPermissionIds.value = [];
  
  // 加载该角色的权限 ID 列表
  try {
    const res = await roleApi.getPermissions(row.id);
    checkedPermissionIds.value = res.data.map(p => p.permissionId || p.id);
    
    await nextTick();
    // 展开所有节点并勾选
    if (permissionTreeRef.value) {
      permissionTreeRef.value.setCheckedKeys(checkedPermissionIds.value);
    }
  } catch (error) {
    ElMessage.error('加载角色权限失败');
  }
};

const submitPermissions = async () => {
  if (!permissionTreeRef.value) return;
  
  const checkedKeys = permissionTreeRef.value.getCheckedKeys();
  const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys();
  // 合并选中节点和半选节点（包含父节点）
  const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys];
  
  try {
    await roleApi.savePermissions({
      roleId: currentRole.value.id,
      permissionIds: allCheckedKeys
    });
    ElMessage.success('权限分配成功');
    permissionDialogVisible.value = false;
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '权限分配失败');
  }
};

const deleteRole = (id) => {
  ElMessageBox.confirm('确定要删除该角色吗？删除后关联用户将失去此角色权限', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await roleApi.delete(id);
      ElMessage.success('删除成功');
      loadRoleList();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

onMounted(() => {
  loadRoleList();
  loadPermissions();
});
</script>

<style scoped>
.role-manage-container {
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
</style>
