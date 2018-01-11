

$(document).ready(function() {

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
});



