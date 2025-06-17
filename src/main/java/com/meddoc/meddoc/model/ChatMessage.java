package com.meddoc.meddoc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender; // Nome do remetente (para exibição)
    private Long senderId; // ID do remetente
    private Long receiverId; // ID do destinatário
    private String content;
    private LocalDateTime timestamp;
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
