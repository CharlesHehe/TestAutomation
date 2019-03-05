package com.service365.test;

import com.service365.common.PropertiesUtils;
import com.service365.customerPage.*;
import com.service365.readData.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;


public class TestCustomer {
    public Properties properties;
    WebDriver webDriver;
    HomePage homePage;
    RegisterPage registerPage;
    LoginPage loginPage;
    MePage mePage;
    EditProfilePage editProfilePage;
    ChangePasswordPage changePasswordPage;
    EditAddressPage editAddressPage;
    MyAddressPage myAddressPage;

    @BeforeTest(groups = "basic")
    public void setup() {
        properties = PropertiesUtils.loadProp("config.properties");
        System.setProperty("webdriver.chrome.driver", "browser/chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        mePage = new MePage(webDriver);
        webDriver.get(properties.getProperty("homePageURL"));
        homePage = new HomePage(webDriver);
    }


    @Test(priority = 1, groups = "register")
    public void testRegisterPage() throws InterruptedException {
        homePage.clickRegister();
        registerPage = new RegisterPage(webDriver);
        registerPage.registerService365("3109272895@qq.com","123456","123456",true,true);
        Thread.sleep(5000);
//        测试是否进入home页面
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("mePageURL"));
//        测试头像是否为空
//        Assert.assertEquals(mePage.imageCheck(), "width: 120px; height: 120px; background-image: url(\"/assets/img/default_user.png\");");
//        测试bio
//        Assert.assertEquals(mePage.bioCheck(), "Bio");
//        测试contact number
//        Assert.assertEquals(mePage.contactNumberCheck(), "");
//        测试总评价
//        Assert.assertEquals(mePage.overallRatingCheck(), "no rating yet");
//        测试add new address
//        Assert.assertEquals(mePage.addNewAddressCheck(), properties.getProperty("editNewAddressURL"));
//        测试edit profile 页面显示
//        Assert.assertEquals(mePage.editProfileOnCheck(), properties.getProperty("editProfileURL"));

    }

    @Test(priority = 1, groups = "login")
    public void testLoginPage() throws InterruptedException {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com","123456");
        Thread.sleep(5000);
//        测试是否进入mePage页面
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("mePageURL"));

    }


    @Test(priority = 2, groups = "changePicture")
    public void testChangePicture() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com","123456");
        String currentPicture = mePage.imageCheck();
        mePage.changePicture();
        String laterPicture = mePage.imageCheck();
        Assert.assertNotEquals(laterPicture, currentPicture);
    }

    @Test(priority = 3, groups = "editProfile")
    public void testEditProfile() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com","123456");
        editProfilePage = new EditProfilePage(webDriver);
        mePage.clickEditProfile();
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("editProfileURL"));
        editProfilePage.editProfile("chenjuner","9999999","Male","I am pretty!");
        Assert.assertEquals(mePage.nickNameCheck(),"chenjuner");
        Assert.assertEquals(mePage.contactNumberCheck(),"9999999");
        Assert.assertEquals(mePage.instructionCheck(),"I am pretty!");
    }

    @Test(priority = 4, groups = "changePassword", dataProvider = "dataProvider")
    public void testChangePassword(HashMap<String, String> data) {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com","123456");
        changePasswordPage = new ChangePasswordPage(webDriver);
        mePage.clickChangePassword();
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("changePasswordURL"));
        changePasswordPage.changePassword(data.get("Current password"),data.get("New password"),data.get("Confirm password"));
//        changePasswordPage.changePassword("123456q","123456","123456");
        webDriver.get(properties.getProperty("loginPageURL"));
        loginPage.loginToService365("hechenjuner@gmail.com",data.get("New password"));
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("mePageURL"));

    }

    @Test(priority = 5, groups = "addAddress")
    public void testAddAddress() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com","123456");
        mePage.clickAddress();
        myAddressPage=new MyAddressPage(webDriver);
        myAddressPage.clickAddNewButton();
        editAddressPage = new EditAddressPage(webDriver);
        editAddressPage.addNewAddress("1q","1w","1e","1r","1t","1y");
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("myAddressURL"));
        myAddressPage.newAddressCheck("1q","1w","1e","1r","1t","1y");

    }


    @DataProvider(name = "dataProvider")
    public Object[][] getDataFromDataprovider() {
        ReadExcelFile readExcelFile = new ReadExcelFile();
        String filePath = "data";
        Object[][] o = null;
        try {
            o = readExcelFile.readExcel(filePath, "asdf.xlsx", "asdf");
        } catch (IOException e) {

        }
        return o;

    }

    @Test(priority = 7, groups = "findPassword")
    public void testFindPassword() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.clickRecoverPassword();
        List listA;

        listA = webDriver.findElements(By.tagName("a"));
        Reporter.log("There are " + listA.size() + " links in this page!");
        Iterator iteratorA = listA.iterator();
        while (iteratorA.hasNext()) {
            WebElement tagA = (WebElement) iteratorA.next();
            String url = tagA.getAttribute("href");
            if (url == null || url.equals(null) || url.equals("")) {
                Reporter.log("current url is \"\"");
            } else {
                Reporter.log("current url " + url);
            }


        }

    }

    @AfterTest()
    public void quit() {
        webDriver.quit();
    }
}
