$(document).ready(function() {
    fetchUserLoans();
});

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

function fetchUserLoans() {
    $.ajax({
        url: '/api/loans',
        method: 'GET',
        success: function(data) {
            if (data.length === 0) {
                $('#noLoansMessage').show();
                $('#loansTable').hide();
            } else {
                $('#noLoansMessage').hide();
                $('#loansTable').show();
                var loansTableBody = $('#loansTableBody');
                loansTableBody.empty(); // Clear existing rows
                data.forEach(function(loan) {
                    loansTableBody.append(
                        '<tr>' +
                        '<td>' + loan.bookTitle + '</td>' +
                        '<td>' + loan.bookAuthor + '</td>' +
                        '<td>' + formatDate(loan.checkoutDate) + '</td>' +
                        '<td>' + formatDate(loan.dueDate) + '</td>' +
                        '<td>' + loan.status + '</td>' +
                        '</tr>'
                    );
                });
            }
        },
        error: function(xhr, status, error) {
            if (xhr.status === 401) {
                window.location.href = '/login';
            } else {
                alert('Error fetching loans: ' + xhr.responseText);
            }
        }
    });
}