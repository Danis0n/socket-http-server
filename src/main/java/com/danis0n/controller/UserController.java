package com.danis0n.controller;

import com.danis0n.radafil.engine.annotation.component.RestController;
import com.danis0n.radafil.engine.annotation.http.method.Delete;
import com.danis0n.radafil.engine.annotation.http.method.Get;
import com.danis0n.radafil.engine.annotation.http.method.Post;
import com.danis0n.radafil.engine.annotation.http.RequestMapping;
import com.danis0n.radafil.engine.annotation.http.input.PathVariable;
import com.danis0n.radafil.engine.exception.exceptions.TestException;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Get(path = "/get/{hello}/{username}")
    public String sayHello(@PathVariable("username") String hello,
                           @PathVariable("hello") String username) {
        System.out.println("Hello from user controller");
        return hello + " " + username;
    }

    @Get(path = "/get/get/{hello}/{username}")
    public String sayGoodBye(@PathVariable("username") String hello,
                           @PathVariable("hello") String username) {
        System.out.println("Hello from user controller");
        return hello + " Bye " + username;
    }

    @Post(path = "/update/{id}/with-username/{username}/showcase")
    public String showId() {
        return "POST";
    }

    @Delete(path = "/{id}")
    public String sayDelete(@PathVariable("id") String id) {
        return "Deleted " + id;
    }

    @Delete()
    public void exception() {
        throw new TestException("Test");
    }


}
