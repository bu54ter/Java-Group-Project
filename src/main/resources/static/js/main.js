function clearNewUserForm() {
    const form = document.getElementById('newUserForm');
    const passwordRule = document.getElementById('passwordRule');

    if (form) {
        const textInputs = form.querySelectorAll('input[type="text"], input[type="password"]');
        textInputs.forEach(function(input) {
            input.value = "";
        });

        const radioInputs = form.querySelectorAll('input[type="radio"]');
        radioInputs.forEach(function(input) {
            input.checked = false;
        });

        const passwordInputs = form.querySelectorAll('.password-input-wrapper input');
        passwordInputs.forEach(function(input) {
            input.type = "password";
        });

        const toggleButtons = form.querySelectorAll('.toggle-password-btn');
        toggleButtons.forEach(function(button) {
            button.setAttribute("aria-label", "Show password");
            button.setAttribute("title", "Show password");
        });
    }

    if (passwordRule) {
        passwordRule.textContent = "Select a role to see the minimum password length.";
    }
}

function openNewUserModal() {
    clearNewUserForm();
    document.getElementById('newUserModal').style.display = 'block';
}

function closeNewUserModal() {
    clearNewUserForm();
    document.getElementById('newUserModal').style.display = 'none';
}

function updatePasswordRule() {
    const selectedRole = document.querySelector('#newUserModal input[name="role"]:checked');
    const passwordRule = document.getElementById('passwordRule');

    if (!passwordRule) {
        return;
    }

    if (!selectedRole) {
        passwordRule.textContent = "Select a role to see the minimum password length.";
    } else if (selectedRole.value === "STUDENT") {
        passwordRule.textContent = "Minimum 8 characters.";
    } else if (selectedRole.value === "LECTURER") {
        passwordRule.textContent = "Minimum 10 characters.";
    } else if (selectedRole.value === "ADMIN") {
        passwordRule.textContent = "Minimum 12 characters.";
    }
}

function togglePassword(button) {
    const wrapper = button.closest('.password-input-wrapper');
    const input = wrapper.querySelector('input');

    if (!input) {
        return;
    }

    if (input.type === "password") {
        input.type = "text";
        button.setAttribute("aria-label", "Hide password");
        button.setAttribute("title", "Hide password");
    } else {
        input.type = "password";
        button.setAttribute("aria-label", "Show password");
        button.setAttribute("title", "Show password");
    }
}

window.onclick = function(event) {
    const loginModal = document.getElementById('loginModal');
    const newUserModal = document.getElementById('newUserModal');
    const newNounModal = document.getElementById('newNounModal');

    if (event.target == loginModal) {
        loginModal.style.display = "none";
    }

    if (event.target == newUserModal) {
        closeNewUserModal();
    }

    if (event.target == newNounModal) {
        newNounModal.style.display = "none";
    }
}

document.addEventListener("DOMContentLoaded", updatePasswordRule);

function toggleAnswer(button) {
    const answer = button.nextElementSibling;

    if (answer.style.display === "none") {
        answer.style.display = "block";
        button.textContent = "Hide Answer";
    } else {
        answer.style.display = "none";
        button.textContent = "Show Answer";
    }
}