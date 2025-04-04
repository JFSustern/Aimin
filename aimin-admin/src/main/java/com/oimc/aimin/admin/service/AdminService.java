package com.oimc.aimin.admin.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.oimc.aimin.admin.model.req.SearchReq;
import com.oimc.aimin.admin.model.req.LoginReq;
import com.oimc.aimin.admin.model.req.UpdateAdminReq;
import com.oimc.aimin.admin.model.entity.Admin;
import com.oimc.aimin.admin.service.pipeline.create.context.AdminCreateContext;
import com.oimc.aimin.admin.service.pipeline.delete.context.AdminDelContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author root
 * @since 2025/02/26
 */
public interface AdminService  extends MPJDeepService<Admin>{

    /**
     * 新增管理员
     * 通过责任链模式处理创建管理员的逻辑
     * 
     * @param adminCreateContext 管理员创建上下文，包含创建管理员所需的全部信息
     */
    void add(AdminCreateContext adminCreateContext);

    /**
     * 删除管理员
     * 通过责任链模式处理删除管理员的逻辑，包括删除关联的角色关系
     * 
     * @param adminDeleteContext 管理员删除上下文，包含需要删除的管理员ID等信息
     */
    void delete(AdminDelContext adminDeleteContext);

    /**
     * 管理员登录
     * 验证用户名和密码，生成登录令牌
     * 
     * @param login 登录请求参数，包含用户名、密码和验证码信息
     * @return 登录成功后的令牌信息，包含token、过期时间等
     */
    SaTokenInfo login(LoginReq login);

    /**
     * 搜索管理员列表
     * 根据条件分页查询管理员信息
     * 
     * @param req 搜索条件，包含关键字、分页参数等
     * @return 分页的管理员列表
     * @throws ExecutionException 执行异常
     * @throws InterruptedException 中断异常
     */
    Page<Admin> search(SearchReq req) throws ExecutionException, InterruptedException;

    /**
     * 获取管理员完整信息
     * 包括基本信息、角色和权限信息
     * 
     * @param adminId 管理员ID
     * @return 包含完整信息的管理员对象
     */
    @Deprecated
    Admin getAllInfoByAdminId(Integer adminId);

    /**
     * 更新管理员信息
     * 根据请求中的参数更新管理员的信息
     * 更新时会进行字段验证，如用户名和手机号的唯一性检查
     * @param updateAdminReq 更新管理员请求对象，包含需要更新的字段
     */
    void updateAdminInfo(UpdateAdminReq updateAdminReq);

    /**
     * 清除管理员相关缓存
     * 当管理员信息变更时调用此方法清除缓存，确保数据一致性
     */
    void clearCache();

    Admin getAdminById(Integer loginId);

    List<Admin> searchFuzzyWithDept(SearchReq req);
}