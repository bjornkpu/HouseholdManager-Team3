$(document).ready(function() {
    var list;
    var items;
    var currentTaskMode = 1;

    $(document).ready(function () {
        var url='http://localhost:8080/scrum/rest/groups/'+getCookie("currenGroup")+'task/';
        $.get(url, function(data, status){
            list = data;

            if(status === "success"){
                console.log("Task content loaded successfully!");
                renderTaskInformation(data);
            }
            if(status === "error"){
                console.log("Error in loading ShoppingList content");
            }
        });
    });

//When clicking
    $("#taskdropdown").on("click", "a.link", function(){
        // currentTaskMode = lists[this.id].id;
        renderTaskInformation(currentTaskMode);
    });

    function renderTaskInformation(data){
        $("#tableTask").empty();

        getTasks(data);

        // $("#TaskName").text(lists[id].name);
    }

    function getTasks(id){
        var url='http://localhost:8080/scrum/rest/groups/'+3+'/shoppingLists/'+id+'/items';

        $.get(url, function(data, status){
            if(status === "success"){
                items = data;
                console.log("Item content loaded successfully!");
                setItemsInTable();
            }
            if(status === "error"){
                console.log("Error in loading Item content");
            }
        });
    }

    function setItemsInTable(){
        var len = items.length;
        var table = document.getElementById("tableTask");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }

        for(var i = 0; i < len; i++){
            var id = items[i].id;
            $("#tableTask").append(
                "<tr>" +
                "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + items[i].name + "</td>" +
                "<td>" + items[i].status + "</td>" +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                "</tr>"
            );
        }
        console.log("Added Items");
    }
});