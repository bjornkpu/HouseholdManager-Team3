
$(document).ready(function(){

    findAllGroups();

    var groups = [];
    var currentGroup;

    //Goes to correct page when reload
    if (window.location.hash == "#shopping"){
        $("#page-content").load("Shoppinglist.html");
    }


    //finds all groups
    function findAllGroups() {
        console.log('findGroups');
        $.ajax({
            type: 'GET',
            url: '/scrum/rest/groups',
            dataType: "json",
            success: renderGroupDropdown
        });
    }

    //function which lists out the different groups into the dropdown menu
    function renderGroupDropdown(data) {
        // console.log("data:");
        // console.log(data);
        // console.log(data.length);
        console.log("render grouplist");
        groups=data;
        if(data === null || data.size === 0){
            console.log("Data er null ved renderDropdown");
        }
        var len = data.length;
        for (var i = 0; i < len;i++ ) {
            var groupname= data[i].name;
            var id='';
            id+=i;
            id+='group';
            $('#groupdropdown').append('<li><a class="dropdown-item group-link" href="#" id="'+id+'">'+
                groupname+'</a></li>'
            );
            console.log("Added group: "+groupname);
        }

        setCookie(groups[0].id);
    }

    $("#groupdropdown").on("click", "a.dropdown-item", function(){
        var i=this.id.charAt(0);
        currentGroup=groups[i];
        setCookie(currentGroup);
        alert(groups[i].id + " Member 0: "+ currentGroup.members[0].email);
    });

    function setCookie(data){
        var domain = document.domain;
        document.cookie = "groupId="+data.id+" " + ((domain !== "localhost") ? ";domain="+domain : "");
        document.cookie = "groupName="+data.name+" " + ((domain !== "localhost") ? ";domain="+domain : "");
        console.log("Cookie 'groupId': " + data.id + ", groupName: " + data.name);
    }


    var lists;
    var url='http://localhost:8080/scrum/rest/groups/'+2+'/members';
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
    if (window.location.hash == "#shopping"){
        $("#page-content").load("Shoppinglist.html");
    };

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



    //TODO: remove 2 from url. Get group from cookies or something.
    $("#invUserButton").click(function () {
        $.ajax({
            type:"POST",
            dataType:"json",
            url:"http://localhost:8080/scrum/rest/groups/2/members/" + $("#invUserField").val(),
            contentType: "application/json",
            data:JSON.stringify({
                "email": $("#invUserField").val()
            }),
            statusCode: {
                200: function () {
                    window.location.href = "Navbars.html";
                },
                500: function () {
                    console.log("Internal Server Error");
                }
            }

        })

    })

});

function renderMembers(data) {
    if(data === null || data.size === 0 || data[0] === undefined){
        console.log("Data er null ved renderMembers");
    }
    var len = data.length;
    for (var i = 0; i < len;i++ ) {
        $('#tabForUsersInGroup').append('<tr> <td>' +data[i].name + '</td></tr>');
    }
}

