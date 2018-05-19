package com.goku.coreui.device.controller;

import com.goku.coreui.device.model.Device;
import com.goku.coreui.device.service.DeviceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * create by richard 2018/5/14
 */
@Controller
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"device:query"})
    public String index() {
        return  "device/index";
    }



    @RequestMapping("/addPage")
    public String  add(Model model) {
        model.addAttribute("pageTitle","货机新增");
        model.addAttribute("device",new Device());
        return  "device/edit";
    }

    @RequestMapping("/editPage")
    public String editPage(Model model, String id) {
        model.addAttribute("pageTitle","货机编辑");

        Device device = deviceService.queryById(id);
        model.addAttribute("device",device);
        return  "device/edit";
    }
    
}
