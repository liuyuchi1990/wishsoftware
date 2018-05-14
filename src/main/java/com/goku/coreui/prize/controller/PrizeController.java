package com.goku.coreui.prize.controller;

import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.prize.service.PrizeService;
import com.goku.coreui.sys.model.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Controller
@RequestMapping("/prize")
public class PrizeController {

    @Autowired
    PrizeService prizeService;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"prize:query"})
    public String index() {
        return  "prize/index";
    }



    @RequestMapping("/addPage")
    public String  add(Model model) {
        model.addAttribute("pageTitle","奖品新增");
        model.addAttribute("prize",new Prize());
        return  "prize/edit";
    }

    @RequestMapping("/editPage")
    public String editPage(Model model,String id) {
        model.addAttribute("pageTitle","奖品编辑");

        Prize prize = prizeService.queryById(id);
        model.addAttribute("prize",prize);
        return  "prize/edit";
    }
}
