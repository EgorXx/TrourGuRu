function togglePasswordSection() {
    const content = document.getElementById('passwordForm');
    const icon = document.getElementById('passwordToggleIcon');

    content.classList.toggle('active');

    if (content.classList.contains('active')) {
        icon.classList.remove('bi-chevron-down');
        icon.classList.add('bi-chevron-up');
    } else {
        icon.classList.remove('bi-chevron-up');
        icon.classList.add('bi-chevron-down');
    }
}

document.getElementById('passwordForm').addEventListener('submit', function(e) {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
        e.preventDefault();
        alert('Новые пароли не совпадают');
        return;
    }

    if (newPassword.length < 8) {
        e.preventDefault();
        alert('Пароль должен содержать минимум 8 символов');
        return;
    }
});