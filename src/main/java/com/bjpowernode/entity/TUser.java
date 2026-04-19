package com.bjpowernode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 *
 * t_user
 */
@Data
public class TUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自动增长，用户ID
     */
    private Integer id;

    /**
     * 登录账号
     */
    private String loginAct;

    /**
     * 登录密码
     */
    @JsonIgnore //忽略该字段，该字段不参与json转换
    private String loginPwd;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 账户是否没有过期，0已过期 1正常
     */
    private Integer accountNoExpired;

    /**
     * 密码是否没有过期，0已过期 1正常
     */
    private Integer credentialsNoExpired;

    /**
     * 账号是否没有锁定，0已锁定 1正常
     */
    private Integer accountNoLocked;

    /**
     * 账号是否启用，0禁用 1启用
     */
    private Integer accountEnabled;

    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 编辑时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;

    /**
     * 编辑人
     */
    private Integer editBy;

    /**
     * 最近登录时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 用户的权限标识符（权限code）List
     */
    @JsonIgnore //忽略该字段，该字段不参与json转换
    private List<TPermission> tPermissionList;

    //------------------实现UserDetails接口中的7个方法----------------------
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (TPermission tPermission : this.tPermissionList) {
            //放入角色
            //authorities.add(new SimpleGrantedAuthority("ROLE_" + tRole.getRole()));
            //放入权限（权限标识符，权限code，权限代码）
            // { "clue:add", "clue:edit", "clue:list" ......  }
            authorities.add(new SimpleGrantedAuthority(tPermission.getCode()));
        }
        return authorities; //返回用户的权限（角色、权限code）
    }

    @JsonIgnore //忽略该字段，该字段不参与json转换
    @Override
    public String getPassword() {
        return this.loginPwd;
    }

    @Override
    public String getUsername() {
        return this.loginAct;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNoExpired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNoLocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNoExpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled == 1;
    }
}