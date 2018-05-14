package com.goku.coreui.prize.controller.impl;

import com.goku.coreui.prize.controller.PrizeController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Controller
@RequestMapping("/prize")
public class PrizeControllerImpl implements PrizeController {

    @Override
    @RequestMapping("/index")
    @RequiresPermissions(value={"prize:query"})
    public String index() {
        return  "prize/index";
    }
}
