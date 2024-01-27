package org.swiftboot.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author swiftech
 */
public class SessionTest {

    @Test
    public void json() {
        String json = "{\"userId\":\"swiftboot\"}";

        ObjectMapper om = new ObjectMapper();
        try {
            Session session = om.readValue(json, Session.class);
            Assertions.assertEquals("swiftboot", session.getUserId());
            Assertions.assertNull(session.getUserName());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
