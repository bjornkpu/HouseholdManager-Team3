$(document).ready(function() {
    $('#editUser').click(function () {

        console.log("reg trykk")
        /**
         var nameReadOnly = document.getElementById("nameReadOnly");
         var emailReadOnly = document.getElementById("emailReadOnly");
         var phoneReadOnly = document.getElementById("phoneReadOnly");
         var editInfoButton = document.getElementById("editUser");

         var nameProfileField = document.getElementById("nameProfileField");
         var emailField = document.getElementById("emailField");
         var phoneField = document.getElementById("phoneField");
         var Confirm = document.getElementById("Confirm");


         nameReadOnly.style.display ="none";
         emailReadOnly.style.display="none";
         phoneReadOnly.style.display="none";
         editInfoButton.style.display="none";

         nameProfileField.style.display ="block";
         emailField.style.display="block";
         phoneField.style.display="block";
         Confirm.style.display="block";
*/

        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "none";
        tableEdit.style.display = "block";

    });

    $('#Confirm').click(function () {

        var tableReadOnly = document.getElementById("tableUserInfoReadOnly");
        var tableEdit = document.getElementById("tableUserInfo");

        tableReadOnly.style.display = "block";
        tableEdit.style.display = "none";
    });
})