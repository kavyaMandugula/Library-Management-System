$(document).ready(function() {
    $('#resetPasswordForm').submit(function(event) {
        event.preventDefault();
        
        var email = $('#email').val();
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();

        // Check if passwords match
        if (newPassword !== confirmPassword) {
            alert("New password and confirm password do not match!");
            return; // Stop the form submission
        }

        $.ajax({
            url: '/api/users/reset-password',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ 
                email: email, 
                newPassword: newPassword 
            }),
            success: function(response) {
                window.location.href = '/reset-success';
            },
            error: function(xhr, status, error) {
                alert('Failed to reset password: ' + xhr.responseText);
            }
        });
    });

    // Add real-time validation
    $('#confirmPassword').on('input', function() {
        var newPassword = $('#newPassword').val();
        var confirmPassword = $(this).val();
        
        if (newPassword === confirmPassword) {
            $('#passwordMatchMessage').text('Passwords match').css('color', 'green');
        } else {
            $('#passwordMatchMessage').text('Passwords do not match').css('color', 'red');
        }
    });
});