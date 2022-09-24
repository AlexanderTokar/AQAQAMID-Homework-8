package ru.netology.sql.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement code = $("[data-test-id='code'] input");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");
    private SelenideElement error = $("data-test-id=error-notification");

    public void VerificationPage() {
        code.shouldBe(Condition.visible);
    }

    public void getError() {
        error.shouldBe(Condition.visible, Duration.ofSeconds(5));
    }

    public void verify(String verificationCode) {
        code.setValue(verificationCode);
        verifyButton.click();
    }

    public DashboardPage validVerify(String verificationCode) {
        verify(verificationCode);
        return new DashboardPage();
    }
}