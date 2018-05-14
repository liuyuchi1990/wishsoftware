package com.goku.coreui.prize.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Controller
@RequestMapping("/prize")
public class PrizeController {

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"prize:query"})
    public String index() {
        return  "prize/index";
    }
}
