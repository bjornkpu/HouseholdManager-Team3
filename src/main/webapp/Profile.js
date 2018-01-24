$(document).ready(function() {


    $('#editUser').click(function () {
        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "none";
        tableEdit.style.display = "block";

    });



    getLoggedOnUser(setData);
    getLoggedOnUser(getGroups);
    var sessionEmail;
    //getEmail();



    function setData(user) {
        console.log("name: " + user.name);
        console.log("email: " + user.email);
        sessionEmail = user.email;
        $('#nameReadOnly').attr('value', user.name);
        $('#emailReadOnly').attr('value', user.email);
        $('#phoneReadOnly').attr('value', user.phone);
        var lists;

        var url='rest/groups/'+ user.email +'/invites';
        $.get(url, function(data,status){
            lists=data;
            renderInvites(data,user);
            if(status === "success"){
                console.log("members content loaded successfully!");
            }
            if(status === "error"){
                console.log("Error in loading members");
            }
        });
    }


    function getInfo(email){
        $.ajax({
            type: 'GET',
            url: 'rest/user/' + email,
            dataType: 'json',
            success: function (data) {
                console.log( "data: " + data);
                console.log(email)
                $('#nameReadOnly').attr('value', data.name);
                $('#emailReadOnly').attr('value', email);
                $('#phoneReadOnly').attr('value', data.phone);
            }
        })
    }


    //Updating user information
    $('#Confirm').click(function () {
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
                    "name": nameField,
                    "phone": phoneField,
                    "email": sessionEmail,
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
    });

    //Updating user password
    $('#changePassword').click(function () {
        //Check if old password correct
        var passPromise = sha256($("#passwordField").val());
        passPromise.then(function(pass){
            $.ajax({
                url: 'rest/user/'+sessionEmail,
                type: 'GET',
                dataType: 'json',
                complete: function (user) {
                    sha256(pass+user.salt).then(function (saltedHashed) {
                        console.log("Pass: "+pass+"\tSalt: "+user.salt+"\tSaltedHashed: "+saltedHashed)
                        if(saltedHashed===(user.password)){
                            //Change the password
                            changePassword();
                        }else{
                            alert("Old password is wrong faggot");
                        }
                    })
                }
            });
        });
    });

    /**(function datatoJSONupdateInfo() {
        json = JSON.stringify({
            "name": $('#nameProfileField1').val(),
            "phone": $('#phoneField1').val(),
            "email": 'abcqwe',
            "password":'qwe'

        });
        console.log(json);
        return json;
    }*/


    $("#createGroup").click(function () {
        createNewGroup();
    });
});


//TODO: Button does not give any feedback wether the join was successful or not. Need to fix that.

function renderInvites(data,user) {
    var len = data.length;
    console.log(user.email + " render invites.");
    for (var i = 0; i < len;i++ ) {
        var s = "inviteGroupButton"+i;
        $('#profileInvites').append('<tr id=\"" + s + "\"> <td>' + data[i].name + "  " + ' </td>');
        var $li = $("<td><button type=\"button\"  class=\"joinGroup\" title=\"joinGroup\">Join</button></td>");
        var $fjern = $("<td><button type=\"button\" class=\"joinGroup\" title=\"joinGroup\">Remove</button></td>");
        (function (i) {
            $li.click(function() {
                $.ajax({
                    type:"PUT",
                    url:"rest/groups/" + data[i].id + "/members/" + user.email+"/"+1,
                    contentType: "application/json",
                    dataType:"json",
                    success: function (jqXHR,textStatus) {
                        window.location.href = "Profile.html";
                    }
                })
            });
            $fjern.click(function () {
                $.ajax({
                    type:"DELETE",
                    url:"rest/groups/" + data[i].id + "/members/" + user.email,
                    contentType: "application/json",
                    dataType:"json",
                    success: function (jqXHR,textStatus) {
                        window.location.href = "Profile.html";
                    }
                })
            });
        }(i));
        $("#profileInvites").append($li);
        $("#profileInvites").append($fjern);
        $("#profileInvites").append("</tr>");
    }
}

function createNewGroup() {
    //console.log("Click");
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
                'name': Groupname,
                'description': null,
                'admin': $("#emailReadOnly").val(),
                'members': null
            }),
            statusCode: {
                404: function () {
                    console.log("404 - Not Found");
                },
                200: function () {
                    console.log("Group Added");
                    window.location.href = "GroupDashboard.html";
                }
            }
        })
    }
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
                "password": pass
            }),
            success: function () {
                //window.location.reload();
                // alertSuccess.style.display="block";
                $("#alertPassword").fadeTo(2000, 500).slideUp(500, function () {
                    $("#alertPassword").slideUp(500);
                });

            },
            error: function () {
                alert("Password did not update")
            }
        });
    });
}

function getInvites(user) {
    $.ajax({
        type:"GET",
        url: "rest/groups/" + user.email + "/invites",
        contentType: "application/json",
        dataType:"json",
        success: function (data) {
            renderInvites(data);
        }
    })
}

function getGroups(user) {
    $.ajax({
        type: 'GET',
        url: '/scrum/rest/groups/list/' + user.email,
        dataType: "json",
        success: homeButtonForOldUsers
    });
    //console.log(getCookie(curGroup));
}

//Users who are connected to a group can press the logo to go the the dashboard while
//new users cant
function homeButtonForOldUsers(data) {
    groups=data;
    var len = data.length;
    if(len>=1){
        $('#houseHoldManagerLogo').click(function () {
            window.location.href = "GroupDashboard.html"
        })
        $('#houseHoldManagerLogo2').click(function () {
            window.location.href = "GroupDashboard.html"
        })
    }
}