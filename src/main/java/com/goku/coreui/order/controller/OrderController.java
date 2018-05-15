package com.goku.coreui.order.controller;

import com.goku.coreui.order.model.Order;
import com.goku.coreui.order.service.OrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * create by richard 2018/5/14
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"order:query"})
    public String index() {
        return  "order/index";
    }



//    @RequestMapping("/addPage")
//    public String  add(Model model) {
//        model.addAttribute("pageTitle","奖品新增");
//        model.addAttribute("order",new Order());
//        return  "order/edit";
//    }
//
//    @RequestMapping("/editPage")
//    public String editPage(Model model,String id) {
//        model.addAttribute("pageTitle","奖品编辑");
//
//        Order order = orderService.queryById(id);
//        model.addAttribute("order",order);
//        return  "order/edit";
//    }
    
}
