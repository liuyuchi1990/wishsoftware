package com.goku.coreui.sys.controller.home.impl;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.sys.controller.home.HomeRestController;
import com.goku.coreui.sys.mapper.ext.SysUserExtMapper;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.service.SysUserService;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.WxUtil;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nbfujx on 2017/12/25.
 */
@RestController
@RequestMapping("/api")
public class HomeRestControllerImpl implements HomeRestController {
    @Autowired
    BreadcrumbUtil breadcrumbUtil;

    @Autowired
    SysUserService sysUserService;

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @Transactional(rollbackFor = {Exception.class}, readOnly = false)
    public ReturnResult doLogin(
            @ApiParam @RequestBody SysUser user){
        String passwordmd5 = new Md5Hash("xyj1234567", "2").toString();
        user.setPassword(passwordmd5);
        user.setName(user.getUsername());
        Subject subject = SecurityUtils.getSubject();
        Map<String,Object> map = new HashMap<>();
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), passwordmd5);
        try {
            map =  WxUtil.getSessionKeyOropenid(user.getCode());
            user.setOpenId(map.get("openid").toString());
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            user.setId(id);
            SysUser hasUser = sysUserService.queryByOpenId(map.get("openid").toString());
            if(hasUser==null) {
                sysUserService.insert(user);
                map.put("id",id);
            }else{
                map.put("id",hasUser.getId());
            }

            subject.login(token);
            result.setResult(map);
            return result;
        }catch (UnknownAccountException e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status","账号不存在");
            result.setResult(map);
            return result;
        }catch (IncorrectCredentialsException e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status","账号密码错误");
            result.setResult(map);
            return result;
        }catch (AuthenticationException e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status","登录异常!请联系管理员!");
            result.setResult(map);
            return result;
        }catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status","系统异常!");
            result.setResult(map);
            return result;
        }
    }
}
