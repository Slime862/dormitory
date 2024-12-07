package com.shixin;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.domain.UserExpand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DormControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetDormView() throws Exception {
        mockMvc.perform(get("/dorm/view"))
                .andExpect(status().isOk())
                .andExpect(view().name("dorm/dorm"));
    }

    @Test
    public void testGetDormInfo() throws Exception {
        mockMvc.perform(get("/dorm/list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    public void testGetDorms() throws Exception {
        MockHttpSession session = new MockHttpSession();

        // 创建一个 Staffinfo 对象
        Staffinfo staffinfo = new Staffinfo();
        staffinfo.setId("staff123");
        staffinfo.setStaffname("John Doe");
        staffinfo.setSex(0);
        staffinfo.setAge(30);
        staffinfo.setDormname("Student Dorm");
        staffinfo.setDormno("A1");
        staffinfo.setPhone("1234567890");

        // 创建 UserExpand 对象并设置 Staffinfo
        UserExpand user = new UserExpand();
        if (user != null) {
            user.setStaffinfo(staffinfo);
        } else {
            throw new NullPointerException("UserExpand 对象为 null");
        }

        // 设置会话属性
        if (session != null) {
            session.setAttribute("LOGIN_USER", user);
        } else {
            throw new NullPointerException("MockHttpSession 对象为 null");
        }

        mockMvc.perform(get("/dorm/getDorms")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testImportExcel() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "test data".getBytes()
        );

        mockMvc.perform(multipart("/dorm/importExcel")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}