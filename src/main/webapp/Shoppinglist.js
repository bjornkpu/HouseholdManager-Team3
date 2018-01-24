var currentGroup;
var paymentRequests;
var currentUser;
$(document).ready(function() {

    var disbursementList;
    var lists;
    var items;
    var currentShoppingList = 0;
    var numberOfMembers = 0;
    var balanceList=0;
    currentGroup = getCookie("currentGroup");
    currentUser = getCookie("userLoggedOn");


    loadShoppingListsFromGroup(currentGroup);

    function fixDisbursementTable(){
        var acceptedString;

        var len = disbursementList.length;
        var table = document.getElementById("disbursementTable");
        console.log("found table");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        $("#disbursementTable").append(
            "<tr>"+
            "<th>#</th>"+
            "<th>Reciet</th>"+
            "<th>Participants</th>"+
            "<th>Cost</th>"+
            "<th>Day/Month/Year</th>"+
            "<th>Buyer</th>"+
            "</tr>"
        );

        for(var i = 0; i < len; i++){
            console.log(disbursementList[i].accepted);
            if(disbursementList[i].accepted === 0){
                acceptedString = "<button value='"+disbursementList[i].id+"' onclick='respondToDisbursement(this,1)'>Accept</button><button value='"+disbursementList[i].id+"' onclick='respondToDisbursement(this,2)'>Decline</button>";
            } else {acceptedString =  "Accepted"}
            var participantsList = disbursementList[i].participants;
            var participantsString = "";
            for(var j=0;j<participantsList.length;j++){
                participantsString+=participantsList[j].name + ", ";
            }
            var dispDate = new Date(disbursementList[i].date);
            var month = dispDate.getUTCMonth() + 1; //months from 1-12
            var day = dispDate.getUTCDate();
            var year = dispDate.getUTCFullYear();
            var d = day + "/" + month + "/" + year;

            $("#disbursementTable").append(
                "<tr>"+
                "<td scope=\"row\">"+(i+1)+"</td>"+
                "<td>"+disbursementList[i].name+"</td>"+
                "<td>"+participantsString+"</td>"+
                "<td>"+disbursementList[i].disbursement+"</td>"+
                "<td>"+d+"</td>"+
                "<td>"+disbursementList[i].payer.name+"</td>"+
                "<td>"+acceptedString+"</td>"+
                "</tr>"
            );
        }
    }

    function getDisbursementList(){
        var url='http://localhost:8080/scrum/rest/groups/' + currentGroup + '/disbursement/'+ currentUser + '/user';

        $.get(url, function(data, status){
            console.log("skrrt");
            if (status === "success") {
                disbursementList = data;
                fixDisbursementTable();
                console.log("Item content loaded successfully!");
            }
            if(status === "error"){
                console.log("Error in loading Item content");
            }
        });
    }

    function getUserBalance(){
        var url='http://localhost:8080/scrum/rest/groups/balance/'+currentGroup;

        $.get(url, function(data, status){
            console.log("skrrt");
            if (status === "success") {
                balanceList = data;
                fixBalanceTable();
                console.log("Item content loaded successfully!");
            }
            if(status === "error"){
                console.log("Error in loading Item content");
            }
        });
    }

    function getPaymentRequests(){
        var url='http://localhost:8080/scrum/rest/groups/payment/'+ 1 +'/'+'en@h.no';

        $.get(url, function(data, status){
            console.log("skrrt");
            if (status === "success") {
                paymentRequests = data;
                fixPaymentRequestsTable();
                console.log(paymentRequests);
                console.log("Number of payments loaded successfully!");
            }
            if(status === "error"){
                console.log("Error in loading number of payments content");
            }
        });
    }

    function fixBalanceTable(){
        var len = balanceList.length;
        var table = document.getElementById("balanceTable");
        console.log("found table");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        $("#balanceTable").append(
            "<tr>"+
            "<th>User</th>"+
            "<th>Balance</th>"+
            "</tr>"
        );

        for(var i = 0; i < len; i++){
            $("#balanceTable").append(
                "<tr>"+
                "<th scope=\"row\">"+balanceList[i].key+"</th>"+
                "<th>"+balanceList[i].value+"</th>"+
                "</tr>"
            );
        }
        console.log("Added Items");
    }

    function fixPaymentRequestsTable(){
        var len = paymentRequests.length;
        console.log(paymentRequests);
        var table = document.getElementById("paymentRequests");
        console.log("found table");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }
        $("#paymentRequests").append(
            "<tr>"+
            "<th>User</th>"+
            "<th>Amount</th>"+
            "<th></th>"+
            "</tr>"
        );

        for(var i = 0; i < len; i++){
            var table1 = [];
            table1[0]=paymentRequests[i].id;
            table1[1]=paymentRequests[i].amount;
            table1[2]=paymentRequests[i].payer;
            $("#paymentRequests").append(
                "<tr>"+
                "<th scope=\"row\">"+paymentRequests[i].payerName+"</th>"+
                "<th>"+paymentRequests[i].amount+"</th>"+
                "<th><button class='acceptPayment' value='"+table1+"' onclick='acceptPaymentsClick(this)'>Accept Payment</button></th>"+
                "</tr>"
            );
        }
        console.log("Added Itempp");
    }



    $('#viewPaymentRequests').click(function(){
        var table = document.getElementById("paymentRequests");
        var button = document.getElementById("viewPaymentRequests");
        if(table.rows>0){
            button.innerHTML = 'View Payment Requests';
            while(table.rows.length > 0) {
                table.deleteRow(0);
            }
        }
        else{
            button.innerHTML = 'Hide Payment Requests';
            getPaymentRequests();
        }
    });

    $('#goToDisbursements').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');
        var table = document.getElementById("paymentRequests");
        console.log("found table");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }

        listOfDisbursements.style.display ="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
        getDisbursementList();
        getUserBalance();
        paymentRequests.innerHTML = 'Payment Requests';
    });

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
            url: "rest/groups/1/shoppingLists/"+lists[currentShoppingList].id+"/items",
            data: JSON.stringify(
                {
                    name: htmlEntities(name),
                    status: 1,
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

        getItemsInShoppingList(1);
    });

    $('#deleteItems').click(function() {
        var checked=getCheckedItems();
        // AJAX Request
        $.ajax({
            url: '/scrum/rest/groups/' +1 + '/shoppingLists/items/',
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

    $('#toBeBought').click(function() {
        var checked=getCheckedItems();
        // AJAX Request
        $.ajax({
            type: "Put",
            url: '/scrum/rest/groups/' +currentGroup + '/shoppingLists/items/'+2+'/',
            data: JSON.stringify(checked),
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function(){
                var table_length = $('#shoppingTable tr').length;
                for (var i =0; i<table_length;i++){
                    if($("#checkbox"+i).is(':checked')){
                        $("#checkbox"+i).closest('tr').addClass('boughtMarked');
                    }
                }
                alert("Items marked");
            },
            error: function(){
                console.log(items.valueOf());
            }
        });
    });

    $('#unmarked').click(function(){
        var checked=getCheckedItems();
        // AJAX Request
        $.ajax({
            type: "Put",
            url: '/scrum/rest/groups/' +currentGroup + '/shoppingLists/items/'+1+'/',
            data: JSON.stringify(checked),
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function(response){
                var table_length = $('#shoppingTable tr').length;
                for (var i =0; i<table_length;i++){
                    if($("#checkbox"+i).is(':checked')){
                        $("#checkbox"+i).closest('tr').removeClass("boughtMarked");
                    }
                }
                alert("Items deleted from shoppinglist");
            },
            error: function(){
                console.log(item.value);
            }
        });
    });


    // $('.backToShoppinglist').click(function () {
    //     var listOfDisbursements = document.getElementById('listOfDisbursements');
    //     var shoppinglist = document.getElementById('shoppinglist');
    //     var creatingShoppinglist =document.getElementById('creatingShoppinglist');
    //     var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');
    //     var creatingDisbursement =document.getElementById('creatingDisbursement');
    //
    //     listOfDisbursements.style.display ="none";
    //     shoppinglist.style.display="block";
    //     creatingShoppinglist.style.display="none";
    //     dropdownShoppinglist.style.display="block";
    //     creatingDisbursement.style.display="none";
    //
    // });

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

    $('#createShoppinglistButton').click(function () {
        var creatingShoppinglist = document.getElementById('creatingShoppinglist');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        creatingShoppinglist.style.display="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";

        var usersInGroup = getUsers();

        var selectedUsers = [];
        var index = 0;

        $('.ui.search')
            .search({
                source: usersInGroup,
                searchFields: [
                    'email',
                    'name'
                ],
                fields:{
                    title: 'email',
                    description: 'name'
                }
            })
        ;

        $("#addUserButton").click(function(){
            var user = htmlEntities($(".ui.search").search('get value'));
            var isUser = false;
            for(var i = 0; i < usersInGroup.length; i++){
                if(usersInGroup[i].email === user){
                    isUser = true;
                    break;
                }
                if(usersInGroup[i].name.toLowerCase() === user.toLowerCase()){
                    user = usersInGroup[i].email;
                    isUser = true;
                    break;
                }
            }
            if(!isUser){
                $("#addedUser").text(user + " is not a member!");
                return;
            }
            var selectedBefore = false;
            for(var i = 0; i < selectedUsers.length; i++){
                if (selectedUsers[i] === user){
                    selectedBefore = true;
                }
            }
            if(selectedBefore){
                $("#addedUser").text(user + " already added!");
                return;
            }

            selectedUsers[index] = user;
            $("#addedUser").text("Added user with email " + user);
            index++;
        });

        $('#confirmShoppinglist').click(function(){
            var name = htmlEntities($("#nameOfShoppinglist").val());
            if(name === '' || name === undefined || name === null){
                alert("You have to give the shoppinglist a name");
                return;
            }
            var userList = [];
            console.log("Adding shoppinglist " + name + "...");
            for(var i = 0; i < selectedUsers.length; i++){
                userList[i] = {
                    email: selectedUsers[i],
                    name: null,
                    phone: null,
                    password: null,
                    salt: null
                };
                console.log("Adding user: " + userList[i].email + "...");
            }
            createShoppingList(name, userList);

            $("#page-content").load("Shoppinglist.html");
        });
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


    $('#createDisbursementButton').click(function () {
        renderCreateDisbursement();
        var creatingDisbursement =document.getElementById('creatingDisbursement');
        var shoppinglist = document.getElementById('shoppinglist');
        var dropdownShoppinglist = document.getElementById('dropdownShoppinglist');

        creatingDisbursement.style.display="block";
        shoppinglist.style.display="none";
        dropdownShoppinglist.style.display="none";
    });

    //finds all disbursements
    /*
    function findAllDisbursements() {
        console.log('findDisbursements');
        $.ajax({
            type: 'GET',
            url: '/scrum/Disbursement',
            dataType: "json",
            success: renderDisbursementsList(),

        });
    }*/

    //function which lists out the different disbursements
    /*
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


        // console.log(bordliste);
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
    }*/


    //function to set memberlist for createDisbursement
    function renderCreateDisbursement() {
        console.log("Rendering Create Disbursement ShoppingListId: "+lists[currentShoppingList].id);
        $.ajax({
            type: "GET",
            url: "rest/groups/1/shoppingLists/" + lists[currentShoppingList].id + "/users/",
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
                data[i].name + '</aclass></li>'
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
                    console.log("Data[0]");
                    $("#shoppinglistName").text(data[0].name);
                    getItemsInShoppingList(groupId);
                }
                $("#you_shoppinglists").text("Shoppinglists for groupId " + currentGroup);
                renderShoppingListDropdownMenu(data);
            }
            if(status === "error"){
                console.log("Error in loading ShoppingList content");
            }
            // if(status === undefined){
            //     console.log("Hva faen");
            // }
        });
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

        for(var i = 0; i < len; i++){
            var id = items[i].id;
            $("#tableShoppinglist").append(
                "<tr id='row"+i+"'>" +
                "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + items[i].name + "</td>" +
                "<td>" + items[i].status + "</td>" +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                "</tr>"
            );
            if(items[i].status===2){
                $("#row"+i).addClass('boughtMarked');
            }
        }
        console.log("Added Items");
    }

    function getCheckedItems(){
        var table_length = $('#shoppingTable tr').length;
        var checked = [];
        for (var i =0; i<table_length;i++){
            if($("#checkbox"+i).is(':checked')){
                var item = {id: $("#checkbox"+i)[0].value};
                checked.push(item);
            }
        }return checked;
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
});
function respondToDisbursement(data,response) {
    // AJAX Request
    console.log(data.value+"  "+response);
    $.ajax({
        type: "PUT",
        url: 'rest/groups/' + currentGroup + '/disbursement/' + getCookie("userLoggedOn") +"/"+ response,
        data: JSON.stringify(
            {id: data.value}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function () {
        }
    });
}

//Used by the buttons generated by function fixPaymentRequests, DON'T DELETE
function acceptPaymentsClick(data){
    var array = data.value.split(",");
    var paymentId = array[0];
    var amount = array[1];
    var payer = array[2];
    console.log("Payment Id=" + paymentId + " Amount=" + amount + " Payer=" + payer);
    $.ajax({
        type: 'PUT',
        url: "http://localhost:8080/scrum/rest/groups/updatePayment/" + paymentId, //test
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function () {
            console.log("accepted paymentt");
        },
        error: function () {
            alert("payment not accepted");
        }
    });

    $.ajax({
        type: 'PUT',
        url: "http://localhost:8080/scrum/rest/groups/updateBalances/" + currentGroup + "/"+ currentUser+"/"+ payer +"/" + amount,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function () {
            console.log("accepted paymentt");
        },
        error: function () {
            alert("payment not accepted");
        }
    })
};





