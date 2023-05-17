package com.danis0n.controller;

import com.danis0n.radafil.engine.annotation.http.RestController;
import com.danis0n.radafil.engine.annotation.http.method.Delete;
import com.danis0n.radafil.engine.annotation.http.method.Get;
import com.danis0n.radafil.engine.annotation.http.method.RequestMapping;
import com.danis0n.radafil.engine.annotation.http.path.PathVariable;

@RestController()
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Get()
    public String sayHello() {
        System.out.println("Hello from user controller");
        return "Hello";
    }

    @Get(path = "{id}")
    public String showId(@PathVariable("id") String id) {
        return id;
    }

    @Delete(path = "{id}")
    public String sayDelete(@PathVariable("id") String id) {
        return "Deleted " + id;
    }


}
