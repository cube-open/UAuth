
// Function to validate email format
function validateEmail(email) {
    let re = /\S+@\S+\.\S+/;
    if(!re.test(email)) {
        alert('Please enter a valid email address');
        return false;
    }
    return true;
}

