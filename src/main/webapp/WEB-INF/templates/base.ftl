<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><@page_title></@page_title></title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <#if .namespace.page_styles??>
        <@page_styles/>
    </#if>
</head>

<body class="d-flex flex-column min-vh-100 bg-light">

    <#if .namespace.header??>
        <@header></@header>
    </#if>

    <#if .namespace.content??>
        <@content></@content>
    </#if>

    <#if .namespace.scripts??>
        <@scripts></@scripts>
    </#if>

    <#if .namespace.footer??>
        <@footer></@footer>
    </#if>

<#--    <#if .namespace.scripts??>-->
<#--        <@scripts></@scripts>-->
<#--    </#if>-->
</body>