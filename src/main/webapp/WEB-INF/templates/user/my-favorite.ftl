<#include "../base.ftl">

<#macro page_title>Мои избранные</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/my-favorite.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</#macro>

<#macro header>
    <#include "../header.ftl">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="tours-container">
        <div class="container">
            <div class="tours-content-wrapper">
                <h2 class="section-title">Мои избранные</h2>

                <div class="row" id="tours-list">
                    <#if tourCards?? && tourCards?has_content>
                        <#list tourCards as tourCard>
                            <div class="col-lg-4 col-md-6 col-sm-12">
                                <div id="tour-card-${tourCard.id()}" class="tour-card">
                                    <img src="${tourCard.mainImageUrl()}" alt="${tourCard.title()}" class="tour-image">

                                    <div class="tour-content">
                                        <h5 class="tour-title">${tourCard.title()}</h5>

                                        <div class="tour-info">
                                            <i class="bi bi-geo-alt-fill"></i>
                                            <span>${tourCard.destination()}</span>
                                        </div>

                                        <div class="tour-info">
                                            <i class="bi bi-clock-fill"></i>
                                            <#if tourCard.duration() == 1>
                                                <span>${tourCard.duration()} день</span>
                                            <#elseif (tourCard.duration() >= 2) && (tourCard.duration() <= 4)>
                                                <span>${tourCard.duration()} дня</span>
                                            <#else>
                                                <span>${tourCard.duration()} дней</span>
                                            </#if>
                                        </div>

                                        <div class="tour-operator">
                                            ${tourCard.operatorName()}
                                        </div>

                                        <div class="tour-actions">
                                            <a href="${contextPath}/tours/${tourCard.id()}" class="btn btn-details">Подробнее</a>
                                            <button class="favorite-btn" data-tour-id="${tourCard.id()}">
                                                <i class="bi bi-heart"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>

                    <#else>
                        <div class="col-12">
                            <div class="empty-state">
                                <i class="bi bi-suitcase-lg"></i>
                                <p>У вас пока нет добавленных в избранное туров</p>
                            </div>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro scripts>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        const contextPath = '${contextPath}';
        const isLoggedIn = <#if user??>true<#else>false</#if>;
        const userFavorites = new Set([
            <#if favoriteTourIds??>
            <#list favoriteTourIds as id>${id}<#sep>,</#list>
            </#if>
        ]);
    </script>
    <script src="${contextPath}/assets/js/favorite.js"></script>
</#macro>

<#macro footer>
    <#include "../footer.ftl">
</#macro>