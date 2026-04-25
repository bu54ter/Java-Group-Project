/* ======================== User Modal ======================== */

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

    if (!passwordRule) return;

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

    if (!input) return;

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


/* ======================== Modal Backdrop ======================== */

window.onclick = function(event) {
    const loginModal = document.getElementById('loginModal');
    const newUserModal = document.getElementById('newUserModal');
    const newNounModal = document.getElementById('newNounModal');

    if (event.target == loginModal) loginModal.style.display = "none";
    if (event.target == newUserModal) closeNewUserModal();
    if (event.target == newNounModal) newNounModal.style.display = "none";
};


/* ======================== Revision – Show/Hide Answers ======================== */

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


/* ======================== Reset Password ======================== */

function getMinLengthForRole(role) {
    if (role === "STUDENT") return 8;
    if (role === "LECTURER") return 10;
    if (role === "ADMIN") return 12;
    return 0;
}

function getDefaultResetPlaceholder(role) {
    if (role === "STUDENT") return "Min 8 chars";
    if (role === "LECTURER") return "Min 10 chars";
    if (role === "ADMIN") return "Min 12 chars";
    return "New password";
}

function setResetPasswordPlaceholders() {
    document.querySelectorAll('.reset-password-input').forEach(function(input) {
        input.placeholder = getDefaultResetPlaceholder(input.dataset.role);
    });
}

function handleResetPasswordSubmit(form) {
    const passwordInput = form.querySelector('.reset-password-input');
    const confirmStepInput = form.querySelector('.confirm-step');
    const firstPasswordEntryInput = form.querySelector('.first-password-entry');
    const submitButton = form.querySelector('.reset-password-btn');

    if (!passwordInput || !confirmStepInput || !firstPasswordEntryInput || !submitButton) {
        return true;
    }

    const role = passwordInput.dataset.role;
    const minLength = getMinLengthForRole(role);
    const defaultPlaceholder = getDefaultResetPlaceholder(role);

    if (passwordInput.value.length < minLength) {
        passwordInput.value = "";
        passwordInput.placeholder = "Enter at least " + minLength + " characters";
        passwordInput.type = "password";
        passwordInput.focus();
        return false;
    }

    if (confirmStepInput.value === "false") {
        firstPasswordEntryInput.value = passwordInput.value;
        passwordInput.value = "";
        passwordInput.placeholder = "Re-enter password";
        confirmStepInput.value = "true";
        submitButton.textContent = "Confirm reset";
        passwordInput.type = "password";
        passwordInput.focus();
        return false;
    }

    if (passwordInput.value !== firstPasswordEntryInput.value) {
        alert("Passwords do not match");
        passwordInput.value = "";
        firstPasswordEntryInput.value = "";
        passwordInput.placeholder = defaultPlaceholder;
        confirmStepInput.value = "false";
        submitButton.textContent = "Reset password";
        passwordInput.type = "password";
        passwordInput.focus();
        return false;
    }

    return true;
}

/* ======================== Password Lengh Helper for user Edit modal ======================== */

