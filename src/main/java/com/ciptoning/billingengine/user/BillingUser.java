package com.ciptoning.billingengine.user;

import jakarta.persistence.*;

@Entity
@SuppressWarnings("unused")
public class BillingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Integer outstandingBalance = 0;
    private Integer weekRemaining = 0;
    private Integer delinquentFrequency = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Integer outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Integer getWeekRemaining() {
        return weekRemaining;
    }

    public void setWeekRemaining(Integer weekRemaining) {
        this.weekRemaining = weekRemaining;
    }

    public Integer getDelinquentFrequency() {
        return delinquentFrequency;
    }

    public void setDelinquentFrequency(Integer delinquentFrequency) {
        this.delinquentFrequency = delinquentFrequency;
    }
}
