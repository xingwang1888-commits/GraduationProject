package com.bjpowernode.service.impl;

import com.bjpowernode.entity.TPermission;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.mapper.TPermissionMapper;
import com.bjpowernode.mapper.TUserMapper;
import com.bjpowernode.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //逆向工程、反向工程（根据数据库表，生成mapper接口、mapper.xml、实体类）

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private TPermissionMapper tPermissionMapper;

    /**
     * 该方法在spring security框架登录的时候被调用
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询数据库，查询页面上传过来的这个用户名是否在数据库中存在，也就是根据该username查询用户对象
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("登录账号不存在");
        }

        //查询该用户的权限code列表（一个用户可能有多个权限code）
        List<TPermission> tPermissionList = tPermissionMapper.selectByUserId(tUser.getId());
        //把查询出来的角色放入用户对象中
        tUser.setTPermissionList(tPermissionList);

        //返回该用户对象
        return tUser;
    }
}
