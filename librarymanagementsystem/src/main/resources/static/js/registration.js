$(document).ready(function() {
    $('#registrationForm').submit(function(event) {
        event.preventDefault();
        
        // Validate form
        var firstName = $('#firstName').val();
        var lastName = $('#lastName').val();
        var username = $('#username').val();
        var email = $('#email').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
  
        if (password !== confirmPassword) {
            alert("Passwords do not match!");
            return;
        }
  
        // Prepare data for API call
        var userData = {
            firstName: firstName,
            lastName: lastName,
            username: username,
            email: email,
            password: password
        };
  
        // Make API call to register user
        $.ajax({
          url: '/api/users/register',
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify(userData),
          success: function(response) {
              alert('Registration successful!');
              window.location.href = '/login';
          },
          error: function(xhr, status, error) {
              alert('Registration failed: ' + xhr.responseText);
          }
        });
    });
  });