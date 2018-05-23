package com.goku.coreui.sys.controller.syslog;

import com.goku.coreui.sys.config.log.LoggerInfo;
import org.springframework.ui.Model;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by nbfujx on 2018/1/16.
 */
@ApiIgnore
public interface LogController {

    @LoggerInfo(Method = "/sys/log/getListPage",Name = "日志列表")
    String  list(Model model);
}
