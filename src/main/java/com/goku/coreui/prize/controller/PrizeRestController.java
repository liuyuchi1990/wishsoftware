package com.goku.coreui.prize.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.beaut.model.Beaut;
import com.goku.coreui.common.UploadUtils;
import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.prize.service.PrizeService;
import com.goku.coreui.sys.mapper.SysUserMapper;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.SysMenu;
import com.goku.coreui.sys.model.SysUser;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/prize")
public class PrizeRestController {

    @Autowired
    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    PrizeService prizeService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    PageUtil pageUtil;

    @Value("${root.img.path.prize}")
    String filePath;
    @Value("${root.img.prize.url}")
    String url;

    @RequestMapping("/getListPage")
    @RequiresPermissions(value={"prize:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/addPage")
    @RequiresPermissions(value={"prize:query"})
    public String  addPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/addPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/editPage")
    @RequiresPermissions(value={"prize:query"})
    public String editPage() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("prize/editPage");
        return JSON.toJSONString(Breadcrumbs);
    }



    @RequestMapping("/queryPage")
    @RequiresPermissions(value={"prize:query"})
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam(required=false) String prize_status,

            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp= pageUtil.getDataForPaging(prizeService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),prize_status,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }


    @RequestMapping("/save")
    @RequiresPermissions(value={"prize:query"})
    public String  save(@RequestBody Prize prize){
        int result = prizeService.insert(prize);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/edit")
    @RequiresPermissions(value={"prize:query"})
    public String  edit(@RequestBody Prize prize){
        int result = prizeService.edit(prize);
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"prize:query"})
    public String  delete(@RequestBody String ids){
        int result = prizeService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/send")
    public String  send(@RequestBody String ids){
        int result = prizeService.send(ids.replaceAll("\"", ""));
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


    /**
     * 前端暴露接口 扫描二维码 绑定奖品信息
     * @param prize
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseBody
    public String bind( Prize prize){
        prize.setSend_time(new Date());
        SysUser sysUser = new SysUser();
        sysUser.setId(prize.getUser_id());
        sysUser.setAddress(prize.getSend_address());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);

        int result = prizeService.edit(prize);
        Map<String,Object> map = new HashedMap();
        if(result>0) {
            map.put("status","success");
            map.put("msg","绑定成功");
            return JSON.toJSONString (map);
        }else{
            map.put("status","fail");
            map.put("msg","绑定失败");
            return JSON.toJSONString (map);
        }
    }


}
