package com.goku.coreui.roll.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.common.UploadUtils;
import com.goku.coreui.roll.model.Roll;
import com.goku.coreui.roll.service.RollService;
import com.goku.coreui.sys.mapper.SysUserMapper;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/roll")
public class RollRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    RollService rollService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    PageUtil pageUtil;

    @Value("${root.img.path.roll}")
    String filePath;
    @Value("${root.img.roll.url}")
    String url;

    @ApiIgnore
    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"roll:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("roll/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @ApiIgnore
    @RequestMapping("/addPage")
    @RequiresPermissions(value={"roll:query"})
    public String  addPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("roll/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @ApiIgnore
    @RequestMapping("/editPage")
    @RequiresPermissions(value={"roll:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("roll/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }


    @ApiIgnore
    @RequestMapping("/queryPage")
    @RequiresPermissions(value={"roll:query"})
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam(required=false) String roll_status,

            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp= pageUtil.getDataForPaging(rollService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),roll_status,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }


    @RequestMapping("/save")
    @RequiresPermissions(value={"roll:query"})
    public String  save(@RequestBody Roll roll){
        int result = rollService.insert(roll);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/edit")
    @RequiresPermissions(value={"roll:query"})
    public String  edit(@RequestBody Roll roll){
        int result = rollService.edit(roll);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"roll:query"})
    public String  delete(@RequestBody String ids){
        int result = rollService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/send")
    public String  send(@RequestBody String ids){
        int result = rollService.send(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult add(@RequestParam("file") MultipartFile[] files){
        String[] imgs = new String[files.length];
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());

        Map<String,Object> map = new HashedMap();
        try{
            //判断file数组不能为空并且长度大于0
            if(files != null && files.length > 0){
                //循环获取file数组中得文件
                for(int i = 0;i < files.length;i++){
                    MultipartFile file = files[i];
                    //保存文件
                    String fileName = UploadUtils.saveFile(file, filePath, UUID.randomUUID().toString());
                    imgs[i] = url + fileName;
                }
            }
            map.put("status","success");
            map.put("msg","上传成功");
            map.put("data",imgs[0]);
            result.setResult(map);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            map.put("status","fail");
            map.put("msg","上传失败");
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            result.setResult(map);
            return result;
        }
    }
}
