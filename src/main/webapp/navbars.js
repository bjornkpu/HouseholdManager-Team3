
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
});

