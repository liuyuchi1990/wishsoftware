package com.goku.coreui.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.sys.mapper.SysUserInfoMapper;
import com.goku.coreui.sys.mapper.SysUserMapper;
import com.goku.coreui.sys.mapper.SysUserRoleMapper;
import com.goku.coreui.sys.mapper.ext.SysUserAuthExtMapper;
import com.goku.coreui.sys.mapper.ext.SysUserExtMapper;
import com.goku.coreui.sys.mapper.ext.SysUserRoleExtMapper;
import com.goku.coreui.sys.model.*;
import com.goku.coreui.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by nbfujx on 2018/1/9.
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserExtMapper sysuserextmapper;

    @Autowired
    SysUserAuthExtMapper sysUserAuthExtMapper;

    @Autowired
    SysUserRoleExtMapper sysUserRoleExtMapper;

    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    SysUserInfoMapper sysUserInfoMapper;


    @Autowired
    SysUserMapper sysUserMapper;

    String ROLE_ADMIN_ID = "28c3ef4eefb111e7a2360a0027000038";

    @Override
    public PageInfo getUserForPaging(String username, String name, String orderFiled, String orderSort, int pageindex, int pagenum) {
        PageHelper.startPage(pageindex, pagenum);
        List<SysUser> list = sysuserextmapper.getUserForPaging(username, name,orderFiled, orderSort);
        PageInfo page = new PageInfo(list);
        return page;
    }

    @Override
    public SysUser selectByPrimaryKey(String UserId) {
        return sysuserextmapper.selectByPrimaryKey(UserId);
    }

    @Override
    public int deleteUser(String UserId) {
        return 0;
    }

    @Override
    public int saveUser(SysUser sysUser) {
        return 0;
    }

    @Override
    public int menuAuth(List<SysMenu> sysMenus, String userid, String moduleId) {
        int deleteResult=0;
        int addResult=0;
        deleteResult=sysUserAuthExtMapper.deleteUserAuthByModuleId(userid,moduleId);
        if(sysMenus!=null) {
            SysUserAuth sua = null;
            for (SysMenu sr : sysMenus) {
                sua = new SysUserAuth();
                sua.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                sua.setUserId(userid);
                sua.setMenuId(sr.getId());
                addResult = sysUserAuthExtMapper.insert(sua);
            }
        }else{
            addResult = 1;
        }

        if (addResult == 1 && deleteResult >= 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int roleauth(List<SysRole> sysRoles, String userid) {
        int deleteResult=0;
        int addResult=0;
        deleteResult=sysUserRoleExtMapper.deleteUserRole(userid);
        if(sysRoles!=null) {
            SysUserRole sur = null;
            for (SysRole sr : sysRoles) {
                sur = new SysUserRole();
                sur.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                sur.setUserId(userid);
                sur.setRoleId(sr.getId());
                addResult = sysUserRoleExtMapper.insert(sur);
            }
        }else
        {
            addResult = 1;
        }
        if (addResult == 1 && deleteResult >= 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int insert(SysUser sysUser) {
        String id = StringUtils.isEmpty(sysUser.getId())?UUID.randomUUID().toString().replaceAll("-", ""):sysUser.getId();
        sysUser.setId(id);
        if(StringUtils.isEmpty(sysUser.getOpenId())){
            sysUser.setPassword(new Md5Hash(sysUser.getPassword(), "2").toString());
            sysUser.setIsAdmin(sysUser.getRoleId().equals(ROLE_ADMIN_ID) ? "1" : "0");

            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            sysUserRole.setUserId(id);
            sysUserRole.setRoleId(sysUser.getRoleId());
            sysUserRoleMapper.insert(sysUserRole);

            SysUserInfo sysUserInfo = new SysUserInfo();
            sysUserInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            sysUserInfo.setUserId(id);
            sysUserInfo.setHomepage("28c3ef4eefb111e7a2360a0027000038");
            sysUserInfoMapper.insert(sysUserInfo);
        }

        return sysUserMapper.insert(sysUser);
    }

    @Override
    public int edit(SysUser sysUser) {
        if(StringUtils.isEmpty(sysUser.getOpenId())){
            SysUser u = this.selectByPrimaryKey(sysUser.getId());
            if(!u.getPassword().equals(sysUser.getPassword())){
                sysUser.setPassword(new Md5Hash(sysUser.getPassword(), "2").toString());
                sysUser.setIsAdmin(sysUser.getRoleId().equals(ROLE_ADMIN_ID) ? "1" : "0");
            }

            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(sysUser.getRoleId());
            sysUserRoleMapper.updateRoleIdByUserId(sysUserRole);
        }

        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public int delete(String ids) {
        String[] idArr = ids.split(",");
        return sysUserMapper.delete(idArr);
    }
}
