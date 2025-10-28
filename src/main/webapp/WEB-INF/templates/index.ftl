<#include "base.ftl">

<#macro page_title>Туры по России — Главная</#macro>
<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/index.css">
</#macro>

<#macro header>
    <#include "header.ftl">
</#macro>

<#macro content>

    <main class="flex-grow-1">
        <section class="hero">
            <div class="container">
                <h1>Открывайте Россию по новому</h1>
                <a href="${contextPath}/tours" class="btn btn-hero">Выбрать тур</a>
            </div>
        </section>

        <section id="about" class="py-5 bg-white">
            <div class="container">
                <h2 class="fw-bold text-center my-green mb-5">Почему выбирают нас</h2>
                <div class="row text-center g-4">
                    <div class="col-md-4">
                        <h5 class="fw-bold">Удобный поиск</h5>
                        <p class="text-muted">Подбирайте туры по направлениям, датам и длительности.</p>
                    </div>
                    <div class="col-md-4">
                        <h5 class="fw-bold">По всей России</h5>
                        <p class="text-muted">От Красной Поляны до Байкала и Камчатки.</p>
                    </div>
                    <div class="col-md-4">
                        <h5 class="fw-bold">Надёжные партнёры</h5>
                        <p class="text-muted">Сотрудничаем только с проверенными туроператорами.</p>
                    </div>
                </div>
            </div>
        </section>

        <section class="py-5 partners-section">
            <div class="container text-center">
                <div class="row justify-content-center">
                    <div class="col-lg-8">
                        <i class="bi bi-briefcase-fill text-success" style="font-size: 3rem;"></i>
                        <h2 class="fw-bold text-dark my-3">Станьте нашим партнёром</h2>
                        <p class="lead text-muted mb-4">
                            Размещайте свои туры на нашей платформе и получайте доступ к тысячам путешественников со всей страны. Мы предлагаем простую интеграцию, удобный личный кабинет и поддержку на всех этапах.
                        </p>
                        <a href="${contextPath}/sign_up" class="btn btn-operator">Зарегистрировать компанию</a>
                    </div>
                </div>
            </div>
        </section>
    </main>

</#macro>

<#macro footer>
    <#include "footer.ftl">
</#macro>
