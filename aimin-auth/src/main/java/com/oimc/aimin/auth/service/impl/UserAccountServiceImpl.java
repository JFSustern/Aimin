package com.oimc.aimin.auth.service.impl;

import com.oimc.aimin.auth.entity.UserAccount;
import com.oimc.aimin.auth.mapper.UserAccountMapper;
import com.oimc.aimin.auth.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2024/12/23
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
