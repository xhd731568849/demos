package com.xhd.demo.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author xhd
 * @date 2017/12/15
 */
@Service
public class DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoService.class);

    public void test(){
        logger.info(Thread.currentThread().getName() + "正在运行......");
        System.out.println("test");
    }
}
