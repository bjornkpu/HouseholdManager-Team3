$(document).ready(function() {

    var disbursementList = [];
    var shoppingListArray = []; //heter det for ikke å blandes med javaklassen shoppinglist



    $('#goToDisbursements').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        listOfDisbursements.style.display ="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
    })

    $('#addItem').click(function () {
        var name=prompt("Add item:","Item name");
        if (name!=null){
            x= name + " registered!";
            alert(x);
        }
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
    })

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
        // console.log("data:");
        // console.log(data);
        // console.log(data.length);

        var i = 0;
        var len = data.length;
        for (; i < len; ) {
            $('#shoppinglistdropdown').append('<li role="presentation"><a role="menuitem" tabindex="-1" href="#" >' +
                data[i].name + '</a></li>'
            );
            i++;
        }
    }

    $('#menu1').click(function () {
        console.log("menu1 pressed")
        $('#shoppinglistdropdown').empty();
        var url='http://localhost:8080/scrum/rest/groups/'+1+'/shoppingLists';
        $.get(url, function(data, status){
            renderShoppingListDropdownMenu(data)
            if(status === "success"){
                console.log("External content loaded successfully!");
            }
            if(status === "error"){
                console.log("Error in loading content");
            }
        });
    });

    $('#menu1.dropdown-item').click(function () {
        alert("link: ");
    });

    //function which lists out information on the choosen shoppinglist
    function renderShoppingListInfo(data) {
        var list = data == null ? [] : (data instanceof Array ? data : [data]);
        shoppingListArray = [];
        var itemArray = [];
        $.each(list, function(index, Shoppinglist) {
            itemArray.push({
                "name": Item.name,
                "status": Item.status,
            });
        });
        console.log(shoppingListArray);
        $.each(itemArray, function (index, Shoppinglist) {
            var scopeNr = 1; //itemNr

                //iteme i shoppingarray og alle item
                $('#tableShoppinglist').append(
                    '<tr>' +
                    '<th scope="row">' + scopeNr + ' </th>' +
                    '<td>' + Shoppinglist.name + '</td>' +
                    '<td >' + Shoppinglist.status + '</td>' +
                    '</tr>');

                console.log("koden kom til bunnen av render info about each shoppinglist");
                scopeNr++; //disbursementNr increment on each new list
            });
        }
});
