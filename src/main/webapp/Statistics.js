var currentGroup;
var dataList;

 function makePie() {
     console.log("Test");
     var pieList=[];
     var totalValue=0;
     for(var j=0;j<dataList.length;j++){
         totalValue+=dataList[j].value;
     }
     for(var i=0;i<dataList.length;i++){
         pieList.push({y: (dataList[i].value/totalValue)*100, label: dataList[i].key})
     }
    var chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: true,
        title: {
            text: "Completed tasks"
        },
        data: [{
            type: "pie",
            startAngle: 240,
            yValueFormatString: "##0.00\"%\"",
            indexLabel: "{label} {y}",
            dataPoints: pieList
        }]
    });
    chart.render();
    console.log("rendered");
};

$(document).ready(function() {
    currentGroup = getCookie("currentGroup");
    //makePie();

    $("#choreStat").click(function () {
        var textbox1 = document.getElementById("descriptionBarChore");
        var textTitle1 = document.getElementById("descriptionBarChoreTitle");
        var textbox2 = document.getElementById("descriptionChartChore");
        var textTitle2 = document.getElementById("descriptionChartChoreTitle");

        var descShow1 = document.getElementById("descShow1");
        var descShow2 = document.getElementById("descShow2");
        var descShow3 = document.getElementById("descShow3");
        var descShow4 = document.getElementById("descShow4");
        descShow1.style.display="block";
        descShow2.style.display="block";
        descShow3.style.display="none";
        descShow4.style.display="none";


        textbox1.innerHTML="Number of chores completed";
        textTitle1.innerHTML="Description: ";
        textbox2.innerHTML="Assigned and unassigned chores(midlertidlig desc)";
        textTitle2.innerHTML="Description: ";

        getChoreStatistics();

    });

    $("#receiptStat").click(function () {
        var textbox3 = document.getElementById("descriptionBarReceipt");
        var textTitle3 = document.getElementById("descriptionBarReceiptTitle");
        var textbox4 = document.getElementById("descriptionChartReceipt");
        var textTitle4 = document.getElementById("descriptionChartReceiptTitle");

        var descShow1 = document.getElementById("descShow1");
        var descShow2 = document.getElementById("descShow2");
        var descShow3 = document.getElementById("descShow3");
        var descShow4 = document.getElementById("descShow4");
        descShow1.style.display="none";
        descShow2.style.display="none";
        descShow3.style.display="block";
        descShow4.style.display="block";

        textbox3.innerHTML="Debt per user";
        textTitle3.innerHTML="Description: ";
        textbox4.innerHTML="Amount of money each member have spend(midlertidig desc)";
        textTitle4.innerHTML="Description: ";

        getDisbursementStatistics();
    });
});


function getChoreStatistics(){
    var url='http://localhost:8080/scrum/rest/groups/' + currentGroup + '/statistics/';

    $.get(url, function(data, status){
        if (status === "success") {
            dataList = data;
            console.log("Item content loaded successfully!");
            setItemsInTable();
            makePie();
        }
        if(status === "error"){
            console.log("Error in loading Item content");
        }
    });
}

function getDisbursementStatistics(){
    var url='http://localhost:8080/scrum/rest/groups/' + currentGroup + '/statistics/costs';

    $.get(url, function(data, status){
        if (status === "success") {
            dataList = data;
            console.log("Item content loaded successfully!");
            setItemsInTable();
            makePie();
        }
        if(status === "error"){
            console.log("Error in loading Item content");
        }
    });
}
function setItemsInTable() {
    console.log("lager tab");
    var maxValue = 0;
    for (var j = 0; j < dataList.length; j++) {
        if (dataList[j].value > maxValue) {
            maxValue = dataList[j].value;
        }
    }
    $(".bar-graph").empty();
    for (var i = 0; i < dataList.length; i++) {
        var count = dataList[i].value;
        var name = dataList[i].key;
        var height = (count / maxValue) * 100;
        $(".bar-graph").append(
            "<li class='bar primary' style='height: " + height + "%;' title='10'>"
            + "<div class='percent'>" + count + "<span></span></div>"
            + "<div class='description'>" + name + "</div> </li>");
    }
};