package com.goku.coreui.beaut.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.beaut.service.BeautService;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by liwenlong on 2018/5/15.
 */
@Controller
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
    public String  queryPage(@RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(pageNumber,pageSize));
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
}
