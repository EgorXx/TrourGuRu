<#include "../base.ftl">

<#macro page_title>Заявки на регистрацию</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/admin_operators.css">
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
                <h2 class="page-title">Заявки операторов на регистрацию</h2>

                <#if operators?? && (operators?size > 0)>
                    <#list operators as operator>
                        <div id="operator-${operator.userId()}" class="application-card">
                            <div class="application-card-body">
                                <div class="application-info">
                                    <span class="status-badge status-pending">В ожидании</span>

                                    <h5 class="application-title">${operator.companyName()}</h5>

                                    <div class="application-detail">
                                        <i class="bi bi-envelope-fill"></i>
                                        <span>${operator.email()}</span>
                                    </div>

                                    <div class="application-user">
                                        <div class="application-user-info">ID пользователя: <strong>${operator.userId()}</strong></div>
                                    </div>
                                </div>

                                <div class="application-actions">
                                    <button
                                            class="btn-application btn-application-approve"
                                            onclick="approveOperator(${operator.userId()})">
                                        Одобрить
                                    </button>
                                    <button
                                            class="btn-application btn-application-reject"
                                            onclick="rejectOperator(${operator.userId()})">
                                        Отклонить
                                    </button>
                                </div>
                            </div>
                        </div>
                    </#list>

                <#else>
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <p>Заявок на регистрацию пока нет</p>
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
    <script src="${contextPath}/assets/js/admin_operators.js"></script>
</#macro>

<#macro footer>
    <#include "../footer.ftl">
</#macro>
