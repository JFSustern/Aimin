package com.oimc.aimin.admin.service.pipeline.delete.context;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/*
 *
 * @author 渣哥
 */
@Data
@AllArgsConstructor
public class AdminDelContext {
    private Set<Integer> adminIds;
    private Map<Integer,List<Integer>> roleMap;

    public AdminDelContext(Set<Integer> adminIds){
        this.adminIds = adminIds;
        this.roleMap = new HashMap<>();
    }

}
