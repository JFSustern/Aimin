package com.oimc.aimin.ai.service;

import com.oimc.aimin.ai.model.docment.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *
 * @author 渣哥
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MsgService {

    private final MongoTemplate mongoTemplate;

    private final static String CONVERSATION_ID = "conversationId";

    public List<Msg> findByConversationId(String conversationId, int lastN) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CONVERSATION_ID).is(conversationId)).limit(lastN);
        return mongoTemplate.find(query, Msg.class);
    }

    public void deleteByConversationId(String conversationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CONVERSATION_ID).is(conversationId));
        mongoTemplate.remove(query, Msg.class);
    }

}
