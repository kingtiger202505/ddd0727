// 全局变量
let currentUser = null;
const API_BASE = '/api';

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    checkLoginStatus();
});

// 检查登录状态
async function checkLoginStatus() {
    try {
        const response = await fetch(`${API_BASE}/users/current`, {
            method: 'GET',
            credentials: 'include'
        });
        
        if (response.ok) {
            const user = await response.json();
            currentUser = user;
            showQuotationSection();
            loadQuotations();
        } else {
            showLoginSection();
        }
    } catch (error) {
        console.error('检查登录状态失败:', error);
        showLoginSection();
    }
}

// 显示登录区域
function showLoginSection() {
    document.getElementById('login-section').style.display = 'block';
    document.getElementById('register-section').style.display = 'none';
    document.getElementById('quotation-section').style.display = 'none';
    document.getElementById('quotation-form-section').style.display = 'none';
}

// 显示注册区域
function showRegister() {
    document.getElementById('login-section').style.display = 'none';
    document.getElementById('register-section').style.display = 'block';
}

// 返回登录
function showLogin() {
    document.getElementById('login-section').style.display = 'block';
    document.getElementById('register-section').style.display = 'none';
}

// 显示报价管理区域
function showQuotationSection() {
    document.getElementById('login-section').style.display = 'none';
    document.getElementById('register-section').style.display = 'none';
    document.getElementById('quotation-section').style.display = 'block';
}

// 处理登录
async function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        const response = await fetch(`${API_BASE}/users/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            const result = await response.json();
            if (result.success) {
                currentUser = result.data;
                alert('登录成功！');
                showQuotationSection();
                loadQuotations();
            } else {
                alert(result.message || '登录失败');
            }
        } else {
            alert('登录失败，请检查用户名和密码');
        }
    } catch (error) {
        console.error('登录错误:', error);
        alert('网络错误，请稍后重试');
    }
}

// 处理注册
async function handleRegister(event) {
    event.preventDefault();
    
    const username = document.getElementById('regUsername').value;
    const password = document.getElementById('regPassword').value;
    const email = document.getElementById('regEmail').value;
    
    try {
        const response = await fetch(`${API_BASE}/users/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, email })
        });
        
        const result = await response.json();
        
        if (response.ok && result.success) {
            alert('注册成功，请登录！');
            showLogin();
        } else {
            alert(result.message || '注册失败');
        }
    } catch (error) {
        console.error('注册错误:', error);
        alert('网络错误，请稍后重试');
    }
}

// 退出登录
async function logout() {
    try {
        await fetch(`${API_BASE}/users/logout`, {
            method: 'POST',
            credentials: 'include'
        });
        currentUser = null;
        alert('已退出登录');
        showLoginSection();
    } catch (error) {
        console.error('退出登录错误:', error);
        showLoginSection();
    }
}

// 加载报价列表
async function loadQuotations() {
    try {
        const response = await fetch(`${API_BASE}/quotations/list`, {
            method: 'GET',
            credentials: 'include'
        });
        
        if (response.ok) {
            const result = await response.json();
            renderQuotationList(result.data || []);
        } else {
            console.error('加载报价列表失败');
        }
    } catch (error) {
        console.error('加载报价列表错误:', error);
    }
}

