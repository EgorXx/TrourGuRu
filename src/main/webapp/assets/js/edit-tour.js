document.addEventListener('DOMContentLoaded', function () {
    const durationInput = document.getElementById('tourDuration');
    const programSection = document.getElementById('programSection');
    const programDaysContainer = document.getElementById('programDaysContainer');

    function renderProgramDays(count) {
        programDaysContainer.innerHTML = '';

        if (count > 0) {
            programSection.style.display = 'block';
        } else {
            programSection.style.display = 'none';
            return;
        }

        for (let i = 1; i <= count; i++) {
            const dayDiv = document.createElement('div');
            dayDiv.className = 'program-day';

            let title = '';
            let description = '';

            if (typeof existingPrograms !== 'undefined' && existingPrograms.length > 0) {
                const existingProgram = existingPrograms.find(p => p.dayNumber === i);
                if (existingProgram) {
                    title = existingProgram.title;
                    description = existingProgram.description;
                }
            }

            dayDiv.innerHTML = `
                <h6>День ${i}</h6>
                <input type="text" name="program_title" class="form-control mb-2" placeholder="Заголовок дня ${i}" value="${title}">
                <textarea name="program_description" class="form-control" rows="2" placeholder="Описание дня ${i}">${description}</textarea>
                <input type="hidden" name="program_day" value="${i}">
            `;
            programDaysContainer.appendChild(dayDiv);
        }
    }

    const initialDuration = parseInt(durationInput.value, 10);
    if (!isNaN(initialDuration) && initialDuration > 0) {
        renderProgramDays(initialDuration);
    }

    durationInput.addEventListener('input', function () {
        const duration = parseInt(this.value, 10);

        if (!isNaN(duration) && duration > 0 && duration <= 30) {
            renderProgramDays(duration);
        } else {
            renderProgramDays(0);
        }
    });

    const serviceInput = document.getElementById('serviceInput');
    const addServiceBtn = document.getElementById('addServiceBtn');
    const servicesList = document.getElementById('servicesList');
    const hiddenServices = document.getElementById('hiddenServices');


    function updateHiddenServices() {
        const items = [];
        servicesList.querySelectorAll('li').forEach(function(item) {
            items.push(item.textContent);
        });

        hiddenServices.value = items.join(',');
    }

    updateHiddenServices();

    addServiceBtn.addEventListener('click', function() {
        const serviceText = serviceInput.value.trim();

        if (serviceText !== "") {
            const listItem = document.createElement('li');
            listItem.className = 'list-group-item d-flex justify-content-between align-items-center';
            listItem.textContent = serviceText;

            const deleteBtn = document.createElement('button');
            deleteBtn.type = 'button';
            deleteBtn.className = 'btn-close';
            deleteBtn.ariaLabel = 'Close';

            deleteBtn.onclick = function() {
                listItem.remove();
                updateHiddenServices();
            };

            listItem.appendChild(deleteBtn);
            servicesList.appendChild(listItem);

            serviceInput.value = '';
            updateHiddenServices();
        }
    });

    servicesList.querySelectorAll('.btn-close').forEach(function(btn) {
        btn.onclick = function() {
            this.closest('li').remove();
            updateHiddenServices();
        };
    });

    const cancelBtn = document.getElementById('cancelBtn');

    cancelBtn.addEventListener('click', function () {
        window.history.back();
    });
});
