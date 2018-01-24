$(document).ready(function() {
    var groupId = getCookie("currentGroup");
    var tasks;

    function setTasksInTable() {
        console.log("Adding tasks To table...");
        var len = tasks.length;
        var table = document.getElementById("tableTask");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        for(var i = 0; i < len; i++){
            var id = tasks[i].id;
            var assigned = tasks[i].assignedTo;
            var complete = tasks[i].completedBy;
            if(assigned == null) assigned = "-";
            if(complete == null) complete = "-";

            $("#tableTask").append(
                "<tr id='row"+i+"'>" +
                // "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + tasks[i].description + "</td>" +
                "<td>" + assigned + "</td>" +
                "<td>" + complete + "</td>" +
                "<td>" + tasks[i].deadline + "</td>" +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                "</tr>"
            );
            if(tasks[i].status===2){
                $("#row"+i).addClass('boughtMarked');
            }
        }
        console.log("Tasks addded!");
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

    function createTask(desc, dead) {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/scrum/rest/groups/' + groupId + '/task/',
            dataType: "json",
            data: JSON.stringify({
                description: desc,
                deadline: dead,
                partyId: groupId
            }),
            contentType: "application/json;charset=UTF-8",
            success: function (data, status) {
                if (status === "success")   console.log("Task added!");
                if (status === "error")     console.log("Could not add Task");
            }
        });
    }
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
            console.log(deadline);
            if(description === '' || description === undefined || description === null){
                alert("You have to give the task a description");
                return;
            }
            console.log("Adding task " + name + "...");

            createTask(description, deadline);

            $("#page-content").load("http://localhost:8080/scrum/GroupDashboard.html#tasks");
        });
    });

    $("#delete_shoppinglist").click(function(){
        if(items.length !== 0){
            alert("Shoppinglist must be empty before deleting");
        } else {
            var toBeDeleted = lists[currentShoppingList].id;
            $.ajax({
                url: '/scrum/rest/groups/' + currentGroup + '/shoppingLists/' + toBeDeleted,
                type: 'DELETE',
                data: toBeDeleted,
                contentType: "application/json; charset=utf-8",
                dataType: "json",

                success: function(){
                    currentShoppingList = 0;
                    loadShoppingListsFromGroup(currentGroup);
                    alert("Shoppinglist deleted!");
                },
                error: function(){
                    console.log("Couldn't delete shoppinglist with id " + toBeDeleted);
                }
            });
        }
    });
});