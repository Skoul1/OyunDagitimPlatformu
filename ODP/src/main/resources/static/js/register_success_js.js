// script.js
document.addEventListener("DOMContentLoaded", function() {
    const content = document.querySelector('.content');
    content.style.opacity = 0;
    window.setTimeout(() => {
        content.style.opacity = 1;
        content.style.transition = 'opacity 2s ease-in-out';
    }, 100);
});
