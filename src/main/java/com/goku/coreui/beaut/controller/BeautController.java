package com.goku.coreui.beaut.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liwenlong on 2018/5/15.
 */
@Controller
@RequestMapping("/beaut")
public class BeautController {

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"beaut:query"})
    public String index() {
        return  "beaut/index";
    }
}
