var currentGroup;
/*
window.onload= function() {

    var chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: true,
        title: {
            text: "Desktop Search Engine Market Share - 2016"
        },
        data: [{
            type: "pie",
            startAngle: 240,
            yValueFormatString: "##0.00\"%\"",
            indexLabel: "{label} {y}",
            dataPoints: [
                {y: 79.45, label: "Google"},
                {y: 7.31, label: "Bing"},
                {y: 7.06, label: "Baidu"},
                {y: 4.91, label: "Yahoo"},
                {y: 1.26, label: "Others"}
            ]
        }]
    });
    chart.render();
    console.log("rendered");
};*/
$(document).ready(function() {
    currentGroup = getCookie("currentGroup");

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
            choreList = data;
            console.log("Item content loaded successfully!");
            var textBox = document.getElementById("description");
            textBox.innerHTML="Number of chores completed by each member in current group";
            var textTitle = document.getElementById("title");
            textTitle.innerHTML="Description:";
            setItemsInTable();
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
            choreList = data;
            console.log("Item content loaded successfully!");
            var textBox = document.getElementById("description");
            textBox.innerHTML="Total costs of receits paid for by each member in current group";
            var textTitle = document.getElementById("title");
            textTitle.innerHTML="Description:";
            setItemsInTable();
        }
        if(status === "error"){
            console.log("Error in loading Item content");
        }
    });
}
function setItemsInTable() {
    console.log("lager tab");
    var maxValue = 0;
    for (var j = 0; j < choreList.length; j++) {
        if (choreList[j].value > maxValue) {
            maxValue = choreList[j].value;
        }
    }
    $(".bar-graph").empty();
    for (var i = 0; i < choreList.length; i++) {
        var count = choreList[i].value;
        var name = choreList[i].key;
        var height = (count / maxValue) * 100;
        $(".bar-graph").append(
            "<li class='bar primary' style='height: " + height + "%;' title='10'>"
            + "<div class='percent'>" + count + "<span></span></div>"
            + "<div class='description'>" + name + "</div> </li>");
    }
};