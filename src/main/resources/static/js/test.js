const STORAGE_KEY = 'test_answers_' + document.querySelector('input[name="testId"]').value;

// Restore saved answers from localStorage on page load
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

// Save all current answers to localStorage
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

// Attach save listeners to all inputs
document.querySelectorAll('input[type="radio"], input[type="text"]').forEach(function(input) {
    input.addEventListener('change', saveAnswers);
    input.addEventListener('input', saveAnswers);
});

restoreAnswers();

// Unanswered warning + clear localStorage on submit
document.querySelector('form').addEventListener('submit', function(e) {
    const questionBlocks = document.querySelectorAll('.question-block');
    let total = questionBlocks.length;
    let answered = 0;

    questionBlocks.forEach(function(block) {
        const radios = block.querySelectorAll('input[type="radio"]');
        const texts = block.querySelectorAll('input[type="text"]');

        if (radios.length > 0) {
            const radioName = radios[0].name;
            if (document.querySelector('input[name="' + radioName + '"]:checked')) {
                answered++;
            }
        } else if (texts.length > 0) {
            if (texts[0].value.trim() !== '') {
                answered++;
            }
        }
    });

    if (answered < total) {
        const unanswered = total - answered;
        const message = 'You have ' + unanswered + ' unanswered question' +
            (unanswered > 1 ? 's' : '') + ' out of ' + total +
            '. Are you sure you want to submit?';
        if (!confirm(message)) {
            e.preventDefault();
            return;
        }
    }

    // Clear saved answers once the form is actually submitted
    localStorage.removeItem(STORAGE_KEY);
});
