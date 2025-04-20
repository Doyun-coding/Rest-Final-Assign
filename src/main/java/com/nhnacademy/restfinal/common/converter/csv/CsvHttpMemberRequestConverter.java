package com.nhnacademy.restfinal.common.converter.csv;

import com.nhnacademy.restfinal.model.request.MemberRequest;
import com.nhnacademy.restfinal.model.type.Role;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CsvHttpMemberRequestConverter extends AbstractHttpMessageConverter<MemberRequest> {

    public CsvHttpMemberRequestConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return MemberRequest.class.equals(clazz);
    }

    @Override
    protected MemberRequest readInternal(Class<? extends MemberRequest> clazz,
                                         HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputMessage.getBody()))) {
            String line = bufferedReader.readLine();

            String[] tokens = line.split(",");

            String id = tokens[0];
            String name = tokens[1];
            String password = tokens[2];
            Integer age = Integer.parseInt(tokens[3]);
            Role role = Role.valueOf(tokens[4]);

            return new MemberRequest(id, name, password, age, role);
        }

    }

    @Override
    protected void writeInternal(MemberRequest memberRequest, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        StringBuilder sb = new StringBuilder();
        sb.append("id,name,password,age,role").append("\n");

        sb.append(memberRequest.getId()).append(",");
        sb.append(memberRequest.getName()).append(",");
        sb.append(memberRequest.getPassword()).append(",");
        sb.append(memberRequest.getAge()).append(",");
        sb.append(memberRequest.getRole()).append("\n");

        OutputStream os = outputMessage.getBody();
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();

    }

}