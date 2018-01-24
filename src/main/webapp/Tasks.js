$(document).ready(function() {
    var groupId;
    var tasks;

    function setTasksInTable() {
        var len = tasks.length;
        var table = document.getElementById("tableTask");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        for(var i = 0; i < len; i++){
            var id = tasks[i].id;
            $("#tableTask").append(
                "<tr id='row"+i+"'>" +
                "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + tasks[i].description + "</td>" +
                "<td>" + tasks[i].assignedTo + "</td>" +
                "<td>" + tasks[i].deadline + "</td>" +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                "</tr>"
            );
            if(tasks[i].status===2){
                $("#row"+i).addClass('boughtMarked');
            }
        }
        console.log("Added Tasks");
    }

    function getTasksInGroup() {
        console.log("Loading tasks from group " + groupId + "...");
        $.get('http://localhost:8080/scrum/rest/groups/' + groupId + 'task/', function(data, status){
            if(status === "success") {
                tasks = data;
                console.log("Tasks loaded successfully!");
                setTasksInTable();
            }
            if(status === "error")      console.log("Error in loading tasks.");
        });
    }

    groupId = getCookie("currentGroup");
    getTasksInGroup();
});