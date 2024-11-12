package com.example.TheShop.database.deserializer;

import com.example.TheShop.database.models.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {

    @Override
    public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // Extract the ID from the node (assuming it's a plain number)
        int customerId = node.asInt();

        // Create a User object using just the ID
        User user = new User();
        user.setId((long) customerId);
        return user;
    }
}
