package com.a206.mychelin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Table(name = "chat_message")
@ToString
@DynamicInsert
@DynamicUpdate
@RequiredArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private int no;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "message")
    private String message;

    @Column(name = "send_date")
    private Timestamp sendDate;

    @Column(name = "flag")
    private String flag;

    @Builder
    public ChatMessage(int roomId, String senderId, String message) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
    }
}
