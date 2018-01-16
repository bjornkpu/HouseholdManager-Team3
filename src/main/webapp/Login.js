$(document).ready(function(){
    $.ajax({
        url: 'rest/session',
        type: 'GET',
        dataType: 'json',
        success: function(session){
            window.location.href="Navbars.html";
        }
    });

    // Log in
    $("#loginButton").click(function() {
        var passPromise = sha256($("#passwordField").val());
        passPromise.then(function(pass){
            // console.log(pass);
            $.ajax({
                url: 'rest/session',
                type: 'POST',
                data: JSON.stringify({
                    email: $("#emailField").val(),
                    password: pass
                }),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                complete: function (jqXHR, textStatus) {
                    switch (jqXHR.status) {
                        case 200:
                            document.cookie = "testcookie=this is a test";
                            window.location.href = "Navbars.html";
                            break;
                        case 401:
                            $("#wrongPasswordAlert").removeClass("hide");
                            alert("Wrong email or password");
                            break;
                        default:
                            // window.location.href="error.html";
                            break;
                    }
                }
            });
        });
    });

    $('#registerButton').click(function () {
        $("#div_for_login").hide();
        $("#div_reg").show();
    })

    $('#confirmReg').click(function () {
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
                    success: function(data){
                        $("#div_reg").hide();
                        $("#div_for_login").show();
                        alert("User is now Active")
                    }
                });
            });
        }else{
            alert("Passwords must match >:)))))");
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

