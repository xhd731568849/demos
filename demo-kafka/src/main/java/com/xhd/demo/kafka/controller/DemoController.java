package com.xhd.demo.kafka.controller;

import com.xhd.demo.kafka.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xhd
 * @date 2017/12/15
 */
@RestController
@RequestMapping("/test")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @RequestMapping("/test1")
    public void test(){
        demoService.test();
    }
}
