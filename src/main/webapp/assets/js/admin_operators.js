function approveOperator(userId) {
    if (!confirm('Одобрить регистрацию этого оператора?')) {
        return;
    }

    $.ajax({
        url: contextPath + '/admin/operators/approve',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ userId: userId }),

        success: function(response) {
            if (response.success) {
                $('#operator-' + userId).fadeOut(300, function() {
                    $(this).remove();

                    if ($('.application-card').length === 0) {
                        $('.applications-content-wrapper').html(`
                            <h2 class="page-title">Заявки операторов на регистрацию</h2>
                            <div class="empty-state">
                                <i class="bi bi-inbox"></i>
                                <p>Заявок на регистрацию пока нет</p>
                            </div>
                        `);
                    }
                });
            } else {
                alert(response.message || 'Ошибка при одобрении');
            }
        },

        error: function() {
            alert('Ошибка сервера');
        }
    });
}

function rejectOperator(userId) {
    if (!confirm('Отклонить регистрацию? Оператор будет удалён из системы.')) {
        return;
    }

    $.ajax({
        url: contextPath + '/admin/operators/reject',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ userId: userId }),

        success: function(response) {
            if (response.success) {
                $('#operator-' + userId).fadeOut(300, function() {
                    $(this).remove();

                    if ($('.application-card').length === 0) {
                        $('.applications-content-wrapper').html(`
                            <h2 class="page-title">Заявки операторов на регистрацию</h2>
                            <div class="empty-state">
                                <i class="bi bi-inbox"></i>
                                <p>Заявок на регистрацию пока нет</p>
                            </div>
                        `);
                    }
                });
            } else {
                alert(response.message || 'Ошибка при отклонении');
            }
        },

        error: function() {
            alert('Ошибка сервера');
        }
    });
}
