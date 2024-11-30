package com.shixin;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DormControllerTest {

    private static String sessionId;  // 存储登录后的 sessionId

    @BeforeAll
    public static void setup() {
        // 设置基本 URI
        baseURI = "http://localhost:8088";
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

    // 1. 当数据库中有宿舍数据时，调用 GET /dorms 接口，验证返回的宿舍数据列表不为空
    @Test
    public void testGetDormsWithData() {
        // 假设数据库中已插入宿舍数据，调用 GET /dorms 接口
        given()
                .cookie("JSESSIONID", sessionId)  // 使用登录后的 sessionId
                .when()
                .get("/dorm/getDorms")
                .then()
                // 假设成功返回 200
                .statusCode(200)
                // 验证返回的宿舍数据列表不为空
                .body("list.size()", greaterThan(0))
                // 验证每个宿舍的名称字段不为空
                .body("list.name", everyItem(notNullValue()))
                // 验证每个宿舍的容量大于 0
                .body("list.capacity", everyItem(greaterThan(0)))
                // 验证每个宿舍的入住人数不小于 0
                .body("list.occupied", everyItem(greaterThanOrEqualTo(0)));
    }


    // 2. 数据库中没有宿舍数据时，调用 GET /dorms 接口，验证返回的宿舍数据列表为空
    @Test
    public void testGetDormsNoData() {
        // 假设数据库中没有宿舍数据，调用 GET /dorms 接口

        given()
                .cookie("JSESSIONID", sessionId)  // 使用登录后的 sessionId
                .when()
                .get("/dorms")
                .then()
                .statusCode(200)  // 假设成功返回 200
                .body("size()", equalTo(0)); // 验证返回的宿舍数据列表为空
    }

    // 3. DormServiceI 返回空数据时，调用 GET /dorms 接口，验证返回的宿舍数据列表为空
    @Test
    public void testGetDormsEmptyData() {
        // 假设 DormServiceI 返回空数据，调用 GET /dorms 接口
        given()
                .cookie("JSESSIONID", sessionId)  // 使用登录后的 sessionId
                .when()
                .get("/dorms")
                .then()
                .statusCode(200)  // 假设成功返回 200
                .body("data.size()", equalTo(0));  // 验证返回的宿舍数据列表为空
    }


    // 4. 宿舍数据已插入数据库时，调用 GET /dorms 接口，验证返回的宿舍数据字段正确
    @Test
    public void testGetDormsWithValidFields() {
        // 假设数据库中已插入有效宿舍数据，调用 GET /dorms 接口
        given()
                .cookie("JSESSIONID", sessionId)  // 使用登录后的 sessionId
                .when()
                .get("/dorms")
                .then()
                .statusCode(200)  // 假设成功返回 200
                .body("data.size()", greaterThan(0))  // 验证返回的宿舍数据列表不为空
                .body("data.name", everyItem(notNullValue()))  // 验证每个宿舍的名称字段不为空
                .body("data.capacity", everyItem(greaterThan(0)))  // 验证每个宿舍的容量大于 0
                .body("data.occupied", everyItem(greaterThanOrEqualTo(0)))  // 验证每个宿舍的入住人数不小于 0
                .body("data.address", everyItem(notNullValue()));  // 验证每个宿舍的地址字段不为空
    }

    // 5. 数据库发生异常（如数据库连接失败等）时，调用 GET /dorms 接口，验证返回错误信息
    @Test
    
    public void testGetDormsDatabaseError() {
        // 假设数据库发生异常，模拟数据库连接失败，调用 GET /dorms 接口
        given()
                .cookie("JSESSIONID", sessionId)  // 使用登录后的 sessionId
                .when()
                .get("/dorms")
                .then()
                .statusCode(500)  // 假设数据库错误会返回 500 状态码
                .body("message", equalTo("Database error"));  // 验证返回的错误信息
    }

}

