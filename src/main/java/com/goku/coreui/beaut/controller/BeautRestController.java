package com.goku.coreui.beaut.controller;

import com.alibaba.fastjson.JSON;
import com.goku.coreui.beaut.model.Beaut;
import com.goku.coreui.beaut.service.BeautService;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.model.ext.Breadcrumb;
import com.goku.coreui.sys.model.ext.TablePage;
import com.goku.coreui.sys.util.BreadcrumbUtil;
import com.goku.coreui.sys.util.DateUtil;
import com.goku.coreui.sys.util.PageUtil;
import io.swagger.annotations.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liwenlong on 2018/5/15.
 */
@Api(value = "Beaut")
@RestController
@RequestMapping("/api/beaut")
public class BeautRestController {

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
    @RequiresPermissions(value={"beaut:query"})
    public String list() {
        List<Breadcrumb> Breadcrumbs= breadcrumbUtil.getBreadcrumbPath("beaut/getListPage");
        return JSON.toJSONString(Breadcrumbs);
    }

    @RequestMapping("/queryPage")
    public String  queryPage(
            @RequestParam(required=false) String user_name,
            @RequestParam(required=false) String begindate,
            @RequestParam(required=false) String enddate,
            @RequestParam int pageNumber, @RequestParam int pageSize){
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(user_name,DateUtil.StrtoDate(begindate,"yyyy-MM-dd"),DateUtil.StrtoDate(enddate,"yyyy-MM-dd"),pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value={"beaut:query"})
    public String  delete(@RequestBody String ids){
        int result = beautService.delete(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }

    @RequestMapping("/delImgStatus")
    @RequiresPermissions(value={"beaut:query"})
    public String  delImgStatus(@RequestBody String ids){
        int result = beautService.delImgStatus(ids.replaceAll("\"", ""));
        if(result>0) {
            return JSON.toJSONString ("true");
        }else{
            return JSON.toJSONString ("false");
        }
    }


    @ApiOperation(value = "获取img信息", response = ReturnResult.class)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "pageNumber", name = "pageNumber", dataType = "Integer", required = true, value = "文件名"),
            @ApiImplicitParam(paramType = "pageSize", name = "pageSize", dataType = "Integer", required = true, value = "客户代码")})
    @ApiResponses({@ApiResponse(code = 0, message = "success"),
            @ApiResponse(code = 0, message = "get label info exception.")})
    @RequestMapping(value = "/queryPageList/{pageNumber}/{pageSize}", method = RequestMethod.POST)
    public String queryPageList(@PathVariable("pageNumber") Integer pageNumber,@PathVariable("pageSize") Integer pageSize){
        TablePage tp = pageUtil.getDataForPaging(beautService.queryPage(null,null,null,pageNumber,pageSize));
        return JSON.toJSONString (tp);
    }

    @RequestMapping(value = "/fabulous/{id}/{type}", method = RequestMethod.POST)
    public String fabulous1(@PathVariable("type") String type,@PathVariable("id") String id){
        int result = beautService.setFabulous(type,id);
        Map<String,Object> map = new HashedMap();
        if(result>0) {
            map.put("status","success");
            map.put("msg","修改成功");
            return JSON.toJSONString (map);
        }else{
            map.put("status","fail");
            map.put("msg","修改失败");
            return JSON.toJSONString (map);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam("file") MultipartFile[] files,Beaut beaut){
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        beaut.setImg_id(id);
        Map<String,Object> map = new HashedMap();

        try{
            //判断file数组不能为空并且长度大于0
            if(files != null && files.length > 0){
                //循环获取file数组中得文件
                for(int i = 0;i < files.length;i++){
                    MultipartFile file = files[i];
                    //保存文件
                    String fileName = saveFile(file, filePath,id);

                    beaut.setImg_name(fileName);
                    beaut.setImg_path(url + fileName);
                    beautService.add(beaut);
                }
            }

            map.put("status","success");
            map.put("msg","添加成功");
            return JSON.toJSONString (map);
        }catch (Exception e){
            e.printStackTrace();
            map.put("status","fail");
            map.put("msg","添加失败");
            return JSON.toJSONString (map);
        }
    }


    /**
     * 保存图片
     * @param file
     * @param path
     * @return
     */
    private String saveFile(MultipartFile file, String path,String name) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                File filepath = new File(path);
                if (!filepath.exists())
                    filepath.mkdirs();
                // 文件保存路径

                String fileName = file.getOriginalFilename();
                int index = fileName.lastIndexOf(".");
                fileName = name + fileName.substring(index);


                String savePath = path + fileName;
                // 转存文件
                file.transferTo(new File(savePath));
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
