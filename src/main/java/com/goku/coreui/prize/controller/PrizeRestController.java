package com.goku.coreui.prize.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.prize.service.PrizeService;
import com.goku.coreui.sys.mapper.SysUserMapper;
import com.goku.coreui.sys.model.SysMenu;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/prize")
public class PrizeRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    PrizeService prizeService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    PageUtil pageUtil;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"prize:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/addPage")
    @RequiresPermissions(value={"prize:query"})
    public String  addPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/editPage")
    @RequiresPermissions(value={"prize:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }



    @RequestMapping("/queryPage")
    @RequiresPermissions(value={"prize:query"})
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam(required=false) String prize_status,

            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp= pageUtil.getDataForPaging(prizeService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),prize_status,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }


    @RequestMapping("/save")
    @RequiresPermissions(value={"prize:query"})
    public String  save(@RequestBody Prize prize){
        int result = prizeService.insert(prize);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/edit")
    @RequiresPermissions(value={"prize:query"})
    public String  edit(@RequestBody Prize prize){
        int result = prizeService.edit(prize);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"prize:query"})
    public String  delete(@RequestBody String ids){
        int result = prizeService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }


    /**
     * 前端暴露接口 扫描二维码 绑定奖品信息
     * @param prize
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseBody
    public String bind( Prize prize){
        prize.setSend_time(new Date());
        SysUser sysUser = new SysUser();
        sysUser.setId(prize.getUser_id());
        sysUser.setAddress(prize.getSend_address());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);

        int result = prizeService.edit(prize);
        Map<String,Object> map = new HashedMap();
        if(result>0) {
            map.put("status","success");
            map.put("msg","绑定成功");
            return JSON.toJSONString (map);
        }else{
            map.put("status","fail");
            map.put("msg","绑定失败");
            return JSON.toJSONString (map);
        }
    }


}
