function deleteTour(tourId) {
    const confirmed = confirm('Вы уверены, что хотите удалить этот тур? Это действие нельзя отменить.');

    if (!confirmed) {
        return;
    }

    $(document).ready(function () {
        const button = $('#delete-btn-' + tourId);
        const jsonString = '{"tourId":' + tourId + '}';

        button.prop('disabled', true);

        $.ajax({
            url: contextPath + '/tour/delete',
            type: 'POST',
            contentType: 'application/json',
            data: jsonString,

            success: function (response) {

                if (response.success) {
                    $('#tour-card-' + tourId).fadeOut(300, function () {
                        $(this).remove();
                    });

                    button.prop('disabled', false);
                } else {
                    alert(response.message || 'Произошла ошибка');

                    button.prop('disabled', false);
                }
            },

            error: function (xhr) {

                if (xhr.status === 401 || xhr.status === 403) {
                    window.location.href = contextPath + '/login';
                } else {
                    alert('Ошибка сервера, попробуйте позже');

                    button.prop('disabled', false);
                }
            }
        });
    });
}