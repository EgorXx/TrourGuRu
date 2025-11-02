<#include "base.ftl">

<#macro page_title>Информация о туре</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/tour_detail.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</#macro>

<#macro header>
    <#include "header.ftl">
</#macro>

<#macro content>
    <div class="page-wrapper">
        <div class="background-blur"></div>

        <div class="container">
            <div class="row">
                <div class="col-lg-8">
                    <div class="tour-container">

                        <div class="tour-header">
                            <#if mainImage?? && tourTitle??>
                                <img src="${mainImage.imageUrl()}" alt="${tourTitle}" class="tour-image">
                            </#if>

                            <div class="tour-info">
                                <#if title??>
                                    <h1 class="tour-title">${tourTitle}</h1>
                                </#if>

                                <#if destination??>
                                    <div class="info-row">
                                        <i class="bi bi-geo-alt-fill"></i>
                                        <span><strong>Место:</strong> ${destination}</span>
                                    </div>
                                </#if>

                                <#if duration??>
                                    <div class="info-row">
                                        <i class="bi bi-calendar-event"></i>
                                        <#if duration == 1>
                                            <span><strong>Длительность:</strong> ${duration} день</span>
                                        <#elseif (duration >= 2) && (duration <= 4)>
                                            <span><strong>Длительность:</strong> ${duration} дня</span>
                                        <#else>
                                            <span><strong>Длительность:</strong> ${duration} дней</span>
                                        </#if>
                                    </div>
                                </#if>

                                <div class="categories">
                                    <#if categories??>
                                        <#list categories as category>
                                            <span class="category-badge">${category.title}</span>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </div>

                        <div class="description-section">
                            <p class="description-text">
                                <#if description??>
                                    ${description}
                                </#if>
                            </p>
                        </div>

                        <div class="row">
                            <div class="col-md-6 content-section">
                                <h2 class="section-title">Услуги</h2>
                                <ul class="services-list">
                                    <#if services??>
                                        <#list services as service>
                                            <li><i class="bi bi-check-circle-fill"></i> ${service.title}</li>
                                        </#list>
                                    </#if>
                                </ul>
                            </div>

                            <div class="col-md-6 content-section">
                                <h2 class="section-title">Информация о туре</h2>
                                <#if programs??>
                                    <#list programs as program>
                                        <div class="program-day">
                                            <div class="day-number">День ${program.dayNumber}</div>
                                            <p class="day-title">${program.title}</p>
                                            <p class="day-description">${program.description}</p>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>


                        <div>
                            <h2 class="section-title">Фотогалерея</h2>
                            <div class="gallery">
                                <#if otherImages??>
                                    <#list otherImages as image>
                                        <div class="gallery-item">
                                            <span class="gallery-label">Фото 1</span>
                                            <img src="${image.imageUrl()}">
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>


                        <div class="reviews-section">
                            <h2 class="section-title">Комментарии</h2>

                            <#if user??>
                                <#if reviewErrors??>
                                    <div class="alert-error">
                                        <#list reviewErrors?values as error>
                                            <p>${error}</p>
                                        </#list>
                                    </div>
                                </#if>

                                <form action="${contextPath}/reviews/add" method="POST" class="review-form">
                                    <input type="hidden" name="tourId" value="${tourId}">
<#--                                    maxlength="1023"-->
                                    <textarea name="text" placeholder="Ваш комментарий..." required>${oldText!""}</textarea>
                                    <button type="submit">Отправить</button>
                                </form>
                            <#else>
                                <p class="login-hint"><a href="${contextPath}/login">Войдите</a>, чтобы оставить комментарий</p>
                            </#if>

                            <div id="reviews-list">
                                <#if reviews?? && reviews?has_content>
                                    <#list reviews as review>
                                        <div class="review-item" id="review-${review.id()}">
                                            <div class="review-top">
                                                <strong>${review.userName()}</strong>
                                                <#if review.isOwner()>
                                                    <span class="badge-owner">Владелец</span>
                                                </#if>
                                                <span class="review-date">${review.createdDate()}</span>
                                                <#if user?? && user.id() == review.userId()>
                                                    <button class="btn-delete" data-review-id="${review.id()}">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </#if>
                                            </div>
                                            <p>${review.text()}</p>
                                        </div>
                                    </#list>
                                <#else>
                                    <p class="empty-text">Пока нет комментариев</p>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="operator-card">
                        <h3 class="operator-title">Организатор тура</h3>
                        <#if operatorCompanyName??>
                            <div class="company-name">${operatorCompanyName}</div>
                        </#if>

                        <#if operatorDescription??>
                            <p class="operator-description">
                                ${operatorDescription}
                            </p>
                        </#if>

                        <div id="application-message" class="application-message"></div>

                        <button class="favorite-btn btn-favorite" data-tour-id="${tourId}">
                            <i class="bi bi-heart"></i>
                            <span>В избранное</span>
                        </button>

                        <#if user?? && user.isUser()>
                            <button id="apply-btn"
                                    class="btn-apply"
                                    data-tour-id="${tourId}">
                                <i class="bi bi-check-circle"></i>
                                Оставить заявку
                            </button>
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
    <script src="${contextPath}/assets/js/tour_detail.js"></script>
    <script src="${contextPath}/assets/js/reviews.js"></script>
</#macro>

<#macro footer>
    <#include "footer.ftl">
</#macro>