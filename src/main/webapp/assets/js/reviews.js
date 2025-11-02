$(document).on('click', '.btn-delete', function() {
    if (!confirm('Удалить?')) return;

    const reviewId = $(this).data('review-id');

    $.ajax({
        url: contextPath + '/reviews/delete',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ reviewId: reviewId }),

        success: function(response) {
            if (response.success) {
                $('#review-' + reviewId).fadeOut(300, function() {
                    $(this).remove();

                    if ($('.review-item').length === 0) {
                        $('#reviews-list').html('<p class="empty-text">Пока нет комментариев</p>');
                    }
                });
            } else {
                alert(response.message);
            }
        },

        error: function (xhr) {
            if (xhr.status === 401) {
                window.location.href = contextPath + '/login';
            } else {
                alert('Ошибка сервера, попробуйте позже');
            }
        }
    });
});