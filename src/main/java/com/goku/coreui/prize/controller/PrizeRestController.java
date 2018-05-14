package com.goku.coreui.prize.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/prize")
public class PrizeRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"sys:log:query"})
    public String  list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }
}
