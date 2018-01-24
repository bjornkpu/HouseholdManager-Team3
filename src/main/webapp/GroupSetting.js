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
            $('#tabForUsersInGroup').find("tbody").append('<tr id=\" " + s + "\"> ' +
                '<td>' + data[i].name + '</td>' +
                '<td>' + statusText + '</td>');
            var $remove = $("<td><button type=\"button\" class='button'>Remove</button></td>");

            //var $x=$('<tr> ' + '<td>' + data[i].name + '</td>' + '<td>' + statusText +'</td>');

            var $removeButton =$('<td><button type="button" value="Delete Row" value="'+id+'" ' +
                'onclick="DeleteRowFunction(this)" class="btn btn-default btn-sm">'
                + '<span class="glyphicon glyphicon-remove"></span>' + '</button></td>');


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
                            url: 'rest/groups/' + y + '/members/' + id, //testemail
                            type: 'DELETE',
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",

                            success: function () {
                                /* var table_length = $('#tabForUsersInGroup tr').length;
                                 for (var i = 0; i < table_length; i++) {
                                     if ($("#checkbox" + i).is(':checked')) {
                                         $("#checkbox" + i).closest('tr').remove();
                                     }
                                 }*/
                                $("#alertRemoveSuccess").fadeTo(4000, 500).slideUp(500, function () {
                                    $("#alertRemoveSuccess").slideUp(500);
                                });
                                //alert("member(s) removed from group");
                            },
                        });
                    }
                })
            }(i));
            //$('#tabForUsersInGroup').append($x);
            $('#tabForUsersInGroup').find("tbody").append($removeButton);
           // $('#tabForUsersInGroup').append($removeButton);
            $('#tabForUsersInGroup').find("tbody").append('</tr>');



            /** $('#tabForUsersInGroup').append('<tr> ' +
                '<td>' + data[i].name + '</td>' +
                '<td>' + statusText +'</td>' +
                "<td> <input value='"+ id +"' id='checkbox"+i+"' type='checkbox' ></td>" +
               '<td><button id="removeMember'+i+'" type="button" value="Delete Row" value="'+id+'" onclick="DeleteRowFunction(this)" class="btn btn-default btn-sm">\n' +
                '<span class="glyphicon glyphicon-remove"></span>' +
                '</button></td>' +
                '</tr>');*/

          /**  $('#removeMember' +i).click(function() {
                console.log(i);
                //var checked = getChecked();

                if (data[i].status===2 && adminCounter ===1 ) {
                    $("#alertRemoveFail2").fadeTo(10000, 500).slideUp(500, function () {
                        $("#alertRemoveFail2").slideUp(500);
                    });
                }else {
                    // AJAX Request
                    $.ajax({
                        url: 'rest/groups/' + y + '/members/' + id, //testemail
                        type: 'DELETE',
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",

                        success: function () {
                            var table_length = $('#tabForUsersInGroup tr').length;
                            for (var i = 0; i < table_length; i++) {
                                if ($("#checkbox" + i).is(':checked')) {
                                    $("#checkbox" + i).closest('tr').remove();
                                }
                            }
                            $("#alertRemoveSuccess").fadeTo(4000, 500).slideUp(500, function () {
                                $("#alertRemoveSuccess").slideUp(500);
                            });
                            //alert("member(s) removed from group");
                        },
                    });
                }
            }); */
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
            var id = $("#removeMember"+i)[0].value;
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





