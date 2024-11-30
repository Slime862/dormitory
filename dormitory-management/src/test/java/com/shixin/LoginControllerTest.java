package com.shixin;

import com.shixin.business.controller.LoginController;
import com.shixin.business.domain.User;
import com.shixin.business.domain.UserExpand;
import com.shixin.business.service.LoginServiceI;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private HttpSession session;

    @Mock
    private LoginServiceI loginServiceI;

    @InjectMocks
    private LoginController loginController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // Initialize mocks
        RestAssured.port = port;  // Set the port to match the application
        RestAssured.basePath = "/login";  // Set base path for all tests
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
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

    @Test
    public void testLoginSuccess() {
        // Prepare mock user data
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        UserExpand userExpand = new UserExpand();  // Simulating a valid user login response

        // Mock the service call to return the expected user info
        when(loginServiceI.getUserLoginInfo(user)).thenReturn(userExpand);

        given()
                .contentType(ContentType.URLENC)
                .formParam("username", "testUser")
                .formParam("password", "testPassword")
                .cookie("JSESSIONID", sessionId)
                .when()
                .post("")  // Login URL mapped in your controller
                .then()
                .statusCode(HttpStatus.FOUND.value())  // 302 Redirect
                .header("Location", "/index");  // Ensure the redirect is to /index

        // Verify that the session has the "LOGIN_USER" attribute set
        verify(session, times(1)).setAttribute("LOGIN_USER", userExpand);
    }

    @Test
    public void testLoginFailure() {
        // Prepare mock user data
        User user = new User();
        user.setUsername("wrongUser");
        user.setPassword("wrongPassword");

        // Mock the service to return null (login failed)
        when(loginServiceI.getUserLoginInfo(user)).thenReturn(null);

        given()
                .contentType(ContentType.URLENC)
                .formParam("username", "wrongUser")
                .formParam("password", "wrongPassword")
                .cookie("JSESSIONID", sessionId)
                .when()
                .post("")  // Login URL mapped in your controller
                .then()
                .statusCode(HttpStatus.OK.value())  // Should return the login page
                .body("msg", equalTo("error"));  // Check if error message is set in model
    }

    @Test
    public void testLogout() {
        // Simulate a logout request
        given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .get("/logout")  // Logout URL mapped in your controller
                .then()
                .statusCode(HttpStatus.OK.value())  // Should return a 200 OK response
                .body(equalTo("true"));  // The logout endpoint should return "true"

        // Verify that the session attribute "LOGIN_USER" was removed
        verify(session, times(1)).removeAttribute("LOGIN_USER");
    }
}

