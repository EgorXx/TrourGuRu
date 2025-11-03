<#include 'base.ftl'>

<#macro page_title>Ошибка ${errorCode!'500'}</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/error.css">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="error-box">
        <h1>${errorCode!'500'}</h1>
        <p>${errorMessage!'Что-то пошло не так'}</p>
        <a href="${contextPath}/">На главную</a>
    </div>
</#macro>

