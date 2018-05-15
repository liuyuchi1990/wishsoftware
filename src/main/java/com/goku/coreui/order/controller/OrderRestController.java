package com.goku.coreui.order.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.order.service.OrderService;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    OrderService orderService;
    @Autowired
    PageUtil pageUtil;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"order:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("order/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/addPage")
    @RequiresPermissions(value={"order:query"})
    public String  addPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("order/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/editPage")
    @RequiresPermissions(value={"order:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("order/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }



    @RequestMapping("/queryPage")
    @RequiresPermissions(value={"order:query"})
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam(required=false) String order_status,

            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp= pageUtil.getDataForPaging(orderService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),order_status,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }


    @RequestMapping("/save")
    public String  save(@RequestBody Order order){
        int result = orderService.insert(order);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/edit")
    @RequiresPermissions(value={"order:query"})
    public String  edit(@RequestBody Order order){
        int result = orderService.edit(order);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"order:query"})
    public String  delete(@RequestBody String ids){
        int result = orderService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

}
