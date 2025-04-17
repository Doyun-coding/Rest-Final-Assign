package com.nhnacademy.restfinalassign.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 *
 *  Jackson 용 직렬화기를 만드는 코드
 *  지금 이 코드는 Jackson 직렬화(-> JSON 변환) 시 Integer 타입을 숫자가 아니라 문자열로 출력하도록 커스텀 Serializer를 구현한 것이다
 *
 *  ex) "count": 123 -> "count": "123"
 *
 */
@JsonComponent
public class JacksonConfig {

    public static class IntegerSerializer extends StdSerializer<Integer> {
        private static final long serialVersionUID = -7524016618355224119L;

        public IntegerSerializer() {
            super(Integer.class);
        }

        @Override
        public void serialize(Integer integer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(integer.toString());
        }
    }

}