// 渲染报价列表
function renderQuotationList(quotations) {
    const tbody = document.getElementById('quotation-list');
    tbody.innerHTML = '';
    
    if (!quotations || quotations.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;">暂无报价数据</td></tr>';
        return;
    }
    
    quotations.forEach(q => {
        const tr = document.createElement('tr');
        const statusClass = getStatusClass(q.status);
        const statusText = getStatusText(q.status);
        
        tr.innerHTML = `
            <td>${q.quotationNo || '-'}</td>
            <td>${q.customerName || '-'}</td>
            <td>${q.productName || '-'}</td>
            <td>${q.quantity || 0}</td>
            <td>¥${(q.totalAmount || 0).toFixed(2)}</td>
            <td class="${statusClass}">${statusText}</td>
            <td>${formatDate(q.createTime)}</td>
            <td class="action-buttons">
                <button class="btn btn-warning" onclick="editQuotation('${q.id}')">编辑</button>
                <button class="btn btn-success" onclick="approveQuotation('${q.id}')">批准</button>
                <button class="btn btn-danger" onclick="rejectQuotation('${q.id}')">拒绝</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// 获取状态样式类
function getStatusClass(status) {
    switch(status) {
        case 'PENDING': return 'status-pending';
        case 'APPROVED': return 'status-approved';
        case 'REJECTED': return 'status-rejected';
        default: return '';
    }
}

// 获取状态文本
function getStatusText(status) {
    switch(status) {
        case 'PENDING': return '待处理';
        case 'APPROVED': return '已批准';
        case 'REJECTED': return '已拒绝';
        default: return status;
    }
}

// 格式化日期
function formatDate(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    return date.toLocaleString('zh-CN');
}

// 显示新建报价表单
function showNewQuotationForm() {
    document.getElementById('form-title').textContent = '新建报价';
    document.getElementById('quotation-id').value = '';
    document.getElementById('customer-name').value = '';
    document.getElementById('product-name').value = '';
    document.getElementById('quantity').value = '';
    document.getElementById('price').value = '';
    document.getElementById('quotation-form-section').style.display = 'flex';
}

// 编辑报价
async function editQuotation(id) {
    try {
        const response = await fetch(`${API_BASE}/quotations/detail/${id}`, {
            method: 'GET',
            credentials: 'include'
        });
        
        if (response.ok) {
            const result = await response.json();
            const q = result.data;
            
            document.getElementById('form-title').textContent = '编辑报价';
            document.getElementById('quotation-id').value = q.id;
            document.getElementById('customer-name').value = q.customerName || '';
            document.getElementById('product-name').value = q.productName || '';
            document.getElementById('quantity').value = q.quantity || 0;
            document.getElementById('price').value = q.unitPrice || 0;
            document.getElementById('quotation-form-section').style.display = 'flex';
        }
    } catch (error) {
        console.error('获取报价详情错误:', error);
        alert('获取报价详情失败');
    }
}

// 关闭表单
function closeForm() {
    document.getElementById('quotation-form-section').style.display = 'none';
}

// 保存报价
async function handleSaveQuotation(event) {
    event.preventDefault();
    
    const id = document.getElementById('quotation-id').value;
    const customerName = document.getElementById('customer-name').value;
    const productName = document.getElementById('product-name').value;
    const quantity = parseInt(document.getElementById('quantity').value);
    const unitPrice = parseFloat(document.getElementById('price').value);
    
    const data = {
        customerName,
        productName,
        quantity,
        unitPrice
    };
    
    try {
        let url, method;
        if (id) {
            url = `${API_BASE}/quotations/update`;
            method = 'PUT';
            data.id = id;
        } else {
            url = `${API_BASE}/quotations/create`;
            method = 'POST';
        }
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify(data)
        });
        
        const result = await response.json();
        
        if (response.ok && result.success) {
            alert('保存成功！');
            closeForm();
            loadQuotations();
        } else {
            alert(result.message || '保存失败');
        }
    } catch (error) {
        console.error('保存报价错误:', error);
        alert('网络错误，请稍后重试');
    }
}

// 批准报价
async function approveQuotation(id) {
    if (!confirm('确定要批准该报价吗？')) return;
    
    try {
        const response = await fetch(`${API_BASE}/quotations/approve/${id}`, {
            method: 'POST',
            credentials: 'include'
        });
        
        const result = await response.json();
        
        if (response.ok && result.success) {
            alert('批准成功！');
            loadQuotations();
        } else {
            alert(result.message || '批准失败');
        }
    } catch (error) {
        console.error('批准报价错误:', error);
        alert('网络错误，请稍后重试');
    }
}

// 拒绝报价
async function rejectQuotation(id) {
    if (!confirm('确定要拒绝该报价吗？')) return;
    
    try {
        const response = await fetch(`${API_BASE}/quotations/reject/${id}`, {
            method: 'POST',
            credentials: 'include'
        });
        
        const result = await response.json();
        
        if (response.ok && result.success) {
            alert('拒绝成功！');
            loadQuotations();
        } else {
            alert(result.message || '拒绝失败');
        }
    } catch (error) {
        console.error('拒绝报价错误:', error);
        alert('网络错误，请稍后重试');
    }
}
