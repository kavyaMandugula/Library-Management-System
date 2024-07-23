$(document).ready(function() {
    $('#loginForm').submit(function(event) {
        event.preventDefault();
        
        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            url: '/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function(response) {
                // Redirect to dashboard on successful login
                window.location.href = '/dashboard';
            },
            error: function(xhr, status, error) {
                alert('Login failed. Please check your credentials.');
            }
        });
    });
});