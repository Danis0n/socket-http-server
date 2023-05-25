package com.danis0n.radafil.engine.annotation.component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface RestController {
}
