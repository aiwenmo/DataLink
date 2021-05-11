package com.datalink.log.config;

import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * TtlMDCAdapterInitializer
 * 初始化TtlMDCAdapter实例，并替换MDC中的adapter对象
 *
 * @author wenmo
 * @since 2021/5/11
 */
public class TtlMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //加载TtlMDCAdapter实例
        TtlMDCAdapter.getInstance();
    }
}