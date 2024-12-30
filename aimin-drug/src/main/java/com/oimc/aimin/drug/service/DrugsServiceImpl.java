package com.oimc.aimin.drug.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oimc.aimin.drug.entity.Drugs;
import com.oimc.aimin.drug.mapper.DrugsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author root
 * @since 2024/12/28
 */
@Service
@RequiredArgsConstructor
public class DrugsServiceImpl extends ServiceImpl<DrugsMapper, Drugs> implements DrugsService {



}
