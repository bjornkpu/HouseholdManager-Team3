
$(document).ready(function(){

    $("#logout").click(function(){
        console.log("logout");
        $.ajax({
            url: 'rest/session',
            type: 'DELETE',
            dataType: 'json',
            success: function(session){
                 window.location.href="Login.html";
            }
        });
    });

    var lists;
    var url='http://localhost:8080/scrum/rest/groups/'+2+'/members';
    $.get(url, function(data, status){
        lists=data;
        renderMembers(data)
        if(status === "success"){
            console.log("members content loaded successfully!");
        }
        if(status === "error"){
            console.log("Error in loading members");
        }
    });

    /*
     * Changing the dropdown arrow to go upwards when clicked
     */
    $(".dropdown").on("hide.bs.dropdown", function(){
        $(".btn").html('My groups <span class="caret"></span>');
    });
    $(".dropdown").on("show.bs.dropdown", function(){
        $(".btn").html('My groups <span class="caret caret-up"></span>');
    });


    if (window.location.hash == "#shopping"){
        $("#page-content").load("Shoppinglist.html");
    };


    $("#loadShoppingList").click(function(){
        $("#page-content").load("Shoppinglist.html");
    });

    $("#loadFeed").click(function(){
        $("#page-content").load("Feed.html");
    });

    $("#loadStatistics").click(function(){
        $("#page-content").load("Statistic.html");
    });

    $("#loadTasks").click(function(){
        $("#page-content").load("Tasks.html");
    });

});

function renderMembers(data) {
    var len = data.length;
    for (var i = 0; i < len;i++ ) {
        $('#tabForUsersInGroup').append('<tr> <td>' +data[i].name + '</td></tr>');
    }
}

