package com.danis0n.controller;

import com.danis0n.dto.TestRequest;
import com.danis0n.radafil.engine.annotation.http.RestController;
import com.danis0n.radafil.engine.annotation.http.method.Delete;
import com.danis0n.radafil.engine.annotation.http.method.Get;
import com.danis0n.radafil.engine.annotation.http.method.Post;
import com.danis0n.radafil.engine.annotation.http.method.RequestMapping;
import com.danis0n.radafil.engine.annotation.http.input.Body;
import com.danis0n.radafil.engine.annotation.http.input.PathVariable;

@RestController()
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Get()
    public String sayHello() {
        System.out.println("Hello from user controller");
        return "Hello";
    }

    @Post()
    public TestRequest showId(@Body TestRequest request) {
        return request;
    }

    @Delete(path = "{id}")
    public String sayDelete(@PathVariable("id") String id) {
        return "Deleted " + id;
    }


}
