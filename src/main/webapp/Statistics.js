$(document).ready(function() {

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
    var choreList;

    console.log("fert");
    getStatistics();
    function getStatistics(){
        var url='http://localhost:8080/scrum/rest/groups/' + 1 + '/statistics/';

        $.get(url, function(data, status){
            console.log("bertt");
            if (status === "success") {
                choreList = data;
                console.log("Item content loaded successfully!");
                setItemsInTable();
            }
            if(status === "error"){
                console.log("Error in loading Item content");
            }
        });
    }
    function setItemsInTable(){
        console.log("lager tab");
        /*
        var table = document.getElementsByClassName("bar-graph");
        while(table.rows.length > 0) {
            table.deleteRow(0);
        }*/
        var totalChores=0;
        for(var j=0;j<choreList.length;j++){
            totalChores+=choreList[j].value;
        }
        for(var i=0; i<choreList.length;i++){
           var count =  choreList[i].value;
           var name = choreList[i].key;
            var height = (count/totalChores)*200;
            $(".bar-graph").append(
            "<li class='bar primary' style='height: "+height+"%;' title='10'>"
            + "<div class='percent'>"+ count + "<span>chors</span></div>"
            +  "<div class='description'>" + name +"</div> </li>");
        }



        /*
        for(var i = 0; i < len; i++){
            var id = items[i].id;
            $("#tableShoppinglist").append(
                "<tr>" +
                "<th scope=\"row\">"+(i+1)+"</th>" +
                "<td>" + items[i].name + "</td>" +
                "<td>" + items[i].status + "</td>" +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                "</tr>"
            );
        }
        console.log("Added Items");*/
    }
});