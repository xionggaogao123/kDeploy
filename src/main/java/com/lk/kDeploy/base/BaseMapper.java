package com.lk.kDeploy.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 特别注意，该接口不能被当做普通Mapper一样被扫描到，否则会出错
 *
 * @author: lk
 * @since: 2017年11月7日
 * @param <T>
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
