package com.goku.coreui.order.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.device.model.DeviceInfo;
import com.goku.coreui.order.model.Cargo;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.order.model.OrderInfo;
import com.goku.coreui.order.service.OrderService;
import com.goku.coreui.sys.config.Constants;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Api(value = "Order")
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
    @RequiresPermissions(value = {"order:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/addPage")
    @RequiresPermissions(value = {"order:query"})
    public String addPage() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/editPage")
    @RequiresPermissions(value = {"order:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }


    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    @RequiresPermissions(value = {"order:query"})
    public String queryPage(
            @RequestParam(required = false) String user_name,
            @RequestParam(required = false) String begindate,
            @RequestParam(required = false) String enddate,
            @RequestParam(required = false) String order_status,

            @RequestParam int pageNumber, @RequestParam int pageSize) {
        TablePage tp = pageUtil.getDataForPaging(orderService.queryPage(user_name, DateUtil.StrtoDate(begindate, "yyyy-MM-dd"), DateUtil.StrtoDate(enddate, "yyyy-MM-dd"), order_status, pageNumber, pageSize));
        return JSON.toJSONString(tp);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult save(@ApiParam @RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        order.setOrder_status("1");
        int rs = orderService.insert(order);
        if (rs > 0) {
            map.put("status", "成功");
            result.setResult(map);
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody String ids) {
        int result = orderService.delete(ids.replaceAll("\"", ""));
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }

    @RequestMapping(value = "/getAllOrder", method = RequestMethod.GET)
    public ReturnResult getAllOrder(@RequestParam(required = true) String device_id,
                                    @RequestParam(required = false) String networkStatus,
                                    @RequestParam(required = false) String temperature) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = orderService.queryByDeviceId(device_id);
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        map.put("timeStamp", df.format(day));
        result.setResult(map);
        return result;
    }

    @ApiOperation(value = "获取掉货信息", response = OrderInfo.class)
    @RequestMapping(value = "/getOrderFeedBack", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderFeedBack(@ApiParam @RequestBody OrderInfo orderInfo) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        Integer  count = 0;
        List<Cargo> CargoLst = Arrays.asList(orderInfo.getCargo_lane());
        for(Cargo f : CargoLst){
            if(("true".equals(f.getStatus()))){
                count++;
            }
        }
        if(count!=CargoLst.size()){
            orderInfo.setOrder_status("2");
        }else{
            orderInfo.setOrder_status("4");
        }
        orderInfo.setIntegral(count * Constants.Integral);
        int rs = orderService.edit(orderInfo);
        int rs2 = orderService.updateUserIntegral(orderInfo);
        if ((rs > 0)&&( rs2>0)) {
            map.put("status", "成功");
            result.setResult(map);
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            if(count!=CargoLst.size()){
                map.put("status", "掉货失败");
            }else{
                map.put("status", "失败");
            }
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderPay", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderPay(@RequestParam(required = true) OrderInfo orderInfo) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        orderInfo.setOrder_status("3");
        int rs = orderService.edit(orderInfo);
        if (rs > 0) {
            map.put("status", "成功");
            result.setResult(map);
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

//    @RequestMapping(value = "/getOrderPay", method = RequestMethod.POST)
//    @ResponseBody
//    public ReturnResult getOrderPayByInterg(@RequestParam(required = true) OrderInfo orderInfo) {
//        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
//        Map<String, Object> map = new HashMap<>();
//        orderInfo.setOrder_status("3");
//        int rs = orderService.edit(orderInfo);
//        if (rs > 0) {
//            map.put("status", "成功");
//            result.setResult(map);
//        } else {
//            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
//            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
//            map.put("status", "失败");
//            result.setResult(map);
//        }
//        return result;
//    }

    @RequestMapping(value = "/getOrderByUserId", method = RequestMethod.GET)
    @ResponseBody
    public ReturnResult getOrderByUserId(@RequestParam(required = true) String user_id) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try{
            List <Map<String, Object>> res = orderService.getOrderByUserId(user_id);
            map.put("data",res);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }
}
