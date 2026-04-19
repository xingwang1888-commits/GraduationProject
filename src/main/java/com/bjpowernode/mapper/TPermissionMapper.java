package com.bjpowernode.mapper;

import com.bjpowernode.entity.TPermission;

import java.util.List;

public interface TPermissionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    int insertSelective(TPermission record);

    TPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    List<TPermission> selectByUserId(Integer userId);
}