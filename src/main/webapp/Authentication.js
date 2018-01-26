//Removes XSS
function htmlEntities(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;')};

//Common AJAX-calls
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
}
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

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
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


//....................................................
// JS FOR TOP-NAVBAR
//....................................................

//function which lists out the different groups into the dropdown menu
function renderGroupDropdown(groups) {
    console.log("render grouplist");
    var len = groups.length;
    if (len === 0) {
        document.cookie = curGroup + "=0";
        document.cookie = "currentGroup=null";
        // $("#page-content").load("Profile.html");
        //Doesnt reload when already on profile.
        if(window.location.pathname!=="/scrum/Profile.html"){
            window.location.href="Profile.html";
        }
    } else {
        $('#groupdropdown').empty();
        if (getCookie(curGroup) < len || !groupsContainsCurrentgroup(groups, getCookie(curGroup))) {
            document.cookie = curGroup + "=" + groups[0].id;
            document.cookie = "groupName=" + groups[0].name;
            $(".navGroupName").html(getCookie("groupName"));
        }
        for (var i = 0; i < len; i++) {
            var groupname = groups[i].name;
            var id = '';
            id += i;
            id += 'group';
            var $x = $('<li><a class="dropdown-item" href="#feed" id="' + id + '">' +
                groupname + '</a></li>'
            );
            (function (i) {
                $x.click(function () {
                    document.cookie = curGroup + "=" + groups[i].id;
                    document.cookie = "groupName=" + groups[i].name;
                    $(".navGroupName").html(getCookie("groupName"));
                    //window.location.reload();
                    if(window.location.pathname==="/scrum/Profile.html"){
                        window.location.href="GroupDashboard.html#feed"
                    }else{
                        $("#page-content").load("Feed.html");
                        window.location.hash = "#feed";
                    }
                })
            }(i));
            $('#groupdropdown').append($x);
            console.log("Added group: " + groupname);
            //$(".navGroupName").html(""+groupname);
        }
    }
}

function setGroupDropdown(user) {
    $.ajax({
        type: 'GET',
        url: '/scrum/rest/groups/list/' + user.email,
        dataType: "json",
        success: renderGroupDropdown
    });
    //console.log(getCookie(curGroup));
    $("#navUsername").html(""+user.name);
}


function groupsContainsCurrentgroup(groups, groupid) {
    var s = false;
    var len = groups.length;
    for(var i = 0; i <len;i++){
        if (groupid === groups[i].id){
            s = true;
        }
    }
    return s;

}

