package com.goku.coreui.sys.controller.syslog;

import com.goku.coreui.sys.config.log.LoggerInfo;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by nbfujx on 2018/1/16.
 */
@ApiIgnore
public interface LogRestController {
    String  getLogForPaging(String username, String begindate, String  enddate, int pageNumber,int pageSize);
}
