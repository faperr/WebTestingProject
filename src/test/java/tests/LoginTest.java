package tests;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void successfulLoginTest() {

        driver.get("https://the-internet.herokuapp.com/login");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("username")
                )
        ).sendKeys("tomsmith");

        driver.findElement(
                By.id("password")
        ).sendKeys("SuperSecretPassword!");

        driver.findElement(
                By.cssSelector("button.radius")
        ).click();

        String message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("flash")
                )
        ).getText();

        Assert.assertTrue(
                message.contains("You logged into a secure area!"),
                "Ошибка: успешный вход не выполнен"
        );

        Assert.assertTrue(
                driver.findElement(
                        By.cssSelector("a.button.secondary.radius")
                ).isDisplayed(),
                "Ошибка: кнопка Logout не отображается"
        );
    }

    @Test
    public void invalidPasswordTest() {

        driver.get("https://the-internet.herokuapp.com/login");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("username")
                )
        ).sendKeys("tomsmith");

        driver.findElement(
                By.id("password")
        ).sendKeys("123456");

        driver.findElement(
                By.cssSelector("button.radius")
        ).click();

        String message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("flash")
                )
        ).getText();

        Assert.assertTrue(
                message.contains("Your password is invalid!"),
                "Ошибка: сообщение о неверном пароле не появилось"
        );
    }
}