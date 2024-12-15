package com.shixin;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // 设置 ChromeDriver 的路径
        System.setProperty("webdriver.chrome.driver", "D://UsedToShool//AppTest//chromedriver-win64 (2)//chromedriver-win64//chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
    }

    @Test
    public void testLogin() throws InterruptedException {

        driver.get("http://127.0.0.1:8088/login/toLogin");


        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("121052022170");


        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("123456");
        Thread.sleep(4000);

        Select permissionDropdown = new Select(driver.findElement(By.id("permission")));
        permissionDropdown.selectByValue("0");
        Thread.sleep(4000);

        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();
        Thread.sleep(4000);

        String expectedUrl = "http://127.0.0.1:8088/index";
        String actualUrl = driver.getCurrentUrl();
        Assertions.assertEquals(expectedUrl, actualUrl, "登录失败，页面未跳转到首页");
    }

    @AfterEach
    public void tearDown() {
        // 关闭浏览器
        if (driver != null) {
            driver.quit();
        }
    }
}