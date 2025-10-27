<#include "base.ftl">

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/login.css">
</#macro>

<#macro page_title>Авторизация</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="form-container">

        <div class="form-header text-center mb-4">
            <h1>Авторизация</h1>
            <p>Быстрый и простой способ начать ваше путешествие</p>
        </div>

        <#if formError??>
            <span id="form-error" class="error-form-message">${formError}</span>
        </#if>

        <form action="${contextPath}/login" method="POST">
            <#if errors?? && errors.email??>
                <span id="email-error" class="error-message">${errors.email}</span>
            </#if>
            <div class="mb-3">
                <input type="email" id="email" class="form-control" name="email" placeholder="Электронная почта" required value="${(email)!'' }">
            </div>

            <#if errors?? && errors.password??>
                <span id="password-error" class="error-message">${errors.password}</span>
            </#if>
            <div class="mb-3">
                <input type="password" id="password" class="form-control" name="password" placeholder="Пароль" required>
            </div>

            <button type="submit" class="btn btn-submit w-100">Войти</button>
        </form>


        <div class="register-link">
            <span>Нет аккаунта? </span>
            <a href="${contextPath}/sign_up">Зарегистрироваться</a>
        </div>

    </div>
</#macro>

<#macro scripts>
    <script src="${contextPath}/assets/js/login.js"></script>
</#macro>