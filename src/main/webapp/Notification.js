/**
 * Lists the notifications for a the user logged inn.
 *
 */

function listNotificationsForUserLoggedIn() {
    $.ajax({
        type:"GET",
        dataType:"json",
        url: "rest/session",
        success: function (data) {
            $.ajax({
                url: "rest/notifications/" + id,
                type: "GET",
                contentType: "application/json",
                dataType: "json",
                success: function (data) {
                    // do something.
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

