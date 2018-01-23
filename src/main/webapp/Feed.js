var user;
function initTable() {
    var group = getCookie("currentGroup");
    $.ajax({
        url: "rest/wallposts/" + group,
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            var id = 0, postedTo = 0;
            var message = "", postedBy = "";
            var datePosted="";
            for (var i = data.length-1; i >= 0; i--) {
                id = data[i].id;
                datePosted = new Date(data[i].datePosted);
                message = data[i].message;
                postedBy = data[i].postedBy;
                postedTo = data[i].postedTo;
                /*
                $("#wallposts").append(
                    "<tr>" +
                    "<td>" + id + "</td>" +
                    "<td>" + datePosted.toLocaleDateString() + " " + datePosted.toLocaleTimeString() + "</td>" +
                    "<td>" + message + "</td>" +
                    "<td>" + postedBy + "</td>" +
                    "<td>" + postedTo + "</td>" +
                    "</tr>"
                );
                */
                $("#wallposts").append("<div class='post'><h6>" +
                    message + "</h6><div id='postedByAndDate'><p>"+ postedBy +
                    "</p><p>" + datePosted.toLocaleDateString() + " " +
                    datePosted.toLocaleTimeString() +"</p></div></div>");
            }
        },
        error: function () {
            //TODO: fix, do something else then return error.html.
            window.location.href = "error.html";
        }
    })
}

$(document).ready(function() {
    //var t = $("#wallposts")
    initTable();
    // Submit-button creates a div with the text from the text area
    $(".post-button").click(function(){
        var text = $("textarea.post-input").val();
        var group = getCookie("currentGroup");
        $.ajax({
            type:"GET",
            dataType:"json",
            url: "rest/session",
            success: function (data) {
                $.ajax({
                    url: "rest/wallposts/",
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({
                        id:0,   // autogenerated in db
                        datePosted: 1516620129000,  // random value, DOES NOT REALLY MATTER.
                        message: text,
                        postedBy: data.email,
                        postedTo: group
                    }),
                    success: function () {
                        console.log("Dette gikk bra. Nå få det inn i en tabell KRISTIAN!");
                        window.location.reload();
                        //$("#wallposts").reload();
                    },
                    error: function () {
                        console.log("Hmm, prøv igjen du kristian.");
                    }
                })
            }
        });
    });
});