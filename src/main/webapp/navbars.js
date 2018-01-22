$(document).ready(function(){

    getLoggedOnUser(setData);
    var groups;


    //Goes to correct page when reload
    if (window.location.hash == "#shopping"){
        $("#page-content").load("Shoppinglist.html");
    }

    function setData(user) {
        $.ajax({
            type: 'GET',
            url: '/scrum/rest/groups/list/' + user.email,
            dataType: "json",
            success: renderGroupDropdown
        });
        //console.log(getCookie(curGroup));
    }

   /** $('#newGroup1').click(function () {
        var test=prompt("test: ");
    })*/


    //function which lists out the different groups into the dropdown menu
    function renderGroupDropdown(data) {
        console.log("render grouplist");
        groups=data;
        var len = data.length;
        if (len === 0){
            document.cookie = curGroup +"=0";
        } else {
            if (getCookie(curGroup) < len || !checkIfCook(data,getCookie(curGroup))){
                document.cookie = curGroup+"="+ data[0].id;
            }
            for (var i = 0; i < len;i++ ) {
                var groupname= data[i].name;
                var id='';
                id+=i;
                id+='group';
                var $x = $('<li><a class="dropdown-item" href="#" id="'+id+'">'+
                    groupname +'</a></li>'
                );
                (function (i) {
                    $x.click(function () {
                        document.cookie = curGroup + "=" + data[i].id;
                        window.location.reload();
                    })
                }(i));
                $('#groupdropdown').append($x);
                console.log("Added group: "+groupname);
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

    var y = getCookie(curGroup);
    var lists;
    var url='rest/groups/'+y+'/members';
    $.get(url, function(data, status){
        lists=data;
        renderMembers(data)
        if(status === "success"){
            console.log("members content loaded successfully!");
        }
        if(status === "error"){
            console.log("Error in loading members");
        }
    });

    /*
     * Changing the dropdown arrow to go upwards when clicked
     */
    $(".dropdown").on("hide.bs.dropdown", function(){
        $(".btn").html('My groups <span class="caret"></span>');
    });

    $(".dropdown").on("show.bs.dropdown", function(){
        $(".btn").html('My groups <span class="caret caret-up"></span>');
    });


    //Assigns logout to logout button
    $("#logout").click(function(){
        logOut();
    });

    //Loads content when clicking sidebar.
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

    $("#loadSettings").click(function(){
        $("#page-content").load("GroupSetting.html");
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
                        window.location.href = "error.html";
                    }
                });
            },
            error: function() {
                window.location.href = "error.html";
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

$("#newGroup1").click(function () {
    createNewGroup();
    window.location.reload();

});

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


