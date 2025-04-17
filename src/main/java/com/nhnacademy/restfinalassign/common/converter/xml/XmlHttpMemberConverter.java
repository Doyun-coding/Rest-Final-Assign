package com.nhnacademy.restfinalassign.common.converter.xml;

import com.nhnacademy.restfinalassign.model.domain.Member;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class XmlHttpMemberConverter extends AbstractHttpMessageConverter<Member> {

    public XmlHttpMemberConverter() {
        super(new MediaType("application", "xml"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    protected Member readInternal(Class<? extends Member> clazz,
                                  HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        

        return null;
    }

    @Override
    protected void writeInternal(Member member, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
