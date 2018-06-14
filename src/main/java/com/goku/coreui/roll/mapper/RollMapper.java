package com.goku.coreui.roll.mapper;

import com.goku.coreui.roll.model.Roll;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by liwenlong on 2018/5/14.
 */
public interface RollMapper {
    int insert(Roll roll);

    int edit(Roll roll);

    int delete(String[] ids);

    int send(String[] ids);

    Roll queryById(@Param("roll_id") String roll_id);

    List<Roll> queryPage(@Param("user_name") String user_name, @Param("begindate") Date begindate, @Param("enddate") Date enddate, @Param("roll_status") String roll_status);

}
