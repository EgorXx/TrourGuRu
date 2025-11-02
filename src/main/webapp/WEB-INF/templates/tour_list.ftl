<#include "base.ftl">

<#macro page_title>Туры</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/tour_list.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</#macro>

<#macro header>
    <#include "header.ftl">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="tours-container">
        <div class="container">
            <div class="row">
=
                <div class="col-lg-2">
                    <form action="${contextPath}/tours" method="GET" id="filter-form">
                        <div class="filters-sidebar">
                            <h3 class="filters-title">Фильтры</h3>

                            <div class="filter-group">
                                <label class="filter-label">Место назначения</label>
                                <select name="destination" class="filter-select">
                                    <option value="">Все направления</option>
                                    <#if destinations??>
                                        <#list destinations as dest>
                                            <option value="${dest}"
                                                    <#if destination?? && destination == dest>selected</#if>>
                                                ${dest}
                                            </option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>

                            <div class="filter-group">
                                <label class="filter-label">Длительность (дней)</label>
                                <div class="duration-inputs">
                                    <input
                                            type="number"
                                            name="minDuration"
                                            class="filter-input"
                                            placeholder="От"
                                            min="1"
                                            value="${minDuration!''}">
                                    <span class="duration-separator">—</span>
                                    <input
                                            type="number"
                                            name="maxDuration"
                                            class="filter-input"
                                            placeholder="До"
                                            min="1"
                                            value="${maxDuration!''}">
                                </div>
                            </div>

                            <input type="hidden" name="page" value="1">

                            <button type="submit" class="btn-filter-apply">Применить</button>
                            <a href="${contextPath}/tours" class="btn-filter-reset">Сбросить</a>
                        </div>
                    </form>
                </div>

                <div class="col-lg-9">
                    <div class="tours-content-wrapper">
                        <div class="search-section">
                            <div class="search-wrapper">
                                <i class="bi bi-search" style="color: #999; font-size: 1.2rem;"></i>
                                <input type="text" name="search" class="search-input" placeholder="Найти тур..." value="${search!''}" form="filter-form">
                                <button type="submit" class="btn-search" form="filter-form">Поиск</button>
                            </div>
                        </div>

                        <h2 class="section-title">Информация о турах</h2>

                        <div class="row" id="tours-list">
                                <#if tourCards??>
                                    <#list tourCards as tourCard>
                                        <div class="col-lg-4 col-md-6 col-sm-12">

                                            <div class="tour-card">
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
                                </#if>
                        </div>

                        <#if hasMore?? && hasMore>
                            <div class="load-more-container">
                                <button class="btn btn-load-more" id="load-more-btn">Загрузить еще</button>
                            </div>
                        </#if>
                    </div>
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
    <script src="${contextPath}/assets/js/tour_list.js"></script>
</#macro>

<#macro footer>
    <#include "footer.ftl">
</#macro>