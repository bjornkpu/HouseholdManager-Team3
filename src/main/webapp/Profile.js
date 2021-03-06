var sessionEmail;
var loggedOnUser;
$(document).ready(function() {
    //Assign click functions
    $('#editUser').click(function () {
        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "none";
        tableEdit.style.display = "block";

    });
    $("#createGroup").click(function () {
        createNewGroup();
    });
    $('#Confirm').click(function(){
        updateUserInformation()
    });

    getLoggedOnUser(function(user){
        setData(user);
        getGroups(user);
    });

    //Updating user password
    $('#changePassword').click(function () {
        passwordIsCorrect().then(function () {
            changePassword();
        }).catch(function (){
            $("#alertPasswordFail").fadeTo(2000, 500).slideUp(500, function () {
                $("#alertPasswordFail").slideUp(500);
            });
        });
    });
});



//TODO: Button does not give any feedback wether the join was successful or not. Need to fix that.

function renderInvites(data,user) {
    var len = data.length;
    console.log(user.email + " render invites.");
    // build html
    var html="";
    for (var i = 0; i < len;i++ ) {
        var s = "inviteGroupButton" + i;
        html += ('<tr id=\"" + s + "\"> <td><strong>' + data[i].name + "  " + ' </strong></td>');
        html += ("<td><button id='" + s + "join' type=\"button\"  class=\"button btn joinGroup\" title=\"joinGroup\">Join</button>");
        html += ("<button id='" + s + "rmw' type=\"button\" class=\"button btn joinGroup\" title=\"Remove Invite\">Remove</button></td></tr>");
    }
    //Applyhtml
    $('#profileInvites').html(html);
    //Assign clicks
    for (i = 0; i < len;i++ ) {
        s="inviteGroupButton" + i;
        (function (i) {
            $("#" + s + "join").click(function () {
                $.ajax({
                    type: "PUT",
                    url: "rest/groups/" + data[i].id + "/members/" + user.email + "/" + 1,
                    contentType: "application/json",
                    dataType: "json",
                    success: function (jqXHR, textStatus) {
                        window.location.href = "Profile.html";
                    }
                })
            });
            $("#" + s + "rmw").click(function () {
                $.ajax({
                    type: "DELETE",
                    url: "rest/groups/" + data[i].id + "/members/" + user.email,
                    contentType: "application/json",
                    dataType: "json",
                    success: function (jqXHR, textStatus) {
                        window.location.href = "Profile.html";
                    }
                })
            });
        }(i));
    }
}

function createNewGroup() {
    var Groupname=prompt("Group name: ");
    if(Groupname == null) {
        return;
    }else
    {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/scrum/rest/groups/',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                'name': htmlEntities(Groupname),
                'description': null,
                'admin': htmlEntities($("#emailReadOnly").val()),
                'members': null
            }),
            statusCode: {
                404: function () {
                    console.log("404 - Not Found");
                },
                200: function () {
                    console.log("Group Added");
                    window.location.href = "GroupDashboard.html#feed";
                }
            }
        })
    }
}

//Check if old password correct
function passwordIsCorrect(){
    return new Promise(function (resolve,reject){
        var passPromise = sha256($("#passwordField").val());
        passPromise.then(function(pass){
            sha256(pass + loggedOnUser.salt).then(function (saltedHashed) {
                // console.log("Pass: "+pass+"\tSalt: "+loggedOnUser.salt+"\npasssalt: "+
                //     pass+loggedOnUser.salt+"\nSaltedHashed: "+saltedHashed+"\nrealPW: "+loggedOnUser.password)
                if(saltedHashed===(loggedOnUser.password)
                    && $("#newPasswordField").val()===$("#confirmPasswordField").val()
                    && $("#confirmPasswordField").val()!=""){

                    console.log("Old password is correct");
                    resolve();
                }else{
                    console.log("Old password is wrong");
                    reject();
                }
            })
        });
    });

}

