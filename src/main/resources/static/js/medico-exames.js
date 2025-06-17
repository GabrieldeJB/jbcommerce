document.addEventListener('DOMContentLoaded', function() {
    const novaExameModal = document.getElementById('novaExameModal');
    const openNewExameModalBtn = document.getElementById('openNewExameModal');
    const closeNewExameModalButton = novaExameModal.querySelector('.close-button');
    const cancelNewExameButton = novaExameModal.querySelector('.btn-cancelar-exame');
    const pacienteIdosoExameSelect = document.getElementById('pacienteIdosoExame');
    const novaExameForm = document.getElementById('novaExameForm');
    const exameIdInput = document.getElementById('exameId');

    const exameDetalhesModal = document.getElementById('exameDetalhesModal');
    const detalhesContentExame = document.getElementById('detalhesContentExame');
    const closeDetalhesModalButtonExame = exameDetalhesModal.querySelector('.close-button');

    const statusFilterExame = document.getElementById('status-filter-exame');
    const examesSection = document.getElementById('exames-section');

    // Função auxiliar para chamadas fetch (reutilizar do script.js principal se necessário)
    async function fetchData(url, options = {}) {
        try {
            const response = await fetch(url, {
                headers: {
                    'Content-Type': 'application/json',
                },
                ...options,
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: response.statusText }));
                throw new Error(`Erro ${response.status}: ${errorData.message || 'Erro desconhecido'}`);
            }

            if (response.status === 204) {
                return null;
            }

            return await response.json();
        } catch (error) {
            console.error('Erro na chamada da API para exames:', error);
            alert(`Erro ao comunicar com o servidor: ${error.message}`);
            throw error; 
        }
    }

    // Carregar lista de idosos para o select
    async function fetchIdososForExames() {
        try {
            const idosos = await fetchData('/api/idosos');
            pacienteIdosoExameSelect.innerHTML = '<option value="">Buscar por nome, CPF ou telefone...</option>';
            idosos.forEach(idoso => {
                const option = document.createElement('option');
                option.value = idoso.id;
                option.textContent = `${idoso.nome} (CPF: ${idoso.cpf || 'N/A'})`;
                pacienteIdosoExameSelect.appendChild(option);
            });
        } catch (error) {
            console.error('Erro ao carregar idosos para exames:', error);
        }
    }

    // Abrir Modal de Novo Exame
    openNewExameModalBtn.addEventListener('click', function() {
        novaExameModal.style.display = 'block';
        fetchIdososForExames();
        novaExameForm.reset();
        exameIdInput.value = ''; // Limpa o ID para nova criação
        document.querySelector('.btn-agendar-exame').style.display = 'block';
        document.querySelector('.btn-atualizar-exame').style.display = 'none';
    });

    // Fechar Modal de Novo Exame
    closeNewExameModalButton.addEventListener('click', function() {
        novaExameModal.style.display = 'none';
        novaExameForm.reset();
    });

    cancelNewExameButton.addEventListener('click', function() {
        novaExameModal.style.display = 'none';
        novaExameForm.reset();
    });

    window.addEventListener('click', function(event) {
        if (event.target == novaExameModal) {
            novaExameModal.style.display = 'none';
            novaExameForm.reset();
        }
    });

    // Renderizar cartões de exames
    function renderExameCards(exames) {
        examesSection.innerHTML = '';
        if (exames.length === 0) {
            examesSection.innerHTML = '<p class="no-results">Nenhum exame encontrado.</p>';
            return;
        }

        exames.forEach(exame => {
            const cardHtml = `
                <div class="consultas-cartao" data-id="${exame.id}" data-type="Exame">
                    <div class="consultas-cartao-cabecalho">
                        <div class="consultas-icone-tipo exame">
                            <i class="fa-solid fa-vial"></i>
                        </div>
                        <div>
                            <span class="consultas-cartao-titulo">${exame.titulo || exame.tipoDeExame || 'N/A'}</span>
                            <span class="consultas-status">${exame.status || 'N/A'}</span>
                        </div>
                        <div class="consultas-cabecalho-acoes">
                            <i class="fa-solid fa-eye action-icon view-details-exame-btn" data-id="${exame.id}"></i>
                            <i class="fa-solid fa-edit action-icon edit-exame-btn" data-id="${exame.id}"></i>
                            <i class="fa-solid fa-trash-alt action-icon delete-exame-btn" data-id="${exame.id}"></i>
                        </div>
                    </div>
                    <div class="consultas-cartao-corpo">
                        <div class="consultas-cartao-detalhes">
                            <div>
                                <p><strong>Tipo de Exame:</strong> ${exame.tipoDeExame || 'N/A'}</p>
                                <p><strong>Sintomas Relatados:</strong> ${exame.sintomas || 'N/A'}</p>
                                <p><strong>Data:</strong> ${exame.data || 'N/A'} | <strong>Hora:</strong> ${exame.hora || 'N/A'}</p>
                                <p><strong>Duração:</strong> ${exame.duracao || 'N/A'} minutos</p>
                            </div>
                            <div class="consultas-local-contato">
                                <b>Local e Contato</b>
                                <div>
                                    <span><i class="fa-solid fa-hospital"></i> ${exame.local || 'N/A'}</span>
                                    <span><i class="fa-solid fa-phone"></i> ${exame.contato || 'N/A'}</span>
                                </div>
                            </div>
                            <div class="consultas-info-adicionais">
                                <b>Detalhes Adicionais</b>
                                <div>
                                    <span><strong>Convênio:</strong> ${exame.convenio || 'N/A'}</span>
                                    <span><strong>Número da Guia:</strong> ${exame.numeroGuia || 'N/A'}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="consultas-acoes">
                        <!-- Ações específicas para médico, se houver -->
                    </div>
                </div>
            `;
            examesSection.insertAdjacentHTML('beforeend', cardHtml);
        });

        attachExameCardEventListeners();
    }

    // Função para anexar event listeners aos botões de ação dos cartões de exames
    function attachExameCardEventListeners() {
        document.querySelectorAll('.view-details-exame-btn').forEach(button => {
            button.onclick = null; // Limpa qualquer listener anterior
            button.addEventListener('click', async function() {
                const exameId = this.dataset.id;
                try {
                    const data = await fetchData(`/api/exames/${exameId}`);
                    document.getElementById('detalheExame-pacienteNome').textContent = data.pacienteNome || 'N/A';
                    document.getElementById('detalheExame-titulo').textContent = data.titulo || 'N/A';
                    document.getElementById('detalheExame-tipoDeExame').textContent = data.tipoDeExame || 'N/A';
                    document.getElementById('detalheExame-preparacaoNecessaria').textContent = data.preparacaoNecessaria || 'N/A';
                    document.getElementById('detalheExame-data').textContent = data.data || 'N/A';
                    document.getElementById('detalheExame-hora').textContent = data.hora || 'N/A';
                    document.getElementById('detalheExame-duracao').textContent = data.duracao || 'N/A';
                    document.getElementById('detalheExame-status').textContent = data.status || 'N/A';
                    document.getElementById('detalheExame-enviadoPor').textContent = data.enviadoPor || 'N/A';
                    document.getElementById('detalheExame-dataEnvio').textContent = data.dataEnvio || 'N/A';
                    document.getElementById('detalheExame-local').textContent = data.local || 'N/A';
                    document.getElementById('detalheExame-contato').textContent = data.contato || 'N/A';
                    document.getElementById('detalheExame-convenio').textContent = data.convenio || 'N/A';
                    document.getElementById('detalheExame-numeroGuia').textContent = data.numeroGuia || 'N/A';
                    document.getElementById('detalheExame-nivelUrgencia').textContent = data.nivelUrgencia || 'N/A';
                    document.getElementById('detalheExame-sintomas').textContent = data.sintomas || 'N/A';
                    document.getElementById('detalheExame-informacoesPaciente').textContent = data.informacoesPaciente || 'N/A';
                    document.getElementById('detalheExame-observacoes').textContent = data.observacoes || 'N/A';
                    document.getElementById('detalheExame-notificarPaciente').textContent = data.notificarPaciente ? 'Sim' : 'Não';
                    document.getElementById('detalheExame-lembreteHoras').textContent = data.lembreteHoras || 'N/A';

                    exameDetalhesModal.style.display = 'block';

                    // Atualizar IDs dos botões de ação no modal de detalhes
                    exameDetalhesModal.querySelector('.editar-exame-btn').dataset.id = exameId;
                    exameDetalhesModal.querySelector('.excluir-exame-btn').dataset.id = exameId;

                } catch (error) {
                    console.error('Erro ao buscar detalhes do exame:', error);
                }
            });
        });

        document.querySelectorAll('.edit-exame-btn').forEach(button => {
            button.onclick = null; // Limpa qualquer listener anterior
            button.addEventListener('click', async function() {
                const exameId = this.dataset.id;
                try {
                    const data = await fetchData(`/api/exames/${exameId}`);
                    // Preencher campos do modal de edição de exame
                    document.getElementById('exameId').value = data.id;
                    document.getElementById('pacienteIdosoExame').value = data.pacienteIdosoId;
                    document.getElementById('tipoDeExame').value = data.tipoDeExame;
                    document.getElementById('tituloExame').value = data.titulo;
                    document.getElementById('preparacaoNecessaria').value = data.preparacaoNecessaria;
                    document.getElementById('dataExame').value = data.data ? data.data.substring(0, 10) : '';
                    document.getElementById('horaExame').value = data.hora ? data.hora.substring(0, 5) : '';
                    document.getElementById('duracaoExame').value = data.duracao;
                    document.getElementById('localExame').value = data.local;
                    document.getElementById('contatoExame').value = data.contato;
                    document.getElementById('convenioExame').value = data.convenio;
                    document.getElementById('numeroGuiaExame').value = data.numeroGuia;
                    document.getElementById('sintomasExame').value = data.sintomas;
                    document.getElementById('informacoesPacienteExame').value = data.informacoesPaciente;
                    document.getElementById('observacoesExame').value = data.observacoes;
                    document.getElementById('notificarPacienteExame').checked = data.notificarPaciente;
                    document.getElementById('lembreteHorasExame').value = data.lembreteHoras;

                    // Preencher nível de urgência
                    const urgenciaRadios = document.querySelectorAll('input[name="nivelUrgencia"]');
                    urgenciaRadios.forEach(radio => {
                        if (radio.value === data.nivelUrgencia) {
                            radio.checked = true;
                        }
                    });

                    document.querySelector('.btn-agendar-exame').style.display = 'none';
                    document.querySelector('.btn-atualizar-exame').style.display = 'block';

                    novaExameModal.style.display = 'block';
                    fetchIdososForExames();

                } catch (error) {
                    console.error('Erro ao buscar exame para edição:', error);
                }
            });
        });

        document.querySelectorAll('.delete-exame-btn').forEach(button => {
            button.onclick = null; // Limpa qualquer listener anterior
            button.addEventListener('click', async function() {
                const exameId = this.dataset.id;
                if (confirm('Tem certeza que deseja excluir este exame?')) {
                    try {
                        await fetchData(`/api/exames/${exameId}`, { method: 'DELETE' });
                        alert('Exame excluído com sucesso!');
                        fetchAndRenderExames();
                    } catch (error) {
                        console.error('Erro ao excluir exame:', error);
                    }
                }
            });
        });
    }

    closeDetalhesModalButtonExame.addEventListener('click', function() {
        exameDetalhesModal.style.display = 'none';
    });

    window.addEventListener('click', function(event) {
        if (event.target == exameDetalhesModal) {
            exameDetalhesModal.style.display = 'none';
        }
    });

    // Função para buscar e renderizar os exames com base nos filtros
    async function fetchAndRenderExames() {
        const status = statusFilterExame.value;
        let examesUrl = `/api/exames/medico/filtrados`;
        const examesParams = new URLSearchParams();
        if (status) {
            examesParams.append('status', status);
        }
        if (examesParams.toString()) {
            examesUrl += `?${examesParams.toString()}`;
        }
        try {
            const data = await fetchData(examesUrl);
            renderExameCards(data);
        } catch (error) {
            console.error('Erro ao buscar exames filtrados:', error);
        }
    }

    // Adicionar event listeners aos filtros
    statusFilterExame.addEventListener('change', fetchAndRenderExames);

    // Lidar com o envio do formulário para criação e atualização de exames
    novaExameForm.addEventListener('submit', async function(event) {
        event.preventDefault(); // Impede o envio padrão do formulário

        const currentExameId = exameIdInput.value;
        console.log('medico-exames.js: currentExameId:', currentExameId);

        const formAction = currentExameId ? `/api/exames/${currentExameId}` : '/api/exames'; // Determina a action dinamicamente
        const formMethod = currentExameId ? 'PUT' : 'POST'; // Determina o method dinamicamente
        console.log('medico-exames.js: Determined formAction:', formAction, 'formMethod:', formMethod);

        const formData = new FormData(novaExameForm);
        const exameData = {};
        for (let [key, value] of formData.entries()) {
            if (key === 'notificarPaciente' && value === 'on') {
                exameData[key] = true;
            } else if (key === 'notificarPaciente') {
                exameData[key] = false;
            } else if (key === 'pacienteIdoso') {
                exameData['pacienteIdosoId'] = parseInt(value);
            } else if (key === 'nivelUrgencia') {
                exameData[key] = value;
            } else if (key === 'dataExame') {
                exameData['data'] = value;
            } else if (key === 'horaExame') {
                exameData['hora'] = value;
            } else if (key === 'duracaoExame' || key === 'lembreteHorasExame') {
                exameData[key.replace('Exame', '')] = parseInt(value);
            } else if (key === 'tipoDeExame' || key === 'tituloExame' || key === 'preparacaoNecessaria' || key === 'localExame' || key === 'contatoExame' || key === 'convenioExame' || key === 'numeroGuiaExame' || key === 'sintomasExame' || key === 'informacoesPacienteExame' || key === 'observacoesExame' || key === 'status') {
                 exameData[key.replace('Exame', '')] = value;
            } else if (key === 'exameId') {
                // Ignorar o exameId aqui, pois ele é parte da URL para PUT
            } else {
                exameData[key] = value;
            }
        }

        // Assegurar que o ID do paciente idoso seja um número
        if (exameData.pacienteIdosoId) {
            exameData.pacienteIdosoId = parseInt(exameData.pacienteIdosoId);
        }

        // Adicionar o ID do exame para a atualização
        if (formMethod === 'PUT' && currentExameId) {
            exameData.id = parseInt(currentExameId);
        }
        console.log('medico-exames.js: Exame data being sent:', exameData);

        try {
            let responseData;
            console.log('medico-exames.js: Calling fetchData...');
            if (formMethod === 'POST') {
                responseData = await fetchData(formAction, { method: 'POST', body: JSON.stringify(exameData) });
            } else if (formMethod === 'PUT') {
                responseData = await fetchData(formAction, { method: 'PUT', body: JSON.stringify(exameData) });
            }

            if (responseData) {
                alert(`Exame ${formMethod === 'POST' ? 'agendado' : 'atualizado'} com sucesso!`);
                novaExameModal.style.display = 'none';
                novaExameForm.reset();
                fetchAndRenderExames(); // Atualizar a lista de exames
            }
        } catch (error) {
            console.error(`Erro ao ${formMethod === 'POST' ? 'agendar' : 'atualizar'} exame:`, error);
            alert(`Erro ao ${formMethod === 'POST' ? 'agendar' : 'atualizar'} exame: ${error.message}`);
        }
    });

    // Chamada inicial para carregar exames
    fetchAndRenderExames();
}); 