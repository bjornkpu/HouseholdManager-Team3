$(window).on('load', activePage)
$(document).ready(function(){

    getLoggedOnUser(setData);
    var groups;

    $(window).on('hashchange', activePage);


    // $('#page-content').on('load', activePage());


    //Goes to correct page when reload
    if (window.location.hash === "#shopping"){
        $("#page-content").load("Shoppinglist.html");
    }
    if (window.location.hash === "#feed"){
        $("#page-content").load("Feed.html");
    }
    if (window.location.hash === "#tasks"){
        $("#page-content").load("Tasks.html");
    }
    if (window.location.hash === "#Receipts"){
        $("#page-content").load("Receipts.html");
    }
    if (window.location.hash === "#Setting"){
        $("#page-content").load("GroupSetting.html");
    }
    if (window.location.hash === "#stats"){
        $("#page-content").load("Statistics.html");
    }

    function setData(user) {
        $.ajax({
            type: 'GET',
            url: '/scrum/rest/groups/list/' + user.email,
            dataType: "json",
            success: renderGroupDropdown
        });
        //console.log(getCookie(curGroup));
        $("#navUsername").html(""+user.name);
    }

    //function which lists out the different groups into the dropdown menu
    function renderGroupDropdown(data) {
       console.log("render grouplist");
       groups = data;
       var len = data.length;
       if (len === 0) {
           document.cookie = curGroup + "=0";
           document.cookie = "currentGroup=null";
           // $("#page-content").load("Profile.html");
           //Doesnt reload when already on profile.
           if(window.location.pathname!=="/scrum/Profile.html"){
               window.location.href="Profile.html";
           }
       } else {
           if (getCookie(curGroup) < len || !checkIfCook(data, getCookie(curGroup))) {
               document.cookie = curGroup + "=" + data[0].id;
               document.cookie = "groupName=" + data[0].name;
               $(".navGroupName").html(getCookie("groupName"));
           }
           for (var i = 0; i < len; i++) {
               var groupname = data[i].name;
               var id = '';
               id += i;
               id += 'group';
               var $x = $('<li><a class="dropdown-item" href="#" id="' + id + '">' +
                   groupname + '</a></li>'
               );
               (function (i) {
                   $x.click(function () {
                       document.cookie = curGroup + "=" + data[i].id;
                       document.cookie = "groupName=" + data[i].name;
                       $(".navGroupName").html(getCookie("groupName"));
                       //window.location.reload();
                       $("#page-content").load("Feed.html");
                       window.location.hash = "#feed";
                   })
               }(i));
               $('#groupdropdown').append($x);
               console.log("Added group: " + groupname);
               //$(".navGroupName").html(""+groupname);
           }
       }
    }
        /*
            $("#groupdropdown").on("click", "a.dropdown-item", function(){
                var i=this.id.charAt(0);
                currentGroup=groups[i];
                document.cookie="groupId="+currentGroup.id;
                alert(groups[i].id + " Member 0: "+ currentGroup.members[0].email );
           });
        */
    /*
     * Changing the dropdown arrow to go upwards when clicked
     */

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

    $("#loadSettings").click(function () {
        $("#page-content").load("GroupSetting.html");
    });

    $("#loadAbout").click(function(){
        $("#page-content").load("About.html");
    });


    $("#loadSettings").click(function(){
        $("#page-content").load("GroupSetting.html");
    });

    $("#newGroup1").click(function () {
        createNewGroup();
        // window.location.reload();

    });


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
                        window.location.href = "Login.html";
                    }
                });
            },
            error: function() {
                window.location.href = "Login.html";
            }
        });
    };
});

var curGroup = "currentGroup";


function checkCookie(id) {
    var username = getCookie(curGroup);
    if (username == "") {
        document.cookie = curGroup + "=" + id;
    }
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

function checkIfCook(data,id) {
    var s = false;
    var len = data.length;
    for(var i = 0; i <len;i++){
        if (id === data[i].id){
            s = true;
        }
    }
    return s;

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
