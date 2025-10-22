<header class="bg-white border-bottom sticky-top">
    <div class="container py-2 d-flex align-items-center justify-content-between">
        <a class="navbar-brand" href="index.html">
            <img src="${contextPath}/assets/image/logo.jpg" alt="TourGuRu" width="144" height="42">
        </a>

        <div class="d-flex align-items-center gap-2">
            <#if user?? && (user.isUser() || user.isOperator())>
                <a class="btn btn-nav-outline" href="${contextPath}/profile">Личный кабинет</a>
                <a class="btn btn-nav-primary" href="${contextPath}/logout">Выйти</a>
                <#else>
                    <a class="btn btn-nav-outline" href="${contextPath}/login">Войти</a>
                    <a class="btn btn-nav-primary" href="${contextPath}/sign_up">Регистрация</a>
            </#if>
        </div>

    </div>
</header>