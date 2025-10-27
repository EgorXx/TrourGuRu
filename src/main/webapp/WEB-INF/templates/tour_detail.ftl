<#include "base.ftl">

<#macro page_title>Информация о туре</#macro>

<#assign useStickyFooter = true>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/tour_detail.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</#macro>

<#macro header>
    <#include "header.ftl">
</#macro>

<#macro content>
    <div class="page-wrapper">
        <div class="background-blur"></div>

        <div class="container">
            <div class="row">
                <div class="col-lg-8">
                    <div class="tour-container">

                        <div class="tour-header">
                            <#if mainImage?? && tourTitle??>
                                <img src="${mainImage.imageUrl()}" alt="${tourTitle}" class="tour-image">
                            </#if>

                            <div class="tour-info">
                                <#if title??>
                                    <h1 class="tour-title">${tourTitle}</h1>
                                </#if>

                                <#if destination??>
                                    <div class="info-row">
                                        <i class="bi bi-geo-alt-fill"></i>
                                        <span><strong>Место:</strong> ${destination}</span>
                                    </div>
                                </#if>

                                <#if duration??>
                                    <div class="info-row">
                                        <i class="bi bi-calendar-event"></i>
                                        <span><strong>Длительность:</strong> ${duration} дней</span>
                                    </div>
                                </#if>

                                <div class="categories">
                                    <#if categories??>
                                        <#list categories as category>
                                            <span class="category-badge">${category.title}</span>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </div>

                        <div class="description-section">
                            <p class="description-text">
                                <#if description??>
                                    ${description}
                                </#if>
                            </p>
                        </div>

                        <div class="row">
                            <div class="col-md-6 content-section">
                                <h2 class="section-title">Услуги</h2>
                                <ul class="services-list">
                                    <#if services??>
                                        <#list services as service>
                                            <li><i class="bi bi-check-circle-fill"></i> ${service.title}</li>
                                        </#list>
                                    </#if>
                                </ul>
                            </div>

                            <div class="col-md-6 content-section">
                                <h2 class="section-title">Информация о туре</h2>
                                <#if programs??>
                                    <#list programs as program>
                                        <div class="program-day">
                                            <div class="day-number">День ${program.dayNumber}</div>
                                            <p class="day-title">${program.title}</p>
                                            <p class="day-description">${program.description}</p>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>


                        <div>
                            <h2 class="section-title">Фотогалерея</h2>
                            <div class="gallery">
                                <#if otherImages??>
                                    <#list otherImages as image>
                                        <div class="gallery-item">
                                            <span class="gallery-label">Фото 1</span>
                                            <img src="${image.imageUrl()}">
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="operator-card">
                        <h3 class="operator-title">Организатор тура</h3>
                        <#if operatorCompanyName??>
                            <div class="company-name">${operatorCompanyName}</div>
                        </#if>

                        <#if operatorDescription??>
                            <p class="operator-description">
                                ${operatorDescription}
                            </p>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro footer>
    <#include "footer.ftl">
</#macro>