package com.shixin;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class StudentWebTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // 设置 ChromeDriver 的路径
        System.setProperty("webdriver.chrome.driver", "D://UsedToShool//AppTest//chromedriver-win64 (2)//chromedriver-win64//chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        // 打开登录页面
        driver.get("http://127.0.0.1:8088/login/toLogin");

        // 输入用户名
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("121052022170");

        // 输入密码
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("123456");

        // 选择权限
        Select permissionDropdown = new Select(driver.findElement(By.id("permission")));
        permissionDropdown.selectByValue("0");

        // 点击登录按钮
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();

        // 验证登录是否成功（假设登录成功后会跳转到首页）
        String expectedUrl = "http://127.0.0.1:8088/index";
        String actualUrl = driver.getCurrentUrl();
        Assertions.assertEquals(expectedUrl, actualUrl, "登录失败，页面未跳转到首页");


    }
    @Test
    public void testStudentTreeLinks() {
        // 获取所有的tree节点
        List<WebElement> treeNodes = driver.findElements(By.cssSelector("#treeview li"));

        // 遍历节点并验证链接
        for (WebElement treeNode : treeNodes) {
            WebElement link = treeNode.findElement(By.cssSelector("a"));
            String href = link.getAttribute("href");
            String text = link.getText();

            // 根据节点文本判断应该访问的链接
            switch (text) {
                case "个人信息":
                    assertEquals("/stu/infoView", href);
                    break;
                case "修改密码":
                    assertEquals("/stu/updatePwd", href);
                    break;
                case "申请报修":
                    assertEquals("/stu/repairView", href);
                    break;
                case "历史报修记录":
                    assertEquals("/stu/repairHistoryView", href);
                    break;
                case "入校登记":
                    assertEquals("/register/stuInRegisterView", href);
                    break;
                case "离校登记":
                    assertEquals("/register/stuOutRegisterView", href);
                    break;
                case "宿舍评分查看":
                    assertEquals("score/stuScoreView", href);
                    break;
                default:
                    // 如果不是预期的节点文本，可以选择记录日志或跳过
                    System.out.println("Skipping node with text: " + text);
            }

            // 可以选择点击链接并验证新页面，但这里我们只验证链接本身
            // link.click();
            // ... 验证新页面的代码 ...
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit(); // 关闭浏览器和WebDriver实例
    }
}
