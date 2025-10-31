<#include "../base.ftl">

<#macro page_title>Мои заявки</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/user_applications.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</#macro>

<#macro header>
    <#include "../header.ftl">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="applications-container">
        <div class="container">
            <div class="applications-content-wrapper">
                <h2 class="page-title">Мои заявки</h2>

                <#if userApplications?? && (userApplications?size > 0)>
                    <#list userApplications as userApplication>
                        <div id="application-${userApplication.id()}" class="application-card">
                            <div class="application-card-body">
                                <img src="${userApplication.cardTour().mainImageUrl()}"
                                     alt="${userApplication.cardTour().title()}"
                                     class="application-image">

                                <div class="application-info">
                                    <#if userApplication.status() == 'PENDING'>
                                            <span class="status-badge status-pending">На рассмотрении</span>
                                        <#elseif userApplication.status() == 'APPROVED'>
                                            <span class="status-badge status-approved">Подтверждена</span>
                                    </#if>

                                    <h5 class="application-title">${userApplication.cardTour().title()}</h5>

                                    <div class="application-detail">
                                        <i class="bi bi-geo-alt-fill"></i>
                                        <span>${userApplication.cardTour().destination()}</span>
                                    </div>

                                    <div class="application-detail">
                                        <i class="bi bi-clock-fill"></i>

                                        <#if userApplication.cardTour().duration() == 1>
                                            <span>${userApplication.cardTour().duration()} день</span>
                                        <#elseif (userApplication.cardTour().duration() >= 2) && (userApplication.cardTour().duration() <= 4)>
                                            <span>${userApplication.cardTour().duration()} дня</span>
                                        <#else>
                                            <span>${userApplication.cardTour().duration()} дней</span>
                                        </#if>

                                    </div>

                                    <div class="application-operator">
                                        Туроператор: ${userApplication.cardTour().operatorName()}.
                                    </div>
                                </div>

                                <div class="application-actions">
                                    <a href="${contextPath}//tours/${userApplication.cardTour().id()}" class="btn btn-details">Подробнее</a>
                                    <#if userApplication.status() == 'PENDING'>
                                        <button id="cancel-btn-${userApplication.id()}" class="btn btn-cancel" onclick="cancelApplication(${userApplication.id()})">
                                            Отозвать
                                        </button>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </#list>

                <#else>
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <p>Заявок пока нет</p>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</#macro>

<#macro scripts>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        const contextPath = '${contextPath}';
    </script>
    <script src="${contextPath}/assets/js/user_applications.js"></script>
</#macro>

<#macro footer>
    <#include "../footer.ftl">
</#macro>