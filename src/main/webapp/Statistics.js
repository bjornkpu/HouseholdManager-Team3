$(document).ready(function() {

    function getStatistics(){
        var url='http://localhost:8080/scrum/rest/groups/' + 1 + '/statistics/';

        $.get(url, function(status){
            var items;
            if (status === "success") {
                var data;
                items = data;
                console.log("Item content loaded successfully!");
            }
            if(status === "error"){
                console.log("Error in loading Item content");
            }
        });
    }
};