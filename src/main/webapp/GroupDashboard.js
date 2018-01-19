
$(document).ready(function(){

    findAllGroups();

    var groups;
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
        var len = data.length;
        for (var i = 0; i < len;i++ ) {
            var groupname= data[i].name;
            var id='';
            id+=i;
            id+='group';
            $('#groupdropdown').append('<li><a class="dropdown-item" href="#" id="'+id+'">'+
                groupname+'</a></li>'
            );
            console.log("Added group: "+groupname);
        }
    }

    $("#groupdropdown").on("click", "a.dropdown-item", function(){
        var i=this.id.charAt(0);
        currentGroup=groups[i];
        document.cookie="groupId="+currentGroup.id;
        alert(groups[i].id + " Member 0: "+ currentGroup.members[0].email );
    });

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

});

