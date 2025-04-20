package com.nhnacademy.restfinal.model.request;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class DoorayRequest {

    String botName;
    String text;

}