//Change password
function changePassword(){
    var passwordField =$('#confirmPasswordField').val();
    console.log(passwordField);
    sha256(passwordField).then(function (pass) {
        console.log(pass);
        $.ajax({
            type: 'PUT',
            url: 'rest/user/' + sessionEmail, //test
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "name": null,
                "email": sessionEmail,
                "password": htmlEntities(pass)
            }),
            success: function () {
                //Alert that password is changed
                $("#alertPassword").fadeTo(2000, 500).slideUp(500, function () {
                    $("#alertPassword").slideUp(500);
                });
                getLoggedOnUser(setData);

            },
            error: function () {
                $("#alertPasswordFail").fadeTo(2000, 500).slideUp(500, function () {
                    $("#alertPasswordFail").slideUp(500);
                });
            }
        });
    });
}

// function getInvites(user) {
//     $.ajax({
//         type:"GET",
//         url: "rest/groups/" + user.email + "/invites",
//         contentType: "application/json",
//         dataType:"json",
//         success: function (data) {
//             renderInvites(data);
//         }
//     })
// }

function getGroups(user) {
    $.ajax({
        type: 'GET',
        url: '/scrum/rest/groups/list/' + user.email,
        dataType: "json",
        success: function(data){
            if (data.length == 0){
                // do nothing
            } else {
                homeButtonForOldUsers(data);
            }
        }
    });
    //console.log(getCookie(curGroup));
}


//Sets the values of the fields to reflect current user information
function setData(userObj) {
    console.log("name: " + userObj.name);
    console.log("email: " + userObj.email);
    sessionEmail = userObj.email;
    $('#nameReadOnly').attr('value', userObj.name);
    $('#emailReadOnly').attr('value', userObj.email);
    $('#phoneReadOnly').attr('value', userObj.phone);
    $('#nameProfileField1').attr('value',userObj.name);
    $('#phoneField1').attr('value',userObj.phone);
    var lists;

    var url='rest/groups/'+ userObj.email +'/invites';
    $.get(url, function(data,status){
        lists=data;
        renderInvites(data,userObj);
        if(status === "success"){
            console.log("members content loaded successfully!");
        }
        if(status === "error"){
            console.log("Error in loading members");
        }
    });
    loggedOnUser=userObj;
}

//Updating user information
function updateUserInformation () {
    var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
    var tableEdit = document.getElementById("tableUserInfo");
    var alertSuccess = document.getElementById("alertSuccessEditProfile");

    var nameField = $('#nameProfileField1').val();
    var phoneField = $('#phoneField1').val();
    if(nameField == ""){
        //alert("please fill out namefield");
        $("#alertNameField").fadeTo(2000, 500).slideUp(500, function(){
            $("#alertNameField").slideUp(500);
        });
        $('#nameProfileField1').css({
            "background-color": "yellow",
        });
    }else if(phoneField ==""){
        //alert("please fill out phonefield");
        $("#alertPhoneField").fadeTo(2000, 500).slideUp(500, function(){
            $("#alertPhoneField").slideUp(500);
        });
        $('#phoneField1').css({
            "background-color": "yellow",
        });
    }else {

        $.ajax({
            type: 'PUT',
            url: 'rest/user/'+ sessionEmail, //test
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "name": htmlEntities(nameField),
                "phone": htmlEntities(phoneField),
                "email": htmlEntities(sessionEmail),
                "password":null
            }),
            success: function () {
                //window.location.reload();
                getLoggedOnUser(setData);
                // alertSuccess.style.display="block";
                $("#alertSuccessEditProfile").fadeTo(2000, 500).slideUp(500, function(){
                    $("#alertSuccessEditProfile").slideUp(500);
                });

            },
            error: function () {
                alert("information did not update")
            }

        });

        tableReadOnly.style.display = "block";
        tableEdit.style.display = "none";
        console.log("confirmknapp trykket")
    }
}