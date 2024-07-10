document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;
    
    // Validate email format
    if (!validateEmail(username)) {
        return;
    }
    
    // Convert password and username to sha256
    let passwordSha256 = sha256(password);
    let usernameSha256 = sha256(username);
    
    // Create JSON object to send
    let data = {
        username: usernameSha256,
        password: passwordSha256
    };
    
    // Create a WebSocket connection
    let ws = new WebSocket('wss://www.micro-wave.cc:8443');
    
    // Handle WebSocket connection errors
    ws.onerror = function(event) {
        alert('Failed to connect to WebSocket server');
    };
    
    // Handle messages from WebSocket server
    ws.onmessage = function(event) {
        let response = JSON.parse(event.data);
        if (response.success) {
            document.getElementById('statusMessage').innerText = 'Login successful';
            document.cookie = `token=${response.token}`;
        } else {
            document.getElementById('statusMessage').innerText = 'Account does not exist';
        }
    };
    
    // Send data when WebSocket is open
    ws.onopen = function() {
        ws.send(JSON.stringify(data));
    };
});

// Function to validate email format
function validateEmail(email) {
    let re = /\S+@\S+\.\S+/;
    if(!re.test(email)) {
        alert('Please enter a valid email address');
        return false;
    }
    return true;
}

// Function to calculate sha256 hash
function sha256(input) {
    // Implement your sha256 calculation logic here
    // This is just a placeholder function
    return 'sha256(' + input + ')';
}