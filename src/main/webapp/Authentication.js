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
                    window.location.href = "error.html";
                }
            });
        },
        error: function() {
            window.location.href = "error.html";
        }
    });
};
function logOut(){
    $.ajax({
        url: 'rest/session',
        type: 'DELETE'
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