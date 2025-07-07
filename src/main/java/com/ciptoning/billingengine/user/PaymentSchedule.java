package com.ciptoning.billingengine.user;

@SuppressWarnings("unused")
public class PaymentSchedule {
    String weeklyLabel;
    int weeklyPayment;

    // Create a List for webview.
    public PaymentSchedule(String weeklyLabel, int weeklyPayment) {
        this.weeklyLabel = weeklyLabel;
        this.weeklyPayment = weeklyPayment;
    }

    public String getWeeklyLabel() {
        return weeklyLabel;
    }

    public int getWeeklyPayment() {
        return weeklyPayment;
    }
}
