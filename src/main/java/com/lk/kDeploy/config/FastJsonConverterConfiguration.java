package com.lk.kDeploy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 让springBoot支持fastjson序列化返回数据
 * @author lk
 *
 */
@Configuration
@ConditionalOnClass({JSON.class})
public class FastJsonConverterConfiguration {

    @Configuration
    @ConditionalOnClass({FastJsonHttpMessageConverter.class})
    @ConditionalOnProperty(
            name = {"spring.http.converters.preferred-json-mapper"},
            havingValue = "fastjson", 
            matchIfMissing = true
    )
    protected static class FastJson2HttpMessageConverterConfiguration {
        protected FastJson2HttpMessageConverterConfiguration() {}

        @Bean
        @ConditionalOnMissingBean({FastJsonHttpMessageConverter.class})
        public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
            return new FastJsonHttpMessageConverter();

//            FastJsonConfig fastJsonConfig = new FastJsonConfig();
//            ValueFilter valueFilter = new ValueFilter() {
//            	@Override
//                public Object process(Object object, String name, Object value) {
//                    if (null == value) return "";
//                    return value;
//                }
//            };
//            fastJsonConfig.setSerializeFilters(valueFilter);
//
//            converter.setFastJsonConfig(fastJsonConfig);
//
//            return converter;
        }
    }
}
