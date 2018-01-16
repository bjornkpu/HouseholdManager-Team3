$(document).ready(function() {
    $('#editUser').click(function () {
        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "none";
        tableEdit.style.display = "block";
    });


    //Updating user information
    $('#Confirm').click(function () {
        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "block";
        tableEdit.style.display = "none";
        console.log("confirmknapp trykket")

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/scrum/rest/user/abcqwe', //test
            data: datafromToJSON(),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            complete: function (jqXHR, textStatus) {
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
        })

        function datafromToJSON() {
            json = JSON.stringify({
                "name": $('#nameProfileField1').val(),
                "email": $('#phoneField1').val(),
            });
            console.log(json);
            console.log("update user info");
            return json;
        }

    })
});