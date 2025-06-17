document.addEventListener('DOMContentLoaded', function() {
    const filtro = document.getElementById('minhas-consultas-filtro');
    const cartoes = document.querySelectorAll('.minhas-consultas-cartao');

    if (filtro) {
        filtro.addEventListener('change', function() {
            const valor = filtro.value;
            cartoes.forEach(cartao => {
                const status = cartao.querySelector('.minhas-consultas-status').textContent.trim().toLowerCase();
                const tipo = cartao.querySelector('.minhas-consultas-icone-tipo').classList.contains('exame') ? 'exames' : 'consultas';

                if (valor === 'todas') {
                    cartao.style.display = '';
                } else if (valor === 'pendentes' && status === 'pendente') {
                    cartao.style.display = '';
                } else if (valor === 'confirmadas' && status === 'confirmada') {
                    cartao.style.display = '';
                } else if (valor === tipo) {
                    cartao.style.display = '';
                } else {
                    cartao.style.display = 'none';
                }
            });
        });
    }
});