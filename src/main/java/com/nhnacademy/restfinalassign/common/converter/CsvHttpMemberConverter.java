package com.nhnacademy.restfinalassign.common.converter;

import com.nhnacademy.restfinalassign.model.domain.Member;
import com.nhnacademy.restfinalassign.model.type.Role;
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

/**
 *  text/csv 형식으로 데이터가 들어왔을 때 변환해주는 Member Converter (readInternal)
 *  text/csv 형식으로 데이터를 출력해주는 MemberConverter (writeInternal)
 */
public class CsvHttpMemberConverter extends AbstractHttpMessageConverter<Member> {

    public CsvHttpMemberConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    protected Member readInternal(Class<? extends Member> clazz,
                                  HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputMessage.getBody()))) {
            String line = bufferedReader.readLine();

            String[] tokens = line.split(",");

            String id = tokens[0];
            String name = tokens[1];
            String password = tokens[2];
            Integer age = Integer.valueOf(tokens[3]);
            Role role = Role.valueOf(tokens[4]);

            return new Member(id, name, password, age, role);
        }

    }

    @Override
    protected void writeInternal(Member member, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        StringBuilder sb = new StringBuilder();
        sb.append("id,name,password,age,role").append("\n");

        sb.append(member.getId()).append(",");
        sb.append(member.getName()).append(",");
        sb.append(member.getPassword()).append(",");
        sb.append(member.getAge()).append(",");
        sb.append(member.getRole()).append("\n");

        OutputStream os = outputMessage.getBody();
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();

    }

}
