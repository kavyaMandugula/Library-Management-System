$(document).ready(function() {
    $('#searchForm').submit(function(event) {
        event.preventDefault();
        
        var searchQuery = $('#searchInput').val();
        
        // For now, just log the search query
        console.log("Search query:", searchQuery);
        
        // You'll implement the actual search API call here later
        alert("Search functionality will be implemented in the future. You searched for: " + searchQuery);
    });
});