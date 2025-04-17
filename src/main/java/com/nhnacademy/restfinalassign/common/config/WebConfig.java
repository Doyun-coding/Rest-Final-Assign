package com.nhnacademy.restfinalassign.common.config;

import com.nhnacademy.restfinalassign.common.converter.csv.CsvHttpMemberConverter;
import com.nhnacademy.restfinalassign.common.converter.csv.CsvHttpMemberRequestConverter;
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
