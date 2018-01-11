$(document).ready(function() {

    var disbursementList = [];
    var shoppingListArray = []; //heter det for ikke å blandes med javaklassen shoppinglist

    $('.dropdown-menu').click(function () {

        var listDisbursements = document.getElementById('listOfDisbursements');
        var addItemButton = document.getElementById('addItem');
        var deleteItemButton = document.getElementById('deleteItem');
        var configureListButton = document.getElementById('configureList');
        var closeListButton = document.getElementById('closeList');
        var shoppinglist = document.getElementById('shoppinglist');
        var backButton = document.getElementById('back');
        var createDisbursement = document.getElementById('createDisbursement');
        var createDisbursementButton = document.getElementById('createDisbursementButton');
        var createShoppinglistButton = document.getElementById('createShoppinglistButton');


        listDisbursements.style.display ="none";
        addItemButton.style.display = "block";
        deleteItemButton.style.display = "block";
        configureListButton.style.display = "block";
        closeListButton.style.display = "block";
        shoppinglist.style.display = "block";
        backButton.style.display = "block";
        createDisbursement.style.display ="none";
        createDisbursementButton.style.display = "none";
        createShoppinglistButton.style.display = "none";
        });

    $('#createDisbursementButton').click(function () {


        var listDisbursements = document.getElementById('listOfDisbursements');
        var addItemButton = document.getElementById('addItem');
        var deleteItemButton = document.getElementById('deleteItem');
        var configureListButton = document.getElementById('configureList');
        var closeListButton = document.getElementById('closeList');
        var shoppinglist = document.getElementById('shoppinglist');
        var shoppinglistButtons = document.getElementById('buttonsForShoppinglist');
        var backButton = document.getElementById('back');
        var createDisbursement = document.getElementById('createDisbursement');
        var createDisbursementButton = document.getElementById('createDisbursementButton');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');
        var headerDisbursement = document.getElementById('headerDisbursement');
        var createShoppinglistButton = document.getElementById('createShoppinglistButton');


        listDisbursements.style.display ="none";
        addItemButton.style.display = "none";
        deleteItemButton.style.display = "none";
        configureListButton.style.display = "none";
        closeListButton.style.display = "none";
        shoppinglist.style.display = "none";
        backButton.style.display = "none";
        createDisbursement.style.display ="block";
        createDisbursementButton.style.display = "none";
        dropdownShoppinglist.style.display = "none";
        headerDisbursement.style.display = "block";
        createShoppinglistButton.style.display = "none";

    });
    $('#createShoppinglistButton').click(function () {


        var listDisbursements = document.getElementById('listOfDisbursements');
        var addItemButton = document.getElementById('addItem');
        var deleteItemButton = document.getElementById('deleteItem');
        var configureListButton = document.getElementById('configureList');
        var closeListButton = document.getElementById('closeList');
        var shoppinglist = document.getElementById('shoppinglist');
        var shoppinglistButtons = document.getElementById('buttonsForShoppinglist');
        var backButton = document.getElementById('back');
        var createDisbursement = document.getElementById('createDisbursement');
        var createDisbursementButton = document.getElementById('createDisbursementButton');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');
        var headerShoppinglist = document.getElementById('headerShoppinglist');
        var headerDisbursement = document.getElementById('headerDisbursement');
        var createShoppinglistButton = document.getElementById('createShoppinglistButton');


        headerShoppinglist.style.display = "block";
        listDisbursements.style.display ="none";
        addItemButton.style.display = "none";
        deleteItemButton.style.display = "none";
        configureListButton.style.display = "none";
        closeListButton.style.display = "none";
        shoppinglist.style.display = "none";
        backButton.style.display = "none";
        createDisbursement.style.display ="block";
        createDisbursementButton.style.display = "none";
        dropdownShoppinglist.style.display = "none";
        headerDisbursement.style.display = "none";
        createShoppinglistButton.style.display = "none";

    });

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

            console.log("koden kom til bunnen av RenderList");
            scopeNr ++; //disbursementNr increment on each new list
        });
    }

    //function which lists out the different shoppinglist in the dropdown menu
    function renderShoppingListDropdownMenu(data) {
        var list = data == null ? [] : (data instanceof Array ? data : [data]);
        shoppingListArray = [];
        $.each(list, function(index, Shoppinglist) {
            shoppingListArray.push({
                "name": Shoppinglist.name,
            });
        });


        console.log(shoppingListArray);
        $.each(shoppingListArray, function (index, Shoppinglist) {
            $('#shoppinglistdropdown').append('<li role="presentation"><a role="menuitem" tabindex="-1" href="#" >' +
                shoppingList.name + '</a></li>'
            );

            console.log("koden kom til bunnen av RenderList");

        });
    }

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
        $.each(disbursementList, function (index, Shoppinglist) {
            var scopeNr = 1; //itemNr
            if(shoppingListArray.item.getName() == itemArray.name) { //denne må endres til å sammenligne
                //iteme i shoppingarray og alle item
                $('#tableShoppinglist').append(
                    '<tr>' +
                    '<th scope="row">' + scopeNr + ' </th>' +
                    '<td>' + Shoppinglist.name + '</td>' +
                    '<td >' + Shoppinglist.status + '</td>' +
                    '</tr>');

                console.log("koden kom til bunnen av RenderList");
                scopeNr++; //disbursementNr increment on each new list
            }
        });
    }
});



