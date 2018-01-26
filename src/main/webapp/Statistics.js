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
        getChoreStatistics();
    });

    $("#receiptStat").click(function () {
        getDisbursementStatistics();
    });
});


function getChoreStatistics(){
    var url='http://localhost:8080/scrum/rest/groups/' + currentGroup + '/statistics/';

    $.get(url, function(data, status){
        if (status === "success") {
            dataList = data;
            console.log("Item content loaded successfully!");
            var textBox = document.getElementById("description");
            textBox.innerHTML="Number of chores completed by each member in current group";
            var textTitle = document.getElementById("title");
            textTitle.innerHTML="Description:";
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
            var textBox = document.getElementById("description");
            textBox.innerHTML="Total costs of receits paid for by each member in current group";
            var textTitle = document.getElementById("title");
            textTitle.innerHTML="Description:";
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