/**
 * Lists the notifications for a the user logged inn.
 *
 */
function listNotificationsForUserLoggedIn() {
    $.ajax({
        type:"GET",
        dataType:"json",
        url: "rest/session",
        success: function (user) {
            $.ajax({
                url: "rest/notifications/" + user.email,
                type: "GET",
                contentType: "application/json",
                dataType: "json",
                success: function (data) {
                    var dl = data.length - 1;
                    var check;
                    if(dl > 10) {
                        dl=10;
                    }
                    for(var i = dl; i > data.length - dl; i--){
                        console.log(data[i].text);
                        console.log("Seen: " +data[i].seen);
                        if(data[i].seen === 0){
                            check = true;
                        }
                        var $item = $('<li><a class="dropdown-item" href="#" id="' + data[i].id + '">' + data[i].text + '</a></li>');
                        /*(function(i){
                            $item.click(function () {
                                // Empty for now.
                                // Makes each dropdown item jquery-clickable
                                // in case of further notification development.
                            })
                        }(i))*/
                        $('.notificationDropdown').append($item);

                    }
                    if(check){
                        console.log("Check: is true");
                        $('.notificationIcon').html('notifications_active');
                        $('.notificationIcon').attr('class', 'material-icons md-24 notificationIcon newNotification');
                    }else if(!check){
                        console.log("Check: is false");
                        $('.notificationIcon').html('notifications');
                        $('.notificationIcon').attr('class', 'material-icons md-24 notificationIcon noNotification');
                    }
                    $('.newNotification').click(function(){
                        console.log("Newnotification click");
                        $.ajax({
                            type:"PUT",
                            url: "rest/notifications/" + user.email + "/seenAll",
                            dataType: "json",
                            contentType: "application/json",
                            success: function () {
                                $('.notificationIcon').html('notifications');
                                $('.notificationIcon').attr('class', 'material-icons md-24 notificationIcon noNotification');
                            },
                            error: function () {

                            }
                        })

                    });
                },
                error: function () {
                    // do something else
                }
            })

        }
    })
}

    /**
     * Lists the notifications posted to a group.
     *
     */

function listGroupNotifications(groupId) {
    $.ajax({
        url: "rest/notifications/" + id,
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            // do something
        },
        error: function () {
            // do something else.
        }
    })
}
/**
 * Post a notification to a user.
 * @param user Email of the user.
 * @param text The message to be posted.
 */

function postNotificationToAUser(user,text) {
    $.ajax({
        url: "rest/notifications", //TODO: change url?
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            //TODO: what to post? waiting for backend.
            message: htmlEntities(text)
        }),
        success: function () {
            //do something
        },
        error: function () {
            //do something
        }
    })
}

