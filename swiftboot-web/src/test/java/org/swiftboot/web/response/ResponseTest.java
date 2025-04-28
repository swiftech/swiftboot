package org.swiftboot.web.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.web.dto.ParentDto;

/**
 *
 */
public class ResponseTest {

    @Test
    public void test() {
        Response<ParentDto> msg = Response.builder(ParentDto.class).code("9999").message("msg").build();
        Assertions.assertEquals("9999", msg.getCode());
        Assertions.assertEquals("msg", msg.getMessage());
    }
}
