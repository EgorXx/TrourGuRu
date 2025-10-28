<#include "base.ftl">

<#macro page_title>Личный кабинет</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/profile_edit.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</#macro>

<#macro content>
    <div class="background-blur"></div>


    <div class="profile-card">
        <h2>Редактирование профиля</h2>

        <form action="${contextPath}/profile/update" method="POST" id="profileForm">

            <#if user.isUser()>
                <div class="mb-3">
                    <label for="name" class="form-label">Имя</label>
                    <input type="text" class="form-control" id="name" name="name" value="${user.displayName()!""}" required>
                </div>
            </#if>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${user.email()!""}" required>
            </div>

            <#if user.isOperator()>
                <div class="mb-3">
                    <label for="companyName" class="form-label">Название компании</label>
                    <input type="text" class="form-control" id="companyName" name="companyName" value="${operator.companyName()!""}" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Описание компании</label>
                    <textarea class="form-control" id="description" name="description">${operator.description()!""}</textarea>
                </div>
            </#if>


            <button type="submit" class="btn-save">Сохранить</button>
        </form>

        <div class="password-section">
            <div class="collapsible-section" onclick="togglePasswordSection()">
                <span class="form-label mb-0">Изменить пароль</span>
                <i class="bi bi-chevron-down" id="passwordToggleIcon"></i>
            </div>

            <form action="${contextPath}/profile/change-password" method="POST" id="passwordForm" class="collapsible-content">
                <div class="mb-3">
                    <label for="currentPassword" class="form-label">Текущий пароль</label>
                    <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                </div>

                <div class="mb-3">
                    <label for="newPassword" class="form-label">Новый пароль</label>
                    <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                </div>

                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Подтвердите новый пароль</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>

                <button type="submit" class="btn-save">Изменить пароль</button>
            </form>
        </div>
    </div>
</#macro>

<#macro scripts>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        const contextPath = '${contextPath}';
    </script>
    <script src="${contextPath}/assets/js/profile_edit.js"></script>
</#macro>