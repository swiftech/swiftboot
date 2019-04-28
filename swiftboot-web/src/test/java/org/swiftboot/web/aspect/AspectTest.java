package org.swiftboot.web.aspect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * Test Aspect in SpringBoot-test
 *
 * @author Allen 2019-04-19
 **/
@ExtendWith(SpringExtension.class)
//@DataJpaTest
@Import(AspectTestConfig.class)
public class AspectTest {

    @Resource
    AspectTarget aspectTarget;

    @Test
    public void testIt() {
        aspectTarget.testAspect();
    }
}
