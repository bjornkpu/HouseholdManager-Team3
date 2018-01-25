var currentGroup;
var currentGroupName;
var paymentRequests=[];
var currentUser;
var disbursementList=[];
var balanceList=0;

$(document).ready(function () {
    var lists;
    var items;
    var currentShoppingList = 0;
    var numberOfMembers = 0;
    currentGroup = getCookie("currentGroup");
    currentUser = getCookie("userLoggedOn");
    var newPaymentUser = 0;


    //functions to call on page load
    renderUserListDropdownMenu(getUsers());
    var usersInGroup = getUsers();
    usersInGroup.sort(compare);

    $('#goToDisbursements').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var goToDisbursementsButton = document.getElementById('goToDisbursements');
        var goToBalanceButton = document.getElementById('goToBalance');
        var divForBalance = document.getElementById('divForBalance');
        var requestButton = document.getElementById('sendPaymentRequest');
        requestButton.style.display="none";
        goToBalanceButton.style.display="block";
        listOfDisbursements.style.display ="block";
        goToDisbursementsButton.style.display="none";
        divForBalance.style.display="none";
        getDisbursementList();
    });

    $('#goToBalance').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var goToDisbursementsButton = document.getElementById('goToDisbursements');
        var goToBalanceButton = document.getElementById('goToBalance');
        var divForBalance = document.getElementById('divForBalance');
        var requestButton = document.getElementById('sendPaymentRequest');
        requestButton.style.display="block";
        goToBalanceButton.style.display="none";
        listOfDisbursements.style.display ="none";
        goToDisbursementsButton.style.display="block";
        divForBalance.style.display="block";
        getUserBalance();
        getPaymentRequests();
    });


    function respondToDisbursement(data,response) {
        // AJAX Request
        console.log(data.value+"  "+response);
        console.log("knut");
        $.ajax({
            type: "PUT",
            url: 'rest/groups/' + currentGroup + '/disbursement/' + getCookie("userLoggedOn") +"/"+ response,
            data: JSON.stringify(
                {id: data.value}),
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function () {
                getDisbursementList();
            }
        });
    }
    $('#sendPaymentRequest').click(function () {
        if(newPaymentUser===0){
            alert("Must select user from dropdown");
            return;
        }
        var amount1=prompt("Amount(must be a number, eks: 200.34):");
        if(amount1 == null){
            return;
        }

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/scrum/rest/groups/newPayment",
            data: JSON.stringify(
                {
                    payer: currentUser,
                    receiver: newPaymentUser,
                    amount: amount1,
                    party: currentGroup
                }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function () {
                $("#alertSentPayment").fadeTo(4000, 500).slideUp(500, function () {
                    $("#alertSentPayment").slideUp(500);
                });
                console.log("Payment sent");
            },
            error: function (xhr, resp, text) {
                console.log(xhr, resp, text);
            }
        });
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

    $("#usersdropdown").on("click", "a.link", function(){
        newPaymentUser = this.id;
        //var uName = document.getElementById("userdropdown").innerHTML;
        document.getElementById("emailSpan").innerHTML = "User selected: " + newPaymentUser;
        console.log(newPaymentUser);
    });

});
///end of docu ready

function fixDisbursementTable(){
    var acceptedString;
    var len = disbursementList.length;
    var table = document.getElementById("disbursementTable");
    console.log("found table");
    while(table.rows.length > 0) {
        table.deleteRow(0);
    }
    $("#disbursementTable").append(
        "<tr class='truncateTableHeader'>"+
        "<th class='removePortrait'>#</th>"+
        "<th>Reciet</th>"+
        "<th class='removePortrait'>Participants</th>"+
        "<th>Cost</th>"+
        "<th>Date</th>"+
        "<th>Buyer</th>"+
        "<th class='removePortrait'>Status</th>" +
        "</tr>"
    );

    for(var i = 0; i < len; i++){
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
            "<tr class='truncateTableHeader'>"+
            "<td scope=\"row\" class='removePortrait truncateTableCell'>"+(i+1)+"</td>"+
            "<td class='truncateTableCell'>"+disbursementList[i].name+"</td>"+
            "<td class='removePortrait truncateTableCell'>"+participantsString+"</td>"+
            "<td class='truncateTableCell'>"+disbursementList[i].disbursement+"</td>"+
            "<td class='truncateTableCell'>"+d+"</td>"+
            "<td class='truncateTableCell'>"+disbursementList[i].payer.name+"</td>"+
            "<td class='removePortrait truncateTableCell'>"+acceptedString+"</td>"+
            "</tr>");
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
        "<th>Asks For</th>"+
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
            "<th><button class='acceptPayment' value='"+table1+"' onclick='acceptPaymentsClick(this)'>Register as paid</button></th>"+
            "</tr>"
        );
    }
}


function compare(a,b) {
    if (a.name.toLowerCase() < b.name.toLowerCase())
        return -1;
    if (a.name.toLowerCase() > b.name.toLowerCase())
        return 1;
    return 0;
}



