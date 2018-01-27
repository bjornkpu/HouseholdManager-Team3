$(document).ready(function(){

//Redirect to dashboard if session is active.
    $.ajax({
        url: 'rest/session',
        type: 'GET',
        dataType: 'json',
        success: function(session){
            // window.location.href="GroupDashboard.html";
        }
    });

    //setTimeout(loadLogin,5000);
    loadLogin();

    function loadLogin() {
        $('#myModal').modal('show');
    }

   //click on the logo on the loginpage to login
    $('#logo').click(function () {
        $('#myModal').modal('show');
    });
    //click on the logo on the loginpage to login
    $('#logo2').click(function () {
        $('#myModal').modal('show');
    });
    //click on the logo on the loginpage to login
    $('#logo3').click(function () {
        $('#myModal').modal('show');
    });


    // Log in
    $("#loginButton").click(function() {
        var email=$("#emailField").val();
        var passPromise = sha256($("#passwordField").val());
        passPromise.then(function(pass){
            // console.log(pass);
            $.ajax({
                url: 'rest/session',
                type: 'POST',
                data: JSON.stringify({
                    email: email,
                    password: pass
                }),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                complete: function (jqXHR, textStatus) {
                    switch (jqXHR.status) {
                        case 200:
                            window.location.href = "GroupDashboard.html#feed";
                            document.cookie ="userLoggedOn =" + email;
                            break;
                        case 401:
                            $("#alertWrongUsernameOrPassword").fadeTo(4000, 500).slideUp(500, function () {
                                $("#alertWrongUsernameOrPassword").slideUp(500);
                            });
                            $("#forgottenPassword").show();
                            break;
                        default:
                            // window.location.href="error.html";
                            break;
                    }
                }
            });
        });
    });

   // $('#forgottenPassword').hide();
    $('#forgottenPassword').click(function () {
        toEmail = $("#emailField").val();
        mess1 = "Send new password to: "+ toEmail;
        x = confirm(mess1);
        if (x == true) {
            $.ajax({
                url: 'rest/user/forgotPw/'+toEmail,
                type: 'PUT',
                complete: function () {
                    $("#alertEmailSent").fadeTo(4000, 500).slideUp(500, function () {
                        $("#alertEmailSent").slideUp(500);
                    });
                }
            });
        }
    })
    
    $('#registerButton').click(function () {
        $("#loginContent").hide();
        $("#div_reg").show();
        $(".modal-footer").hide();
        $("#myModalLabel2").show();
        $("#myModalLabel").hide();
    });

    $('#cancelBtn').click(function () {
        $("#div_reg").hide();
        $("#loginContent").show();
        $(".modal-footer").show();
        $("#myModalLabel2").hide();
        $("#myModalLabel").show();

    })

    $('#confirmReg').click(function click() {

        if(!$("#name_of_user_field").val() || !$("#emailRegField").val() ||
            !$("#passwordRegField").val() || !$("#passwordConfirmField").val()){
            $("#alertMissingInfo").fadeTo(4000, 500).slideUp(500, function () {
                $("#alertMissingInfo").slideUp(500);
            });
            return;
        }
        /*//--Checks if the email is valid - Doesn't work, should tho...
        var bool;
        $.get("rest/user/emailCheck/"+$("#emailRegField").val(), function(data, status){ bool = data;});
        if(!bool) {
            alert("Not a valid email1");
            return;
        }*/


        if($("#passwordRegField").val()==$("#passwordConfirmField").val()){
            sha256($("#passwordRegField").val()).then(function (value) {
                $.ajax({
                    url: 'rest/user',
                    type: 'POST',
                    data: JSON.stringify({
                        email: $("#emailRegField").val(),
                        password: value,
                        name: $("#name_of_user_field").val()
                    }),
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    statusCode: {
                        200: function() {
                            $("#div_reg").hide();
                            $("#loginContent").show();
                            $(".modal-footer").show();
                            $("#myModalLabel2").hide();
                            $("#myModalLabel").show();
                            $("#alertUserActive").fadeTo(4000, 500).slideUp(500, function () {
                                $("#alertUserActive").slideUp(500);
                            });
                        },
                        400: function() {
                            $("#alertInvalidEmail").fadeTo(4000, 500).slideUp(500, function () {
                                $("#alertInvalidEmail").slideUp(500);
                            });
                        }
                    }
                });
            });
        }else{
            $("#alertMismatchingPassword").fadeTo(4000, 500).slideUp(500, function () {
                $("#alertMismatchingPassword").slideUp(500);
            });
        }
    })
});


//FB Login
//vvvvvvvvvvvvvvvvvvvvvvv

// This is called with the results from from FB.getLoginStatus().
function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
        // Logged into your app and Facebook.
        testAPI();
    } else {
        // The person is not logged into your app or we are unable to tell.
        document.getElementById('status').innerHTML = 'Please log ' +
            'into this app.';
    }
}

// This function is called when someone finishes with the Login
// Button.  See the onlogin handler attached to it in the sample
// code below.
function checkLoginState() {
    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });
}

window.fbAsyncInit = function() {
    FB.init({
        appId      : '179985056083093',
        cookie     : true,  // enable cookies to allow the server to access
                            // the session
        xfbml      : true,  // parse social plugins on this page
        version    : 'v2.8' // use graph api version 2.8
    });

    // Now that we've initialized the JavaScript SDK, we call
    // FB.getLoginStatus().  This function gets the state of the
    // person visiting this page and can return one of three states to
    // the callback you provide.  They can be:
    //
    // 1. Logged into your app ('connected')
    // 2. Logged into Facebook, but not your app ('not_authorized')
    // 3. Not logged into Facebook and can't tell if they are logged into
    //    your app or not.
    //
    // These three cases are handled in the callback function.

    FB.getLoginStatus(function(response) {
        statusChangeCallback(response);
    });

};

// Load the SDK asynchronously
(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

// Here we run a very simple test of the Graph API after login is
// successful.  See statusChangeCallback() for when this call is made.
function testAPI() {
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me', function(response) {
        console.log('Successful login for: ' + response.name);
        document.getElementById('status').innerHTML =
            'Thanks for logging in, ' + response.name + '!';
    });
}

