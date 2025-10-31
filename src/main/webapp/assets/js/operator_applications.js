function rejectApplication(applicationId) {
    $(document).ready(function () {
        const button = $('#cancel-btn-' + applicationId);
        const jsonString = '{"applicationId":' + applicationId + '}';

        button.prop('disabled', true);

        $.ajax({
            url: contextPath + '/application/reject',
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

function approveApplication(applicationId) {
    const approveButton = $(`#approve-btn-${applicationId}`);
    const rejectButton = $(`#reject-btn-${applicationId}`);
    const applicationCard = $(`#application-${applicationId}`);
    const statusBadge = applicationCard.find('.status-badge');
    const actionsContainer = applicationCard.find('.application-actions');

    const jsonString = '{"applicationId":' + applicationId + '}';

    approveButton.prop('disabled', true);
    rejectButton.prop('disabled', true);

    $.ajax({
        url: contextPath + '/application/approve',
        type: 'POST',
        contentType: 'application/json',
        data: jsonString,
        success: function (response) {
            if (response.success) {
                statusBadge
                    .removeClass('status-pending')
                    .addClass('status-approved')
                    .text('Одобрено');

                actionsContainer.fadeOut(300, function() {
                    $(this).remove();
                });
            } else {
                alert(response.message || 'Произошла ошибка при одобрении');

                approveButton.prop('disabled', false);
                rejectButton.prop('disabled', false);
            }
        },
        error: function (xhr) {
            if (xhr.status === 401 || xhr.status === 403) {
                window.location.href = contextPath + '/login';
            } else {
                alert('Ошибка сервера, попробуйте позже');
                approveButton.prop('disabled', false);
                rejectButton.prop('disabled', false);
            }
        }
    });
}