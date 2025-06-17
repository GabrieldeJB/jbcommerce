// Base URL da API
const API_BASE_URL = "."; // Usar caminho relativo para a API no mesmo servidor

// Função auxiliar para chamadas fetch
async function fetchData(url, options = {}) {
    try {
        const response = await fetch(API_BASE_URL + url, {
            headers: {
                'Content-Type': 'application/json',
                // Adicionar cabeçalho de autorização se necessário (ex: JWT)
                // 'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            },
            ...options,
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({ message: response.statusText }));
            throw new Error(`Erro ${response.status}: ${errorData.message || 'Erro desconhecido'}`);
        }

        // Retorna vazio para respostas No Content (204)
        if (response.status === 204) {
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error('Erro na chamada da API:', error);
        alert(`Erro ao comunicar com o servidor: ${error.message}`);
        throw error; // Re-lança o erro para quem chamou a função
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // Verificar qual página está sendo carregada
    const currentPage = window.location.pathname.split('/').pop();

    // Inicializar funcionalidades específicas de cada página
    switch(currentPage) {
        case 'login.html':
            initLoginPage();
            break;
        case 'cadastro.html':
            initCadastroPage();
            break;
        case 'recuperar-senha.html':
            initRecuperarSenhaPage();
            break;
        case 'index.html':
            initDashboardPage();
            break;
        case 'calendario.html':
            initCalendarioPage();
            break;
        case 'medicamentos.html':
            initMedicamentosPage();
            break;
        case 'consultas.html':
            initConsultasPage();
            break;
        case 'notificacoes.html':
            initNotificacoesPage();
            break;
        case 'perfil.html':
            initPerfilPage();
            break;
    }

    highlightActiveMenuItem(); // Chamada para destacar o item de menu ativo
});

// Nova função para destacar o item de menu ativo
function highlightActiveMenuItem() {
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar-link');

    sidebarLinks.forEach(link => {
        // Remover a classe 'active' de todos os links primeiro
        link.classList.remove('active');
        
        // Comparar o href do link com o caminho atual da URL
        // Normalizamos os caminhos para garantir a correspondência, lidando com barras finais, etc.
        const linkPath = link.getAttribute('href').replace(/\/$/, ''); // Remove barra final
        const normalizedCurrentPath = currentPath.replace(/\/$/, ''); // Remove barra final

        if (normalizedCurrentPath.includes(linkPath) && linkPath !== '') {
            // Adiciona 'active' se o caminho atual contiver o caminho do link (e não for vazio)
            // Ex: /idoso/dashboard inclui /idoso/dashboard
            // /idoso/perfil inclui /idoso/perfil
            link.classList.add('active');
        } else if (normalizedCurrentPath === '/idoso' && linkPath === '/idoso/dashboard') {
            // Caso especial para a raiz do idoso redirecionar para o dashboard
            link.classList.add('active');
        }
    });
}

// --- Funções de Inicialização ---

async function initLoginPage() {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const email = document.getElementById('username').value; // Campo é email ou cpf, mas API espera email
            const password = document.getElementById('password').value;

            try {
                const response = await fetchData('/api/auth/login', {
                    method: 'POST',
                    body: JSON.stringify({ email, password })
                });

                // Armazenar informações de login (ex: username, ou um token se a API retornar)
                localStorage.setItem('loggedInUser', response.username); // Exemplo
                // localStorage.setItem('authToken', response.token); // Se a API retornar token

                console.log('Login bem-sucedido:', response);
                window.location.href = 'index.html'; // Redireciona para o dashboard
            } catch (error) {
                // Erro já é tratado no fetchData, mas pode adicionar lógica extra aqui
                console.error('Falha no login:', error);
            }
        });
    }
}

async function initCadastroPage() {
    const cadastroForm = document.getElementById('cadastro-form');
    if (cadastroForm) {
        cadastroForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                alert('As senhas não coincidem.');
                return;
            }

            const formData = new FormData(cadastroForm);
            const userData = Object.fromEntries(formData.entries());

            // Ajustar nome do campo de senha para 'password'
            userData.password = userData.senha;
            delete userData.senha; // Remover campo antigo
            delete userData.confirmPassword; // Remover confirmação

            try {
                const response = await fetchData('/api/auth/register', {
                    method: 'POST',
                    body: JSON.stringify(userData)
                });
                console.log('Cadastro realizado:', response);
                alert('Cadastro realizado com sucesso! Faça o login.');
                window.location.href = 'login.html';
            } catch (error) {
                console.error('Falha no cadastro:', error);
            }
        });
    }
}

