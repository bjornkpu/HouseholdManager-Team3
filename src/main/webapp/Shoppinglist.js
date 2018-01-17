$(document).ready(function() {

    var group = function () {
        
    };
    var disbursementList = [];
    var lists;
    var items;
    var currentShoppingList = 0;

    $('#goToDisbursements').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        listOfDisbursements.style.display ="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
    });

    function itemDataJSON(){
        return JSON.stringify(
            {
                navn: $("#navnInput").val()
            });
    }

    $('#addItem').click(function () {
        var name=prompt("Add item:","Item name");
        if (name!=null){
            x= name + " registered!";
            alert(x);
        }

        $.ajax({
            type: "POST",
            url: "rest/groups/1/shoppingLists/1/items",
            data: JSON.stringify(
                {
                    name: name,
                    status: 1,
                    shoppingListId: 1,
                    id: 0,
                    disbursementId: -1
                }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function () {
                console.log("Fikk registrert");
            },
            error: function (xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        });

        var tableRef = document.getElementById('shoppingTable').getElementsByTagName('tbody')[0];

// Insert a row in the table at the last row
        var newRow   = tableRef.insertRow(tableRef.rows.length);
        alert(tableRef.rows.length);

// Insert a cell in the row at index 0
        var newCell  = newRow.insertCell(0);

// Append a text node to the cell
        newText  = document.createTextNode(String(tableRef.rows.length));
        newCell.appendChild(newText);
        newCell.scope='row';
        newCell = newRow.insertCell(1);
        var newText  = document.createTextNode(name);
        newCell.appendChild(newText);
        newCell = newRow.insertCell(2);
        newText  = document.createTextNode('unassigned');
        newCell.appendChild(newText);
        newCell = newRow.insertCell(3);
        newCell.innerHTML = '<button type="button"  class="removeItemButton" title="Remove this row">Delete</button>';
    });

    $('.backToShoppinglist').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var shoppinglist = document.getElementById('shoppinglist');
        var creatingShoppinglist =document.getElementById('creatingShoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');
        var creatingDisbursement =document.getElementById('creatingDisbursement');

        listOfDisbursements.style.display ="none";
        shoppinglist.style.display="block";
        creatingShoppinglist.style.display="none";
        dropdownShoppinglist.style.display="block";
        creatingDisbursement.style.display="none";

    })

    $('#createShoppinglistButton').click(function () {
        var creatingShoppinglist =document.getElementById('creatingShoppinglist');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        creatingShoppinglist.style.display="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
    })

    $('#createDisbursementButton').click(function () {
        var creatingDisbursement =document.getElementById('creatingDisbursement');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        creatingDisbursement.style.display="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
    })

    $(document).on('click', 'button.removeItemButton', function () {
        // AJAX Request
        $.ajax({
            url: '/scrum/groups/' +1 + '/shoppingLists/' +shoppingListId + '/items/' + itemId,
            type: 'POST',
            data: { id:id },
            success: function(response){
                alert("Item deleted from shoppinglist");
                $(this).closest('tr').remove();
            }
        });
    });

    //finds all disbursements
    function findAllDisbursements() {
        console.log('findDisbursements');
        $.ajax({
            type: 'GET',
            url: '/scrum/Disbursement',
            dataType: "json",
            success: renderDisbursementsList(),

        });
    }

    //function which lists out the different disbursements
    function renderDisbursementsList(data) {
        var list = data == null ? [] : (data instanceof Array ? data : [data]);
        disbursementList = [];
        $.each(list, function(index, Shoppinglist) {
            disbursementList.push({
                //SHOPPINGLIST ER BARE ET EKS, SKAL BYTTES MED DEN JAVAKLASSEN FOR DISBURSEMENT
                "description": Shoppinglist.description,
                "participants": Shoppinglist.users,
                "cost": Shoppinglist.price,
                "added": Shoppinglist.date,
            });
        });


        console.log(bordliste);
        var scopeNr = 1; //disbursementNr
        $.each(disbursementList, function (index, Shoppinglist) {
            $('#tableDisbursements').append('<tr>' +
                '<th scope="row">' + scopeNr + ' </th>' +
                '<td>' + Shoppinglist.description + '</td>' +
                '<td >' + Shoppinglist.users + '</td>' +
                '<td >' + Shoppinglist.price  + '</td>' +
                '<td >' + Shoppinglist.date + '</td>' +
                '</tr>');

            console.log("koden kom til bunnen av renderDisbursements");
            scopeNr ++; //disbursementNr increment on each new list
        });
    }

    //function which lists out the different shoppinglist into the dropdown menu
    function renderShoppingListDropdownMenu(data) {
        var len = data.length;
        for (var i = 0; i < len;i++ ) {
            $('#shoppinglistdropdown').append('<li tabindex="-1" class="list" role="presentation"><a class="link" role="menuitem" id="'+i+'" href="#">' +
                data[i].name + '</aclass></li>'
            );
        }
    }

    $(document).ready(function () {
        // console.log("menu1 pressed")
        $('#shoppinglistdropdown').empty();
        var url='http://localhost:8080/scrum/rest/groups/'+1+'/shoppingLists';
        $.get(url, function(data, status){
            lists = data;
            // renderShoppingListDropdownMenu(data);
            // getItemsInShoppingList(data[0].id);

            if(status === "success"){
                console.log("ShoppingList content loaded successfully!");
                //Here to prevent undefined variables and methods out of order
                renderShoppingListDropdownMenu(data);
                $("#shoppinglistName").text(data[0].name);
                getItemsInShoppingList(data[0].id);
            }
            if(status === "error"){
                console.log("Error in loading ShoppingList content");
            }
        });
    });

    //When clicking
    $("#shoppinglistdropdown").on("click", "a.link", function(){
        currentShoppingList = this.id;
        renderShoppingListInformation(this.id);
    });

    function renderShoppingListInformation(){
        $("#tableShoppinglist").empty();

        getItemsInShoppingList(lists[currentShoppingList].id);

        $("#shoppinglistName").text(lists[currentShoppingList].name);
    }

    function getItemsInShoppingList(id){
        var url='http://localhost:8080/scrum/rest/groups/'+1+'/shoppingLists/'+id+'/items';

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
        for(var i = 0; i < len; i++){
            $("#tableShoppinglist").append(
                "<tr>" +
                "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + items[i].name + "</td>" +
                "<td>" + items[i].status + "</td>" +
                "<td> <button type=\"button\" class=\"removeItemButton\" title=\"Remove this row\">Delete</button></td>" +
                "</tr>"
            );
        }
        console.log("Added Items");
    }

    //function which lists out information on the choosen shoppinglist
    // function renderShoppingListInfo(data) {
    //     var list = data == null ? [] : (data instanceof Array ? data : [data]);
    //     shoppingListArray = [];
    //     var itemArray = [];
    //     $.each(list, function(index, Shoppinglist) {
    //         itemArray.push({
    //             "name": Item.name,
    //             "status": Item.status,
    //         });
    //     });
    //     console.log(shoppingListArray);
    //
    //     $.each(itemArray, function (index, Shoppinglist) {
    //         var scopeNr = 1; //itemNr
    //
    //             //iteme i shoppingarray og alle item
    //             $('#tableShoppinglist').append(
    //                 '<tr>' +
    //                 '<th scope="row">' + scopeNr + ' </th>' +
    //                 '<td>' + Shoppinglist.name + '</td>' +
    //                 '<td >' + Shoppinglist.status + '</td>' +
    //                 '</tr>');
    //
    //             console.log("koden kom til bunnen av render info about each shoppinglist");
    //             scopeNr++; //disbursementNr increment on each new list
    //         });
    //     }
});
