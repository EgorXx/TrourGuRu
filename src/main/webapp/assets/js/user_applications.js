function cancelApplication(applicationId) {
    $(document).ready(function () {
        const button = $('#cancel-btn-' + applicationId);
        const jsonString = '{"applicationId":' + applicationId + '}';

        button.prop('disabled', true);

        $.ajax({
            url: contextPath + '/application/cancel',
            type: 'POST',
            contentType: 'application/json',
            data: jsonString,

            success: function (response) {

                if (response.success) {
                    $('#application-' + applicationId).fadeOut(300, function () {
                        $(this).remove();
                    });

                    button.prop('disabled', false);
                } else {
                    alert(response.message || 'Произошла ошибка');

                    button.prop('disabled', false);
                }
            },

            error: function (xhr) {

                if (xhr.status === 401) {
                    window.location.href = contextPath + '/login';
                } else {
                    alert('Ошибка сервера, попробуйте позже');

                    button.prop('disabled', false);
                }
            }
        });
    });
}