$(document).ready(function() {
    // Compile Handlebars template
    var searchResultTemplate = Handlebars.compile($("#search-result-template").html());

    // Initialize autocomplete for the main search input
    $('#searchInput').autocomplete({
        source: function(request, response) {
            $.ajax({
                url: "/api/books/search",
                dataType: "json",
                data: {
                    query: request.term
                },
                success: function(data) {
                    response($.map(data, function(item) {
                        return {
                            label: item.title + " by " + item.author,
                            value: item.id
                        };
                    }));
                }
            });
        },
        minLength: 2,
        select: function(event, ui) {
            event.preventDefault();
            $("#searchInput").val(ui.item.label);
            fetchBookDetails(ui.item.value);
        }
    });

    // Handle main search form submission
    $('#searchForm').submit(function(event) {
        event.preventDefault();
        var searchQuery = $('#searchInput').val();
        performSearch(searchQuery);
    });

    // Function to perform search
    function performSearch(query) {
        $("#bookDetails").empty(); // Clear previous results
        $.ajax({
            url: "/api/books/search",
            method: "GET",
            data: { query: query },
            success: function(books) {
                displaySearchResults(books);
            },
            error: function(xhr, status, error) {
                console.error("Error performing search:", error);
            }
        });
    }

    // Function to display search results
    function displaySearchResults(books) {
        var resultsHtml = searchResultTemplate({ books: books });
        $("#bookDetails").html(resultsHtml);
    }

    // Handle loan button click
    $(document).on('click', '.loan-button', function() {
        var bookId = $(this).data('book-id');
        var searchQuery = $('#searchInput').val(); // Get the current search query
        
        $.ajax({
            url: '/api/books/' + bookId + '/loan',
            type: 'POST',
            beforeSend: function(xhr) {
                if (csrfHeader && csrfToken) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(response) {
                alert('Book loaned successfully!');
                // Refresh the search results
                if (searchQuery) {
                    performSearch(searchQuery);
                } else {
                    fetchBookDetails(bookId); // Refresh single book details if no search query
                }
            },
            error: function(xhr, status, error) {
                alert('Error loaning book: ' + xhr.responseText);
            }
        });
    });


    // Toggle advanced search
    $('#toggleAdvancedSearch').click(function() {
        $('#advancedSearch').toggle();
        if ($('#advancedSearch').is(':visible')) {
            loadAdvancedSearchOptions();
        }
    });

    // Load advanced search options
    function loadAdvancedSearchOptions() {
        $.ajax({
            url: "/api/books/advanced-search-options",
            method: "GET",
            beforeSend: function(xhr) {
                if (csrfHeader && csrfToken) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            },
            success: function(options) {
                populateSelect2Dropdown('#isbn', options.isbns);
                populateSelect2Dropdown('#author', options.authors);
                populateSelect2Dropdown('#title', options.titles);
                populateSelect2Dropdown('#status', options.statuses);
            },
            error: function(xhr, status, error) {
                console.error("Error loading advanced search options:", error);
            }
        });
    }

    // Populate Select2 dropdowns
    function populateSelect2Dropdown(selector, options) {
        var dropdown = $(selector);
        dropdown.empty().append($('<option></option>').attr('value', '').text('Select an option'));
        $.each(options, function(index, value) {
            dropdown.append($('<option></option>').attr('value', value).text(value));
        });
        dropdown.select2({
            placeholder: 'Select an option',
            allowClear: true,
            width: '100%'
        });
    }

    // Handle advanced search form submission
    $('#advancedSearchForm').submit(function(event) {
        event.preventDefault();
        var searchCriteria = {
            isbn: $('#isbn').val(),
            author: $('#author').val(),
            title: $('#title').val(),
            status: $('#status').val()
        };
    
        $.ajax({
            url: "/api/books/advanced-search",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(searchCriteria),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(books) {
                displaySearchResults(books);
            },
            error: function(xhr, status, error) {
                console.error("Error performing advanced search:", error);
                if (xhr.status === 403) {
                    alert("You don't have permission to perform this search. Please check if you're logged in.");
                }
            }
        });
    });

    // Clear advanced search form
    $('#clearAdvancedSearch').click(function() {
        $('#isbn').val(null).trigger('change');
        $('#author').val(null).trigger('change');
        $('#title').val(null).trigger('change');
        $('#status').val(null).trigger('change');
        $('#bookDetails').empty();
    });

    // Function to fetch and display individual book details
    function fetchBookDetails(bookId) {
        $.ajax({
            url: "/api/books/" + bookId,
            method: "GET",
            success: function(book) {
                displayBookDetails(book);
            },
            error: function(xhr, status, error) {
                console.error("Error fetching book details:", error);
            }
        });
    }

    // Function to display individual book details
    function displayBookDetails(book) {
        var detailsHtml = "<div class='book-details'>";
        if (book.thumbnail) {
            detailsHtml += "<img src='" + book.thumbnail + "' alt='Book cover' class='book-thumbnail'>";
        }
        detailsHtml += "<h3>" + book.title + "</h3>" +
            "<p><strong>Author:</strong> " + book.author + "</p>" +
            "<p><strong>ISBN:</strong> " + book.isbn + "</p>";

        if (book.publishedYear) {
            detailsHtml += "<p><strong>Published Year:</strong> " + book.publishedYear + "</p>";
        }

        detailsHtml += "<p><strong>Status:</strong> " + book.status + "</p>" +
            "<p><strong>Description:</strong> " + book.description + "</p>" +
            "<p><strong>Genre:</strong> " + book.genre + "</p>" +
            "<p><strong>Average Rating:</strong> " + book.averageRating + "</p>" +
            "<p><strong>Number of Pages:</strong> " + book.numPages + "</p>" +
            "<p><strong>Available Quantity:</strong> " + book.availableQuantity + " / " + book.quantity + "</p>";
        
        if (book.availableQuantity > 0) {
            detailsHtml += "<button class='loan-button' data-book-id='" + book.id + "'>Loan</button>";
        } else {
            detailsHtml += "<p>Not available for loan</p>";
        }
        
        detailsHtml += "</div>";

        $("#bookDetails").html(detailsHtml);
        
    }
});