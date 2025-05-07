package com.oimc.aimin.ai.repository;

import com.oimc.aimin.ai.model.docment.Msg;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
 * 
 * @author 渣哥
*/
public interface MsgRepository extends MongoRepository<Msg, String> {
}
