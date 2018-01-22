$(document).ready(function() {

    $('#editGroupName').click(function () {
        var readOnlyField = document.getElementById('groupName');
        var buttonEdit = document.getElementById('editGroupName');
        var buttonConfirm = document.getElementById('confirmNewName');
        var newNameField = document.getElementById('newNameField');

        readOnlyField.style.display= "none";
        buttonEdit.style.display="none";
        buttonConfirm.style.display="block";
        newNameField.style.display="block";
    });

    var y = getCookie("currentGroup");
    console.log("y: " + y);
    var lists;

    var url='rest/groups/'+ y +'/members';
    $.get(url, function(data, status){
        lists=data;
        renderMembers(data)
        if(status === "success"){
            console.log("members content loaded successfully!");
        }
        if(status === "error"){
            console.log("Error in loading members");
        }
    });

    //TODO: fikse groupservice og dao slik at man kan update name
    $('#confirmNewName').click(function () {
        var readOnlyField = document.getElementById('groupName');
        var buttonEdit = document.getElementById('editGroupName');
        var buttonConfirm = document.getElementById('confirmNewName');
        var newNameField = document.getElementById('newNameField');

        var newName = $('newNameField').val();
        $.ajax({
            type: 'PUT',
            url: 'rest/groups/'+ y,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "name": newName,
            }),
            success: function (jqXHR, textStatus) {
                console.log("new name updated test");
                switch (jqXHR.status) {
                    case 204:
                        console.log("ajax update group name");
                        alert("name updated");
                        break;
                    case 404:
                        alert("Could not update");
                        break;
                    default:
                        break;
                }
            }
        });

        readOnlyField.style.display= "block";
        buttonEdit.style.display="block";
        buttonConfirm.style.display="none";
        newNameField.style.display="none";

    });



    function renderMembers(data) {
        var len = data.length;
        //var memberStatus = getUserStatus(data);
        console.log(data);
        //console.log("Status på brukere er: ");
       // console.log(memberStatus);
        var statusText;

        for (var i = 0; i < len; i++) {
            var id= data[i].email;
            console.log(data[i].status);
            if (data[i].status === 1){
                statusText="member";
            }else{
                statusText="Admin";
            }
            $('#tabForUsersInGroup').append('<tr> ' +
                '<td>' + data[i].name + '</td>' +
                '<td>' + statusText +'</td>' +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
                '</tr>');
            //console.log("id: "+id);
        }
    }

    /**Remove member fungerer nå, eneste er at email er hardcordet inn, får vi opp en generell mail er alt klart */
    $('#removeMember').click(function(data) {
        var checked=getChecked();
        // AJAX Request
        $.ajax({
            url: 'rest/groups/'+ y + '/members/' + data.email, //testemail
            type: 'DELETE',
            data: JSON.stringify(checked),
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function(response){
                var table_length = $('#tabForUsersInGroup tr').length;
                for (var i =0; i<table_length;i++){
                    if($("#checkbox"+i).is(':checked')){
                        $("#checkbox"+i).closest('tr').remove();
                    }
                }
                alert("member(s) removed from group");
            },
            error: function(){
                console.log("member could not be removed");
            }
        });
    });
});

function getChecked(){
    var table_length = $('#tabForUsersInGroup tr').length;
    var checked = [];
    for (var i =0; i<table_length;i++){
        if($("#checkbox"+i).is(':checked')){
            var id = $("#checkbox"+i)[0].value;
            checked.push(id);
        }
    }//console.log(checked);
    return checked;

}
