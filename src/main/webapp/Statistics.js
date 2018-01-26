var currentGroup;
var dataList1 = [];
var dataList2 = [];
var list=0;

 function makePie() {
     console.log("Test");
     var pieList=[];
     var totalValue=0;
         for(var j=0;j<dataList2.length;j++){
             totalValue+=dataList2[j].value;
         }
         console.log("Totalvalue:" + totalValue);
         for(var i=0;i<dataList2.length;i++){
             pieList.push({y: (dataList2[i].value/totalValue)*100, label: dataList2[i].key});
             console.log("i " + i + ":" + (dataList2[i].value/totalValue)*100);
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
    console.log(list);
    $.get(url, function(data, status){
        if (status === "success") {
            dataList1 = data;
            console.log("Item content loaded successfully!");
            var textBox = document.getElementById("description");
            textBox.innerHTML="Number of chores completed by each member in current group";
            var textTitle = document.getElementById("title");
            textTitle.innerHTML="Description:";
        }
        if(status === "error"){
            console.log("Error in loading Item content");
        }
    });

    url='http://localhost:8080/scrum/rest/groups/' + currentGroup + '/statistics/choreStatus';

    $.get(url, function(data, status){
        if (status === "success") {
            dataList2 = data;
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
    console.log(list);
    $.get(url, function(data, status){
        if (status === "success") {
            dataList2 = data;
            console.log("Item content loaded successfully!");
            var textBox = document.getElementById("description");
            textBox.innerHTML="Total costs of receits paid for by each member in current group";
            var textTitle = document.getElementById("title");
            textTitle.innerHTML="Description:";
        }
        if(status === "error"){
            console.log("Error in loading Item content");
        }
    });

    url='http://localhost:8080/scrum/rest/groups/' + currentGroup + '/statistics/userDebt';

    $.get(url, function(data, status){
        if (status === "success") {
            dataList1 = data;
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
        for (var a = 0; a < dataList1.length; a++) {
            if (dataList1[a].value > maxValue) {
                console.log("Endret maks: " + maxValue);
                maxValue = dataList1[a].value;
            }
        }
        console.log("max:" + maxValue);
    $(".bar-graph").empty();
    for (var i = 0; i < dataList1.length; i++) {
        var name = dataList1[i].key;
        var height =0;
        var count=0;
        if(list=1){
            count = dataList1[i].value;
            height = (count / maxValue)*100;
            console.log(i+": " + height);
        }
        else{
            count = dataList1[i].value;
            height = (count / maxValue) * 100;
        }
        $(".bar-graph").append(
            "<li class='bar primary' style='height: " + height + "%;' title='10'>"
            + "<div class='percent'>" + count + "<span></span></div>"
            + "<div class='description'>" + name + "</div> </li>");
    }
};