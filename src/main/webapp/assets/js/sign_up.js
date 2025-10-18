const roleButtons = document.querySelectorAll('.role-selector .btn');
const roleInput = document.getElementById('role');

const userNameField = document.getElementById('user-name-field');
const operatorNameField = document.getElementById('operator-name-field');
const userInput = userNameField.querySelector('input');
const operatorInput = operatorNameField.querySelector('input');

const formInputs = document.querySelectorAll('.form-container form input');

roleButtons.forEach(button => {
    button.addEventListener('click', () => {
        roleButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');

        if (button.textContent.trim() === 'Тур оператор') {
            roleInput.value = 'OPERATOR';

            operatorNameField.classList.remove('d-none');
            userNameField.classList.add('d-none');

            operatorInput.disabled = false
            operatorInput.required = true;

            userInput.disabled = true;
            userInput.required = false;
        } else {
            roleInput.value = 'USER';

            userNameField.classList.remove('d-none');
            operatorNameField.classList.add('d-none');

            userInput.disabled = false
            userInput.required = true;

            operatorInput.disabled = true;
            operatorInput.required = false;
        }
    });
});

formInputs.forEach(input => {
    input.addEventListener('input', () => {
        const errorElement = document.getElementById(input.id + '-error');
        if (errorElement) {
            errorElement.remove();
        }
    });
});