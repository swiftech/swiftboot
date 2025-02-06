package org.swiftboot.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.annotation.Resource;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author swiftech
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SwiftBootShiroTestApplication.class)
@AutoConfigureMockMvc
@Import(SwiftbootShiroTestConfig.class)
public class SwiftbootShiroTest {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private UserPermissionDao userPermissionDao;

    @Resource
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        // Inject security manager manually because of some reason that I don't know.
        SecurityUtils.setSecurityManager(applicationContext.getBean(SecurityManager.class));

        // prepare user permission
        UserPermissionEntity permAdmin = new UserPermissionEntity();
        permAdmin.setId("1001"); // auto generate id doesn't work for test TODO
        permAdmin.setLoginName(TestConstants.adminUser);
        permAdmin.setPermCode("admin");

        UserPermissionEntity permStaff = new UserPermissionEntity();
        permStaff.setId("1002"); // auto generate id doesn't work for test TODO
        permStaff.setLoginName(TestConstants.staffUser);
        permStaff.setPermCode("staff");
        userPermissionDao.saveAll(Arrays.asList(permAdmin, permStaff));
    }

    @Test
    public void testNotAuthenticated() throws Exception {
        mvc.perform(get("/unit-test/greeting").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        System.out.println("done");
    }

    @Test
    public void testAdmin() throws Exception {
        MvcResult signinResult = mvc.perform(get("/unit-test/signin")
                .param("loginName", TestConstants.adminUser)
                .param("loginPwd", TestConstants.adminPassword))
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(get("/unit-test/admin_only")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(signinResult.getResponse().getCookies())
        )
                .andExpect(status().isOk())
                .andExpect(content().string("hello, admin"));

        mvc.perform(get("/unit-test/staff_only")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(signinResult.getResponse().getCookies())
        )
                .andExpect(status().isUnauthorized());
        System.out.println("done");
    }

    @Test
    public void testStaff() throws Exception {
        MvcResult signinResult = mvc.perform(get("/unit-test/signin")
                .param("loginName", TestConstants.staffUser)
                .param("loginPwd", TestConstants.staffPassword))
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(get("/unit-test/staff_only")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(signinResult.getResponse().getCookies())
        )
                .andExpect(status().isOk())
                .andExpect(content().string("hello, staff"));

        mvc.perform(get("/unit-test/admin_only")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(signinResult.getResponse().getCookies())
        )
                .andExpect(status().isUnauthorized());
        System.out.println("done");
    }

}
