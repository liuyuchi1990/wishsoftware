package com.goku.coreui.beaut.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.beaut.model.Beaut;
import com.goku.coreui.beaut.service.BeautService;
import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.prize.service.PrizeService;
import com.goku.coreui.sys.model.SysMenu;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liwenlong on 2018/5/15.
 */
@RestController
@RequestMapping("/api/beaut")
public class BeautRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    BeautService beautService;
    @Autowired
    PageUtil pageUtil;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"beaut:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("beaut/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/queryPage")
    @RequiresPermissions(value={"beaut:query"})
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"beaut:query"})
    public String  delete(@RequestBody String ids){
        int result = beautService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/add")
    public String add(@RequestBody Beaut beaut){
        int result = beautService.add(beaut);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delImgStatus")
    @RequiresPermissions(value={"beaut:query"})
    public String  delImgStatus(@RequestBody String ids){
        int result = beautService.delImgStatus(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/queryPageList/{pageNumber}/{pageSize}")
    public String queryPageList(@PathVariable("pageNumber") Integer pageNumber,@PathVariable("pageSize") Integer pageSize){
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(null,null,null,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }

    @RequestMapping("/fabulous/{id}/{type}")
    public String fabulous1(@PathVariable("type") String type,@PathVariable("id") String id){
        int result = beautService.setFabulous(type,id);
        Map<String,Object> map = new HashedMap();
        if(result>0) {
            map.put("status","success");
            map.put("msg","修改成功");
            return JSON.toJSONString (map);
        }else{
            map.put("status","fail");
            map.put("msg","修改失败");
            return JSON.toJSONString (map);
        }
    }

}
