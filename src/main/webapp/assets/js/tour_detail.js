$(document).ready(function() {

    $('#apply-btn').on('click', function() {

        const tourId = $(this).data('tour-id');
        const button = $(this);
        const messageDiv = $('#application-message');
        const jsonString = '{"tourId":' + tourId + '}';

        button.prop('disabled', true);

        messageDiv.hide();
        messageDiv.removeClass('success error');

        $.ajax({
            url: contextPath + '/application/add',
            type: 'POST',
            contentType: 'application/json',
            data: jsonString,

            success: function(response) {
                if (response.success) {
                    messageDiv.text(response.message);
                    messageDiv.addClass('success');
                    messageDiv.show();

                    button.prop('disabled', false);

                    setTimeout(function() {
                        messageDiv.hide();
                    }, 5000);

                } else {
                    messageDiv.text(response.message);
                    messageDiv.addClass('error');
                    messageDiv.show();

                    button.prop('disabled', false);

                    setTimeout(function() {
                        messageDiv.hide();
                    }, 5000);
                }
            },

            error: function(xhr) {
                if (xhr.status === 401) {
                    messageDiv.text('Необходима авторизация');
                    messageDiv.addClass('error');
                    messageDiv.show();

                    setTimeout(function() {
                        window.location.href = '${contextPath}/login';
                    }, 2000);

                } else {
                    messageDiv.text('Ошибка сервера, попробуйте позже');
                    messageDiv.addClass('error');
                    messageDiv.show();

                    button.prop('disabled', false);

                    setTimeout(function() {
                        messageDiv.hide();
                    }, 5000);
                }
            }
        });
    });
});
