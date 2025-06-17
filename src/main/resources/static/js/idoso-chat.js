document.addEventListener('DOMContentLoaded', function() {
    const messagesArea = document.getElementById('messagesArea');
    const messageInput = document.getElementById('messageInput');
    const sendMessageBtn = document.getElementById('sendMessageBtn');
    const conversationsList = document.getElementById('conversationsList');
    const currentChatMedicoName = document.getElementById('currentChatMedicoName');

    let stompClient = null;
    let selectedMedicoId = null;
    let selectedMedicoName = null;
    let currentIdosoId = null;

    // Função auxiliar para chamadas fetch
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
            console.error('Erro na chamada da API:', error);
            alert(`Erro ao comunicar com o servidor: ${error.message}`);
            throw error;
        }
    }

    // Conectar ao WebSocket
    function connect() {
        const socket = new SockJS('/chat-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    async function onConnected() {
        try {
            const idosoData = await fetchData('/api/usuarios/me');
            currentIdosoId = idosoData.id;
            console.log('Idoso conectado com ID:', currentIdosoId);

            // Inscrever-se no tópico de mensagens diretas do idoso
            stompClient.subscribe('/topic/messages/' + currentIdosoId, onMessageReceived);
            // Inscrever-se no tópico de mensagens públicas
            stompClient.subscribe('/topic/public', onMessageReceived);

            fetchConversations();
        } catch (error) {
            console.error('Erro ao obter informações do idoso ou conectar ao chat:', error);
            alert('Não foi possível iniciar o chat. Por favor, tente novamente.');
        }
    }

    function onError(error) {
        console.error('Erro ao conectar ao WebSocket:', error);
        alert('Não foi possível conectar ao serviço de chat. Por favor, tente novamente mais tarde.');
    }

    // Carregar mensagens de um médico específico
    async function loadMessages(medicoId) {
        messagesArea.innerHTML = '';
        try {
            const messages = await fetchData(`/api/chat/idoso/${medicoId}`);
            messages.forEach(message => displayMessage(message));
            messagesArea.scrollTop = messagesArea.scrollHeight;
        } catch (error) {
            console.error('Erro ao carregar mensagens:', error);
            alert('Não foi possível carregar as mensagens para esta conversa.');
        }
    }

    // Exibir uma mensagem no chat
    function displayMessage(message) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message-bubble');
        messageElement.classList.add(message.sender === 'Idoso' ? 'sent' : 'received');

        const content = document.createElement('p');
        content.textContent = message.content;
        messageElement.appendChild(content);

        const timestamp = document.createElement('span');
        timestamp.classList.add('message-timestamp');
        timestamp.textContent = new Date(message.timestamp).toLocaleTimeString();
        messageElement.appendChild(timestamp);

        messagesArea.appendChild(messageElement);
        messagesArea.scrollTop = messagesArea.scrollHeight;
    }

    // Enviar mensagem
    function sendMessage() {
        const messageContent = messageInput.value.trim();
        if (messageContent && stompClient && selectedMedicoId) {
            const chatMessage = {
                sender: 'Idoso',
                receiverId: selectedMedicoId,
                content: messageContent,
                timestamp: new Date().toISOString(),
                type: 'CHAT'
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        } else if (!selectedMedicoId) {
            alert('Por favor, selecione um médico para conversar.');
        }
    }

    function onMessageReceived(payload) {
        const message = JSON.parse(payload.body);
        
        const isForSelectedMedico = selectedMedicoId && 
                                  ((message.senderId === selectedMedicoId && message.receiverId === currentIdosoId) ||
                                   (message.senderId === currentIdosoId && message.receiverId === selectedMedicoId));

        if (!isForSelectedMedico && message.type === 'CHAT' && message.senderId && message.senderId !== currentIdosoId) {
            fetchConversations();
        }

        if (isForSelectedMedico) {
            displayMessage(message);
        }
    }

    // Carregar lista de conversas (médicos)
    async function fetchConversations() {
        try {
            const medicos = await fetchData('/api/chat/medicos');
            conversationsList.innerHTML = '';
            if (medicos.length === 0) {
                conversationsList.innerHTML = '<li class="no-results">Nenhum médico encontrado.</li>';
                return;
            }
            medicos.forEach(medico => {
                const li = document.createElement('li');
                li.classList.add('conversation-item');
                if (selectedMedicoId === medico.id) {
                    li.classList.add('active');
                }
                li.dataset.medicoId = medico.id;
                li.dataset.medicoName = medico.nome;

                li.innerHTML = `
                    <div class="avatar">${medico.nome.charAt(0).toUpperCase()}</div>
                    <div class="details">
                        <h4>${medico.nome}</h4>
                        <p>Última mensagem...</p>
                    </div>
                `;
                li.addEventListener('click', () => {
                    document.querySelectorAll('.conversation-item').forEach(item => item.classList.remove('active'));
                    li.classList.add('active');

                    selectedMedicoId = medico.id;
                    selectedMedicoName = medico.nome;
                    currentChatMedicoName.textContent = `Conversa com ${selectedMedicoName}`;
                    loadMessages(selectedMedicoId);
                });
                conversationsList.appendChild(li);
            });
        } catch (error) {
            console.error('Erro ao carregar lista de conversas:', error);
            conversationsList.innerHTML = '<li class="no-results">Erro ao carregar conversas.</li>';
        }
    }

    sendMessageBtn.addEventListener('click', sendMessage);
    messageInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

    // Iniciar
    connect();
    fetchConversations();
}); 