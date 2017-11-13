package com.lk.kDeploy.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lk.kDeploy.base.dto.ResponseDTO;
import com.lk.kDeploy.config.RespMessage;
import com.lk.kDeploy.constants.ReturnCode;

/**
 * response构造工具
 *
 * @author: lk
 * @since: 2017年11月7日
 */
@Component
public class RespBuildUtil {

    private static RespMessage respMessage;

    @Autowired(required = true)
    public void setRespMessage(RespMessage respMessage) {
    	RespBuildUtil.respMessage = respMessage;
    }
	
	public static ResponseDTO success(Object obj) {
		ResponseDTO resp = success();
		resp.setData(obj);
		
		return resp;
	}
	
	public static ResponseDTO success(List<? extends Object> list) {
		ResponseDTO resp = success();
		resp.setDataList(list);
		
		return resp;
	}
	
	/**
	 * 返回多条数据和分页参数
	 * @param list
	 * @param page 页码从1开始
	 * @param pageSize 每页数据数
	 * @param total 数据总数
	 * @return
	 */
	public static ResponseDTO success(List<? extends Object> list, Integer page, Integer pageSize, Integer total) {
		ResponseDTO resp = success();
		resp.setDataList(list);
		resp.setPage(page);
		resp.setPageSize(pageSize);
		
		Integer totalPage = 1;
		if (total > 0) {
			totalPage = (total - 1) / pageSize + 1;
		}
		resp.setTotalPage(totalPage);
		
		return resp;
	}

	public static ResponseDTO success() {
		ResponseDTO resp = new ResponseDTO();
		resp .setReturnCode(ReturnCode.SUCCESS);
		return resp;
	}
	
	public static ResponseDTO error(int returnCode) {
		ResponseDTO resp = new ResponseDTO();
		resp.setReturnCode(returnCode);
		resp.setReturnMsg(respMessage.get(returnCode));
		return resp;
	}
}
