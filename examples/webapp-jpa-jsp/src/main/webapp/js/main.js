$(document).ready(function () {

    $('a[name=ticket-delete]').click(function () {
        var $url = $(this);
        var ticketId = $url.attr("ticket-id");
        console.log("Delete ticket = " + ticketId);

        $.ajax({
            method: "GET",
            url: baseURI + "/tickets/delete/" + ticketId,
            success: function (response) {
                console.log("Ticket was succesfully deleted!");
                $("div").find("[ticket-id='" + ticketId + "']").remove();

            },
            error: function (t, s, error) {
                console.log("error\t", error);
            }
        });
    });

    $('#comment-save-btn').click(function () {
        var $element = $(this);
        var ticketId = $element.attr("ticket-id");

        var comment = $('#comment-content').val();
        var author = $('#comment-author').val();

        $.ajax({
            method: "POST",
            url: baseURI + "/tickets/comment/add/" + ticketId,
            data: {comment: comment, userId: author},
            success: function (response) {
                console.log("Comment was succesfully added!");
                window.location.reload(true);
            },
            error: function (t, s, error) {
                console.log("error\t", error);
            }
        });
    });
})