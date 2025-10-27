<#include "../base.ftl">

<#macro page_title>Туры по России — Главная</#macro>

<#macro page_styles>
    <link rel="stylesheet" href="${contextPath}/assets/css/add_tour.css">
</#macro>

<#macro content>
    <div class="background-blur"></div>

    <div class="tour-form-container">
        <div class="form-header text-center">
            <h2>Добавить новый тур</h2>
        </div>

        <#if errors?? && errors?has_content>
            <div class="error-block">
                <h6>Исправьте следующие ошибки:</h6>
                <ul>
                    <#list errors as field, message>
                        <li>${message}</li>
                    </#list>
                </ul>
            </div>
        </#if>

        <form class="form-body" action="${contextPath}/operator/add-tour" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-6">

                    <div class="mb-3">
                        <label for="tourTitle" class="form-label">Название тура</label>
                        <input type="text" class="form-control" id="tourTitle" name="title" value="${(tourTitle)!''}">
                    </div>

                    <div class="mb-3">
                        <label for="tourDestination" class="form-label">Место, Город</label>
                        <input type="text" class="form-control" id="tourDestination" name="destination" value="${(tourDestination)!''}" required>
                    </div>

                    <div class="mb-3">
                        <label for="tourDuration" class="form-label">Длительность (в днях)</label>
                        <input type="number" class="form-control" id="tourDuration" name="duration" min="1" max="30" required>
                    </div>

                    <div class="mb-3">
                        <label for="tourDescription" class="form-label">Описание тура</label>
                        <textarea class="form-control" id="tourDescription" name="description" rows="4" required>${(tourDescription)!''}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="serviceInput" class="form-label">Услуги в туре</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="serviceInput" placeholder="Например, 'Проживание'">
                            <button class="btn btn-outline-secondary" type="button" id="addServiceBtn">Добавить</button>
                        </div>
                        <ul id="servicesList" class="list-group mt-2">
                        </ul>

                        <input type="hidden" name="services" id="hiddenServices">
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">

                        <label class="form-label">Категории тура</label>
                        <#if categories??>
                            <#list categories as category>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="category_ids" value="${category.id}">
                                    <label class="form-check-label">${category.title}</label>
                                </div>
                            </#list>
                        </#if>

                    </div>

                    <div class="mb-3">
                        <label for="mainImage" class="form-label">Главная фотография тура</label>
                        <input type="file" class="form-control" id="mainImage" name="main-image" accept="image/*" required>
                    </div>

                    <div class="mb-3">
                        <label for="tourImages" class="form-label">Дополнительные фотографии</label>
                        <input type="file" class="form-control" id="tourImages" name="other-images" accept="image/*" multiple>
                    </div>
                </div>
            </div>

            <div id="programSection" style="display: none;">
                <hr>
                <h5 class="mb-3">Программа тура по дням</h5>
                <div id="programDaysContainer">
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-12 d-flex justify-content-end">
                    <button type="button" id="cancelBtn" class="btn btn-nav-outline me-2">Отменить</button>
                    <button type="submit" class="btn btn-form-submit">Сохранить тур</button>
                </div>
            </div>
        </form>
    </div>
</#macro>

<#macro scripts>
    <script src="${contextPath}/assets/js/add_tour.js"></script>
</#macro>