function initRecuperarSenhaPage() {
    const recuperarSenhaForm = document.getElementById('recuperar-senha-form');
    if (recuperarSenhaForm) {
        recuperarSenhaForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const email = document.getElementById('emailRecuperacao').value;

            // TODO: Implementar API de recuperação de senha no backend
            alert('Funcionalidade de recuperação de senha ainda não implementada no backend.');
            // try {
            //     await fetchData('/api/auth/recover-password', { // Exemplo de endpoint
            //         method: 'POST',
            //         body: JSON.stringify({ email })
            //     });
            //     alert(`Um código de recuperação foi enviado para ${email}`);
            // } catch (error) {
            //     console.error('Falha na recuperação de senha:', error);
            // }
        });
    }
}

function initDashboardPage() {
    console.log('Dashboard inicializado');
    // TODO: Carregar dados relevantes para o dashboard (ex: próximos medicamentos/consultas)
}

function initCalendarioPage() {
    // Mantém a lógica de exibição do calendário como estava
    const currentDate = new Date();
    const currentMonthYear = document.getElementById('current-month-year');
    const calendarGrid = document.getElementById('calendar-grid');
    const prevMonthBtn = document.getElementById('prev-month');
    const nextMonthBtn = document.getElementById('next-month');
    const eventsContainer = document.getElementById('daily-events');

    let displayMonth = currentDate.getMonth();
    let displayYear = currentDate.getFullYear();
    let eventosDoMes = []; // Armazenar eventos carregados da API

    async function carregarEventosDoMes() {
        // TODO: Implementar API para buscar eventos (medicamentos/consultas) por mês/ano
        console.warn('API para buscar eventos do calendário por mês ainda não implementada.');
        // Exemplo de chamada API:
        // try {
        //     const dataInicio = `${displayYear}-${String(displayMonth + 1).padStart(2, '0')}-01`;
        //     const dataFim = new Date(displayYear, displayMonth + 1, 0).toISOString().split('T')[0];
        //     const [medicamentos, consultas] = await Promise.all([
        //         fetchData(`/api/medicamentos?dataInicio=${dataInicio}&dataFim=${dataFim}`), // Ajustar API
        //         fetchData(`/api/consultas?dataInicio=${dataInicio}&dataFim=${dataFim}`)      // Ajustar API
        //     ]);
        //     eventosDoMes = [...medicamentos, ...consultas]; // Combinar e formatar eventos
        //     generateCalendarDays(); // Regenerar calendário com eventos
        // } catch (error) {
        //     console.error('Erro ao carregar eventos do calendário:', error);
        // }
        eventosDoMes = []; // Resetar eventos por enquanto
        generateCalendarDays();
    }

    function updateCalendarHeader() {
        const monthNames = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
                           'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
        currentMonthYear.textContent = `${monthNames[displayMonth]} ${displayYear}`;
    }

    function generateCalendarDays() {
        if (!calendarGrid) return;
        calendarGrid.innerHTML = '';
        const firstDay = new Date(displayYear, displayMonth, 1).getDay();
        const daysInMonth = new Date(displayYear, displayMonth + 1, 0).getDate();

        for (let i = 0; i < firstDay; i++) {
            const emptyDay = document.createElement('div');
            emptyDay.className = 'calendar-day empty';
            calendarGrid.appendChild(emptyDay);
        }

        for (let day = 1; day <= daysInMonth; day++) {
            const dayElement = document.createElement('div');
            dayElement.className = 'calendar-day';
            dayElement.textContent = day;
            const currentDateStr = `${displayYear}-${String(displayMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

            if (displayMonth === currentDate.getMonth() &&
                displayYear === currentDate.getFullYear() &&
                day === currentDate.getDate()) {
                dayElement.classList.add('today');
            }

            // Verificar se há eventos para este dia (ajustar lógica conforme API)
            const eventosDoDia = eventosDoMes.filter(evento => {
                // A lógica aqui depende de como a data/hora está armazenada nos eventos
                // Exemplo: return evento.data === currentDateStr;
                return false; // Placeholder
            });

            if (eventosDoDia.length > 0) {
                dayElement.classList.add('has-event');
                dayElement.addEventListener('click', () => showDayEvents(day, eventosDoDia));
            }

            calendarGrid.appendChild(dayElement);
        }
    }

    function showDayEvents(day, eventos) {
        if (!eventsContainer) return;
        eventsContainer.innerHTML = '';
        if (eventos.length === 0) {
             // Simulação se não houver eventos reais
             if (Math.random() > 0.7) {
                 eventsContainer.innerHTML = `
                    <li class="event-item">
                        <i class="fas fa-pills"></i>
                        <span>Tomar Remédio X - 09:00</span>
                    </li>`;
             } else {
                 eventsContainer.innerHTML = `<li>Nenhum evento para o dia ${day}.</li>`;
             }
             return;
        }

        eventos.forEach(evento => {
            const item = document.createElement('li');
            item.className = 'event-item';
            const iconClass = evento.tipo === 'medicamento' ? 'fa-pills' : 'fa-stethoscope'; // Ajustar tipo
            item.innerHTML = `<i class="fas ${iconClass}"></i> <span>${evento.titulo} - ${evento.hora}</span>`; // Ajustar propriedades
            eventsContainer.appendChild(item);
        });
    }

    if (prevMonthBtn) {
        prevMonthBtn.addEventListener('click', () => {
            displayMonth--;
            if (displayMonth < 0) {
                displayMonth = 11;
                displayYear--;
            }
            updateCalendarHeader();
            carregarEventosDoMes();
        });
    }

    if (nextMonthBtn) {
        nextMonthBtn.addEventListener('click', () => {
            displayMonth++;
            if (displayMonth > 11) {
                displayMonth = 0;
                displayYear++;
            }
            updateCalendarHeader();
            carregarEventosDoMes();
        });
    }

    updateCalendarHeader();
    carregarEventosDoMes(); // Carregar eventos ao inicializar
}

async function initMedicamentosPage() {
    const addMedicamentoBtn = document.getElementById('add-medicamento-btn');
    const medicamentoModal = document.getElementById('medicamento-modal');
    const closeModalBtn = document.getElementById('close-modal');
    const cancelModalBtn = document.getElementById('cancel-modal');
    const medicamentoForm = document.getElementById('medicamento-form');
    const medicamentosList = document.getElementById('medicamentos-list');
    let medicamentosCache = []; // Cache local

    async function renderMedicamentos() {
        if (!medicamentosList) return;
        try {
            medicamentosCache = await fetchData('/api/medicamentos');
            medicamentosList.innerHTML = ''; // Limpa a lista

            if (medicamentosCache.length === 0) {
                medicamentosList.innerHTML = '<p>Nenhum medicamento cadastrado.</p>';
                return;
            }

            medicamentosCache.forEach(med => {
                const card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <div class="card-header">
                        <h3 class="card-title">${med.nome}</h3>
                        <div class="card-actions">
                            <button class="edit-btn" data-id="${med.id}"><i class="fas fa-edit"></i></button>
                            <button class="delete-btn" data-id="${med.id}"><i class="fas fa-trash"></i></button>
                        </div>
                    </div>
                    <div class="card-content">
                        <p><strong>Dosagem:</strong> ${med.dosagem}</p>
                        <p><strong>Horário:</strong> ${med.horario}</p>
                        <p><strong>Frequência:</strong> ${med.frequencia}</p>
                        ${med.observacoes ? `<p><strong>Observações:</strong> ${med.observacoes}</p>` : ''}
                    </div>
                `;
                medicamentosList.appendChild(card);

                card.querySelector('.edit-btn').addEventListener('click', () => editMedicamento(med.id));
                card.querySelector('.delete-btn').addEventListener('click', () => deleteMedicamento(med.id));
            });
        } catch (error) {
            console.error('Erro ao renderizar medicamentos:', error);
            medicamentosList.innerHTML = '<p>Erro ao carregar medicamentos.</p>';
        }
    }

    function openModal(medicamento = null) {
        if (medicamentoModal && medicamentoForm) {
            medicamentoForm.reset();
            if (medicamento) {
                document.getElementById('medicamentoId').value = medicamento.id;
                document.getElementById('nomeMedicamento').value = medicamento.nome;
                document.getElementById('dosagem').value = medicamento.dosagem;
                document.getElementById('horario').value = medicamento.horario;
                document.getElementById('frequencia').value = medicamento.frequencia;
                document.getElementById('observacoes').value = medicamento.observacoes || '';
                document.getElementById('modal-title').textContent = 'Editar Medicamento';
            } else {
                document.getElementById('medicamentoId').value = '';
                document.getElementById('modal-title').textContent = 'Adicionar Medicamento';
            }
            medicamentoModal.style.display = 'flex';
        }
    }

    function closeModal() {
        if (medicamentoModal) {
            medicamentoModal.style.display = 'none';
        }
    }

    function editMedicamento(id) {
        const medicamento = medicamentosCache.find(med => med.id === id);
        if (medicamento) {
            openModal(medicamento);
        }
    }

    async function deleteMedicamento(id) {
        if (confirm('Tem certeza que deseja excluir este medicamento?')) {
            try {
                await fetchData(`/api/medicamentos/${id}`, { method: 'DELETE' });
                renderMedicamentos(); // Atualiza a lista
            } catch (error) {
                console.error('Erro ao excluir medicamento:', error);
            }
        }
    }

    if (addMedicamentoBtn) {
        addMedicamentoBtn.addEventListener('click', () => openModal());
    }
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal);
    }
    if (cancelModalBtn) {
        cancelModalBtn.addEventListener('click', closeModal);
    }
    if (medicamentoForm) {
        medicamentoForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const medicamentoId = document.getElementById('medicamentoId').value;
            const data = {
                nome: document.getElementById('nomeMedicamento').value,
                dosagem: document.getElementById('dosagem').value,
                horario: document.getElementById('horario').value,
                frequencia: document.getElementById('frequencia').value,
                observacoes: document.getElementById('observacoes').value
            };

            const url = medicamentoId ? `/api/medicamentos/${medicamentoId}` : '/api/medicamentos';
            const method = medicamentoId ? 'PUT' : 'POST';

            try {
                await fetchData(url, { method, body: JSON.stringify(data) });
                renderMedicamentos();
                closeModal();
            } catch (error) {
                console.error('Erro ao salvar medicamento:', error);
            }
        });
    }

    renderMedicamentos(); // Carrega e renderiza ao iniciar
}

