package com.oimc.aimin.ai.config;

import com.oimc.aimin.ai.model.docment.Msg;
import com.oimc.aimin.ai.repository.MsgRepository;
import com.oimc.aimin.ai.service.MsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 *
 * @author 渣哥
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ChatStorageMemory implements ChatMemory {

    private final MsgRepository msgRepository;
    private final MsgService msgService;

    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            Msg msg = new Msg(conversationId, message);
            msgRepository.save(msg);
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Msg> list = msgService.findByConversationId(conversationId, lastN);
        return list.stream().map(Msg::toMessage).toList();

    }

    @Override
    public void clear(String conversationId) {
        msgService.deleteByConversationId(conversationId);
    }
}
