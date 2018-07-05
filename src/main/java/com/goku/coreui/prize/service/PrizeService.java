package com.goku.coreui.prize.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.common.QRCodeUtils;
import com.goku.coreui.prize.mapper.PrizeMapper;
import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.sys.model.SysLog;
import com.goku.coreui.sys.util.WxUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * Created by liwenlong on 2018/5/14.
 */
@Service
public class PrizeService {

    @Autowired
    PrizeMapper prizeMapper;

    @Value("${root.img.path.qr}")
    String qrPath;

    @Value("${root.rq.url}")
    String qrUrl;

    public int insert(Prize prize) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        prize.setPrize_id(id);
        prize.setPrize_status(0);
        String url = qrUrl.replace("{prizeId}", id);
        String accessToken = WxUtil.getWxAccessToken();
        QRCodeUtils.getminiqrQr(accessToken, qrPath + prize.getPrize_name() + id + ".png", url);
        return prizeMapper.insert(prize);
    }

    public int edit(Prize prize) {
        return prizeMapper.edit(prize);
    }

    public int delete(String ids) {
        String[] idArr = ids.split(",");
        this.removeQr(idArr);
        return prizeMapper.delete(idArr);
    }

    public int send(String ids) {
        String[] idArr = ids.split(",");
        return prizeMapper.send(idArr);
    }

    public Prize queryById(String prize_id) {
        return prizeMapper.queryById(prize_id);
    }

    public List<Map<String,Object>> queryByUserId(String user_id) {
        return prizeMapper.queryByUserId(user_id);
    }

    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String prize_status, int pageindex, int pagenum) {
        PageHelper.startPage(pageindex, pagenum);
        List<Prize> list = prizeMapper.queryPage(user_name, begindate, enddate, prize_status);
        for(Prize pr : list){
            if(StringUtils.isNotEmpty(pr.getSend_address())){
                Map mp = JSON.parseObject(pr.getSend_address());
                pr.setSend_address(mp.get("address").toString());
                pr.setMobile(mp.get("phone").toString());
                pr.setUser_name(mp.get("name").toString());
            }
        }
        PageInfo page = new PageInfo(list);
        return page;
    }

    private void removeQr(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            File file = new File(qrPath + ids[i] + ".png");
            file.delete();
        }
    }
}
