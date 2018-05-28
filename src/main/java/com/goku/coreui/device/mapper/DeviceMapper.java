package com.goku.coreui.device.mapper;

import com.goku.coreui.device.model.Device;
import com.goku.coreui.sys.model.WarnInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by richard on 2018/5/14.
 */
public interface DeviceMapper {
    int insert(Device device);

    int edit(Device device);

    int editDeviceStatus(WarnInfo warninfo);

    int delete(String[] ids);

    int loadGoods(String[] ids);

    Device queryById(@Param("device_id") String device_id);

    List<Device> queryPage(@Param("user_name") String user_name, @Param("begindate") Date begindate, @Param("enddate") Date enddate, @Param("device_status") String device_status);
}
