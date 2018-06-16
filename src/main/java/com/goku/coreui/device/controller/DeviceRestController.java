package com.goku.coreui.device.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.device.model.Device;
import com.goku.coreui.device.service.DeviceService;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.sys.config.Constants;
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
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;


@RestController
@RequestMapping("/api/device")
public class DeviceRestController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceRestController.class);

    @Value("${root.img.path.qr}")
    String qrPath;
    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    DeviceService deviceService;
    @Autowired
    PageUtil pageUtil;

    @ApiIgnore
    @RequestMapping("/getListPage")
    @RequiresPermissions(value = {"device:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("device/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @ApiIgnore
    @RequestMapping("/addPage")
    @RequiresPermissions(value = {"device:query"})
    public String addPage() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("device/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/editPage")
    @RequiresPermissions(value = {"device:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("device/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @ApiIgnore
    @RequestMapping("/queryPage")
    @RequiresPermissions(value = {"device:query"})
    public String queryPage(
            @RequestParam(required = false) String user_name,
            @RequestParam(required = false) String begindate,
            @RequestParam(required = false) String enddate,
            @RequestParam(required = false) String device_status,

            @RequestParam int pageNumber, @RequestParam int pageSize) {
        TablePage tp = pageUtil.getDataForPaging(deviceService.queryPage(user_name, DateUtil.StrtoDate(begindate, "yyyy-MM-dd"), DateUtil.StrtoDate(enddate, "yyyy-MM-dd"), device_status, pageNumber, pageSize));
        return JSON.toJSONString(tp);
    }


    @RequestMapping("/save")
    public String save(@RequestBody Device device) {
        int result = deviceService.insert(device);
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }

    @RequestMapping(value = "/getDeviceById", method = RequestMethod.GET)
    public ReturnResult getDeviceById(@RequestParam(required = true) String device_id) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Device device = deviceService.queryById(device_id);
        Map<String, Object> map = new HashMap<>();
        List lst = new ArrayList();
        List<Field> fields = Arrays.asList(device.getClass().getDeclaredFields());
        try {
            for (Field field : fields) {
                Map<String, Object> mp = new HashMap<>();
                field.setAccessible(true);
                if (field.getName().contains("cargo_lane_")) {
                    mp.put("status", field.get(device));
                    mp.put("goodOrder", Integer.valueOf(field.getName().replace("cargo_lane_", "")).intValue());
                    mp.put("isActive", false);
                    lst.add(mp);
                }
            }
            map.put("goods", lst);
            map.put("price", Constants.PRICE);
            map.put("description",device.getDevice_name());
            map.put("status",device.getDevice_status());
            result.setResult(map);
        } catch (IllegalAccessException e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping("/edit")
    @RequiresPermissions(value = {"device:query"})
    public String edit(@RequestBody Device device) {
        SysUser user = (SysUser) SessionUtil.getSessionAttribute("USERVO");
        device.setUpdate_user_id(user.getId());
        int result = deviceService.edit(device);
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }

    @RequestMapping("/loadGoods")
    public String loadGoods(@RequestBody String ids) {
        int result = deviceService.loadGoods(ids.replaceAll("\"", ""));
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }

    @RequestMapping("/delete")
    public String delete(@RequestBody String ids) {
        int result = deviceService.delete(ids.replaceAll("\"", ""));
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }

    /**
     * 预警接口
     *
     * @param warninfo
     * @return
     */
    @RequestMapping(value = "/getWarningInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getWarningInfo(@ApiParam @RequestBody WarnInfo warninfo) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        int rs = deviceService.editDeviceStatus(warninfo);
        Map<String, Object> map = new HashMap<>();
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

    @ApiIgnore
    @RequestMapping(value = "/downloadQR", method = RequestMethod.GET)
    @ResponseBody
    public void downloadQR(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(Constants.FILE_NAME) String fileName) throws IOException {
        String userAgent = request.getHeader("User-Agent");
        String fileNameDecode = URLDecoder.decode(fileName, Constants.EN_CODING);
        File file = new File(qrPath + fileNameDecode);
        if (file.exists()) {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                try {
                    response.setHeader(Constants.CONTENT_DISPOSITION, String.format(Constants.ATTACHMENT_FILENAME,
                            new String(fileName.getBytes(Constants.EN_CODING_GBK), Constants.EN_CODING_ISO)));
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                    response.setHeader(Constants.CONTENT_DISPOSITION,
                            String.format(Constants.ATTACHMENT_FILENAME, fileNameDecode));
                }
            } else {
                // 非IE浏览器的处理：
                fileNameDecode = new String(fileNameDecode.getBytes(Constants.EN_CODING), Constants.EN_CODING_ISO);
                response.setHeader(Constants.CONTENT_DISPOSITION, String.format(Constants.ATTACHMENT_FILENAME, fileNameDecode));
            }
            Map<String, Object> logData = new HashMap<>();
            logData.put(Constants.FILE_NAME, fileName);
            output(response, file);
        }
    }

    /**
     * @param @param response
     * @param @param file 设定文件
     * @param file
     * @return void 返回类型
     * @throws @param response
     * @Title: output
     * @Description: 输出文件流
     */
    private void output(HttpServletResponse response, File file) {
        byte[] buffer = new byte[1024];
        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis)) {
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
