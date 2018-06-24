package com.goku.coreui.beaut.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.beaut.model.Beaut;
import com.goku.coreui.beaut.service.BeautService;
import com.goku.coreui.common.UploadUtils;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import io.swagger.annotations.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liwenlong on 2018/5/15.
 */
@RestController
@RequestMapping("/api/beaut")
public class BeautRestController {
    private static final Logger logger = LoggerFactory.getLogger(BeautRestController.class);
    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    BeautService beautService;
    @Autowired
    PageUtil pageUtil;
    @Value("${root.img.path.beaut}")
    String filePath;
    @Value("${root.img.url}")
    String url;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value = {"beaut:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("beaut/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/queryPage")
    public String queryPage(
            @RequestParam(required = false) String user_name,
            @RequestParam(required = false) String begindate,
            @RequestParam(required = false) String enddate,
            @RequestParam int pageNumber, @RequestParam int pageSize) {
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(user_name, DateUtil.StrtoDate(begindate, "yyyy-MM-dd"), DateUtil.StrtoDate(enddate, "yyyy-MM-dd"), pageNumber, pageSize));
        return JSON.toJSONString(tp);
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = {"beaut:query"})
    public String delete(@RequestBody String ids) {
        int result = beautService.delete(ids.replaceAll("\"", ""));
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }

    @RequestMapping("/delImgStatus")
    @RequiresPermissions(value = {"beaut:query"})
    public String delImgStatus(@RequestBody String ids) {
        int result = beautService.delImgStatus(ids.replaceAll("\"", ""));
        if (result > 0) {
            return JSON.toJSONString("true");
        } else {
            return JSON.toJSONString("false");
        }
    }


    @RequestMapping(value = "/queryPageList/{pageNumber}/{pageSize}", method = RequestMethod.POST)
    @ResponseBody
    public String queryPageList(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(null, null, null, pageNumber, pageSize));
        return JSON.toJSONString(tp);
    }

    @RequestMapping(value = "/fabulous/{id}/{type}", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult fabulous(@PathVariable("type") String type, @PathVariable("id") String id) {
        int rs = beautService.setFabulous(type, id);
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        if (rs > 0) {
            map.put("status", "success");
            map.put("msg", "修改成功");
            result.setResult(map);
            return result;
        } else {
            map.put("status", "fail");
            map.put("msg", "修改失败");
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            result.setResult(map);
            return result;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult add(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        String user_id = request.getParameter("user_id");
        Beaut beaut = new Beaut();
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        beaut.setImg_id(id);
        beaut.setUser_id(user_id);
        Map<String, Object> map = new HashedMap();

        try {
//            //判断file数组不能为空并且长度大于0
//            if(files != null && files.length > 0){
//                //循环获取file数组中得文件
//                for(int i = 0;i < files.length;i++){
//                    MultipartFile file = files[i];
//                    //保存文件
            String fileName = UploadUtils.saveFile(file, filePath, id);
            beaut.setImg_name(fileName);
            beaut.setImg_path(url + fileName);
            beautService.add(beaut);
//                }
//            }
            map.put("status", "success");
            map.put("msg", "上传成功");
            result.setResult(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "fail");
            map.put("msg", "修改失败");
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            result.setResult(map);
            return result;
        }
    }

    @ApiOperation(value = "上传文件(小程序)")
    @PostMapping("/fileUpload")
    public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile files) {
        logger.info("上传测试");
        Beaut beaut = new Beaut();
        String user_id = request.getParameter("user_id");
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        beaut.setImg_id(id);
        beaut.setUser_id(user_id);
        //多文件上传
        if (files != null) {
            BufferedOutputStream bw = null;
            try {
                String fileName = files.getOriginalFilename();
                //判断是否有文件(实际生产中要判断是否是音频文件)
                if (StringUtils.isNoneBlank(fileName)) {
                    fileName = UploadUtils.saveFile(files, filePath, id);
                    beaut.setImg_name(fileName);
                    beaut.setImg_path(url + fileName);
                    beautService.add(beaut);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "success";
    }

}
