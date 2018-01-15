
$(document).ready(function(){
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

