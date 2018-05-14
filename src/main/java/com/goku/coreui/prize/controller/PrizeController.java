package com.goku.coreui.prize.controller;


import com.goku.coreui.sys.config.log.LoggerInfo;
import org.springframework.ui.Model;

public interface PrizeController {

    @LoggerInfo(Method = "/prize/index",Name = "奖品模块")
    String index();
}
