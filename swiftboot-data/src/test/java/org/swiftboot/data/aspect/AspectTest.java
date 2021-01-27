package org.swiftboot.data.aspect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * Test Aspect in SpringBoot-test
 *
 * @author swiftech
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(AspectTestConfig.class)
public class AspectTest {

    @Resource
    AspectTarget aspectTarget;

    @Test
    public void testIt() {
        aspectTarget.testAspect();
    }
}
