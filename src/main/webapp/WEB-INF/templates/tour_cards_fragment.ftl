<#if tourCards??>
    <#list tourCards as tourCard>
        <div class="col-lg-4 col-md-6 col-sm-12">

            <div class="tour-card">
                <img src="${tourCard.mainImageUrl()}" alt="${tourCard.title()}" class="tour-image">

                <div class="tour-content">
                    <h5 class="tour-title">${tourCard.title()}</h5>

                    <div class="tour-info">
                        <i class="bi bi-geo-alt-fill"></i>
                        <span>${tourCard.destination()}</span>
                    </div>

                    <div class="tour-info">
                        <i class="bi bi-clock-fill"></i>
                        <#if tourCard.duration() == 1>
                            <span>${tourCard.duration()} день</span>
                        <#elseif (tourCard.duration() >= 2) && (tourCard.duration() <= 4)>
                            <span>${tourCard.duration()} дня</span>
                        <#else>
                            <span>${tourCard.duration()} дней</span>
                        </#if>
                    </div>

                    <div class="tour-operator">
                        ${tourCard.operatorName()}
                    </div>

                    <a href="${contextPath}/tours/${tourCard.id()}" class="btn btn-details">Подробнее</a>
                </div>
            </div>

        </div>
    </#list>
</#if>