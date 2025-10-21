const formInputs = document.querySelectorAll('.form-container form input');

formInputs.forEach(input => {
    input.addEventListener('input', () => {
        const errorElement = document.getElementById(input.id + '-error');

        const formError = document.getElementById('form-error');

        if (formError) {
            formError.remove();
        }

        if (errorElement) {
            errorElement.remove();
        }
    });
});