package com.danis0n.radafil.engine.annotation.http;

import com.danis0n.radafil.engine.annotation.singleton.Singleton;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Singleton
@Inherited
public @interface RestController {
}
