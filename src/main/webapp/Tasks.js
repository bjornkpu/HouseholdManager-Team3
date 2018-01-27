$(document).ready(function() {
    var groupId = getCookie("currentGroup");
    var tasks;//For rendering the Task List
    var members;

    //Setting up the datatable
    function setTasksInTable() {
        console.log("Adding tasks To table...");
        var len = tasks.length;
        var table = document.getElementById("taskTable");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        $("#taskTable").append(
            "<tr class='truncateTableHeader'>" +
            "<th>Task Description</th> " +
            "<th>Assigned To</th>" +
            '<th class="removePortrait"> Completed By  </th>' +
            '<th class="removePortrait">Deadline</th>' +
            "<th></th>" +
            "</tr>"
        );

        for(var i = 0; i < len; i++){
            var id = tasks[i].choreId;
            var assigned;
            var complete;

            assigned = getUserByEmail(tasks[i].assignedTo);
            console.log(assigned)
            complete = getUserByEmail(getCompletedBy(id));
            console.log(complete)

            if(assigned === undefined || assigned === null) assigned = "-";
            if(complete === undefined || complete === null) complete = "-";

            var date = new Date(tasks[i].deadline).toString().substr(4, 11);

                $("#taskTable").append(
                    "<tr id='row" + i + "' class='truncateRow' onclick='clickRowk(checkbox"+i+")'>" +
                    // "<th scope=\"row\">"+(i+1)+"</th>" +
                    "<td class='truncateTableCell'>" + tasks[i].description + "</td>" +
                    "<td class='truncateTableCell'>" + assigned + "</td>" +
                    "<td class='removePortrait truncateTableCell'>" + complete + "</td>" +
                    "<td class='removePortrait truncateTableCell'>" + date + "</td>" +
                    "<td class='removePortrait truncateTableCell'> <input value='" + id + "' id='checkbox" + i + "' type='checkbox' ></td>" +
                    "</tr>"
                );

            if(isMobile()) {
                $("#taskTable").append(
                    "<tr class='info info_" + i +"' style='width:100%' onclick=''>" +
                    "<td scope=\"row\" class='truncateTableCell'>Description:</td>" +
                    "<td scope=\"row\" class='truncateTableCell'>"+tasks[i].description+"</td>" +
                    "</tr>" +
                    "<tr class='info info_" + i +"'>" +
                    "<td scope=\"row\" class='truncateTableCell'>Assigned to</td>" +
                    "<td scope=\"row\" class='truncateTableCell'>"+assigned+"</td>" +
                    "</tr>" +
                    "<tr class='info info_" + i +"'>" +
                    "<td scope=\"row\" class='truncateTableCell'>Completed by</td>" +
                    "<td scope=\"row\" class='truncateTableCell'>"+complete+",-</td>" +
                    "</tr>" +
                    "<tr class='info info_" + i +"'>" +
                    "<td scope=\"row\" class='truncateTableCell'>Deadline</td>" +
                    "<td scope=\"row\" class='truncateTableCell'>"+date+"</td>" +
                    "</tr>" +
                    "<tr class='info info_" + i +"'>" +
                    "<td class='truncateTableCell selectedTask'> <input value='"+ id +"' id='checkbox"+ i +"' type='checkbox' ></td>" +
                    "</tr>"
                );
            }

            if(tasks[i].status===2){
                $("#row"+i).addClass('boughtMarked');
            }
        }
        console.log("Tasks added to table!");
    }
    function getTasksInGroup() {
        console.log("Loading tasks from group " + groupId + "...");
        $.get('http://localhost:8080/scrum/rest/groups/' + groupId + '/task/', function(data, status){
            if(status === "success") {
                tasks = data;
                console.log("Tasks loaded successfully!");
                $("#tableShoppinglist").empty();
                setTasksInTable();
            }
            if(status === "error")      console.log("Error in loading tasks.");
        });
    }
    getTasksInGroup();

    //Setting up the members in the drop-down menu
    function setMembersList(data) {
        var len = data.length;
        if(len === 0){
            $('#taskAssignDropdown').append('<li tabindex="-1" class="list" role="presentation" ' +
                'style="text-align: center">Empty</li>');
            $('#taskCompDropdown').append('<li tabindex="-1" class="list" role="presentation" ' +
                'style="text-align: center">Empty</li>');
            return;
        }
        for (var i = 0; i < len;i++ ) {
            $('#taskAssignDropdown').append('<li tabindex="-1" class="list" role="presentation">' +
                '<a class="link" role="menuitem" id="'+i+'" href="#">' + data[i].name + '</aclass></li>'
            );
            $('#taskCompDropdown').append('<li tabindex="-1" class="list" role="presentation">' +
                '<a class="link" role="menuitem" id="'+i+'" href="#">' + data[i].name + '</aclass></li>'
            );
        }
    }
    function getMemberList() {
        console.log("Loading members from group " + groupId + "...");
        $('#taskAssignDropdown').empty();
        $('#taskCompDropdown').empty();
        var url='http://localhost:8080/scrum/rest/groups/'+groupId+'/members';

        $.get(url,function (data, status) {
            members = data;
            if(status === "success")    console.log("Members loaded successfully!");
            if(status === "error")      console.log("Error in loading members.");
            setMembersList(data);
        });
    }
    getMemberList();

    //Help methods
    function getCheckedTasks(){
        var table_length = $('#taskTable tr').length;
        var checked = [];
        for (var i =0; i<table_length;i++){
            if($("#checkbox"+i).is(':checked')){
                var task = { id: tasks[i].choreId };
                console.log("task id: " + task);
                checked.push(task);
            }
        }return checked;
    }
    function getUserByEmail(email){
        if (email === undefined || email === null) return null;
        var r = undefined;
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/scrum/rest/user/' + email,
            async: false,
            success: function (data, status) {
                r = data.name;
            }});
        return r;
    }
    function getCompletedBy(id){
        if (id === undefined || id === null) return null;
        var r = undefined;
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/scrum/rest/groups/' + groupId + '/task/CompletedBy/' + id,
            async: false,
            success: function (data, status) {
                r = data[0];
            }});
        return r;
    }

    //The buttons
    $('#addTask').click(function () {
        var addingTask = document.getElementById('creatingTask');
        var taskTable = document.getElementById('task');
        var taskBtns = document.getElementById('button_row');

        addingTask.style.display="block";
        taskTable.style.display="none";
        taskBtns.style.display="none";

        $('#confirmTask').click(function(){
            var description = htmlEntities($("#descriptionOfTask").val());
            var deadline = htmlEntities($("#deadlineTask").val());
            if(description === '' || description === undefined || description === null){
                alert("You have to give the task a description");
                return;
            }
            console.log("Adding task " + description + "...");
            deadline = ""+deadline;
            console.log(deadline);

            $.ajax({
                type: "POST",
                url: 'http://localhost:8080/scrum/rest/groups/' + groupId + '/task/',
                dataType: "json",
                data: JSON.stringify({
                    description: description,
                    deadline: deadline,
                    partyId: groupId
                }),
                contentType: "application/json;charset=UTF-8",
                success: function (data, status) {
                    if (status === "success")   console.log("Task added!");
                    if (status === "error")     console.log("Could not add Task");
                }
            });

            $("#page-content").load("Tasks.html");
        });
        $('#backTask').click(function() {$("#page-content").load("Tasks.html");});
    });

    $("#taskAssignDropdown").on("click", "a.link", function(){
        var checked = getCheckedTasks();
        if(checked.length === 0) {
            alert("You must check what task you want to assign");
            return;
        }

        var clickedUserIndex = this.id;
        var clickedUserEmail = members[clickedUserIndex].email;

        for(var i = 0; i < checked.length; i++){
            $.ajax({
                type: "PUT",
                url: 'http://localhost:8080/scrum/rest/groups/' + groupId + '/task/' + checked[i].id ,
                dataType: "json",
                data: JSON.stringify({
                    email: clickedUserEmail
                }),
                contentType: "application/json;charset=UTF-8",
                success: function (data, status) {
                    if (status === "success")   console.log("Task Deleted!");
                    if (status === "error")     console.log("Could not delete Task");
                }
            });
        }
        $("#page-content").load("Tasks.html");
    });

    $("#taskCompDropdown").on("click", "a.link", function(){
        var checked = getCheckedTasks();
        if(checked.length === 0) {
            alert("You must check what task you want to complete");
            return;
        }

        var clickedUserIndex = this.id;
        var clickedUserEmail = members[clickedUserIndex].email;

        for(var i = 0; i < checked.length; i++){
            $.ajax({
                type: "PUT",
                url: 'http://localhost:8080/scrum/rest/groups/' + groupId + '/task/CompletedBy/' + checked[i].id ,
                dataType: "json",
                data: JSON.stringify([
                    clickedUserEmail
                ]),
                contentType: "application/json;charset=UTF-8",
                success: function (data, status) {
                    if (status === "success")   console.log("Task Deleted!");
                    if (status === "error")     console.log("Could not delete Task");
                }
            });
        }
        $("#page-content").load("Tasks.html");
    });

    $("#deleteTask").click(function(){
        var checked = getCheckedTasks();
        var url = "http://localhost:8080/scrum/rest/groups/3/task/";
        for(var i = 0; i < checked.length; i++){
            $.ajax({
                type: "DELETE",
                url: url + checked[i].id ,
                success: function (data, status) {
                    if (status === "success")   console.log("Task Deleted!");
                    if (status === "error")     console.log("Could not delete Task");
                }
            });
        }
        $("#page-content").load("Tasks.html");
    });

});
$("#tasktable").on('click', 'tr.truncateRow', function() {
});

var selected = -1;
//mobile site view
$("#taskTable").on('click', 'tr.truncateRow', function(){
    var id = this.id.split("w").pop();
    if(isMobile()) {
        $(".info").css("display", "none");
        if(id !== selected){
            $(".info_"+id).css("display", "table-header-group");
            selected = id;
        } else {
            selected = -1;
        }
    } else {
        if(!$("#checkbox"+id).is(':checked')){
            $("#checkbox"+id).prop('checked', true);
        } else {

            $("#checkbox"+id).prop('checked', false);
        }
    }
});
$("#taskTable").on('click', 'td.selectedTask', function(){
    if(isMobile()) {
        console.log("selected");
        $(".info").css("display", "none");
        selected = -1;
    }
});

//checks if mobile or laptop
function isMobile(){
    return ($(window).width() < 550);
}
function clickRowk(data) {
    if(document.getElementById(data).checked === true){
        document.getElementById(data).checked = true
    }else document.getElementById(data).checked = true;
}
