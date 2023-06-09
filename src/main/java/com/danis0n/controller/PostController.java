package com.danis0n.controller;

import com.danis0n.dto.TestRequest;
import com.danis0n.radafil.engine.annotation.component.RestController;
import com.danis0n.radafil.engine.annotation.http.method.Get;
import com.danis0n.radafil.engine.annotation.http.RequestMapping;

@RestController
@RequestMapping(path = "/api/v1/post")
public class PostController {

    @Get
    public TestRequest sayHello() {
        System.out.println("Hello from post controller");
        return new TestRequest("name", "password");
    }

}
