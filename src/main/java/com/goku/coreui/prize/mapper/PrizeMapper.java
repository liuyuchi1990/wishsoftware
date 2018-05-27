package com.goku.coreui.prize.mapper;

import com.goku.coreui.prize.model.Prize;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by liwenlong on 2018/5/14.
 */
public interface PrizeMapper {
    int insert(Prize prize);

    int edit(Prize prize);

    int delete(String[] ids);

    int send(String[] ids);

    Prize queryById(@Param("prize_id") String prize_id);

    List<Prize> queryPage(@Param("user_name") String user_name, @Param("begindate") Date begindate, @Param("enddate") Date enddate, @Param("prize_status") String prize_status);

}
