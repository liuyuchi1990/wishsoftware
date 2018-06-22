package com.goku.coreui.order.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.device.model.DeviceInfo;
import com.goku.coreui.device.service.DeviceService;
import com.goku.coreui.order.model.Cargo;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.order.model.OrderInfo;
import com.goku.coreui.order.service.OrderService;
import com.goku.coreui.sys.config.Constants;
import com.goku.coreui.sys.model.OrderMessage;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Api(value = "Order")
@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    OrderService orderService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    PageUtil pageUtil;

    @ApiIgnore
    @RequestMapping("/getListPage")
    @RequiresPermissions(value = {"order:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @ApiIgnore
    @RequestMapping("/addPage")
    @RequiresPermissions(value = {"order:query"})
    public String addPage() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @ApiIgnore
    @RequestMapping("/editPage")
    @RequiresPermissions(value = {"order:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }


    @ApiIgnore
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

    /**
     * 订单生成
     * @param order
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor=Exception.class)
    public ReturnResult save(@ApiParam @RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        final DelayQueue<OrderMessage> delayQueue = new DelayQueue<OrderMessage>();
        long time = System.currentTimeMillis();
        Map<String, Object> mapLane = orderService.queryForLane(order);
        //判断是否货道有货
        if (mapLane.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            order.setOrder_status("1");
            order.setOrder_id(UUID.randomUUID().toString().replaceAll("-", ""));
            int rs = orderService.insert(order);
            int rs2 = deviceService.release(order);
            if (rs > 0&&rs2>0) {
                map.put("status", "成功");
                map.put("orderId", order.getOrder_id());
                result.setResult(map);
                delayQueue.add(new OrderMessage(order.getOrder_id(), time,"60秒后执行"));
                /**启动一个线程，处理延迟订单消息**/
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        OrderMessage message = null;
                        while (!delayQueue.isEmpty()) {
                            try {
                                message = delayQueue.take();
                                if(message!=null){
                                    Order order = orderService.queryById(message.getOrderId());
                                    OrderInfo orderInfo = new OrderInfo();
                                    orderInfo.setOrder_id(order.getOrder_id());
                                    if("1".equals(order.getOrder_status())){//订单仍未支付，释放货物
                                        deviceService.rollback(order);
                                        orderInfo.setOrder_status("5");
                                        orderService.edit(orderInfo);
                                    }else{

                                    }
                                }
                                System.out.println(new Date() + "  处理延迟消息:  " + JSON.toJSONString(message));
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                });
            } else {
                result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
                result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
                map.put("status", "失败");
                result.setResult(map);
            }
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            mapLane.put("description", "货道无货");
            result.setResult(mapLane);
        }

        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(rollbackFor=Exception.class)
    public ReturnResult delete(@RequestParam String ids) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        Order order = orderService.queryById(ids);
        try{
            orderService.delete(ids.replaceAll("\"", ""));
            deviceService.rollback(order);
            map.put("status", "成功");
            return result;
        } catch(Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            return result;
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
    @Transactional(rollbackFor = {Exception.class}, readOnly = false)
    @RequestMapping(value = "/getOrderFeedBack", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderFeedBack(@ApiParam @RequestBody OrderInfo orderInfo) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        Integer count = 0;
        List<Cargo> CargoLst = Arrays.asList(orderInfo.getCargo_lane());
        for (Cargo f : CargoLst) {
            if (("true".equals(f.getStatus()))) {
                count++;
            }
        }
        if (count != CargoLst.size()) {
            orderInfo.setOrder_status("2");
        } else {
            orderInfo.setOrder_status("4");
        }
        orderInfo.setIntegral(count * Constants.Integral);
        int rs = orderService.edit(orderInfo);
        int rs2 = orderService.updateUserIntegral(orderInfo);
        if ((rs > 0) && (rs2 > 0)) {
            map.put("status", "成功");
            result.setResult(map);
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            if (count != CargoLst.size()) {
                map.put("status", "掉货失败");
            } else {
                map.put("status", "失败");
            }
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderPay", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderPay(@ApiParam @RequestBody OrderInfo orderInfo) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        orderInfo.setOrder_status("3");
        int rs = orderService.edit(orderInfo);
        if ("0".equals(orderInfo.getPay_type())) {
            int rs2 = orderService.updateUserIntegral(orderInfo);
        }
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

    @RequestMapping(value = "/getOrderByUserId", method = RequestMethod.GET)
    @ResponseBody
    public ReturnResult getOrderByUserId(@RequestParam(required = true) String user_id) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> res = orderService.getOrderByUserId(user_id);
            map.put("data", res);
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
