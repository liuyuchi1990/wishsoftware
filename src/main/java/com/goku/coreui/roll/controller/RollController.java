package com.goku.coreui.roll.controller;

import com.goku.coreui.roll.model.Roll;
import com.goku.coreui.roll.service.RollService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Controller
@RequestMapping("/roll")
public class RollController {

    @Autowired
    RollService rollService;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"roll:query"})
    public String index() {
        return  "roll/index";
    }



    @RequestMapping("/addPage")
    public String  add(Model model) {
        model.addAttribute("pageTitle","奖品新增");
        model.addAttribute("roll",new Roll());
        return  "roll/edit";
    }

    @RequestMapping("/editPage")
    public String editPage(Model model,String id) {
        model.addAttribute("pageTitle","奖品编辑");

        Roll roll = rollService.queryById(id);
        model.addAttribute("roll",roll);
        return  "roll/edit";
    }
}
