<#include "../base.ftl">

<#macro page_title>Заявки</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/operator_applications.css">
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
                <h2 class="page-title">Заявки операторов</h2>

                <#if operatorApplications?? && (operatorApplications?size > 0)>
                    <#list operatorApplications as operatorApplication>
                        <div class="application-card">
                            <div class="application-card-body">
                                <div class="application-info">
                                    <#if operatorApplication.status() == 'PENDING'>
                                        <span class="status-badge status-pending">В ожидании</span>
                                    <#elseif operatorApplication.status() == 'APPROVED'>
                                        <span class="status-badge status-approved">Одобрено</span>
                                    </#if>

                                    <h5 class="application-title">${operatorApplication.tourTitle()}</h5>

                                    <div class="application-detail">
                                        <i class="bi bi-geo-alt-fill"></i>
                                        <span>${operatorApplication.tourDestination()}</span>
                                    </div>

                                    <div class="application-detail">
                                        <i class="bi bi-clock-fill"></i>

                                        <#if operatorApplication.tourDuration() == 1>
                                            <span>${operatorApplication.tourDuration()} день</span>
                                        <#elseif (operatorApplication.tourDuration() >= 2) && (operatorApplication.tourDuration() <= 4)>
                                            <span>${operatorApplication.tourDuration()} дня</span>
                                        <#else>
                                            <span>${operatorApplication.tourDuration()} дней</span>
                                        </#if>
                                    </div>

                                    <div class="application-user">
                                        <div class="application-user-info">Имя пользователя: <strong>${operatorApplication.userName()}</strong></div>
                                        <div class="application-user-info">Email: <strong>${operatorApplication.userEmail()}</strong></div>
                                    </div>
                                </div>

                                <div class="application-actions">
                                    <#if operatorApplication.status() == 'PENDING'>
                                        <button class="btn-application btn-application-approve" onclick="approveApplication(123)">
                                            Одобрить
                                        </button>
                                        <button class="btn-application btn-application-reject" onclick="rejectApplication(123)">
                                            Отклонить
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