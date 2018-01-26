var groupId;
var sessionEmail;
$(document).ready(function() {
    $('#editGroupName').click(function () {
        $("#groupName").hide();
        $("#editGroupName").hide();
        $("#confirmNewName").show();
        $("#newNameField").show();
    });

    //TODO: fikse groupservice og dao slik at man kan update name
    $('#confirmNewName').click(function () {
        registerNewName(function () {
            getGroupName();
            $("#groupName").show();
            $("#editGroupName").show();
            $("#confirmNewName").hide();
            $("#newNameField").hide();
        });
    });

    groupId = getCookie("currentGroup");
    console.log("GroupID: " + groupId);

    //Get the members of the group
    getMembers();

    //Enable adminbuttons if admin
    getLoggedOnUser(checkAdmin);

    getGroupName();


    $("#invUserButton").click(function () {
        inviteUser();
        console.log("asd")
    });
});

function getMembers() {
    var url='rest/groups/'+ groupId +'/members';
    $.get(url, function(members, status){
        if(status === "success"){
            renderMembers(members);
            checkAdmin(members)
            console.log("members content loaded successfully!");
        }
        if(status === "error"){
            console.log("Error in loading members");
        }
    });
}

function getGroupName() {
    $.ajax({
        type:'GET',
        url:'rest/groups/'+groupId,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (data) {
            $('#groupName').attr('value', data.name);
            $('#newNameField').val(data.name);
        }
    });
}

function inviteUser() {
    $.ajax({
        type:"POST",
        dataType:"json",
        url:"rest/groups/"+htmlEntities(groupId)+"/members/" + htmlEntities($("#invUserField").val()),
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
    });
}

function renderMembers(members) {
    var len = members.length;
    console.log("members length: "+len);

    var statusText;
    var adminCounter =0;
    for (var i = 0; i < len; i++) {
        if(members[i].status===2){
            adminCounter++;
        }
    }
    for (var i = 0; i < len; i++) {
        var userEmail= members[i].email;
        console.log("member#: "+i);
        console.log("id: "+userEmail);
        console.log("status: "+members[i].status);

        //Sets member status
        if (members[i].status === 1){
            statusText="Member";
        }else{
            statusText="Admin";
        }

        var tableRowId = "memberRow"+i;
        $('#memberListGroup').append("<tr>" +
            "<td>" + members[i].name + "</td>" +
            "<td>" + statusText + "</td><td id= '" + tableRowId + "'></td></tr>");

        var $removeButton = $("<button id='"+tableRowId+"rmw' type='button center' " +
            "class='adminButton button tablebutton btn' disabled>Remove</button>");
        var $promoteButton = $("<button id='"+tableRowId+"promo' type='button center' " +
            "class='adminButton button tablebutton btn' disabled>Promote</button>");

        $('#' + tableRowId).append($removeButton);
        $('#' + tableRowId).append($promoteButton);
        (function (i, userEmail) {
            $('#'+tableRowId+'rmw').click(function () {
                removeUserFromGroup(members[i],adminCounter);
                getMembers();
            })
            $('#'+tableRowId+'promo').click(function () {
                promoteUser(members[i]);
                getMembers();
            })
        }(i,userEmail));

    }
}
function promoteUser(user) {
    console.log("id: "+user.email);
    //var checked = getChecked();
    if (user.status===2) {
        $("#alertPromoteFail").fadeTo(10000, 500).slideUp(500, function () {
            $("#alertPromoteFail").slideUp(500);
        });
    }else {
        // AJAX Request
        $.ajax({
            url: 'rest/groups/' + groupId + '/members/' + user.email + "/" + 2, //testemail
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function () {
                $("#alertPromoteSuccess").fadeTo(4000, 500).slideUp(500, function () {
                    $("#alertPromoteSuccess").slideUp(500);
                });
            }
        });
    }
}

function removeUserFromGroup(user,adminCounter) {
    console.log("id: "+user.email);
    //var checked = getChecked();
    if (user.status===2 && adminCounter ===1 ) {
        $("#alertRemoveFail2").fadeTo(10000, 500).slideUp(500, function () {
            $("#alertRemoveFail2").slideUp(500);
        });
    }else {
        // AJAX Request
        $.ajax({
            url: 'rest/groups/' + groupId + '/members/' + user.email + "/" + 3, //testemail
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            dataType: "json",

            success: function () {
                $("#alertRemoveSuccess").fadeTo(4000, 500).slideUp(500, function () {
                    $("#alertRemoveSuccess").slideUp(500);
                });
            }
        });
    }
}


//Registers the new name
function registerNewName(success){
    var newName = $('#newNameField').val();
    console.log("the new name for the group: "+ newName);
    $.ajax({
        type: 'PUT',
        url: 'rest/groups/'+ groupId,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify({
            "id": htmlEntities(groupId),
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
            success();
        }
    });
}

//Checks if user is admin and adds admin buttons
function checkAdmin(members) {
    console.log("Checking if admin");
    getLoggedOnUser(function (user) {
        console.log("useremail: "+user.email);
        for(i=0; i<members.length; i++){
            console.log("memberemail: "+members[i].email);
            if(members[i].email === user.email){
                if(members[i].status===2){
                    console.log("Admin");
                    $('.adminButton').attr("disabled",false);
                    break;
                }else{
                    console.log("Not admin, status: "+ member.status);
                }
            }
        }
    });
}

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





