package com.goku.coreui.beaut.mapper;


import com.goku.coreui.beaut.model.Beaut;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by liwenlong on 2018/5/15.
 */
public interface BeautMapper {

    int add(Beaut beaut);

    int delImgStatus(String[] ids);

    int delete(String[] ids);

    List<Beaut> queryPage(@Param("user_name") String user_name, @Param("begindate") Date begindate, @Param("enddate") Date enddate);

    int setFabulous(@Param("type") String type, @Param("id") String id);
}
