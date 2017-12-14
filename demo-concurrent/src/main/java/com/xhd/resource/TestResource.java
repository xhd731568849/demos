package com.xhd.resource;

import com.xhd.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by administrator on 2017/12/14.
 */
@Component
@Singleton
@Path("/test")
public class TestResource {
    @Autowired
    private TestService testService;

    @GET
    @Path("/test1")
    public void test1() throws InterruptedException {
        testService.test1();
    }

    @GET
    @Path("/test2")
    public void test2() throws InterruptedException {
        testService.test2();
    }

    public static void main(String[] args) {
        for(int i = 0 ; i < 1000 ; i ++ ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getForObject("http://localhost:8080/api/test/test1", String.class, "");
                }
            }).start();

        }
    }

}
