package ru.netology.sql.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.*;
import ru.netology.sql.page.*;

import static ru.netology.sql.data.SQLHelper.cleanDatabase;
import static com.codeborne.selenide.Selenide.*;

public class LoginTest {


    @AfterAll
    static void tearDown () {
        cleanDatabase();
    }


    @Test
    public void shouldBeSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        //verificationPage.VerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    public void shouldShowErrorIfRandomUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.getError();
    }

    @Test
    public void shouldShowErrorIfInvalidLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo(DataHelper.getRandomUser().getLogin(), DataHelper.getAuthInfoWithTestData().getPassword());
        loginPage.validLogin(authInfo);
        loginPage.getError();
    }

    @Test
    public void shouldShowErrorIfInvalidPassword() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfo);
        loginPage.getError();
    }

    @Test
    public void shouldShowErrorIfInvalidVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.VerificationPage();
        var verificationCode = DataHelper.getVerificationCode().getCode();
        //verificationPage.validVerify(verificationCode);
        verificationPage.verify(verificationCode);
        verificationPage.getError();
    }

    @Test
    public void shouldBlockLoginIfInvalidPasswordThreeTimesInRow() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfoFirstTry = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfoFirstTry);
        loginPage.getError();
        loginPage.cleanFields();
        var authInfoSecondTry = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfoSecondTry);
        loginPage.getError();
        loginPage.cleanFields();
        var authInfoThirdTry = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfoThirdTry);
        loginPage.getBlockError(); //здесь должно появляться сообщение о том, что 3 раза введен неверный пароль
    }

}
