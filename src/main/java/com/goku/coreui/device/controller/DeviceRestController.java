package com.goku.coreui.device.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.device.model.Device;
import com.goku.coreui.device.service.DeviceService;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.model.WarnInfo;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import com.goku.coreui.sys.util.SessionUtil;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/device")
public class DeviceRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    DeviceService deviceService;
    @Autowired
    PageUtil pageUtil;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"device:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("device/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/addPage")
    @RequiresPermissions(value={"device:query"})
    public String  addPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("device/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/editPage")
    @RequiresPermissions(value={"device:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("device/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }



    @RequestMapping("/queryPage")
    @RequiresPermissions(value={"device:query"})
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam(required=false) String device_status,

            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp= pageUtil.getDataForPaging(deviceService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),device_status,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }


    @RequestMapping("/save")
    public String  save(@RequestBody Device device){
        int result = deviceService.insert(device);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping(value = "/getDeviceById", method = RequestMethod.GET)
    public ReturnResult getDeviceById(@RequestParam(required = true) String device_id) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Device device = deviceService.queryById(device_id);
        Arrays.asList(device.getClass().getDeclaredFields()).forEach(f -> {
            if (f.getName().contains("cargo_lane_")) {
                f.setAccessible(true);
                try {
                    f.set(device, 5);
                    //System.out.println("属性名:" + f.getName() + " 属性值:"+ f.get(device) );
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        });
        //result.setResult(map);
        return result;
    }

    @RequestMapping("/edit")
    @RequiresPermissions(value={"device:query"})
    public String  edit(@RequestBody Device device){
        SysUser user = (SysUser)SessionUtil.getSessionAttribute("USERVO");
        device.setUpdate_user_id(user.getId());
        int result = deviceService.edit(device);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"device:query"})
    public String  delete(@RequestBody String ids){
        int result = deviceService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    /**
     * 预警接口
     * @param warninfo
     * @return
     */
    @RequestMapping(value = "/getWarningInfo", method = RequestMethod.POST)
    public ReturnResult getWarningInfo(@ApiParam @RequestBody WarnInfo warninfo){
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        int rs = deviceService.editDeviceStatus(warninfo);
        Map<String, Object> map = new HashMap<>();
        if(rs>0) {
            map.put("status","成功");
            result.setResult(map);
        }else{
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status","失败");
            result.setResult(map);
        }
        return result;
    }
}
