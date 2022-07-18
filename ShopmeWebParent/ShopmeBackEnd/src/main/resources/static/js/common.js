function clearFilter() {
    window.location=moduleUrl;
}

$(document).ready(function() {
    $("#logoutLink").on("click", function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    });
});