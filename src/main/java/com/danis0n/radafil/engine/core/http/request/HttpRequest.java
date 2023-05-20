package com.danis0n.radafil.engine.core.http.request;

import com.danis0n.radafil.engine.core.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HttpRequest {
    private String urn;
    private String url;
    private String keyPrefix;
    private String content;
    private HttpMethod httpMethod;
}
