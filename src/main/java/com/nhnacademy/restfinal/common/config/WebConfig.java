package com.nhnacademy.restfinal.common.config;

import com.nhnacademy.restfinal.common.converter.csv.CsvHttpMemberConverter;
import com.nhnacademy.restfinal.common.converter.csv.CsvHttpMemberRequestConverter;
import com.nhnacademy.restfinal.model.DoorayFeign;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new CsvHttpMemberConverter());
        converters.add(new CsvHttpMemberRequestConverter());
    }

}
