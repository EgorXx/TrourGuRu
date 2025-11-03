<#include 'base.ftl'>

<#macro page_title>Регистрация</#macro>
<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/sign_up.css">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="form-container">
        <div class="form-header text-center mb-4">
            <h1>Регистрация аккаунта</h1>
            <p>Быстрый и простой способ начать ваше путешествие</p>
        </div>

        <#if errors?? && errors.role??>
                <span id="role-error" class="error-message">Ошибка: ${errors.role}</span>
        </#if>

        <div class="role-selector btn-group d-flex p-1 mb-4" role="group">
            <button type="button" class="btn active flex-fill">Пользователь</button>
            <button type="button" class="btn flex-fill">Тур оператор</button>
        </div>

        <form action="${contextPath}/sign_up" method="POST">
            <input type="hidden" id="role" name="role" value="USER">

            <div class="mb-3">
                <#if errors?? && errors.email??>
                    <span id="email-error" class="error-message">Ошибка: ${errors.email}</span>
                </#if>
                <input type="email" id="email" class="form-control" name="email" placeholder="Электронная почта" required value="${(email)!''}">
                <span id="email-status" class="check-message"></span>
            </div>

            <div id="user-name-field" class="mb-3">
                <#if errors?? && errors.username??>
                    <span id="username-error" class="error-message">Ошибка: ${errors.username}</span>
                </#if>
                <input type="text" id="username" class="form-control" name="username" placeholder="Имя пользователя" required value="${(username)!''}">
            </div>

            <div id="operator-name-field" class="mb-3 d-none">
                <#if errors?? && errors.company_name??>
                    <span id="company_name-error" class="error-message">Ошибка: ${errors.company_name}</span>
                </#if>
                <input type="text" id="company_name" class="form-control" name="company_name" placeholder="Название компании" required disabled>
            </div>

            <div class="mb-3">
                <#if errors?? && errors.password??>
                    <span id="password-error" class="error-message">Ошибка: ${errors.password}</span>
                </#if>
                <input type="password" id="password" class="form-control" name="password" placeholder="Пароль" required minlength="8">
            </div>
            <button id="submit-button" type="submit" class="btn btn-submit w-100">Зарегистрироваться</button>
        </form>

        <div class="login-link">
            <span>Уже есть аккаунт? </span>
            <a href="${contextPath}/login">Войти</a>
        </div>
</#macro>

<#macro scripts>
    <script src="${contextPath}/assets/js/sign_up.js"></script>
        <script>
        const contextPath = '${contextPath}';
    </script>
    <script src="${contextPath}/assets/js/user_exist.js"></script>
</#macro>