async function initConsultasPage() {
    const addConsultaBtn = document.getElementById('add-consulta-btn');
    const consultaModal = document.getElementById('consulta-modal');
    const closeModalBtn = document.getElementById('close-modal-consulta');
    const cancelModalBtn = document.getElementById('cancel-modal-consulta');
    const consultaForm = document.getElementById('consulta-form');
    const consultasList = document.getElementById('consultas-list');
    let consultasCache = [];

    async function renderConsultas() {
        if (!consultasList) return;
        try {
            consultasCache = await fetchData('/api/consultas');
            consultasList.innerHTML = '';

            if (consultasCache.length === 0) {
                consultasList.innerHTML = '<p>Nenhuma consulta agendada.</p>';
                return;
            }

            consultasCache.forEach(consulta => {
                const dataObj = new Date(consulta.data + 'T00:00:00'); // Ajuste para evitar problemas de fuso
                const dataFormatada = dataObj.toLocaleDateString('pt-BR');

                const card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <div class="card-header">
                        <h3 class="card-title">${consulta.medico}</h3>
                        <div class="card-actions">
                            <button class="edit-btn" data-id="${consulta.id}"><i class="fas fa-edit"></i></button>
                            <button class="delete-btn" data-id="${consulta.id}"><i class="fas fa-trash"></i></button>
                        </div>
                    </div>
                    <div class="card-content">
                        <p><strong>Especialidade:</strong> ${consulta.especialidade}</p>
                        <p><strong>Local:</strong> ${consulta.local}</p>
                        <p><strong>Data:</strong> ${dataFormatada}</p>
                        <p><strong>Hora:</strong> ${consulta.hora}</p>
                        ${consulta.observacoes ? `<p><strong>Observações:</strong> ${consulta.observacoes}</p>` : ''}
                    </div>
                `;
                consultasList.appendChild(card);

                card.querySelector('.edit-btn').addEventListener('click', () => editConsulta(consulta.id));
                card.querySelector('.delete-btn').addEventListener('click', () => deleteConsulta(consulta.id));
            });
        } catch (error) {
            console.error('Erro ao renderizar consultas:', error);
            consultasList.innerHTML = '<p>Erro ao carregar consultas.</p>';
        }
    }

    function openModal(consulta = null) {
        if (consultaModal && consultaForm) {
            consultaForm.reset();
            if (consulta) {
                document.getElementById('consultaId').value = consulta.id;
                document.getElementById('medico').value = consulta.medico;
                document.getElementById('especialidade').value = consulta.especialidade;
                document.getElementById('local').value = consulta.local;
                document.getElementById('dataConsulta').value = consulta.data;
                document.getElementById('horaConsulta').value = consulta.hora;
                document.getElementById('observacoes').value = consulta.observacoes || '';
                document.getElementById('modal-title-consulta').textContent = 'Editar Consulta';
            } else {
                document.getElementById('consultaId').value = '';
                document.getElementById('modal-title-consulta').textContent = 'Agendar Consulta';
            }
            consultaModal.style.display = 'flex';
        }
    }

    function closeModal() {
        if (consultaModal) {
            consultaModal.style.display = 'none';
        }
    }

    function editConsulta(id) {
        const consulta = consultasCache.find(c => c.id === id);
        if (consulta) {
            openModal(consulta);
        }
    }

    async function deleteConsulta(id) {
        if (confirm('Tem certeza que deseja cancelar esta consulta?')) {
            try {
                await fetchData(`/api/consultas/${id}`, { method: 'DELETE' });
                renderConsultas();
            } catch (error) {
                console.error('Erro ao excluir consulta:', error);
            }
        }
    }

    if (addConsultaBtn) {
        addConsultaBtn.addEventListener('click', () => openModal());
    }
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal);
    }
    if (cancelModalBtn) {
        cancelModalBtn.addEventListener('click', closeModal);
    }
    if (consultaForm) {
        consultaForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const consultaId = document.getElementById('consultaId').value;
            const data = {
                medico: document.getElementById('medico').value,
                especialidade: document.getElementById('especialidade').value,
                local: document.getElementById('local').value,
                data: document.getElementById('dataConsulta').value,
                hora: document.getElementById('horaConsulta').value,
                observacoes: document.getElementById('observacoes').value
            };

            const url = consultaId ? `/api/consultas/${consultaId}` : '/api/consultas';
            const method = consultaId ? 'PUT' : 'POST';

            try {
                await fetchData(url, { method, body: JSON.stringify(data) });
                renderConsultas();
                closeModal();
            } catch (error) {
                console.error('Erro ao salvar consulta:', error);
            }
        });
    }

    renderConsultas();
}

async function initNotificacoesPage() {
    const notificationsList = document.getElementById('notifications-list');

    async function renderNotificacoes() {
        if (!notificationsList) return;
        try {
            const notificacoes = await fetchData('/api/notificacoes');
            notificationsList.innerHTML = '';

            if (notificacoes.length === 0) {
                notificationsList.innerHTML = '<p>Nenhuma notificação encontrada.</p>';
                return;
            }

            notificacoes.forEach(notificacao => {
                const item = document.createElement('div');
                item.className = `notification ${notificacao.lida ? '' : 'unread'}`;
                item.dataset.id = notificacao.id;

                let iconClass = 'fa-info-circle';
                if (notificacao.tipo === 'LEMBRETE_MEDICAMENTO') iconClass = 'fa-pills';
                if (notificacao.tipo === 'LEMBRETE_CONSULTA') iconClass = 'fa-stethoscope';

                const timestamp = new Date(notificacao.timestamp);
                // TODO: Formatar timestamp de forma mais amigável (ex: 'Agora mesmo', 'Ontem', '2 dias atrás')
                const timeString = timestamp.toLocaleString('pt-BR');

                item.innerHTML = `
                    <div class="notification-icon"><i class="fas ${iconClass}"></i></div>
                    <div class="notification-content">
                        <div class="notification-title">${notificacao.titulo}</div>
                        <p>${notificacao.mensagem}</p>
                        <div class="notification-time">${timeString}</div>
                    </div>
                    <button class="delete-notification-btn"><i class="fas fa-times"></i></button>
                `;
                notificationsList.appendChild(item);

                // Marcar como lida ao clicar (se não estiver lida)
                if (!notificacao.lida) {
                    item.addEventListener('click', async (e) => {
                        if (e.target.closest('.delete-notification-btn')) return; // Não marcar se clicar no delete
                        try {
                            await fetchData(`/api/notificacoes/${notificacao.id}/ler`, { method: 'PATCH' });
                            item.classList.remove('unread');
                        } catch (error) {
                            console.error('Erro ao marcar notificação como lida:', error);
                        }
                    });
                }

                // Excluir notificação
                item.querySelector('.delete-notification-btn').addEventListener('click', async () => {
                     if (confirm('Tem certeza que deseja excluir esta notificação?')) {
                        try {
                            await fetchData(`/api/notificacoes/${notificacao.id}`, { method: 'DELETE' });
                            item.remove(); // Remove da UI
                        } catch (error) {
                            console.error('Erro ao excluir notificação:', error);
                        }
                    }
                });
            });

        } catch (error) {
            console.error('Erro ao renderizar notificações:', error);
            notificationsList.innerHTML = '<p>Erro ao carregar notificações.</p>';
        }
    }

    renderNotificacoes();
}

function initPerfilPage() {
    // TODO: Implementar API para buscar e atualizar dados do perfil
    console.warn('API para perfil ainda não implementada.');

    const editProfileBtn = document.getElementById('perfil-btn-editar'); // Alterado para o botão correto
    const changePasswordBtn = document.getElementById('change-password-btn');

    // Elementos do modal de perfil de IDOSO (existentes)
    const editProfileModalIdoso = document.getElementById('edit-profile-modal');
    const closeProfileModalBtnIdoso = document.getElementById('close-profile-modal');
    const cancelProfileModalBtnIdoso = document.getElementById('cancel-profile-modal');
    const editProfileFormIdoso = document.getElementById('edit-profile-form');

    // Elementos do modal de perfil de MÉDICO (novos)
    const editProfileModalMedico = document.getElementById('edit-profile-modal-medico');
    const closeProfileModalBtnMedico = document.getElementById('close-profile-modal-medico');
    const cancelProfileModalBtnMedico = document.getElementById('cancel-profile-modal-medico');
    const editProfileFormMedico = document.getElementById('edit-profile-form-medico');

    const changePasswordModal = document.getElementById('change-password-modal');
    const closePasswordModalBtn = document.getElementById('close-password-modal');
    const cancelPasswordModalBtn = document.getElementById('cancel-password-modal');
    const changePasswordForm = document.getElementById('change-password-form');

    let currentUserData = {}; // Para armazenar os dados do usuário logado

    async function carregarDadosPerfil() {
        console.log('Carregando dados do perfil...');
        try {
            // Endpoint para buscar o perfil do usuário logado (Spring Security)
            currentUserData = await fetchData('/api/usuarios/me'); 
            console.log('Dados do perfil carregados:', currentUserData);
            preencherDadosPerfil();
        } catch (error) {
            console.error('Erro ao carregar dados do perfil:', error);
        }
    }

    function preencherDadosPerfil() {
        if (!currentUserData || Object.keys(currentUserData).length === 0) {
            console.warn('Nenhum dado de perfil para preencher.');
            return;
        }

        // Preencher informações de exibição (comuns a ambos)
        const elementsToUpdate = {
            'perfil-nome-valor': currentUserData.nome,
            'perfil-email-valor': currentUserData.email,
            'perfil-celular-valor': currentUserData.celular,
            'perfil-endereco-valor': currentUserData.endereco,
            'perfil-numero-valor': currentUserData.numero,
            'perfil-bairro-valor': currentUserData.bairro,
            'perfil-cidade-valor': currentUserData.cidade,
        };

        for (const [id, value] of Object.entries(elementsToUpdate)) {
            const element = document.getElementById(id);
            if (element) {
                element.textContent = value || '';
            }
        }

        // Preencher informações específicas do IDOSO
        if (currentUserData.tipoUsuario === 'IDOSO') {
            const idadeElement = document.getElementById('perfil-idade-valor');
            if (idadeElement && currentUserData.dataNascimento) {
                const birthDate = new Date(currentUserData.dataNascimento);
                const today = new Date();
                let age = today.getFullYear() - birthDate.getFullYear();
                const m = today.getMonth() - birthDate.getMonth();
                if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                    age--;
                }
                idadeElement.textContent = age;
            }
            const tipoSanguineoElement = document.getElementById('perfil-tiposanguineo-valor');
            if (tipoSanguineoElement) tipoSanguineoElement.textContent = currentUserData.tipoSanguineo || '';
            const alergiasElement = document.getElementById('perfil-alergias-valor');
            if (alergiasElement) alergiasElement.textContent = currentUserData.alergias ? currentUserData.alergias.join(', ') : 'Nenhuma';
            const condicoesCronicasElement = document.getElementById('perfil-condicoes-cronicas-valor');
            if (condicoesCronicasElement) condicoesCronicasElement.textContent = currentUserData.condicoesCronicas ? currentUserData.condicoesCronicas.join(', ') : 'Nenhuma';
            const medicamentosRegularesElement = document.getElementById('perfil-medicamentos-regulares-valor');
            if (medicamentosRegularesElement) medicamentosRegularesElement.textContent = currentUserData.medicamentosRegulares ? currentUserData.medicamentosRegulares.join(', ') : 'Nenhum';
            const emergenciaNomeElement = document.getElementById('perfil-emergencia-nome-valor');
            if (emergenciaNomeElement) emergenciaNomeElement.textContent = currentUserData.nomeparentescoEmergencia || '';
            const emergenciaTelefoneElement = document.getElementById('perfil-emergencia-telefone-valor');
            if (emergenciaTelefoneElement) emergenciaTelefoneElement.textContent = currentUserData.telefoneEmergencia || '';
            const emergenciaParentescoElement = document.getElementById('perfil-emergencia-parentesco-valor');
            if (emergenciaParentescoElement) emergenciaParentescoElement.textContent = currentUserData.parentescoEmergencia || '';
        }
        
        // Preencher informações específicas do MÉDICO
        if (currentUserData.tipoUsuario === 'MEDICO') {
            const crmElement = document.getElementById('perfil-crm-valor');
            if (crmElement) {
                crmElement.textContent = currentUserData.crm || '';
            }
        }
    }

    // Funções para abrir e fechar modais
    function openEditProfileModal() {
        if (currentUserData.tipoUsuario === 'IDOSO' && editProfileModalIdoso && editProfileFormIdoso) {
            // Preencher campos do modal de IDOSO
            document.getElementById('edit-nome').value = currentUserData.nome || '';
            document.getElementById('edit-email').value = currentUserData.email || '';
            document.getElementById('edit-celular').value = currentUserData.celular || '';
            document.getElementById('edit-endereco').value = currentUserData.endereco || '';
            document.getElementById('edit-numero').value = currentUserData.numero || '';
            document.getElementById('edit-bairro').value = currentUserData.bairro || '';
            document.getElementById('edit-cidade').value = currentUserData.cidade || '';
            document.getElementById('edit-emergencia-nome').value = currentUserData.nomeparentescoEmergencia || '';
            document.getElementById('edit-emergencia-telefone').value = currentUserData.telefoneEmergencia || '';
            document.getElementById('edit-emergencia-parentesco').value = currentUserData.parentescoEmergencia || '';
            // ... outros campos de idoso
            editProfileModalIdoso.style.display = 'flex';
        } else if (currentUserData.tipoUsuario === 'MEDICO' && editProfileModalMedico && editProfileFormMedico) {
            // Preencher campos do modal de MÉDICO
            document.getElementById('edit-nome-medico').value = currentUserData.nome || '';
            document.getElementById('edit-email-medico').value = currentUserData.email || '';
            document.getElementById('edit-celular-medico').value = currentUserData.celular || '';
            document.getElementById('edit-crm-medico').value = currentUserData.crm || ''; // Preencher CRM
            document.getElementById('edit-endereco-medico').value = currentUserData.endereco || '';
            document.getElementById('edit-numero-medico').value = currentUserData.numero || '';
            document.getElementById('edit-bairro-medico').value = currentUserData.bairro || '';
            document.getElementById('edit-cidade-medico').value = currentUserData.cidade || '';
            editProfileModalMedico.style.display = 'flex';
        }
    }

    function openChangePasswordModal() {
        if (changePasswordModal) {
            changePasswordForm.reset();
            changePasswordModal.style.display = 'flex';
        }
    }

    function closeModals() {
        if (editProfileModalIdoso) editProfileModalIdoso.style.display = 'none';
        if (changePasswordModal) changePasswordModal.style.display = 'none';
        if (editProfileModalMedico) editProfileModalMedico.style.display = 'none'; // Fechar modal de médico
    }

    // Adicionar listeners para abrir e fechar modais
    if (editProfileBtn) editProfileBtn.addEventListener('click', openEditProfileModal);
    if (changePasswordBtn) changePasswordBtn.addEventListener('click', openChangePasswordModal);
    
    if (closeProfileModalBtnIdoso) closeProfileModalBtnIdoso.addEventListener('click', closeModals);
    if (cancelProfileModalBtnIdoso) cancelProfileModalBtnIdoso.addEventListener('click', closeModals);

    if (closeProfileModalBtnMedico) closeProfileModalBtnMedico.addEventListener('click', closeModals);
    if (cancelProfileModalBtnMedico) cancelProfileModalBtnMedico.addEventListener('click', closeModals);

    if (closePasswordModalBtn) closePasswordModalBtn.addEventListener('click', closeModals);
    if (cancelPasswordModalBtn) cancelPasswordModalBtn.addEventListener('click', closeModals);

    // Listener para submissão do formulário de edição de perfil do IDOSO
    if (editProfileFormIdoso) {
        editProfileFormIdoso.addEventListener('submit', async function(e) {
            e.preventDefault();
            const formData = new FormData(editProfileFormIdoso);
            const data = Object.fromEntries(formData.entries());
            // ... (lógica existente para Idoso) ...
            try {
                await fetchData('/api/perfil/update', { // Endpoint geral de update
                    method: 'PUT',
                    body: JSON.stringify(data)
                });
                alert('Perfil atualizado com sucesso!');
                closeModals();
                carregarDadosPerfil(); // Recarrega dados após atualização
            } catch (error) {
                console.error('Erro ao atualizar perfil:', error);
            }
        });
    }

    // Listener para submissão do formulário de edição de perfil do MÉDICO (NOVO)
    if (editProfileFormMedico) {
        editProfileFormMedico.addEventListener('submit', async function(e) {
            e.preventDefault();
            const formData = new FormData(editProfileFormMedico);
            const data = Object.fromEntries(formData.entries());
            
            // O CRM já está no formData.entries(), então não precisa de tratamento especial aqui

            try {
                // TODO: Criar este endpoint no AuthController para médicos
                await fetchData('/api/medico/perfil/update', { 
                    method: 'PUT',
                    body: JSON.stringify(data)
                });
                alert('Perfil de médico atualizado com sucesso!');
                closeModals();
                carregarDadosPerfil(); // Recarrega dados após atualização
            } catch (error) {
                console.error('Erro ao atualizar perfil de médico:', error);
            }
        });
    }

    if (changePasswordForm) {
        changePasswordForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const data = {
                currentPassword: document.getElementById('current-password').value,
                newPassword: document.getElementById('new-password').value,
                confirmPassword: document.getElementById('confirm-password').value
            };
            if (data.newPassword !== data.confirmPassword) {
                alert('As novas senhas não coincidem.');
                return;
            }
            // TODO: Implementar API de alteração de senha
            alert('API de alteração de senha ainda não implementada.');
            // try {
            //     await fetchData('/api/perfil/change-password', { method: 'POST', body: JSON.stringify(data) });
            //     closeModals();
            //     alert('Senha alterada com sucesso!');
            // } catch (error) {
            //     console.error('Erro ao alterar senha:', error);
            // }
        });
    }

    carregarDadosPerfil(); // Carrega dados ao iniciar
}
