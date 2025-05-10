package com.oimc.aimin.ai.service;

import com.oimc.aimin.ai.model.docment.Msg;

import java.util.List;

/**
 * 消息服务接口
 * @author 渣哥
 */
public interface MsgService {
    /**
     * 根据会话ID查找消息
     * @param conversationId 会话ID
     * @param lastN 最后N条消息
     * @return 消息列表
     */
    List<Msg> findByConversationId(String conversationId, int lastN);

    /**
     * 根据会话ID删除消息
     * @param conversationId 会话ID
     */
    void deleteByConversationId(String conversationId);
}
