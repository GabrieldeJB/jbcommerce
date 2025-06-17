document.addEventListener('DOMContentLoaded', function() {
    const messagesArea = document.getElementById('messagesArea');
    const messageInput = document.getElementById('messageInput');
    const sendMessageBtn = document.getElementById('sendMessageBtn');
    const conversationsList = document.getElementById('conversationsList');
    const currentChatIdosoName = document.getElementById('currentChatIdosoName');

    let stompClient = null;
    let selectedIdosoId = null;
    let selectedIdosoName = null;
    let currentMedicoId = null; // Para armazenar o ID do médico logado

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
        const socket = new SockJS('/chat-websocket'); // Altera para o endpoint correto
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    async function onConnected() {
        // Obter o ID do médico logado
        try {
            const medicoData = await fetchData('/api/usuarios/me');
            currentMedicoId = medicoData.id;
            console.log('Médico conectado com ID:', currentMedicoId);

            // Inscrever-se no tópico de mensagens diretas do médico
            stompClient.subscribe('/topic/messages/' + currentMedicoId, onMessageReceived);
            // Inscrever-se no tópico de mensagens públicas (para o idoso, se ele mandar para /topic/public)
            stompClient.subscribe('/topic/public', onMessageReceived);

            fetchConversations();
        } catch (error) {
            console.error('Erro ao obter informações do médico ou conectar ao chat:', error);
            alert('Não foi possível iniciar o chat. Por favor, tente novamente.');
        }
    }

    function onError(error) {
        console.error('Erro ao conectar ao WebSocket:', error);
        alert('Não foi possível conectar ao serviço de chat. Por favor, tente novamente mais tarde.');
    }

    // Carregar mensagens de um idoso específico
    async function loadMessages(idosoId) {
        messagesArea.innerHTML = ''; // Limpar mensagens anteriores
        try {
            const messages = await fetchData(`/api/chat/medico/${idosoId}`);
            messages.forEach(message => displayMessage(message));
            messagesArea.scrollTop = messagesArea.scrollHeight; // Rolar para a última mensagem
        } catch (error) {
            console.error('Erro ao carregar mensagens:', error);
            alert('Não foi possível carregar as mensagens para esta conversa.');
        }
    }

    // Exibir uma mensagem no chat
    function displayMessage(message) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message-bubble');
        messageElement.classList.add(message.sender === 'Médico' ? 'sent' : 'received');

        const content = document.createElement('p');
        content.textContent = message.content;
        messageElement.appendChild(content);

        const timestamp = document.createElement('span');
        timestamp.classList.add('message-timestamp');
        timestamp.textContent = new Date(message.timestamp).toLocaleTimeString(); // Formatar a hora
        messageElement.appendChild(timestamp);

        messagesArea.appendChild(messageElement);
        messagesArea.scrollTop = messagesArea.scrollHeight; // Rolar para a última mensagem
    }

    // Enviar mensagem
    function sendMessage() {
        const messageContent = messageInput.value.trim();
        if (messageContent && stompClient && selectedIdosoId) {
            const chatMessage = {
                sender: 'Médico', // O médico é o remetente
                receiverId: selectedIdosoId, // ID do idoso
                content: messageContent,
                timestamp: new Date().toISOString(),
                type: 'CHAT'
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        } else if (!selectedIdosoId) {
            alert('Por favor, selecione um idoso para conversar.');
        }
    }

    function onMessageReceived(payload) {
        const message = JSON.parse(payload.body);
        
        // Verifica se a mensagem é para a conversa atual selecionada ou foi enviada pelo médico logado
        const isForSelectedIdoso = selectedIdosoId && 
                                  ((message.senderId === selectedIdosoId && message.receiverId === currentMedicoId) ||
                                   (message.senderId === currentMedicoId && message.receiverId === selectedIdosoId));

        // Se a mensagem não é para a conversa selecionada, mas é uma nova mensagem de outro idoso, recarrega a lista de conversas
        if (!isForSelectedIdoso && message.type === 'CHAT' && message.senderId && message.senderId !== currentMedicoId) {
            fetchConversations(); // Recarregar a lista para mostrar a nova mensagem/conversa
        }

        if (isForSelectedIdoso) {
            displayMessage(message);
        }
    }

    // Carregar lista de conversas (idosos)
    async function fetchConversations() {
        try {
            const idosos = await fetchData('/api/chat/idosos'); // Endpoint para listar idosos com quem o médico pode conversar
            conversationsList.innerHTML = '';
            if (idosos.length === 0) {
                conversationsList.innerHTML = '<li class="no-results">Nenhum idoso encontrado.</li>';
                return;
            }
            idosos.forEach(idoso => {
                const li = document.createElement('li');
                li.classList.add('conversation-item');
                if (selectedIdosoId === idoso.id) {
                    li.classList.add('active');
                }
                li.dataset.idosoId = idoso.id;
                li.dataset.idosoName = idoso.nome;

                li.innerHTML = `
                    <div class="avatar">${idoso.nome.charAt(0).toUpperCase()}</div>
                    <div class="details">
                        <h4>${idoso.nome}</h4>
                        <p>Última mensagem...</p> <!-- TODO: Implementar a última mensagem -->
                    </div>
                `;
                li.addEventListener('click', () => {
                    // Remover classe 'active' de todos os itens e adicionar ao clicado
                    document.querySelectorAll('.conversation-item').forEach(item => item.classList.remove('active'));
                    li.classList.add('active');

                    selectedIdosoId = idoso.id;
                    selectedIdosoName = idoso.nome;
                    currentChatIdosoName.textContent = `Conversa com ${selectedIdosoName}`;
                    loadMessages(selectedIdosoId);
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