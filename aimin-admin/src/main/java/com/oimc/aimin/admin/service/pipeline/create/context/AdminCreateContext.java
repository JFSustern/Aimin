package com.oimc.aimin.admin.service.pipeline.create.context;

import com.oimc.aimin.admin.model.request.AdminRequest;
import lombok.Data;


@Data
public class AdminCreateContext {
    private String username;
    private String phone;
    private String password;
    private String encryptedPassword;
    private String nickname;
    private boolean gender;
    private Integer deptId;

    // createAdminModel 入参的构造函数
    public AdminCreateContext(AdminRequest model) {
        this.username = model.getUsername();
        this.phone = model.getPhone();
        this.password = model.getPassword();
        this.nickname = model.getNickname();
        this.gender = model.isGender();
        this.deptId = model.getDeptId();

    }

}
