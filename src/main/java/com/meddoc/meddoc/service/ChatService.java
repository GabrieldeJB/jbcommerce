package com.meddoc.meddoc.service;

import com.meddoc.meddoc.model.ChatMessage;
import com.meddoc.meddoc.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesBetweenUsers(Long user1Id, Long user2Id) {
        // Busca mensagens onde user1 é remetente e user2 é destinatário, OU vice-versa
        return chatMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                user1Id, user2Id, user1Id, user2Id);
    }
} 