$("#loginForm").submit(function (event) {
    event.preventDefault();

    // Get the form data
    var formData = {
        username: $("#username").val(),
        password: $("#password").val()
    };

    // Send the login request to the server
        $.ajax({
            type: "POST",
            url: "/authenticate",
            data: formData,
            success: function (response) {
                // Handle the response, which contains the JWT token
                var token = response.token;

                // Store the token securely in a cookie
                setTokenInCookie(token);

                $.ajaxSetup({
                            beforeSend: function(xhr) {
                                xhr.setRequestHeader('Authorization', 'Bearer ' + token);
                            }
                        });

                // Redirect to the profile page or perform any other necessary action
                window.location.href = "/profile";
            },
            error: function (error) {
                window.location.href = "/login";
            }
        });
    });

    function setTokenInCookie(token) {
        document.cookie = `jwtToken=${token}; path=/`;
    }