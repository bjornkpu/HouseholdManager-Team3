$(document).ready(function() {
    var groupId = getCookie("currentGroup");
    var tasks;                                  //For rendering the Task List
    var members;

    //Setting up the datatable
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

            var date = new Date(tasks[i].deadline).toString().substr(4, 11);

            $("#tableTask").append(
                "<tr id='row"+i+"'>" +
                // "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + tasks[i].description + "</td>" +
                "<td>" + assigned + "</td>" +
                "<td>" + complete + "</td>" +
                "<td>" + date + "</td>" +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                "</tr>"
            );
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

    //Help methods
    function getCheckedTasks(){
        var table_length = $('#taskTable tr').length;
        var checked = [];
        for (var i =0; i<table_length;i++){
            if($("#checkbox"+i).is(':checked')){
                var task = { id: tasks[i].choreId };
                checked.push(task);
            }
        }return checked;
    }
    function getMemberList() {
        console.log("Loading members from group " + groupId + "...");
        $('#taskAssignDropdown').empty();
        $('#taskCompDropdown').empty();
        var url='http://localhost:8080/scrum/rest/groups/'+groupId+'/shoppingLists/user';
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

            $("#page-content").load("http://localhost:8080/scrum/GroupDashboard.html#tasks");
        });
    });

   /* $("#assignTask").click(function(){
        var checked = getCheckedTasks();
        var url = "http://localhost:8080/scrum/rest/groups/3/task/";

        $.get("http://localhost:8080/scrum/rest/groups/" + groupId + "/members", function (data) {
            members = data;
            for(var i = 0; 0 < members.length; i++){

            }
            console.log(data[0].name)
        });
        var email = prompt(members,"asd");

        // if ()

        for(var i = 0; i < checked.length; i++){
            $.ajax({
                type: "PUT",
                url: url + checked[i].id ,
                dataType: "json",
                data: JSON.stringify({
                    email: email
                }),
                contentType: "application/json;charset=UTF-8",
                success: function (data, status) {
                    if (status === "success")   console.log("Task Deleted!");
                    if (status === "error")     console.log("Could not delete Task");
                }
            });
        }
        $("#page-content").load("http://localhost:8080/scrum/GroupDashboard.html#tasks");
    });*/

    $("#completeTask").click(function(){

        $("#page-content").load("http://localhost:8080/scrum/GroupDashboard.html#tasks");
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
        $("#page-content").load("http://localhost:8080/scrum/GroupDashboard.html#tasks");
    });


});