package com.shixin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ScoreControllerTest {

    @BeforeAll
    public static void setup() {
        // 设置基本的 API 地址
        baseURI = "http://localhost:8088"; // 根据实际情况修改
    }

    @Test
    public void testGetScoresWithValidStudentId() {
        // 假设数据库中已插入该学生成绩数据，调用 GET /scores/{studentId} 接口
        String studentId = "12345"; // 假设这是一个有效的 studentId

        given()
                .when()
                .get("/scores/{studentId}", studentId)
                .then()
                .statusCode(200) // 期望返回状态码 200
                .body("studentId", equalTo(studentId)) // 验证返回的成绩数据包含正确的 studentId
                .body("score", greaterThanOrEqualTo(0)) // 验证成绩不小于 0
                .body("subject", notNullValue()); // 验证返回的成绩数据包含科目字段
    }

    @Test
    public void testGetScoresWithInvalidStudentId() {
        // 假设数据库中无该学生成绩数据，调用 GET /scores/{studentId} 接口
        String studentId = "99999"; // 假设这是一个无效的 studentId

        given()
                .when()
                .get("/scores/{studentId}", studentId)
                .then()
                .statusCode(404) // 期望返回状态码 404
                .body("message", equalTo("Score data not found")); // 假设接口返回了 "Score data not found"
    }

    @Test
    public void testGetScoresWithEmptyData() {
        // 假设 ScoreServiceI 返回空数据，调用 GET /scores/{studentId} 接口
        String studentId = "12345"; // 假设这是一个有效的 studentId

        given()
                .when()
                .get("/scores/{studentId}", studentId)
                .then()
                .statusCode(200) // 期望返回状态码 200
                .body("score", equalTo(0)) // 验证返回的数据为空，成绩为 0
                .body("subject", nullValue()); // 验证返回的科目为空
    }

    @Test
    public void testGetScoresWithValidData() {
        // 假设 ScoreServiceI 正常工作且成绩数据正确，调用 GET /scores/{studentId} 接口
        String studentId = "12345"; // 假设这是一个有效的 studentId

        given()
                .when()
                .get("/scores/{studentId}", studentId)
                .then()
                .statusCode(200) // 期望返回状态码 200
                .body("studentId", equalTo(studentId)) // 验证返回的成绩数据包含正确的 studentId
                .body("score", greaterThan(0)) // 验证成绩大于 0
                .body("subject", equalTo("Mathematics")); // 验证科目正确
    }

    @Test
    public void testGetScoresDatabaseError() {
        // 假设数据库连接发生异常，模拟 500 错误
        String studentId = "12345"; // 假设这是一个有效的 studentId

        given()
                .when()
                .get("/scores/{studentId}", studentId)
                .then()
                .statusCode(500) // 期望返回状态码 500
                .body("message", equalTo("Database connection error")); // 假设错误信息为 "Database connection error"
    }
}

