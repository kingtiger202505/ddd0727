import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue')
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/product',
    name: 'Product',
    component: () => import('@/views/product/ProductManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/sku',
    name: 'Sku',
    component: () => import('@/views/sku/SkuManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/inventory',
    name: 'Inventory',
    component: () => import('@/views/inventory/InventoryManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('@/views/order/OrderManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/quotation',
    name: 'Quotation',
    component: () => import('@/views/quotation/QuotationManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/role',
    name: 'Role',
    component: () => import('@/views/role/RoleManage.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
