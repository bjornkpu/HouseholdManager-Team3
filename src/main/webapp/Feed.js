$(document).ready(function() {

    $("#post-button").click(function(){
        var text = document.getElementsByClassName("post-input").value();
        //var name = getLoggedInUser() lel

            $("#textPosts").prepend('<div class="post">"text"</div>');

            jQuery('<div/>', {
                class: 'post',
                text: text
            }).appendTo('#textPosts');

            $("#post").prepend('<p>"" + text</p>');

            $("#textPosts").createElement('div');
        };




    });



});