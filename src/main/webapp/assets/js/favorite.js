$(document).ready(function () {

    initializeFavoriteButtons();


    $(document).on('click', '.favorite-btn', function() {
        const btn = $(this);
        const tourId = parseInt(btn.data('tour-id'));


        if (!isLoggedIn) {
            window.location.href = contextPath + '/login';
            return;
        }


        btn.prop('disabled', true);

        const jsonString = '{"tourId":' + tourId + '}';

        $.ajax({
            url: contextPath + '/favorite/toggle',
            type: 'POST',
            contentType: 'application/json',
            data: jsonString,

            success: function (response) {
                if (response.success) {
                    if (response.isFavorite) {
                        btn.addClass('active');
                        userFavorites.add(tourId);
                    } else {
                        btn.removeClass('active');
                        userFavorites.delete(tourId);
                    }
                } else {
                    alert(response.message || 'Произошла ошибка');
                }

                btn.prop('disabled', false);
            },

            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.href = contextPath + '/login';
                } else if (xhr.status === 403) {
                    alert('У вас нет прав для этого действия');
                } else {
                    alert('Ошибка сервера, попробуйте позже');
                }

                btn.prop('disabled', false);
            }
        });
    });
});

function initializeFavoriteButtons() {
    document.querySelectorAll('.favorite-btn').forEach(btn => {
        const tourId = parseInt(btn.dataset.tourId);

        if (userFavorites.has(tourId)) {
            $(btn).addClass('active');
        } else {
            $(btn).removeClass('active');
        }
    });
}
