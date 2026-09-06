(function () {
    'use strict';

    // Bootstrap 5 client-side validation
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            // Password match
            var pwd = form.querySelector('[name="password"]');
            var confirm = form.querySelector('[name="confirm"]');
            if (pwd && confirm && confirm.value !== pwd.value) {
                confirm.setCustomValidity('Mật khẩu xác nhận không khớp');
                event.preventDefault();
                event.stopPropagation();
            } else if (confirm) {
                confirm.setCustomValidity('');
            }
            form.classList.add('was-validated');
        }, false);
    });
})();
