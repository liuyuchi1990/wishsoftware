package com.goku.coreui.prize.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.common.QRCodeUtils;
import com.goku.coreui.prize.mapper.PrizeMapper;
import com.goku.coreui.prize.model.Prize;
import com.goku.coreui.sys.model.SysLog;
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

    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String prize_status, int pageindex, int pagenum) {
        PageHelper.startPage(pageindex, pagenum);
        List<Prize> list = prizeMapper.queryPage(user_name, begindate, enddate, prize_status);
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
