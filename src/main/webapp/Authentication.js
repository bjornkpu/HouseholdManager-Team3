function getLoggedOnUser(success) {
    $.ajax({
        url: 'webresources/session',
        type: 'GET',
        dataType: 'json',
        success: function(session) {
            $.ajax({
                url: 'webresources/user/' + session.email,
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