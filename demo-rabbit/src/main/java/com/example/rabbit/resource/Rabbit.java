package com.example.rabbit.resource;

import com.example.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * @author tanzhibo
 * @date 17-12-13 下午3:03
 */
@Component
@Singleton
@Path("/rabbit")
public class Rabbit {
    @Autowired
    private RabbitService rabbitService;

    @GET
    public void send(@QueryParam("exchange") String exchange, @QueryParam("msg") String msg) {
        rabbitService.send(exchange, msg);
    }

    @GET
    @Path("addFanoutExchange/{name}")
    public void add(@PathParam("name") String name) {
        rabbitService.addFanoutExchange(name);
    }

    @GET
    @Path("delExchange/{name}")
    public void del(@PathParam("name") String name) {
        rabbitService.delExchange(name);
    }

    @GET
    @Path("addQueue/{name}")
    public void addQueue(@PathParam("name") String name) {
        rabbitService.addQueue(name);
    }

    @GET
    @Path("delQueue/{name}")
    public void delQueue(@PathParam("name") String name) {
        rabbitService.delQueue(name);
    }

    @GET
    @Path("bind")
    public void bind(@QueryParam("queue") String queue, @QueryParam("fanoutExchange") String fanoutExchange) {
        rabbitService.bind(queue, fanoutExchange);
    }

    @GET
    @Path("removeBinding")
    public void removeBinding(@QueryParam("queue") String queue, @QueryParam("fanoutExchange") String fanoutExchange) {
        rabbitService.removeBinding(queue, fanoutExchange);
    }
}
