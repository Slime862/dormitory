package com.shixin;

import com.shixin.business.controller.RegisterController;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.RegisterServiceI;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegisterControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private RegisterServiceI registerServiceI;

    @InjectMocks
    private RegisterController registerController;

    private HttpSession session;
    @BeforeEach
    public void login() {
        // 模拟登录并获取会话 ID
        Response response = given()
                .param("username", "121052022170")
                .param("password", "123456")  // 使用正确的用户名和密码
                .when()
                .post("/login/toLogin")  // 假设登录接口为 /login
                .then()
                // 假设登录成功返回 200 状态码
                .extract().response();
        // 输出响应的内容，检查是否返回了 HTML（而不是数据）
        String responseBody = response.getBody().asString();
        System.out.println("Login Response Body: " + responseBody);

        // 检查登录是否成功
        if (responseBody.contains("登录失败")) {
            throw new IllegalStateException("Login failed, response contains login page.");
        }
        // 获取登录后的 session ID（假设会话 ID 存储在 cookies 中）
        sessionId = response.cookie("JSESSIONID");
    }
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // JUnit 4 version of openMocks
        RestAssured.port = port;
        RestAssured.basePath = "/register";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Simulate a session for tests
        session = mock(HttpSession.class);
        when(session.getAttribute("LOGIN_USER")).thenReturn(new UserExpand());  // Mock user session data
    }

    @Test
    public void testGetInRegisterView() {
        // Simulate a GET request to /inRegisterView
        RestAssured.given()
                .sessionId(String.valueOf(session))
                .when()
                .get("/inRegisterView")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("register/inRegisterView"));
    }

    @Test
    public void testIsCreateRegister() {
        // Simulate a GET request to /isCreateRegister
        when(registerServiceI.isCreateRegister()).thenReturn(true);

        RestAssured.given()
                .sessionId(String.valueOf(session))
                .when()
                .get("/isCreateRegister")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("true"));
    }

    @Test
    public void testCreateRegister() {
        // Simulate a POST request to /createRegister
        String name = "Test Name";
        //when(registerServiceI.createRegister(name, null)).thenReturn(true);  // Mock behavior

        RestAssured.given()
                .sessionId(String.valueOf(session))
                .contentType(ContentType.URLENC)
                .formParam("name", name)
                .when()
                .post("/createRegister")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("true"));
    }

    @Test
    public void testGetRegisterNames() {
        // Simulate a GET request to /getRegisterNames
        //when(registerServiceI.getRegisterNames(null, "in")).thenReturn(List(new Registerbatch()));

        RestAssured.given()
                .sessionId(String.valueOf(session))
                .when()
                .get("/getRegisterNames")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThan(0));  // Check that the response is a non-empty list
    }

    @Test
    public void testInRegisterList() {
        // Simulate a GET request to /inRegisterList with pagination
        Pageable pageable = Pageable.unpaged();
        //when(registerServiceI.inRegisterList(pageable, null)).thenReturn(new PageImpl<>(List.of(new InRegisterExtend())));

        RestAssured.given()
                .sessionId(String.valueOf(session))
                .when()
                .get("/inRegisterList")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThan(0));
    }

    @Test
    public void testCreateInHistory() {
        // Simulate a GET request to /createInHistory
        when(registerServiceI.createInHistory(null)).thenReturn(true);

        RestAssured.given()
                .sessionId(String.valueOf(session))
                .when()
                .get("/createInHistory")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("true"));
    }

    @Test
    public void testHistoryList() {
        // Simulate a GET request to /inHistoryList with a registerBatchId
        String registerBatchId = "12345";
        Pageable pageable = Pageable.unpaged();
        //when(registerServiceI.historyList(pageable, null, registerBatchId)).thenReturn(new PageImpl<>(List.of(new InRegisterExtend())));

        RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .queryParam("registerBatchId", registerBatchId)
                .when()
                .get("/inHistoryList")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThan(0));
    }

    @Test
    public void testInExportExcel() {
        // Simulate a GET request to /inExportExcel
        //List<ExportInRegisterEntity> list = List.of(new ExportInRegisterEntity());
       // when(registerServiceI.findInHistoryList(null)).thenReturn(list);

        RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .get("/inExportExcel")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testGetInRegisterViewWithModel() {
        // Simulate a GET request to /stuInRegisterView
        //when(registerServiceI.getInRegisterId(null)).thenReturn(new InRegister());

        RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .get("/stuInRegisterView")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("inRegisterId", notNullValue());  // Check if the response has the inRegisterId attribute set
    }

    @Test
    public void testSaveStuRegister() {
        // Simulate a POST request to /saveStuRegister
        Date arrivetime = new Date();
        String inRegisterId = "12345";
        //when(registerServiceI.saveStuRegister(arrivetime, inRegisterId)).thenReturn(true);

        RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.URLENC)
                .formParam("arrivetime", arrivetime.toString())
                .formParam("inRegisterId", inRegisterId)
                .when()
                .post("/saveStuRegister")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("true"));
    }

    @Test
    public void testIsStuSaveRegister() {
        // Simulate a GET request to /isStuSaveRegister
        when(registerServiceI.isStuSaveRegister(null)).thenReturn(true);

        RestAssured.given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .get("/isStuSaveRegister")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("true"));
    }
}
