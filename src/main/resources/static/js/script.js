document.addEventListener('DOMContentLoaded', function() {
    // Lógica para o menu de navegação, se houver
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
}); 