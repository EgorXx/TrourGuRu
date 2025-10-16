const roleButtons = document.querySelectorAll('.role-selector .btn');
const roleInput = document.getElementById('role');

const userNameField = document.getElementById('user-name-field');
const operatorNameField = document.getElementById('operator-name-field');
const userInput = userNameField.querySelector('input');
const operatorInput = operatorNameField.querySelector('input');

roleButtons.forEach(button => {
    button.addEventListener('click', () => {
        roleButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');

        if (button.textContent.trim() === 'Тур оператор') {
            roleInput.value = 'OPERATOR';

            operatorNameField.classList.remove('d-none');
            userNameField.classList.add('d-none');

            operatorInput.required = true;
            userInput.required = false;
        } else {
            roleInput.value = 'USER';

            userNameField.classList.remove('d-none');
            operatorNameField.classList.add('d-none');

            userInput.required = true;
            operatorInput.required = false;
        }
    });
});