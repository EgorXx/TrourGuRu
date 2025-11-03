<#include "base.ftl">

<#macro page_title>Личный кабинет</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/profile.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="profile-card">
        <div class="profile-card-header">
            <a href="${contextPath}/main">
                <img src="${contextPath}/assets/image/logo.jpg" alt="TourGuRu" width="120">
            </a>
            <a href="${contextPath}/logout" class="btn-logout">Выйти</a>
        </div>

        <div>
            <h1>Привет, ${user.displayName()}!</h1>
            <p class="text-muted">Email: ${user.email()}</p>
        </div>

        <div class="profile-actions">
            <a href="${contextPath}/profile/edit" class="btn btn-profile-action">
                <span class="icon-circle"><i class="bi bi-pencil-fill"></i></span>
                Редактировать профиль
            </a>

            <#if user.isUser()>
                <a href="${contextPath}/user/applications" class="btn btn-profile-action">
                    <span class="icon-circle"><i class="bi bi-calendar-check-fill"></i></span>
                    Мои заявки
                </a>

                <a href="${contextPath}/user/my-favorite" class="btn btn-profile-action">
                    <span class="icon-circle"><i class="bi bi-heart-fill"></i></span>
                    Избранное
                </a>
            </#if>

            <#if user.isOperator()>
                <a href="${contextPath}/operator/my-tours" class="btn btn-profile-action">
                    <span class="icon-circle"><i class="bi bi-brightness-high"></i></span>
                    Мои туры
                </a>

                <a href="${contextPath}/operator/add-tour" class="btn btn-profile-action">
                    <span class="icon-circle"><i class="bi bi-plus-circle"></i></span>
                    Добавить тур
                </a>

                <a href="${contextPath}/operator/applications" class="btn btn-profile-action">
                    <span class="icon-circle"><i class="bi bi-clipboard-check"></i></span>
                    Заявки
                </a>
            </#if>
        </div>
    </div>
</#macro>