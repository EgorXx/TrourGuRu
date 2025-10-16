<#include 'base.ftl'>

<#macro title>Регистрация</#macro>
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

        <div class="role-selector btn-group d-flex p-1 mb-4" role="group">
            <button type="button" class="btn active flex-fill">Пользователь</button>
            <button type="button" class="btn flex-fill">Тур оператор</button>
        </div>

        <form action="/sign_up" method="POST">
            <input type="hidden" id="role" name="role" value="USER">

            <div class="mb-3">
                <input type="email" class="form-control" name="email" placeholder="Электронная почта" required>
            </div>

            <div id="user-name-field" class="mb-3">
                <input type="text" class="form-control" name="username" placeholder="Имя пользователя" required>
            </div>

            <div id="operator-name-field" class="mb-3 d-none">
                <input type="text" class="form-control" name="company_name" placeholder="Название компании" required>
            </div>

            <div class="mb-3">
                <input type="password" class="form-control" name="password" placeholder="Пароль" required>
            </div>
            <button type="submit" class="btn btn-submit w-100">Зарегистрироваться</button>
        </form>

        <div class="login-link">
            <span>Уже есть аккаунт? </span>
            <a href="/login">Войти</a>
        </div>
</#macro>

<#macro scripts>
    <script src="${contextPath}/assets/js/sign_up.js"></script>
</#macro>