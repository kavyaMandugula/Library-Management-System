// admin.js

$(document).ready(function () {
    initializeUserSearch();
});

function initializeUserSearch() {
    $("#userSearch").on("input", function () {
        var query = $(this).val();
        $.ajax({
            url: "/api/admin/users/search",
            method: "GET",
            data: { query: query },
            success: function (response) {
                populateUserSelect(response);
            },
            error: function (xhr, status, error) {
                console.error("Search failed:", xhr, status, error);
            }
        });
    });
}

function populateUserSelect(users) {
    $("#userSelect").empty();
    users.forEach(function (user) {
        $("#userSelect").append(new Option(user.username + " (" + user.email + ")", user.id));
    });
}

function loadUserDetails(userId) {
    if (!userId) {
        $("#userDetailsSection").hide();
        return;
    }
    $.ajax({
        url: "/api/admin/users/" + userId,
        method: "GET",
        success: function (user) {
            displayUserDetails(user);
            fetchUserLoans(userId);
            $("#userDetailsSection").show();
        },
        error: function (xhr, status, error) {
            console.error("Failed to load user details:", xhr, status, error);
        }
    });
}

function displayUserDetails(user) {
    $("#userDetails").html(
        "<p>ID: " + user.id + "</p>" +
        "<p>Username: " + user.username + "</p>" +
        "<p>Email: " + user.email + "</p>" +
        "<p>Full Name: " + user.firstName + " " + (user.lastName || "") + "</p>" +
        "<p>Role: " + user.role + "</p>" +
        "<p>Status: " + user.status + "</p>" +
        "<p>Created At: " + formatDate(user.createdAt) + "</p>" +
        "<p>Last Login: " + (user.lastLogin ? formatDate(user.lastLogin) : "N/A") + "</p>"
    );

    // Always fetch loans separately
    fetchUserLoans(user.id);
}


function fetchUserLoans(userId) {
    $.ajax({
        url: '/api/admin/users/' + userId + '/loans',
        method: 'GET',
        success: function(loans) {
            displayUserLoans(loans, userId);
        },
        error: function(xhr, status, error) {
            console.error("Failed to fetch user loans:", xhr, status, error);
            $('#noLoansMessage').show();
            $('#loansTable').hide();
        }
    });
}
function displayUserLoans(loans, userId) {
    if (!loans || loans.length === 0) {
        $('#noLoansMessage').show();
        $('#loansTable').hide();
    } else {
        $('#noLoansMessage').hide();
        $('#loansTable').show();
        var loansTableBody = $('#loansTableBody');
        loansTableBody.empty(); // Clear existing rows
        loans.forEach(function(loan) {
            loansTableBody.append(
                '<tr>' +
                '<td>' + loan.bookTitle + '</td>' +
                '<td>' + loan.bookAuthor + '</td>' +
                '<td>' + formatDate(loan.checkoutDate) + '</td>' +
                '<td>' + formatDate(loan.dueDate) + '</td>' +
                '<td>' + loan.status + '</td>' +
                '<td><button onclick="removeBook(' + userId + ',' + loan.id + ')">Remove</button></td>' +
                '</tr>'
            );
        });
    }
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}
function removeBook(userId, loanId) {
    // Get the CSRF token
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: "/api/admin/users/" + userId + "/loans/" + loanId,
        method: "DELETE",
        beforeSend: function(xhr) {
            // Set the CSRF token in the header
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function () {
            console.log("Loan removed successfully");
            fetchUserLoans(userId);  // Refresh loans list
        },
        error: function (xhr, status, error) {
            console.error("Error details:", xhr.responseText);
            if (xhr.status === 403) {
                alert("You don't have permission to remove this loan. Please check if you're logged in as an admin.");
            } else {
                console.error("Failed to remove book:", xhr, status, error);
                alert("An error occurred while trying to remove the loan. Status: " + xhr.status);
            }
        }
    });
}
