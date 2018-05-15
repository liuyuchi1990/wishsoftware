package com.goku.coreui.beaut.mapper;


import com.goku.coreui.beaut.model.Beaut;

import java.util.Date;
import java.util.List;

/**
 * Created by liwenlong on 2018/5/15.
 */
public interface BeautMapper {

    int delete(String[] ids);

    List<Beaut> queryPage();
}
