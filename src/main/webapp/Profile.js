$(document).ready(function() {
    $('#editUser').click(function () {
        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "none";
        tableEdit.style.display = "block";
        sessionEmail();
    });

    var sessionEmail = function () {
        $.ajax({
            type: 'GET',
            url:'http://localhost:8080/scrum/rest/session',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (data) {
                var x;
                x.email=data.email;
                x.loggedOn=data.loggedOn;
                console.log(sessionEmail());
                return x;
            }
        })
    };

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
                url: 'http://localhost:8080/scrum/rest/user/abcqwe' /**+ sessionEmail()*/, //test
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                data: JSON.stringify({
                    "name": nameField,
                    "phone": phoneField,


                }),
                success: function (jqXHR, textStatus) {
                    switch (jqXHR.status) {
                        case 200:
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
            url: 'http://localhost:8080/scrum/rest/user/tre@h.no' /**+ sessionEmail()*/, //test
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
        console.log("Click");
        $.ajax({
            type:'POST',
            url:'http://localhost:8080/scrum/rest/groups/',
            contentType: 'application/json; charset=utf-8',
            dataType:'json',
            data: JSON.stringify({
                'name': $("#nameGroup").val(),
                'description':null,
                'admin':"en@h.no",
                'members':null
            }),
            /*
            error: function() {
                window.location.href = "error.html";
            },
            complete: function(jqXHR,textStatus) {
                switch (jqXHR.status) {
                    case 200:
                        console.log("creating group");
                        window.location.href = "Navbar.html";
                        break;
                    case 404:
                        console.log("Group not created");
                        break;
                    default:
                        window.location.href="error.html";
                        break;
                }
            }
            */
            statusCode: {
                404: function () {
                    console.log("404 - Not Found");
                },
                200: function () {
                    console.log("Group Added");

                }
            }
        })



    })
});