function updateEditPasswordRule() {
    const selectedRole = document.querySelector('#editUserModal input[name="role"]:checked');
    const passwordRule = document.getElementById('editPasswordRule');

    if (!passwordRule) return;

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

/* ======================== Edit Noun Modal ======================== */

function openEdit(button) {
    const id = button.getAttribute('data-id');
    const welshWord = button.getAttribute('data-welsh');
    const englishWord = button.getAttribute('data-english');
    const welshSent = button.getAttribute('data-welshsent');
    const englishSent = button.getAttribute('data-englishsent');
    const gender = button.getAttribute('data-gender');

    document.getElementById('editNounForm').action = '/nouns/' + id + '/update';
    document.getElementById('editWelshWord').value = welshWord || '';
    document.getElementById('editEnglishWord').value = englishWord || '';
    document.getElementById('editWelshSent').value = welshSent || '';
    document.getElementById('editEnglishSent').value = englishSent || '';
    document.getElementById('editGenderMasculine').checked = (gender === 'MASCULINE');
    document.getElementById('editGenderFeminine').checked = (gender === 'FEMININE');
    document.getElementById('editNounModal').style.display = 'block';
}

/* ======================== Edit User Modal ======================== */

function openEditUser(button) {
    const id = button.getAttribute('data-id');
    const username = button.getAttribute('data-username');
    const firstname = button.getAttribute('data-firstname');
    const surname = button.getAttribute('data-surname');
    const role = button.getAttribute('data-role');

    document.getElementById('editUserForm').action = '/users/' + id + '/update';
	document.getElementById('editUsername').value = username || '';
    document.getElementById('editFirstname').value = firstname || '';
    document.getElementById('editSurname').value = surname || '';
	document.getElementById('editRoleAdmin').checked = (role === 'ADMIN');
	document.getElementById('editRoleLecturer').checked = (role === 'LECTURER');
	document.getElementById('editRoleStudent').checked = (role === 'STUDENT');
	
	document.getElementById('editPassword').value = '';
	document.getElementById('editConfirmPassword').value = '';
	
	updateEditPasswordRule();
	
    document.getElementById('editUserModal').style.display = 'block';
}


/* ======================== Flashcard Revision ======================== */

let nouns = [];
let currentIndex = 0;

function loadNounsFromHTML() {
    const nounElements = document.querySelectorAll('#nouns-data .noun-item');
    nouns = Array.from(nounElements).map(el => ({
        welshWord: el.dataset.welsh,
        englishWord: el.dataset.english,
        gender: el.dataset.gender
    }));
}

function updateCard() {
    if (!nouns || nouns.length === 0) return;

    const noun = nouns[currentIndex];
    const card = document.getElementById('revisionCard');
    const genderCard = document.getElementById('genderCard');

    if (!card || !genderCard) return;

    card.classList.remove('is-flipped');
    genderCard.classList.remove('is-flipped');

    setTimeout(() => {
        document.getElementById('cardQuestion').textContent = `How do you say "${noun.welshWord}" in English?`;
        document.getElementById('cardAnswer').textContent = noun.englishWord;
        document.getElementById('genderQuestion').textContent = `What is the gender of "${noun.welshWord}"?`;
        document.getElementById('genderAnswer').textContent = noun.gender;
    }, 150);
}

function toggleFlashcardAnswer(cardId) {
    const card = document.getElementById(cardId);
    if (card) card.classList.toggle('is-flipped');
}

function nextCard() {
    if (!nouns || nouns.length === 0) return;
    currentIndex = (currentIndex + 1) % nouns.length;
    updateCard();
}


/* ======================== Test Page – Answer Persistence ======================== */

document.addEventListener("DOMContentLoaded", function() {
    const testIdInput = document.querySelector('input[name="testId"]');
    if (!testIdInput) return;

    const STORAGE_KEY = 'test_answers_' + testIdInput.value;

    function restoreAnswers() {
        const saved = localStorage.getItem(STORAGE_KEY);
        if (!saved) return;
        const answers = JSON.parse(saved);
        Object.entries(answers).forEach(function(entry) {
            const name = entry[0];
            const value = entry[1];
            const radios = document.querySelectorAll('input[type="radio"][name="' + name + '"]');
            if (radios.length > 0) {
                radios.forEach(function(r) { if (r.value === value) r.checked = true; });
            } else {
                const text = document.querySelector('input[type="text"][name="' + name + '"]');
                if (text) text.value = value;
            }
        });
    }

    function saveAnswers() {
        const answers = {};
        document.querySelectorAll('.question-block').forEach(function(block) {
            const radios = block.querySelectorAll('input[type="radio"]');
            const texts = block.querySelectorAll('input[type="text"]');
            if (radios.length > 0) {
                const checked = block.querySelector('input[type="radio"]:checked');
                if (checked) answers[checked.name] = checked.value;
            } else if (texts.length > 0) {
                answers[texts[0].name] = texts[0].value;
            }
        });
        localStorage.setItem(STORAGE_KEY, JSON.stringify(answers));
    }

    document.querySelectorAll('input[type="radio"], input[type="text"]').forEach(function(input) {
        input.addEventListener('change', saveAnswers);
        input.addEventListener('input', saveAnswers);
    });

    restoreAnswers();

    const testForm = document.querySelector('form');
    if (testForm) {
        testForm.addEventListener('submit', function(e) {
            const questionBlocks = document.querySelectorAll('.question-block');
            let total = questionBlocks.length;
            let answered = 0;

            questionBlocks.forEach(function(block) {
                const radios = block.querySelectorAll('input[type="radio"]');
                const texts = block.querySelectorAll('input[type="text"]');

                if (radios.length > 0) {
                    if (document.querySelector('input[name="' + radios[0].name + '"]:checked')) answered++;
                } else if (texts.length > 0) {
                    if (texts[0].value.trim() !== '') answered++;
                }
            });

            if (answered < total) {
                const unanswered = total - answered;
                const message = 'You have ' + unanswered + ' unanswered question' +
                    (unanswered > 1 ? 's' : '') + ' out of ' + total + '. Are you sure you want to submit?';
                if (!confirm(message)) {
                    e.preventDefault();
                    return;
                }
            }

            localStorage.removeItem(STORAGE_KEY);
        });
    }
});


/* ======================== DOMContentLoaded Initialisations ======================== */

document.addEventListener("DOMContentLoaded", function() {

    updatePasswordRule();
    setResetPasswordPlaceholders();

    loadNounsFromHTML();
    if (nouns && nouns.length > 0) {
        updateCard();
    } else {
        const cardQuestion = document.getElementById('cardQuestion');
        if (cardQuestion) cardQuestion.textContent = "No nouns found. Add some to start revising!";
        const nextBtn = document.getElementById('nextBtn');
        if (nextBtn) nextBtn.style.display = 'none';
    }

    const newTestForm = document.getElementById('new-test-form');
    if (newTestForm) {
        newTestForm.addEventListener('submit', function() {
            Object.keys(localStorage).forEach(function(key) {
                if (key.startsWith('test_answers_')) localStorage.removeItem(key);
            });
        });
    }

    const cooldownEl = document.getElementById('cooldown-seconds');
    if (cooldownEl) {
        var seconds = parseInt(cooldownEl.value, 10);
        var display = document.getElementById('cooldown-display');
        var btn = document.getElementById('new-test-btn');
        var notice = document.getElementById('cooldown-notice');

        function updateDisplay() {
            var m = Math.floor(seconds / 60);
            var s = seconds % 60;
            display.textContent = m + 'm ' + (s < 10 ? '0' + s : s) + 's';
        }

        updateDisplay();

        var timer = setInterval(function() {
            seconds--;
            if (seconds <= 0) {
                clearInterval(timer);
                notice.style.display = 'none';
                btn.disabled = false;
            } else {
                updateDisplay();
            }
        }, 1000);
    }

    if (document.getElementById('login-error-flag')) {
        const loginModal = document.getElementById('loginModal');
        if (loginModal) loginModal.style.display = 'block';
    }

    if (document.getElementById('show-new-user-modal-flag')) {
        const newUserModal = document.getElementById('newUserModal');
        if (newUserModal) newUserModal.style.display = 'block';
    }

});
