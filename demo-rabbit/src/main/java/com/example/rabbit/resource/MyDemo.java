package com.example.rabbit.resource;

import com.example.rabbit.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author xhd
 * @date 2017/12/14
 */
@Component
@Singleton
@Path("/demo")
public class MyDemo {
    @Autowired
    private MyService myService;

    @GET
    @Path("/test1")
    public void test1(){
        myService.test1();
    }

    public static void main(String[] args) {
        for(int i = 0 ; i < 20 ; i ++ ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getForObject("http://localhost:8080/api/demo/test1", String.class, "");
                }
            }).start();

        }
    }

}
