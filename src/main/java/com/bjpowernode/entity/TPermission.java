package com.bjpowernode.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限表
 * t_permission
 */
@Data
public class TPermission implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源代码（权限标识符）
     */
    private String code;

    /**
     * 资源URL
     */
    private String url;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 父资源ID
     */
    private Integer parentId;

    /**
     * 资源的排序
     */
    private Integer orderNo;

    /**
     * 资源图标（菜单的图标）
     */
    private String icon;

    /**
     * 菜单对应要渲染的Vue组件名称
     */
    private String component;

    private static final long serialVersionUID = 1L;
}