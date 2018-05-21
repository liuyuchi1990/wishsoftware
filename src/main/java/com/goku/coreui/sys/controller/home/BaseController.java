package com.goku.coreui.sys.controller.home;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public abstract class BaseController {
	@Autowired
	private ImmutableMap<String, String> errorCodeMap;

	/**
	 * 接口数据返回
	 * @param errorCode
	 * @param data
	 * @return
	 */
	protected Map<String,Object> rtnParam(Integer errorCode,Object data) {
		//正常的业务逻辑 
		if(errorCode == 0){
			return ImmutableMap.of("errorCode", errorCode,"data", (data == null)? new Object() : data);
		}else{
			return ImmutableMap.of("errorCode", errorCode, "msg", errorCodeMap.get(String.valueOf(errorCode)));
		}
	}
}
