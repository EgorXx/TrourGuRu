$(document).on("input", '#email', function () {
    const email = $('#email').val().trim();

    if (!email) {
        $('#email-status').text('').removeClass('error success');
        $("#submit-button").prop("disabled", false);
        return;
    }

    $.ajax({
        url: contextPath + '/api/check-email',
        type: "GET",
        data: { email: email },

        success(response) {
            if (response.exist) {
                $('#email-status').text("Email уже занят").removeClass('success').addClass('error');
                $("#submit-button").prop("disabled", true);
            } else {
                $('#email-status').text("Email свободен").removeClass('error').addClass('success');
                $("#submit-button").prop("disabled", false);
            }
        },

    });

})
