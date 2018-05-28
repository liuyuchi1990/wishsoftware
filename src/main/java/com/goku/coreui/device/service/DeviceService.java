package com.goku.coreui.device.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goku.coreui.common.QRCodeUtils;
import com.goku.coreui.device.mapper.DeviceMapper;
import com.goku.coreui.device.model.Device;
import com.goku.coreui.sys.model.WarnInfo;
import com.goku.coreui.sys.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by richard on 2018/5/14.
 */
@Service
public class DeviceService {
    @Autowired
    DeviceMapper deviceMapper;

    @Value("${root.img.path.qr}")
    String qrPath;

    @Value("${root.device.url}")
    String qrUrl;

    public int insert(Device device) {
        device.setDevice_id(UUID.randomUUID().toString().replaceAll("-", ""));
        device.setDevice_status("1");
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
        String url = qrUrl.replace("{deviceId}",device.getDevice_id()).replace("{deviceAddress}",device.getDevice_address());
        String accessToken = WxUtil.getWxAccessToken();
        QRCodeUtils.getminiqrQr(accessToken,qrPath + device.getDevice_name()+ device.getDevice_id() + ".png",url);
        return deviceMapper.insert(device);
    }

    public int edit(Device device) {
        return deviceMapper.edit(device);
    }

    public int editDeviceStatus(WarnInfo warninfo) {
        return deviceMapper.editDeviceStatus(warninfo);
    }

    public int delete(String ids) {
        return deviceMapper.delete(ids.split(","));
    }

    public int loadGoods(String ids) {
        return deviceMapper.loadGoods(ids.split(","));
    }

    public Device queryById(String device_id) {
        return deviceMapper.queryById(device_id);
    }

    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String device_status, int pageindex, int pagenum) {
        PageHelper.startPage(pageindex, pagenum);
        List<Device> list = deviceMapper.queryPage(user_name, begindate, enddate, device_status);
        PageInfo page = new PageInfo(list);
        return page;
    }
}
