let currentPage = 1
let isLoading = false;

$(document).ready(function () {
    $("#load-more-btn").click(function() {
        if (isLoading) return;
        isLoading = true;

        currentPage++;

        $.get(contextPath + "/tours/load-more?page=" + currentPage)
            .done(function (html){
                $('#tours-list').append(html);

                initializeFavoriteButtons();

                let newCardsCount = $(html).filter('.col-lg-4').length;
                if (newCardsCount < 6) {
                    $("#load-more-btn").hide();
                }
                isLoading = false;
            })
            .fail(function () {
                alert("Ошибка загрузки туров");
                currentPage--;
            })
    });
});