$(document).ready(function () {
    var lists;
    var items;
    var currentShoppingList = 0;
    var numberOfMembers = 0;
    currentGroup = getCookie("currentGroup");
    currentUser = getCookie("userLoggedOn");


    //functions to call on page load

    $('#goToDisbursements').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var goToDisbursementsButton = document.getElementById('goToDisbursements');
        var goToBalanceButton = document.getElementById('goToBalance');
        goToBalanceButton.style.display="block";
        listOfDisbursements.style.display ="block";
        goToDisbursementsButton.style.display="none";
        getDisbursementList();
    });

    $('#goToBalance').click(function () {
        var listOfDisbursements = document.getElementById('listOfDisbursements');
        var goToDisbursementsButton = document.getElementById('goToDisbursements');
        var goToBalanceButton = document.getElementById('goToBalance');
        goToBalanceButton.style.display="none";
        listOfDisbursements.style.display ="none";
        goToDisbursementsButton.style.display="block";
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
               // getUserBalance();
            }
        });
    }

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
                "</tr>");
            /*if(items[i].status===2){
                $("#row"+i).addClass('boughtMarked');
            }*/
        }
        console.log("Added Items");
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

});

