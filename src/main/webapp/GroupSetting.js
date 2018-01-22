$(document).ready(function() {


    var sessionEmail;
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
        renderMembers(data);
        checkAdmin(data);
        if(status === "success"){
            console.log("members content loaded successfully!");
        }
        if(status === "error"){
            console.log("Error in loading members");
        }
    });

    function getGroupName() {
        $.ajax({
            type:'GET',
            url:'rest/groups/'+y,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (data) {
                $('#groupName').attr('value', data.name);
            }
        });
    }
    getGroupName();

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




    function checkAdmin(data) {
        sessionEmail;
        getLoggedOnUser(function(user){
            sessionEmail=user.email;
        });
        console.log("session email: " + sessionEmail);
        console.log("cookie" + getCookie("userLoggedOn"));
        var len= data.length;
            for(var i=0;i<len;i++){
                console.log("status på brukere: " + data[i].status);
                if(data[i].status===2 && data[i].email === getCookie("userLoggedOn")){
                    $('#adminButtons').append(
                        '<tr> ' +
                        '<td> <button class="button">Promote</button></td>' +
                        '<td><button class="button">Delete group</button></td>' +
                        '</tr>');
                }
            }
        if(status === "error"){
            console.log("Error in loading adminbuttons");
        }
    }



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

            /**eneste som ikke funker er at det kan kun slettes en om gangen */
            $('#removeMember').click(function() {
                if (confirm("You are about to remove a member from your group, do you want to continue?")) {
                    var checked = getChecked();
                    // AJAX Request
                    $.ajax({
                        url: 'rest/groups/' + y + '/members/' + id, //testemail
                        type: 'DELETE',
                        data: JSON.stringify(checked),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",

                        success: function (response) {
                            var table_length = $('#tabForUsersInGroup tr').length;
                            for (var i = 0; i < table_length; i++) {
                                if ($("#checkbox" + i).is(':checked')) {
                                    $("#checkbox" + i).closest('tr').remove();
                                }
                            }
                            //alert("member(s) removed from group");
                        },
                        error: function () {
                            console.log("member could not be removed");
                        }
                    });
                }
            });
        }
    }

    $("#invUserButton").click(function () {
        $.ajax({
            type:"POST",
            dataType:"json",
            url:"rest/groups/"+y+"/members/" + $("#invUserField").val(),
            contentType: "application/json",
            data:JSON.stringify({
                "email": $("#invUserField").val()
            }),
            statusCode: {
                200: function () {
                    window.location.href = "Navbars.html";
                },
                500: function () {
                    console.log("Internal Server Error");
                }
            }

        })

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





