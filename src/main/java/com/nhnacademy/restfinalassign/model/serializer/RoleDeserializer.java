package com.nhnacademy.restfinalassign.model.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nhnacademy.restfinalassign.model.type.Role;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {

    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();

        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }

        // 기본값 주기 원하면 이렇게:
        return Role.MEMBER;
    }

}
