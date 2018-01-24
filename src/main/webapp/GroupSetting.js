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

        var newName = $('#newNameField').val();
        console.log("the new name for the group: "+ newName);
        $.ajax({
            type: 'PUT',
            url: 'rest/groups/'+ y,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "id": htmlEntities(y),
                "name": htmlEntities(newName),
            }),
            success: function(){
                console.log("updated group name");
                document.cookie = "groupName=" + htmlEntities(newName);
                //TODO: noen som vet hvorfor denne replacen ikke refresher siden
                window.location.replace('GroupDashboard.html#Setting');
                getGroupName();
                $("#alertEditGroupNameSuccess").fadeTo(4000, 500).slideUp(500, function () {
                    $("#alertEditGroupNameSuccess").slideUp(500);


                });
            },
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
        console.log(data);
        var statusText;
        var adminCounter =0;
        for (var i = 0; i < len; i++) {
            if(data[i].status===2){
                adminCounter++;
            }
        }
        for (var i = 0; i < len; i++) {
            var id= data[i].email;
            console.log(data[i].status);
            if (data[i].status === 1){
                statusText="member";
            }else{
                statusText="Admin";
            }
            var s = "removeButton"+i;
            $('#memberListGroup').append('<tr id=\" ' + s + '\"> ' +
                '<td>' + data[i].name + '</td>' +
                '<td>' + statusText + '</td></tr>');
            var $removeButton = $("<td><button type=\"button center\" class='button'>Remove</button></td>");
            (function (i) {
                $removeButton.click(function () {
                    console.log(i);
                    //var checked = getChecked();
                    if (data[i].status===2 && adminCounter ===1 ) {
                        $("#alertRemoveFail2").fadeTo(10000, 500).slideUp(500, function () {
                            $("#alertRemoveFail2").slideUp(500);
                        });
                    }else {
                        // AJAX Request
                        $.ajax({
                            url: 'rest/groups/' + y + '/members/' + id + "/" + 3, //testemail
                            type: 'PUT',
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",

                            success: function () {
                                $("#alertRemoveSuccess").fadeTo(4000, 500).slideUp(500, function () {
                                    $("#alertRemoveSuccess").slideUp(500);
                                });
                            },
                        });
                    }
                })
            }(i));
            $('#' + s).append($removeButton);

        }
    }

    $("#invUserButton").click(function () {
        $.ajax({
            type:"POST",
            dataType:"json",
            url:"rest/groups/"+htmlEntities(y)+"/members/" + htmlEntities($("#invUserField").val()),
            contentType: "application/json",
            data:JSON.stringify({
                "email": htmlEntities($("#invUserField").val())
            }),
            statusCode: {
                200: function () {
                    $("#alertInvSuccess").fadeTo(4000, 500).slideUp(500, function(){
                        $("#alertInvSuccess").slideUp(500);
                    });
                },
                500: function () {
                    console.log("Internal Server Error");
                },
                404: function () {
                    $("#alertInvFail").fadeTo(4000, 500).slideUp(500, function(){
                        $("#alertInvFail").slideUp(500);
                    });
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


function getCheckedButton(){
    var table_length = $('#tabForUsersInGroup tr').length;
    var checked = [];
    for (var i =0; i<table_length;i++){
        if($("#removeMember"+i).is(':checked')){
            var id = htmlEntities($("#removeMember"+i)[0].value);
            checked.push(id);
        }
    }//console.log(checked);
    return checked;
}

function DeleteRowFunction(btndel) {
    if (typeof(btndel) == "object") {
        $(btndel).closest("tr").remove();
    } else {
        return false;
    }
}





