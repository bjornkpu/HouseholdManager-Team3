var curGroup = "currentGroup";

$(window).on('load', activePage);
$(document).ready(function(){

    //gets info of groups from server and renders the dropdown for them.
    getLoggedOnUser(setGroupDropdown);
    listNotificationsForUserLoggedIn();

    //Changes active page when anchor changes
    $(window).on('hashchange', activePage);

    // Ugly hack for the group dropdown caret
    $(document).on('click', function(){
        groupDropdownArrow();
    });

    //Assigns logout to logout button
    $("#logout").click(function(){
        logOut();
    });


    $("#newGroup1").click(function () {
        createNewGroup();
        // window.location.reload();
    });
});

function checkCookie(id) {
    var username = getCookie(curGroup );
    if (username == "") {
        document.cookie = curGroup + "=" + id;
    }
}

function activePage(){
    //Goes to correct page when reload
    console.log("Aktive page.")
    if (window.location.hash === "#shopping"){
        $("#page-content").load("Shoppinglist.html");
    }if (window.location.hash === "#feed"){
        $("#page-content").load("Feed.html");
    }if (window.location.hash === "#tasks"){
        $("#page-content").load("Tasks.html");
    }if (window.location.hash === "#Receipts"){
        $("#page-content").load("Receipts.html");
    }if (window.location.hash === "#Setting"){
        $("#page-content").load("GroupSetting.html");
    }if (window.location.hash === "#stats"){
        $("#page-content").load("Statistic.html");
    }

    var element;
    var sidebarDivs = document.getElementsByClassName("leftsidebar-list-items");
    var sidebarLinks = document.getElementsByClassName("sidelink");
    for (var i = 0; i < sidebarLinks.length; i++) {
        if (sidebarLinks[i].hash === window.location.hash) {
            element = sidebarDivs[i];
        }
        sidebarDivs[i].className = 'leftsidebar-list-items';
    }
    element.className = 'leftsidebar-list-items activePage';
}
function notificationIcon(){
    if($('#navbarDropdownMenuLink3').attr("aria-expanded") === "false"){
        $('.notificationIcon').html('notifications_active');
    }else {
        $('.notificationIcon').html('notifications');
    }
}

function groupDropdownArrow(){
    if($('#navbarDropdownMenuLink1').attr('aria-expanded') === "true"){
        $('.groupArrow').html("arrow_drop_up");
    }else{
        $('.groupArrow').html("arrow_drop_down");
    }
}