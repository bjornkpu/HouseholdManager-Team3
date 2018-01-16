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

    //finds all shoppinglists
    function getShoppingListByGroupId() {
        var groupId=this.id;
        $.ajax({
            type: 'GET',
            url: '/scrum/groups/'+1+'/ShoppingLists/',
            dataType: "json",
            success: renderShoppingListDropdownMenu(data)

        });
    }
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
        // var list = data == null ? [] : (data instanceof Array ? data : [data]);
        // shoppingListArray = [];
        //adding the names for all shoppinglist into the array shoppingListArray
        // $.each(list, function (data) {
        var obj = JSON.parse(data);
        console.log(obj);
            $('#shoppinglistdropdown').append('<li role="presentation"><a role="menuitem" tabindex="-1" href="#" >' +
                obj.name + '</a></li>'
            );
        // });
    }

    $('#menu1').click(function () {
        console.log("menu1 pressed")
        var url='http://localhost:8080/scrum/rest/groups/'+1+'/shoppingLists';
        $.getJSON(url, function(data, status){
            renderShoppingListDropdownMenu(data)
            if(status == "success"){
                console.log("External content loaded successfully!");
            }
            if(status == "error"){
                console.log("Error in loading content");
            }
        });
    });


    $.each(shoppingListArray, function (index, Shoppinglist) {
        $('#shoppinglistdropdown').append('<li role="presentation"><a role="menuitem" tabindex="-1" href="#" >' +
            shoppingList.name + '</a></li>'
        );
        console.log("koden kom til bunnen av renderShoppinglist");
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
