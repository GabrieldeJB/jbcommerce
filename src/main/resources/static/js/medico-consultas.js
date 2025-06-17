document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('novaConsultaModal');
    const openModalBtn = document.getElementById('openNewConsultaModal');
    const closeButton = modal.querySelector('.close-button');
    const cancelButton = modal.querySelector('.btn-cancelar');
    const pacienteIdosoSelect = document.getElementById('pacienteIdoso');
    const novaConsultaForm = document.getElementById('novaConsultaForm');

    // Elementos dos novos filtros
    const statusFilter = document.getElementById('status-filter-consulta');
    const consultasSection = document.getElementById('consultas-section');
    const consultasLista = document.querySelector('.consultas-lista');

    // Novos elementos para controle de visibilidade (remover lógia de exame)
    const especialidadeSelect = document.getElementById('especialidade');

    // Elementos do modal de detalhes da consulta
    const consultaDetalhesModal = document.getElementById('consultaDetalhesModal');
    const detalhesContent = document.getElementById('detalhesContent');
    const closeDetalhesModalButton = consultaDetalhesModal.querySelector('.close-button');

    // Função para alternar a visibilidade dos campos (apenas para consulta)
    function toggleFields() {
        // Nada a fazer aqui, pois só há campos de consulta neste modal
    }

    // Não há necessidade de event listeners para rádios de tipo, pois só há um tipo de modal
    // Não é necessário chamar toggleFields no carregamento, pois não há lógica condicional

    // Open Modal
    openModalBtn.addEventListener('click', function() {
        modal.style.display = 'block';
        fetchIdosos(); // Load idosos when modal opens
        novaConsultaForm.action = '/api/consultas'; // Default para agendamento de consulta
        novaConsultaForm.method = 'POST';
        document.querySelector('.btn-agendar').style.display = 'block';
        const updateButton = document.querySelector('.btn-atualizar');
        if (updateButton) {
            updateButton.style.display = 'none';
        }
    });

    // Close Modal
    closeButton.addEventListener('click', function() {
        modal.style.display = 'none';
        novaConsultaForm.reset(); // Limpa o formulário ao fechar
        document.querySelector('.btn-agendar').style.display = 'block';
        const updateButton = document.querySelector('.btn-atualizar');
        if (updateButton) {
            updateButton.style.display = 'none';
        }
        novaConsultaForm.action = '/api/consultas'; // Resetar a action para agendamento
        novaConsultaForm.method = 'POST';
    });

    cancelButton.addEventListener('click', function() {
        modal.style.display = 'none';
        novaConsultaForm.reset(); // Limpa o formulário ao fechar
        document.querySelector('.btn-agendar').style.display = 'block';
        const updateButton = document.querySelector('.btn-atualizar');
        if (updateButton) {
            updateButton.style.display = 'none';
        }
        novaConsultaForm.action = '/api/consultas'; // Resetar a action para agendamento
        novaConsultaForm.method = 'POST';
    });

    window.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
            novaConsultaForm.reset(); // Limpa o formulário ao fechar
            document.querySelector('.btn-agendar').style.display = 'block';
            const updateButton = document.querySelector('.btn-atualizar');
            if (updateButton) {
                updateButton.style.display = 'none';
            }
            novaConsultaForm.action = '/api/consultas'; // Resetar a action para agendamento
            novaConsultaForm.method = 'POST';
        }
    });

    // Função para renderizar os cartões (apenas para consulta)
    function renderCards(items) { // Removido o parâmetro 'type'
        const targetSection = consultasSection;
        targetSection.innerHTML = ''; // Limpa a seção existente

        if (items.length === 0) {
            targetSection.innerHTML = '<p class="no-results">Nenhum registro de consulta encontrado.</p>';
            return;
        }

        items.forEach(item => {
            const cardHtml = `
                <div class="consultas-cartao" data-id="${item.id}" data-type="Consulta">
                    <div class="consultas-cartao-cabecalho">
                        <div class="consultas-icone-tipo consulta">
                            <i class="fa-solid fa-stethoscope"></i>
                        </div>
                        <div>
                            <span class="consultas-cartao-titulo">${item.titulo || item.especialidade || 'N/A'}</span>
                            <span class="consultas-status">${item.status || 'N/A'}</span>
                        </div>
                        <div class="consultas-cabecalho-acoes">
                            <i class="fa-solid fa-eye action-icon view-details-btn" data-id="${item.id}" data-type="Consulta"></i>
                            <i class="fa-solid fa-edit action-icon edit-btn" data-id="${item.id}" data-type="Consulta"></i>
                            <i class="fa-solid fa-trash-alt action-icon delete-btn" data-id="${item.id}" data-type="Consulta"></i>
                        </div>
                    </div>
                    <div class="consultas-cartao-corpo">
                        <div class="consultas-cartao-detalhes">
                            <div>
                                <p><strong>Especialidade:</strong> ${item.especialidade || 'N/A'}</p>
                                <p><strong>Sintomas Relatados:</strong> ${item.sintomas || 'N/A'}</p>
                                <p><strong>Data:</strong> ${item.data || 'N/A'} | <strong>Hora:</strong> ${item.hora || 'N/A'}</p>
                                <p><strong>Duração:</strong> ${item.duracao || 'N/A'} minutos</p>
                            </div>
                            <div class="consultas-local-contato">
                                <b>Local e Contato</b>
                                <div>
                                    <span><i class="fa-solid fa-hospital"></i> ${item.local || 'N/A'}</span>
                                    <span><i class="fa-solid fa-phone"></i> ${item.contato || 'N/A'}</span>
                                </div>
                            </div>
                            <div class="consultas-info-adicionais">
                                <b>Detalhes Adicionais</b>
                                <div>
                                    <span><strong>Convênio:</strong> ${item.convenio || 'N/A'}</span>
                                    <span><strong>Número da Guia:</strong> ${item.numeroGuia || 'N/A'}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="consultas-acoes">
                        <!-- Ações específicas para médico, se houver -->
                    </div>
                </div>
            `;
            targetSection.insertAdjacentHTML('beforeend', cardHtml);
        });

        attachCardEventListeners(); // Reanexar listeners após renderizar novos cartões
    }

    // Função para anexar event listeners aos botões de ação dos cartões
    function attachCardEventListeners() {
        document.querySelectorAll('.view-details-btn').forEach(button => {
            button.onclick = null; // Limpa qualquer listener anterior
            button.addEventListener('click', function() {
                const itemId = this.dataset.id;
                const itemType = this.dataset.type; // Deve ser sempre 'Consulta' aqui

                fetch(`/api/consultas/${itemId}`)
                    .then(response => response.json())
                    .then(data => {
                        let detalhesHtml = `
                            <div class="detalhes-grupo">
                                <h3>Informações da Consulta</h3>
                                <p><strong>Tipo:</strong> Consulta</p>
                                <p><strong>Título:</strong> ${data.titulo || 'N/A'}</p>
                                <p><strong>Paciente:</strong> ${data.pacienteNome || 'N/A'}</p>
                                <p><strong>Médico:</strong> ${data.medico || 'N/A'}</p>
                                <p><strong>Especialidade:</strong> ${data.especialidade || 'N/A'}</p>
                                <p><strong>Data:</strong> ${data.data || 'N/A'}</p>
                                <p><strong>Hora:</strong> ${data.hora || 'N/A'}</p>
                                <p><strong>Duração:</strong> ${data.duracao || 'N/A'} minutos</p>
                            </div>

                            <div class="detalhes-grupo">
                                <h3>Local e Contato</h3>
                                <p><strong>Local:</strong> ${data.local || 'N/A'}</p>
                                <p><strong>Contato:</strong> ${data.contato || 'N/A'}</p>
                            </div>

                            <div class="detalhes-grupo">
                                <h3>Detalhes Adicionais</h3>
                                <p><strong>Convênio:</strong> ${data.convenio || 'N/A'}</p>
                                <p><strong>Número da Guia:</strong> ${data.numeroGuia || 'N/A'}</p>
                                <p><strong>Nível de Urgência:</strong> ${data.nivelUrgencia || 'N/A'}</p>
                            </div>

                            <div class="detalhes-grupo">
                                <h3>Sintomas e Observações</h3>
                                <p><strong>Sintomas Relatados:</strong> ${data.sintomas || 'N/A'}</p>
                                <p><strong>Informações para o Paciente:</strong> ${data.informacoesPaciente || 'N/A'}</p>
                                <p><strong>Observações:</strong> ${data.observacoes || 'N/A'}</p>
                            </div>

                            <div class="detalhes-grupo">
                                <h3>Status e Envio</h3>
                                <p><strong>Status:</strong> ${data.status || 'N/A'}</p>
                                <p><strong>Enviado por:</strong> ${data.enviadoPor || 'N/A'}</p>
                                <p><strong>Data de Envio:</strong> ${data.dataEnvio || 'N/A'}</p>
                            </div>

                            <div class="detalhes-grupo">
                                <h3>Configurações de Lembrete</h3>
                                <p><strong>Notificar Paciente:</strong> ${data.notificarPaciente ? 'Sim' : 'Não'}</p>
                                <p><strong>Lembrete para Médico (horas antes):</strong> ${data.lembreteHoras || 'N/A'}</p>
                            </div>
                        `;
                        detalhesContent.innerHTML = detalhesHtml;
                        consultaDetalhesModal.style.display = 'block';
                    })
                    .catch(error => console.error('Erro ao buscar detalhes da consulta:', error));
            });
        });

        document.querySelectorAll('.edit-btn').forEach(button => {
            button.onclick = null; // Limpa qualquer listener anterior
            button.addEventListener('click', function() {
                const itemId = this.dataset.id;
                const itemType = this.dataset.type; // Deve ser sempre 'Consulta' aqui

                fetch(`/api/consultas/${itemId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! status: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(data => {
                        // Preencher os campos do formulário do modal com os dados da consulta
                        document.getElementById('consultaId').value = data.id;
                        document.getElementById('pacienteIdoso').value = data.pacienteIdosoId;
                        document.getElementById('especialidade').value = data.especialidade;
                        document.getElementById('sintomas').value = data.sintomas;
                        // Garantir que a data esteja no formato YYYY-MM-DD
                        document.getElementById('data').value = data.data ? data.data.substring(0, 10) : ''; 
                        // Garantir que a hora esteja no formato HH:mm
                        document.getElementById('hora').value = data.hora ? data.hora.substring(0, 5) : '';
                        document.getElementById('duracao').value = data.duracao;
                        document.getElementById('local').value = data.local;
                        document.getElementById('contato').value = data.contato;
                        document.getElementById('convenio').value = data.convenio;
                        document.getElementById('numeroGuia').value = data.numeroGuia;
                        
                        // Preencher nível de urgência (botões de rádio)
                        const nivelUrgenciaRadios = document.querySelectorAll('input[name="nivelUrgencia"]');
                        nivelUrgenciaRadios.forEach(radio => {
                            if (radio.value === data.nivelUrgencia) {
                                radio.checked = true;
                            }
                        });

                        document.getElementById('informacoesPaciente').value = data.informacoesPaciente;
                        document.getElementById('observacoes').value = data.observacoes;
                        document.getElementById('status').value = data.status;
                        document.getElementById('enviadoPor').value = data.enviadoPor;
                        document.getElementById('dataEnvio').value = data.dataEnvio;
                        document.getElementById('notificarPaciente').checked = data.notificarPaciente;
                        document.getElementById('lembreteHoras').value = data.lembreteHoras;

                        // Configurar o formulário para PUT
                        novaConsultaForm.action = `/api/consultas/${itemId}`;
                        novaConsultaForm.method = 'PUT';
                        novaConsultaForm.dataset.itemId = itemId; // Armazenar o ID da consulta para uso posterior
                        novaConsultaForm.dataset.itemType = itemType; // Armazenar o tipo para uso posterior

                        // Abrir o modal de nova consulta para edição
                        modal.style.display = 'block';

                        // Esconder o botão de agendar e mostrar o de atualizar
                        document.querySelector('.btn-agendar').style.display = 'none';
                        const updateButton = document.querySelector('.btn-atualizar');
                        if (updateButton) {
                            updateButton.style.display = 'block';
                        }

                    })
                    .catch(error => console.error('Erro ao buscar detalhes para edição:', error));
            });
        });

        document.querySelectorAll('.delete-btn').forEach(button => {
            button.onclick = null; // Limpa qualquer listener anterior
            button.addEventListener('click', function() {
                const itemId = this.dataset.id;
                const itemType = this.dataset.type; // Deve ser sempre 'Consulta' aqui

                if (confirm(`Tem certeza que deseja excluir esta consulta?`)) {
                    fetch(`/api/consultas/${itemId}`, {
                        method: 'DELETE'
                    })
                    .then(response => {
                        if (response.ok) {
                            fetchAndRenderConsultations(); // Recarrega a lista após exclusão
                        } else {
                            console.error(`Erro ao excluir consulta:`, response.statusText);
                            alert(`Erro ao excluir consulta. Verifique o console.`);
                        }
                    })
                    .catch(error => {
                        console.error('Erro na requisição de exclusão:', error);
                        alert('Erro na requisição de exclusão. Verifique o console para mais detalhes.');
                    });
                }
            });
        });
    }

    closeDetalhesModalButton.addEventListener('click', function() {
        consultaDetalhesModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target == consultaDetalhesModal) {
            consultaDetalhesModal.style.display = 'none';
        }
    });

    // Função para buscar e renderizar as consultas com base nos filtros
    async function fetchAndRenderConsultations() {
        const status = statusFilter.value;

        let consultasUrl = `/api/medico/consultas/filtradas`;
        const consultasParams = new URLSearchParams();
        if (status) {
            consultasParams.append('status', status);
        }
        if (consultasParams.toString()) {
            consultasUrl += `?${consultasParams.toString()}`;
        }
        fetch(consultasUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                renderCards(data); // Removido o parâmetro 'type'
            })
            .catch(error => console.error('Erro ao buscar consultas filtradas:', error));
        consultasSection.style.display = 'block';
    }

    // Adicionar event listeners aos filtros
    statusFilter.addEventListener('change', fetchAndRenderConsultations);

    // Chamada inicial para carregar todas as consultas
    fetchAndRenderConsultations();

    // Handle form submission (ajustado para PUT e POST para Consulta)
    novaConsultaForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent default form submission

        const formData = new FormData(novaConsultaForm);
        const data = {};
        formData.forEach((value, key) => (data[key] = value));

        // Special handling for checkbox (if not checked, it won't be in formData)
        data.notificarPaciente = novaConsultaForm.elements['notificarPaciente'].checked;

        const formMethod = novaConsultaForm.method;
        let formAction;

        if (formMethod === 'PUT') {
            const itemId = novaConsultaForm.dataset.itemId;
            formAction = `/api/consultas/${itemId}`;
        } else { // POST
            formAction = '/api/consultas';
        }

        fetch(formAction, {
            method: formMethod === 'PUT' ? 'PUT' : 'POST', // Usar PUT para atualização, POST para criação
            headers: {
                'Content-Type': 'application/json', // Enviar como JSON
            },
            body: JSON.stringify(data), // Enviar dados como JSON
        })
        .then(response => {
            if (response.ok) {
                modal.style.display = 'none';
                novaConsultaForm.reset();
                document.querySelector('.btn-agendar').style.display = 'block';
                const updateButton = document.querySelector('.btn-atualizar');
                if (updateButton) {
                    updateButton.style.display = 'none';
                }
                delete novaConsultaForm.dataset.itemId; // Limpa o ID após a operação
                delete novaConsultaForm.dataset.itemType; // Limpa o tipo após a operação
                fetchAndRenderConsultations(); // Recarrega a lista após salvar/atualizar
            } else {
                console.error('Erro ao salvar/atualizar consulta:', response.statusText);
                alert('Erro ao salvar/atualizar consulta. Verifique o console para mais detalhes.');
            }
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            alert('Erro na requisição. Verifique o console para mais detalhes.');
        });
    });

    // Fetch Idosos and Populate Select
    function fetchIdosos() {
        fetch('/api/idosos')
            .then(response => response.json())
            .then(idosos => {
                pacienteIdosoSelect.innerHTML = '<option value="">Selecione o Paciente</option>';
                idosos.forEach(idoso => {
                    const option = document.createElement('option');
                    option.value = idoso.id;
                    option.textContent = idoso.nome;
                    pacienteIdosoSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Erro ao buscar idosos:', error));
    }
}); 