package com.shixin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.shixin.business.domain.External;
import com.shixin.business.domain.Staffinfo;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.ExternalServiceI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ExternalServiceI externalServiceI;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetExternalView() throws Exception {
        mockMvc.perform(get("/externalView"))
                .andExpect(status().isOk())
                .andExpect(view().name("external/externalView"));
    }

    @Test
    public void testSaveExternal() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserExpand userExpand = new UserExpand();
        userExpand.setStaffinfo(new Staffinfo());
        session.setAttribute("LOGIN_USER", userExpand);

        External external = new External();
        // 模拟 void 方法
        Mockito.doNothing().when(externalServiceI).saveExternal(external, userExpand.getStaffinfo());

        mockMvc.perform(post("/saveExternal")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Test External\", \"description\": \"Test Description\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boolean").value(true));
    }

    @Test
    public void testExternalList() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserExpand userExpand = new UserExpand();
        userExpand.setStaffinfo(new Staffinfo());
        session.setAttribute("LOGIN_USER", userExpand);


// 创建一个Pageable对象，表示分页请求，这里假设我们请求第0页，每页10个元素
        Pageable pageable = PageRequest.of(0, 10);

// 创建一个PageImpl对象，它实现了Page接口
        List<External> content = Arrays.asList(new External(), new External());
        Page<External> page = new PageImpl<>(content, pageable, content.size());
        when(externalServiceI.externalList(any(), any())).thenReturn(page);

        mockMvc.perform(get("/externalList")
                        .session(session)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").exists());
    }

    @Test
    public void testOverVisit() throws Exception {
        Mockito.doNothing().when(externalServiceI).overVisit("123");

        mockMvc.perform(get("/overVisit")
                        .param("id", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boolean").value(true));
    }

    @Test
    public void testGetHistoryView() throws Exception {
        mockMvc.perform(get("/historyView"))
                .andExpect(status().isOk())
                .andExpect(view().name("external/historyView"));
    }

    @Test
    public void testExternalHistoryList() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserExpand userExpand = new UserExpand();
        userExpand.setStaffinfo(new Staffinfo());
        session.setAttribute("LOGIN_USER", userExpand);

        Pageable pageable = PageRequest.of(0, 10);

// 创建一个PageImpl对象，它实现了Page接口
        List<External> content = Arrays.asList(new External(), new External());
        Page<External> page = new PageImpl<>(content, pageable, content.size());
        when(externalServiceI.externalHistoryList(any(), any(), anyString(), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(get("/externalHistoryList")
                        .session(session)
                        .param("page", "0")
                        .param("size", "10")
                        .param("stuname", "test")
                        .param("startTime", "2023-01-01")
                        .param("endTime", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content[0].name").exists());
    }

    @Test
    public void testExportHistoryExcel() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserExpand userExpand = new UserExpand();
        userExpand.setStaffinfo(new Staffinfo());
        session.setAttribute("LOGIN_USER", userExpand);

        List<External> list = Arrays.asList(new External(), new External());
        when(externalServiceI.findHistoryList(userExpand.getStaffinfo())).thenReturn(list);

        MvcResult result = mockMvc.perform(get("/exportHistoryExcel")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=历史来访登记表.xlsx"))
                .andReturn();

        // 这里可以添加更多的验证，例如检查返回的文件内容
    }
}