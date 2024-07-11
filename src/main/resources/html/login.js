document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    const statusMessage = document.getElementById('statusMessage');

    // 添加事件监听器以处理表单提交
    form.addEventListener('submit', function(event) {
        event.preventDefault(); // 阻止默认的表单提交行为

        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        const username = usernameInput.value.trim();
        const password = passwordInput.value.trim();

        if (username === '' || password === '') {
            // 如果用户名或密码为空，显示错误消息
            statusMessage.textContent = 'Username or password cannot be empty.';
            return;
        } else if (!validateEmail(username)) {
            // 如果邮箱格式不正确，显示错误消息
            statusMessage.textContent = 'Invalid email format.';
            return;
        }

        // 如果所有检查都通过，则可以继续进行登录操作
        // 这里你可以添加你的登录逻辑，例如调用API进行身份验证
        statusMessage.textContent = 'Login successful!';

        // 清空输入框和状态消息
        usernameInput.value = '';
        passwordInput.value = '';
        statusMessage.textContent = '';
    });

    // 邮箱格式验证函数
    function validateEmail(email) {
        const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }
});

// Function to validate email format

