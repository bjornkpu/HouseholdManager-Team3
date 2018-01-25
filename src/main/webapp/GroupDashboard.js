var curGroup = "currentGroup";

$(window).on('load', activePage);
$(document).ready(function(){

    //gets info of groups from server and renders the dropdown for them.
    getLoggedOnUser(setGroupDropdown);


    //No clue what this does
    $(window).on('hashchange', activePage);
    // $('#page-content').on('load', activePage());

    if(window.location.pathname==="/scrum/GroupDashboard.html"){
        window.location.href="GroupDashboard.html#feed";
    }


    //Goes to correct page when reload
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
        $("#page-content").load("Statistics.html");
    }


    //Assigns logout to logout button
    $("#logout").click(function(){
        logOut();
    });

    //Loads pages into the content div when clicking sidebar items.
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

    // DO NOT FUCKING USE. creates double load.
    /*
    $("#loadSettings").click(function () {
        $("#page-content").load("GroupSetting.html");
    });
    */
    $("#loadReceipts").click(function(){
        $("#page-content").load("Receipts.html");
    });
    $("#loadSettings").click(function(){
        $("#page-content").load("GroupSetting.html");
    });


    $("#newGroup1").click(function () {
        createNewGroup();
        // window.location.reload();
    });
});

function checkCookie(id) {
    var username = getCookie(curGroup);
    if (username == "") {
        document.cookie = curGroup + "=" + id;
    }
}

function activePage(){
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
