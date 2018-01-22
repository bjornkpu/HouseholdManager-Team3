$(document).ready(function() {


    // Submit-button creates a div with the text from the text area
    $(".post-button").click(function(){
        var text = $("textarea.post-input").val();

        if(text.length !== 0) {
            var $newdiv1 = $("<div class='post'><p>" + text + "</p></div>");


            $(".textPosts").prepend($newdiv1);
        }else{
            alert("You must type something to post!")
        }

    });



});