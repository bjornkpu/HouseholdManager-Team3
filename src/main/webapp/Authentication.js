function htmlEntities(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;')};

function getLoggedOnUser(success) {
    $.ajax({
        url: 'rest/session',
        type: 'GET',
        dataType: 'json',
        success: function(session) {
            $.ajax({
                url: 'rest/user/' + session.email,
                type: 'GET',
                dataType: 'json',
                success: function(user) {
                    success(user);
                },
                error: function() {
                    window.location.href = "Login.html";
                }
            });
        },
        error: function() {
            window.location.href = "Login.html";
        }
    });
};

function logOut(){
    console.log("logout");
    $.ajax({
        url: 'rest/session',
        type: 'DELETE',
        dataType: 'json',
        success: function(session){
            window.location.href="Login.html";
            document.cookie ="userLoggedOn =";

        }
    });
}


//Returns a Promise that returns the hash on success.
function sha256(str) {
    // We transform the string into an arraybuffer.
    var buffer = new TextEncoder("utf-8").encode(str);
    return crypto.subtle.digest("SHA-256", buffer).then(function (hash) {
        // console.log(hex(hash))
        return hex(hash);
    });
}

function hex(buffer) {
    var hexCodes = [];
    var view = new DataView(buffer);
    for (var i = 0; i < view.byteLength; i += 4) {
        // Using getUint32 reduces the number of iterations needed (we process 4 bytes each time)
        var value = view.getUint32(i)
        // toString(16) will give the hex representation of the number without padding
        var stringValue = value.toString(16)
        // We use concatenation and slice for padding
        var padding = '00000000'
        var paddedValue = (padding + stringValue).slice(-padding.length)
        hexCodes.push(paddedValue);
    }

    // Join all the hex strings into one
    return hexCodes.join("");
}

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else {
        begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1) {
            end = dc.length;
        }
    }
    return decodeURI(dc.substring(begin + prefix.length, end));
}

function getGroup(groupId, success){
    $.ajax({
        url: 'rest/groups/'+groupId,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            success(data);
        }
    });
}

