package com.goku.coreui.sys.controller.sysuser.impl;

import com.goku.coreui.sys.controller.sysuser.UserController;
import com.goku.coreui.sys.model.SysRole;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.service.SysRoleService;
import com.goku.coreui.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by nbfujx on 2017/12/25.
 */
@Controller
@RequestMapping("/sys/user")
public class UserControllerImpl implements UserController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Override
    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"sys:user:query"})
    public String  list(Model model) {
        return  "sys/user/list";
    }

    @Override
    @RequestMapping("/addPage")
    @RequiresPermissions(value={"sys:user:add"})
    public String  add(Model model) {
        model.addAttribute("pageTitle","用户新增");
        SysUser sysUser = new SysUser();
        model.addAttribute("sysUser",sysUser);
        List<SysRole> sysRoles = sysRoleService.getRoleForPaging();
        model.addAttribute("sysRoles",sysRoles);
        return  "sys/user/edit";
    }

    @Override
    @RequestMapping("/editPage")
    @RequiresPermissions(value={"sys:user:edit"})
    public String edit(@PathParam("userId") String userId, Model model) {
        model.addAttribute("pageTitle","用户修改");
        SysUser sysUser=sysUserService.selectByPrimaryKey(userId);
        model.addAttribute("sysUser",sysUser);
        List<SysRole> sysRoles = sysRoleService.getRoleForPaging();
        model.addAttribute("sysRoles",sysRoles);
        return  "sys/user/edit";
    }

    @RequestMapping("/vxEditPage")
    @RequiresPermissions(value={"sys:user:edit"})
    public String vxEditPage(@PathParam("userId") String userId, Model model) {
        model.addAttribute("pageTitle","用户微信修改");
        SysUser sysUser=sysUserService.selectByPrimaryKey(userId);
        model.addAttribute("sysUser",sysUser);
        return  "sys/user/vxedit";
    }

    @Override
    @RequestMapping("/menuAuthPage")
    @RequiresPermissions(value={"sys:user:menuauth"})
    public String menuAuth(String UserId, Model model) {
        model.addAttribute("pageTitle","用户菜单赋值");
        SysUser sysUser=sysUserService.selectByPrimaryKey(UserId);
        model.addAttribute("sysUser",sysUser);
        return  "sys/user/menuauth";
    }

    @Override
    @RequestMapping("/roleAuthPage")
    @RequiresPermissions(value={"sys:user:roleauth"})
    public String roleAuth(String UserId, Model model) {
        model.addAttribute("pageTitle","用户权限赋值");
        SysUser sysUser=sysUserService.selectByPrimaryKey(UserId);
        model.addAttribute("sysUser",sysUser);
        return  "sys/user/roleauth";
    }


}
