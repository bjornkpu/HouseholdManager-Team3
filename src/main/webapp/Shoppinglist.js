var currentGroup;
var currentGroupName;
// var paymentRequests=[];
var currentUser;
// var disbursementList=[];
// var balanceList=0;
var checkedItems = [];
$(document).ready(function() {

    var lists;
    var items;
    var currentShoppingList = 0;
    var numberOfMembers = 0;
    currentGroupName = getCookie("groupName");
    var newPaymentUser = 0;
    currentGroup = getCookie("currentGroup");
    currentUser = getCookie("userLoggedOn");
    renderUserListDropdownMenu(getUsers());



    loadShoppingListsFromGroup(currentGroup);

    $('#createDisbursementButton').click(function () {
       var menuShoppinglist = document.getElementById('menuShopinglist');
       var createReceipts = document.getElementById('creatingDisbursement');
       var creatingShoppinglist = document.getElementById('creatingShoppinglist');
        var overviewShoppinglist = document.getElementById('overviewShoppinglist');
        overviewShoppinglist.style.display="none";
       creatingShoppinglist.style.display="none";
       menuShoppinglist.style.display="none";
       createReceipts.style.display="block";
        renderCreateDisbursement();
    });

    $('#createShoppinglistButton').click(function () {
        var menuShoppinglist = document.getElementById('menuShopinglist');
        var createReceipts = document.getElementById('creatingDisbursement');
        var creatingShoppinglist = document.getElementById('creatingShoppinglist');
        var overviewShoppinglist = document.getElementById('overviewShoppinglist');
        overviewShoppinglist.style.display="none";
        creatingShoppinglist.style.display="block";
        menuShoppinglist.style.display="none";
        createReceipts.style.display="none";
    });

    $('#overviewOfShoppinglistsButton').click(function () {
        var menuShoppinglist = document.getElementById('menuShopinglist');
        var createReceipts = document.getElementById('creatingDisbursement');
        var creatingShoppinglist = document.getElementById('creatingShoppinglist');
        var overviewShoppinglist = document.getElementById('overviewShoppinglist');
        overviewShoppinglist.style.display="block";
        creatingShoppinglist.style.display="none";
        menuShoppinglist.style.display="none";
        createReceipts.style.display="none";
    });



    function getUsers(){
        var users = [];
        var url='http://localhost:8080/scrum/rest/groups/'+currentGroup+'/members';
        $.ajax({
            url: url,
            type: 'GET',
            success: function(json){
                users = json;
            },
            async: false
        });
        return users;
    }

    function renderUserListDropdownMenu(data) {
        var len = data.length;
        if(len === 0){
            $('#usersdropdown').append('<li tabindex="-1" class="list" role="presentation" style="text-align: center">' +
                'Empty</li>');
            return;
        }
        for (var i = 0; i < len;i++ ) {
            $('#usersdropdown').append('<li tabindex="-1" class="list" role="presentation"><a class="link" role="menuitem" id="'+data[i].email+'" href="#">' +
                data[i].name + '</aclass></li>'
            );
        }
    }

    $('#backToShoppinglist').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');


        listOfDisbursements.style.display ="none";
        shoppinglist.style.display="block";
        dropdownShoppinglist.style.display="block";
    });

    $('#addItem').click(function () {
        var name=prompt("Add item:");
        if(name == null){
            return;
        }

        $.ajax({
            type: "POST",
            url: "rest/groups/"+ currentGroup+"/shoppingLists/"+lists[currentShoppingList].id+"/items",
            data: JSON.stringify(
                {
                    name: htmlEntities(name),
                    status: 0,
                    shoppingListId: lists[currentShoppingList].id,
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

        getItemsInShoppingList();
    });

    $('#deleteItems').click(function() {
        var checked=getCheckedItems();
        // AJAX Request
        $.ajax({
            url: '/scrum/rest/groups/' +currentGroup + '/shoppingLists/items/',
            type: 'DELETE',
            data: JSON.stringify(checked),
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function(response){
                var table_length = $('#shoppingTable tr').length;
                for (var i =0; i<table_length;i++){
                    if($("#checkbox"+i).is(':checked')){
                        $("#checkbox"+i).closest('tr').remove();
                    }
                }
                alert("Items deleted from shoppinglist");
            },
            error: function(){
                console.log(item.value);
            }
        });
    });

    //TODO om man adder nye items kan disse ikke bli assigned

    $('#assignItem').click(function() {
        var checked=getCheckedItems();
        console.log("alle som er marked: ");
        // AJAX Request
        $.ajax({
            type: "Put",
            url: '/scrum/rest/groups/' +currentGroup + '/shoppingLists/items/1/',
            data: JSON.stringify(checked),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(){
                // var table_length = $('#shoppingTable tr').length;
                // for (var i =0; i<table_length;i++){
                //     if($("#checkbox"+i).is(':checked')){
                //
                //         $("#checkbox"+i).closest('tr').addClass('boughtMarked');
                //     }
                // }
                setItemStatus(1, checked);
                setItemsInTable();
                checkedItems.length = 0;
                console.log(checkedItems);

            },
            error: function(){
                console.log(items.valueOf());
            }
        });
    });

//TODO denne fungerer ikke
    $('#unmarked').click(function(){
        var checked=getCheckedItems();

        console.log(checked);

        // AJAX Request
        $.ajax({
            type: "Put",
            url: '/scrum/rest/groups/' +currentGroup + '/shoppingLists/items/0/',
            data: JSON.stringify(checked),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(){
                // var table_length = $('#shoppingTable tr').length;
                // for (var i =0; i<table_length;i++){
                //     if($("#checkbox"+i).is(':checked')){
                //         $("#checkbox"+i).closest('tr').removeClass("boughtMarked");
                //     }
                // }
                console.log("Success");
                setItemStatus(0, checked);
                setItemsInTable();
                checkedItems.length = 0;
                console.log(checkedItems);
            },
            error: function(response){
                console.log("Error");
            }
        });
    });

    $('#createShoppinglistButton').click(function () {
        var creatingShoppinglist = document.getElementById('creatingShoppinglist');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        creatingShoppinglist.style.display="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";

        var usersInGroup = getUsers();
        usersInGroup.sort(compare);

        var selectedUsers = [];
        var index = 0;

        // $('.ui.search')
        //     .search({
        //         source: usersInGroup,
        //         searchFields: [
        //             'email',
        //             'name'
        //         ],
        //         fields:{
        //             title: 'email',
        //             description: 'name'
        //         }
        //     })
        // ;

        for(var i = 0; i < usersInGroup.length; i++){
            $(".search_results").append("<div class='links' id='link_" + i + "'>" + usersInGroup[i].name + "" +
                "<br><span class='email_span'>" + usersInGroup[i].email +"</span></div>");
        }

        usersDropdown();

        $('#confirmShoppinglist').click(function(){
            var name = $("#nameOfShoppinglist").val();
            if(name === '' || name === undefined || name === null){
                alert("You have to give the shoppinglist a name");
                return;
            }
            var userList = [];

            //TODO clean this up
            for(var i = 0; i < selectedUsers.length; i++){
                if(selectedUsers[i] === 'empty') {
                    console.log("SKIP FOUND");
                    userList[i] = {
                        email: 'SKIP',
                        name: null,
                        phone: null,
                        password: null,
                        salt: null
                    };
                } else {
                    userList[i] = {
                        email: selectedUsers[i].email,
                        name: null,
                        phone: null,
                        password: null,
                        salt: null
                    };
                    console.log("Adding user: " + userList[i].email + "...");
                }
            }
            createShoppingList(name, userList);

            $("#page-content").load("Shoppinglist.html");
        });
        function usersDropdown(){
            $(".search_results").on('click', '.links', function(){
                var user = usersInGroup[this.id.split("_").pop()];

                var addedBefore = false;
                for(var i = 0; i < selectedUsers.length; i++){
                    if (selectedUsers[i].email === user.email){
                        addedBefore = true;
                    }
                }
                if(addedBefore){
                    $("#addedUser").text(user.name + " is already added!");
                    return;
                }

                selectedUsers[index] = user;

                // $("#addedUser").text("Added user " + user.name + ", with email " + user.email);

                $("#addedUsers").append("<table><tr><td>" + user.name + "</td>" +
                    "<td><button class='b button' id='" + index + "'>remove</td></tr></button></table>");
                index++;
            });
        }

        $('#addedUsers').on('click', 'button.b', function(){
            console.log(selectedUsers[this.id].name + ' er fjernet!');
            selectedUsers[this.id] = 'empty';
            reloadList();
        });

        function reloadList(){
            $("#addedUsers").empty();
            for(var i = 0; i < selectedUsers.length; i++){
                if(selectedUsers[i] === "empty") continue;
                $("#addedUsers").append("<li>" + selectedUsers[i].name + "" +
                    "<button class='b button' id='" + i + "'>Remove</button></li>");
            }
        }
    });

    function createShoppingList(name, participants){
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/scrum/rest/groups/'+currentGroup+'/shoppingLists/addShoppinglist',
            data: JSON.stringify(
                {
                    name: name,
                    groupId: currentGroup,
                    itemList: null,
                    userList: participants
                }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(){
                console.log("Shoppinglist added!");
            },
            error: function(){
                console.log("Could not add shoppinglist");
            }
        });
    }

    $("#delete_shoppinglist").click(function(){
        if(!itemStatusCheck()){
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

    function itemStatusCheck(){
        var notBought = true;
        for(var i = 0; i < items.length; i++){
            console.log("i: " + i + ", " + items[i].status);
            if(items[i].status === 0 || items[i].status === 1) {
                notBought = false;
                break;
            }
        }
        return notBought;
    }

/**
    $('#createDisbursementButton').click(function () {
        renderCreateDisbursement();
        var creatingDisbursement =document.getElementById('creatingDisbursement');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        creatingDisbursement.style.display="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
    });
*/


    //function to set memberlist for createDisbursement
    function renderCreateDisbursement() {
        console.log("Rendering Create Disbursement ShoppingListId: "+lists[currentShoppingList].id);
        $.ajax({
            type: "GET",
            url: "rest/groups/"+ currentGroup+"/shoppingLists/" + lists[currentShoppingList].id + "/users/",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                $('#shoppinglistMembers').replaceWith(" <tr id=\"shoppinglistMembers\"></tr>");
                numberOfMembers = data.length;
                for (var i = 0; i < data.length; i++) {
                    $('#shoppinglistMembers').append("<tr><td>" + data[i].name + "</td><td><input id='memberCheckbox"+i
                        +"' value='" + data[i].email + "' type='checkbox' checked </td></tr>")
                }
            }
        });
    }

    //function for creating disbursement
    $('#confirmDisbursement').click( function () {

        // AJAX Request
        $.ajax({
            type: "POST",
            url: 'rest/groups/' +currentGroup + '/disbursement/',
            data: JSON.stringify(
                {
                    items: getCheckedItems(),
                    payer: {email: getCookie("userLoggedOn")},
                    participants: getCheckedMembers(),
                    name: htmlEntities($('#nameOfDisbursement').val()),
                    disbursement: htmlEntities($('#totalAmount').val())
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function(){
                var creatingDisbursement =document.getElementById('creatingDisbursement');
                var shoppinglist = document.getElementById('shoppinglist');
                var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

                creatingDisbursement.style.display="none";
                shoppinglist.style.display="block";
                dropdownShoppinglist.style.display="block";
            },
            error: function(){
                var disb = {
                    items: getCheckedItems(),
                    payer: {email: getCookie("userLoggedOn")},
                    participants: getCheckedMembers(),
                    name: htmlEntities($('#nameOfDisbursement').val()),
                    disbursement: htmlEntities($('#totalAmount').val())
                };
                console.log(disb.valueOf())
            }
        });
    });

    //function which lists out the different shoppinglist into the dropdown menu
    function renderShoppingListDropdownMenu(data) {
        var len = data.length;
        if(len === 0){
            $('#shoppinglistdropdown').append('<li tabindex="-1" class="list" role="presentation" style="text-align: center">' +
                'Empty</li>');
            return;
        }
        for (var i = 0; i < len;i++ ) {
            $('#shoppinglistdropdown').append('<li tabindex="-1" class="list" role="presentation"><a class="link" role="menuitem" id="'+i+'" href="#">' +
                data[i].name + '</a></li>'
            );
        }
    }

    function loadShoppingListsFromGroup(groupId){
        console.log("Loading from group " + groupId + "...");
        $('#shoppinglistdropdown').empty();
        var url='http://localhost:8080/scrum/rest/groups/'+groupId+'/shoppingLists/user';
        $.get(url, function(data, status){
            if(status === "success"){
                console.log("ShoppingList content from user in group " + groupId + " loaded successfully!");
                lists = data;
                //Here to prevent undefined variables and methods out of order
                if(data === null || data.size === 0 || data[0] === undefined){
                    $("#shoppinglistName").text("No shoppinglists available");
                } else {
                    $("#shoppinglistName").text(data[0].name);
                    getItemsInShoppingList(groupId);
                }
                $("#you_shoppinglists").text("Shoppinglists for " + currentGroupName);
                renderShoppingListDropdownMenu(data);
            }
            if(status === "error"){
                console.log("Error in loading ShoppingList content");
            }
        });
    }

    //When clicking
    $("#shoppinglistdropdown").on("click", "a.link", function(){
        currentShoppingList = this.id;
        renderShoppingListInformation(this.id);
    });

    function renderShoppingListInformation(id){
        $("#tableShoppinglist").empty();

        getItemsInShoppingList(currentGroup);

        $("#shoppinglistName").text(lists[id].name);
    }

    function getItemsInShoppingList(){
        var url='http://localhost:8080/scrum/rest/groups/'+currentGroup+'/shoppingLists/'+lists[currentShoppingList].id+'/items';
        //var url='http://localhost:8080/scrum/rest/groups/'+currentGroup+'/shoppingLists/'+1+'/items';

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
        var table = document.getElementById("tableShoppinglist");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        //
        //status 1: x skal kjøpe
        //status 2: x har kjøpt

        for(var i = 0; i < len; i++){
            var id = items[i].id;
            var statusName;

            if (items[i].status === 0){
                statusName ="-";
                $("#row"+i).removeClass('boughtMarked');
            }
            else if (items[i].status === 1){
                statusName ="To be bought";
                $("#row"+i).addClass('boughtMarked');
            }
            else if (items[i].status === 2){
                //er status 3 er allerede varen betalt og satt på en kvittering
                continue;
            }

            $("#tableShoppinglist").append(
                "<tr class='item' id='row"+i+"'>" +
                "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + items[i].name + "</td>" +
                "<td>" + statusName + "</td>" +
                "</tr>"
            );
        }
    }

    function getCheckedItems(){
        var table_length = $('#shoppingTable tr').length;
        // for (var i =0; i<table_length;i++){
        //     console.log("I: " + i);
        //     if($("#checkbox"+i).is(':checked')){
        //         var item = {id: $("#checkbox"+i)[0].value};
        //         checked.push(item);
        //     }
        // }return checked;
        var checked = [];
        var skip = 0;
        for(var i = 0; i < checkedItems.length; i++){
            if(checkedItems[i] === "checked"){
                checked[i-skip] = items[i];
            } else {
                skip++;
            }
        }
        return checked;
    }

    function setItemStatus(status, checked){
        var index = 0;
        for(var i = 0; i < items.length; i++){
            if(index >= checked.length){
                break;
            }
            if(items[i].id === checked[index].id) {
                items[i].status = status;
                index++;
                console.log(items[i].status);
            }
        }
    }

    function getCheckedMembers() {
        var members = [];
        for(var i = 0;i<numberOfMembers; i++){
            if($("#memberCheckbox"+i).is(':checked')) {
                members.push({email: $("#memberCheckbox"+i)[0].value});
            }
        }
        return members;
    }

    $('#shoppingTable').on('click', 'tr.item', function(){
        var id = this.id.split('w').pop();
        if(checkedItems[id] === "checked"){
            checkedItems[id] = "";
            $('#shoppingTable #'+this.id).css('background', 'grey');
        } else {
            checkedItems[id] = "checked";
            $('#shoppingTable #'+this.id).css('background', 'red');
        }
    });

    // $('[data-toggle="tooltip"]').tooltip();

});

function compare(a,b) {
    if (a.name.toLowerCase() < b.name.toLowerCase())
        return -1;
    if (a.name.toLowerCase() > b.name.toLowerCase())
        return 1;
    return 0;
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}





