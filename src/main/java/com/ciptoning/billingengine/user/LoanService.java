package com.ciptoning.billingengine.user;

import com.ciptoning.billingengine.BillingConstants;
import com.ciptoning.billingengine.BillingRepository;

public class LoanService {

    private final BillingRepository repository;

    // All LoanService related to billing schedule is here.
    public LoanService(BillingRepository repository) {
        this.repository = repository;
    }

    // Create a fixed Loan, and if there's already a loan won't create new loan until it's paid.
    public void CreateLoan(BillingUser billingUser) {
        if (billingUser.getOutstandingBalance() >= BillingConstants.LOAN_AMOUNT_PAID)
            return;

        billingUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT);
        billingUser.setWeekRemaining(BillingConstants.LOAN_WEEK_DUE);
        repository.save(billingUser);
    }

    // Make fixed payment weekly until it finished paid in full.
    public void MakePayment(BillingUser billingUser) {
        int delinquentFrequency = billingUser.getDelinquentFrequency() != 0 ? billingUser.getDelinquentFrequency() : 1;
        int outstandingBalance = billingUser.getOutstandingBalance();
        int weekRemaining = billingUser.getWeekRemaining();
        if (outstandingBalance < BillingConstants.LOAN_AMOUNT_PAID)
            return;

        billingUser.setOutstandingBalance(outstandingBalance - (BillingConstants.LOAN_AMOUNT_PAID*delinquentFrequency));
        billingUser.setWeekRemaining(weekRemaining-delinquentFrequency);
        billingUser.setDelinquentFrequency(0);
        repository.save(billingUser);
    }

    // For test only to show the delinquency capability.
    public void SkipPayment(BillingUser billingUser) {
        Integer delinquentFrequency = billingUser.getDelinquentFrequency();

        if (delinquentFrequency == 0)
            billingUser.setDelinquentFrequency(1);
        else
            billingUser.setDelinquentFrequency(delinquentFrequency+1);

        repository.save(billingUser);
    }

    // Get Outstanding Balance Value from memory.
    public Integer GetOutstanding(BillingUser billingUser) {
        return billingUser.getOutstandingBalance();
    }

    // Get Remaining Week Value from memory.
    public Integer GetWeekRemaining(BillingUser billingUser) {
        return billingUser.getWeekRemaining();
    }

    // Get Skipped Week (Week not paid) Value from memory.
    public Integer GetSkipWeek(BillingUser billingUser) {
        return billingUser.getDelinquentFrequency();
    }

    // Decide delinquency based on threshold value.
    public boolean IsDelinquent(BillingUser billingUser) {
        if (billingUser.getDelinquentFrequency() != null)
            return billingUser.getDelinquentFrequency() >= BillingConstants.DELINQUENT_THRESHOLD;
        return false;
    }
}
