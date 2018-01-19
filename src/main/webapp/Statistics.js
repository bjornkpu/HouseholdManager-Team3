document.ready{

window.onload = function() {

    var options = {
        title: {
            text: "Tasks completed"
        },
        data: [{
            type: "pie",
            startAngle: 45,
            showInLegend: "true",
            legendText: "",
            indexLabel: "{label} ({y})",
            yValueFormatString:"#,##0.#"%"",
            dataPoints: [
                { label: "Organic", y: 36 },
                { label: "Email Marketing", y: 31 },
                { label: "Referrals", y: 7 },
                { label: "Twitter", y: 7 },
                { label: "Facebook", y: 6 },
                { label: "Google", y: 10 },
                { label: "Others", y: 3 }
            ]
        }]
    };
    $("#chartContainer").CanvasJSChart(options);

}