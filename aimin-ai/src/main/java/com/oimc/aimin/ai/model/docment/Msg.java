package com.oimc.aimin.ai.model.docment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.ai.chat.messages.Message;


import java.util.Date;

/*
 *
 * @author 渣哥
 */
@Document( "msg")
@Data
@NoArgsConstructor
public class Msg {
    @Id
    private String id;
    private String conversationId;
    private String text;
    private Date date;
    private String type;

    public Msg(String conversationId, Message message) {
        this.conversationId = conversationId;
        this.text = message.getText();
        this.date = new Date();
        this.type = message.getMessageType().getValue();
    }

    public static Message toMessage(Msg msg) {
        return switch (msg.getType()) {
            case "user" -> new UserMessage(msg.getText());
            case "assistant" -> new AssistantMessage(msg.getText());
            case "system" -> new SystemMessage(msg.getText());
            default -> null;
        };
    }

}
