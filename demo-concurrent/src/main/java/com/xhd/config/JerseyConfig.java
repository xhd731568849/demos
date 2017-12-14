package com.xhd.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * @author tanzhibo
 * @date 2017/12/13 14:55.
 */
@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        packages("com.xhd.resource");
    }
}
