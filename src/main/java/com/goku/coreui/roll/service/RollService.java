package com.goku.coreui.roll.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.common.QRCodeUtils;
import com.goku.coreui.roll.mapper.RollMapper;
import com.goku.coreui.roll.model.Roll;
import com.goku.coreui.sys.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Service
public class RollService {

    @Autowired
    RollMapper rollMapper;

    @Value("${root.img.path.qr}")
    String qrPath;

    @Value("${root.rq.url}")
    String qrUrl;

    public int insert(Roll roll) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        roll.setRoll_id(id);
        roll.setRoll_status(0);
        String url = qrUrl.replace("{rollId}", id);
        String accessToken = WxUtil.getWxAccessToken();
        //QRCodeUtils.getminiqrQr(accessToken, qrPath + roll.getRoll_name() + id + ".png", url);
        return rollMapper.insert(roll);
    }

    public int edit(Roll roll) {
        return rollMapper.edit(roll);
    }

    public int delete(String ids) {
        String[] idArr = ids.split(",");
        this.removeQr(idArr);
        return rollMapper.delete(idArr);
    }

    public int send(String ids) {
        String[] idArr = ids.split(",");
        return rollMapper.send(idArr);
    }

    public Roll queryById(String roll_id) {
        return rollMapper.queryById(roll_id);
    }

    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String roll_status, int pageindex, int pagenum) {
        PageHelper.startPage(pageindex, pagenum);
        List<Roll> list = rollMapper.queryPage(user_name, begindate, enddate, roll_status);
        PageInfo page = new PageInfo(list);
        return page;
    }

    public List<Roll> queryPage() {
        List<Roll> list = rollMapper.queryPage(null, null, null, "1");
        return list;
    }

    private void removeQr(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            File file = new File(qrPath + ids[i] + ".png");
            file.delete();
        }
    }
}
