var curGroup = "currentGroup";
$(document).ready(function () {
    //gets info of groups from server and renders the dropdown for them.
    if(window.location.pathname!=="/scrum/index.html"){
        getLoggedOnUser(setGroupDropdown);
    }

    listNotificationsForUserLoggedIn();

    // Ugly hack for the group dropdown caret
    $(document).on('click', function(){
        groupDropdownArrow();
    });

    //Assigns logout to logout button
    $("#logout").click(function(){
        logOut();
    });
});

//Removes XSS
function htmlEntities(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;')};

//Common AJAX-calls
function getLoggedOnUser(success) {
    $.ajax({
        url: 'rest/session',
        type: 'GET',
        dataType: 'json',
        success: function(session) {
            $.ajax({
                url: 'rest/user/' + session.email,
                type: 'GET',
                dataType: 'json',
                success: function(user) {
                    success(user);
                },
                error: function() {
                    window.location.href = "index.html";
                }
            });
        },
        error: function() {
            window.location.href = "index.html";
        }
    });
}
function logOut(){
    console.log("logout");
    $.ajax({
        url: 'rest/session',
        type: 'DELETE',
        dataType: 'json',
        success: function(session){
            window.location.href="index.html";
            document.cookie ="userLoggedOn =";

        }
    });
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

//Returns a Promise that returns the hash on success.
function sha256(str) {
    // We transform the string into an arraybuffer.
    var buffer = new TextEncoder("utf-8").encode(str);
    return crypto.subtle.digest("SHA-256", buffer).then(function (hash) {
        // console.log(hex(hash))
        return hex(hash);
    });
}
function hex(buffer) {
    var hexCodes = [];
    var view = new DataView(buffer);
    for (var i = 0; i < view.byteLength; i += 4) {
        // Using getUint32 reduces the number of iterations needed (we process 4 bytes each time)
        var value = view.getUint32(i)
        // toString(16) will give the hex representation of the number without padding
        var stringValue = value.toString(16)
        // We use concatenation and slice for padding
        var padding = '00000000'
        var paddedValue = (padding + stringValue).slice(-padding.length)
        hexCodes.push(paddedValue);
    }
    // Join all the hex strings into one
    return hexCodes.join("");
}

function getGroup(groupId, success){
    $.ajax({
        url: 'rest/groups/'+groupId,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            success(data);
        }
    });
}

//.................................
// JS FOR NOTIFICATION
//..................................

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
                    if(data.length > 10) {
                        dl=10;
                    }
                    for(var i = dl; i > dl - 10 && i > -1; i--){
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





//....................................................
// JS FOR TOP-NAVBAR
//....................................................

//function which lists out the different groups into the dropdown menu
function renderGroupDropdown(groups) {
    homeButtonForOldUsers(groups);
    console.log("render grouplist");
    var len = groups.length;
    if (len === 0) {
        document.cookie = curGroup + "=0";
        document.cookie = "currentGroup=null";
        // $("#page-content").load("Profile.html");
        //Doesnt reload when already on profile.
        if(window.location.pathname!=="/scrum/Profile.html"){
            window.location.href="Profile.html";
        }
    } else {
        $('#groupdropdown').empty();
        if (getCookie(curGroup) < len || !groupsContainsCurrentgroup(groups, getCookie(curGroup))) {
            document.cookie = curGroup + "=" + groups[0].id;
            document.cookie = "groupName=" + groups[0].name;
            $(".navGroupName").html(getCookie("groupName"));
        }
        for (var i = 0; i < len; i++) {
            var groupname = groups[i].name;
            var id = '';
            id += i;
            id += 'group';
            var $x = $('<li><a class="dropdown-item" href="GroupDashboard.html#feed" id="' + id + '">' +
                groupname + '</a></li>'
            );
            (function (i) {
                $x.click(function () {
                    document.cookie = curGroup + "=" + groups[i].id;
                    document.cookie = "groupName=" + groups[i].name;
                    $(".navGroupName").html(getCookie("groupName"));
                    //window.location.reload();
                    if(window.location.pathname==="/scrum/Profile.html"){
                        window.location.href="GroupDashboard.html#feed"
                    }else{
                        $("#page-content").load("Feed.html");
                        window.location.hash = "#feed";
                    }
                })
            }(i));
            $('#groupdropdown').append($x);
            console.log("Added group: " + groupname);
            //$(".navGroupName").html(""+groupname);
        }
    }
}
//Users who are connected to a group can press the logo to go the the dashboard while
//new users cant
function homeButtonForOldUsers(groups) {
    var len = groups.length;
    if (len == 0){
    }
    if(len>=1){
        $('#houseHoldManagerLogo').click(function () {
            window.location.href = "GroupDashboard.html#feed"
        })
        $('#houseHoldManagerLogo2').click(function () {
            window.location.href = "GroupDashboard.html#feed"
        })
    }
}

function setGroupDropdown(user) {
    $.ajax({
        type: 'GET',
        url: '/scrum/rest/groups/list/' + user.email,
        dataType: "json",
        success: renderGroupDropdown
    });
    //console.log(getCookie(curGroup));
    $("#navUsername").html(""+user.name);
}


function groupsContainsCurrentgroup(groups, groupid) {
    var s = false;
    var len = groups.length;
    for(var i = 0; i <len;i++){
        if (groupid === groups[i].id){
            s = true;
        }
    }
    return s;
}
function groupDropdownArrow(){
    if($('#navbarDropdownMenuLink1').attr('aria-expanded') === "true"){
        $('.groupArrow').html("arrow_drop_up");
    }else{
        $('.groupArrow').html("arrow_drop_down");
    }
}
function notificationIcon(){
    if($('#navbarDropdownMenuLink3').attr("aria-expanded") === "false"){
        $('.notificationIcon').html('notifications_active');
    }else {
        $('.notificationIcon').html('notifications');
    }
}

