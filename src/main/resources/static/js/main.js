// Open the edit noun modal and populate fields
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

// Toggle flashcard answers
function toggleAnswer(button) {
    const answer = button.nextElementSibling;
    if (answer.style.display === "none" || answer.style.display === "") {
        answer.style.display = "block";
        button.textContent = "Hide Answer";
    } else {
        answer.style.display = "none";
        button.textContent = "Show Answer";
    }
}

// Single window.onclick listener to close all modals safely
window.onclick = function(event) {
    const loginModal = document.getElementById('loginModal');
    const newUserModal = document.getElementById('newUserModal');
    const newNounModal = document.getElementById('newNounModal');
    const editNounModal = document.getElementById('editNounModal');

    if (loginModal && event.target == loginModal) loginModal.style.display = "none";
    if (newUserModal && event.target == newUserModal) newUserModal.style.display = "none";
    if (newNounModal && event.target == newNounModal) newNounModal.style.display = "none";
    if (editNounModal && event.target == editNounModal) editNounModal.style.display = "none";
}
