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