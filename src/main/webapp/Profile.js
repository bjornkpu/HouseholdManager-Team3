$(document).ready(function() {
    $('#editUser').click(function () {
        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "none";
        tableEdit.style.display = "block";

    });

    getLoggedOnUser(setData);
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

    function getEmail () {
        $.ajax({
            type: 'GET',
            url:'rest/session',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (data) {
                sessionEmail=data.email;
                getInfo(data.email);

            }
        })
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

        var nameField = $('#nameProfileField1').val();
        var phoneField = $('#phoneField1').val();
        if(nameField == ""){
            alert("please fill out namefield");
            $('#nameProfileField1').css({
                "background-color": "yellow",
            });
        }else if(phoneField ==""){
            alert("please fill out phonefield");
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
                success: function (jqXHR, textStatus) {
                    switch (jqXHR.status) {
                        case 204:
                            console.log("ajax update user info");
                            alert("Information updated");
                            break;
                        case 404:
                            alert("Could not update");
                            break;
                        default:
                            break;
                    }
                }
            });
            tableReadOnly.style.display = "block";
            tableEdit.style.display = "none";
            console.log("confirmknapp trykket")
        }
    });

    //Updating user password
    $('#changePassword').click(function () {
        $.ajax({
            type: 'PUT',
            url: 'rest/user/tre@h.no' /**+ sessionEmail()*/, //test
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify({
                "password": $('#confirmPasswordField').val(),
            }),
            success: function (jqXHR, textStatus) {
                switch (jqXHR.status) {
                    case 204:
                        console.log("ajax update user info");
                        alert("Information updated");
                        break;
                    case 404:
                        alert("Could not update");
                        break;
                    default:
                        break;
                }
            }
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


    $("#createGroupButton").click(function () {
        //console.log("Click");
        $.ajax({
            type:'POST',
            url:'rest/groups/',
            contentType: 'application/json; charset=utf-8',
            dataType:'json',
            data: JSON.stringify({
                'name': $("#nameGroup").val(),
                'description':null,
                'admin':$("#emailReadOnly").val(),
                'members':null
            }),
            statusCode: {
                404: function () {
                    console.log("404 - Not Found");
                },
                200: function () {
                    console.log("Group Added");
                    window.location.href = "Navbars.html";
                }
            }
        })

    })

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


