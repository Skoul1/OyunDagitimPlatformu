function showSection(sectionId) {
    document.querySelectorAll('.form-section').forEach(section => {
        section.classList.remove('active');
    });
    const section = document.getElementById(sectionId);
    section.classList.add('active');
}