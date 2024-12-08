package com.shixin;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import com.shixin.business.domain.Dorm;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.DormServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
public class DormControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DormServiceI dormServiceI;

    private MockHttpSession session;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
        UserExpand user = new UserExpand();
        Staffinfo staffinfo = new Staffinfo();
        staffinfo.setId("123");
        staffinfo.setStaffname("John Doe");
        staffinfo.setSex(0); // 男
        staffinfo.setAge(30);
        staffinfo.setDormname("Main Dorm");
        staffinfo.setDormno("A1");
        staffinfo.setPhone("1234567890");

        user.setStaffinfo(staffinfo); // 设置Staffinfo对象
        session.setAttribute("LOGIN_USER", user);
    }

    @Test
    public void testGetDormView() throws Exception {
        mockMvc.perform(get("/dorm/view").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dorm/dorm"));
    }

    @Test
    public void testGetDormInfo() throws Exception {
        // 模拟分页数据
        Page<Dorm> dormPage = new PageImpl<>(Arrays.asList(new Dorm(), new Dorm()));
        Mockito.when(dormServiceI.findDorms(any(Pageable.class))).thenReturn(dormPage);

        mockMvc.perform(get("/dorm/list").session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetDorms() throws Exception {
        // 模拟宿舍列表
        List<Dorm> dorms = Arrays.asList(new Dorm(), new Dorm());
        // 使用Mockito.any(Staffinfo.class)来模拟任意Staffinfo对象
        Mockito.when(dormServiceI.getDorms(Mockito.any(Staffinfo.class))).thenReturn(dorms);

        MvcResult result = mockMvc.perform(get("/dorm/getDorms").session(session))
                .andExpect(status().isOk())
                .andReturn();

        // 可以进一步检查返回的数据
        String responseContent = result.getResponse().getContentAsString();
        // 确保返回的数据是预期的宿舍列表
        System.out.println("Response Content: " + responseContent);
    }

    @Test
    public void testImportExcel() throws Exception {
        // 模拟MultipartFile
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(dormServiceI.importExcel(file)).thenReturn(true);

        mockMvc.perform(post("/dorm/importExcel").session(session)
                        .param("file", "test.xlsx"))
                .andExpect(status().isOk());
    }
}