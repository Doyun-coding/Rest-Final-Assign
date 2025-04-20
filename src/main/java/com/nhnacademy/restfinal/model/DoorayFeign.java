package com.nhnacademy.restfinal.model;

import com.nhnacademy.restfinal.model.request.DoorayRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "doorayFeign", url = "https://hook.dooray.com/services")
public interface DoorayFeign {

    @PostMapping("/{serviceId}/{botId}/{botToken}")
    String sendMessage(@RequestBody DoorayRequest doorayRequest,
                       @PathVariable String serviceId, @PathVariable String botId, @PathVariable String botToken);

}