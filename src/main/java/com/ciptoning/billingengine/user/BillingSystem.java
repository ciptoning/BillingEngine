package com.ciptoning.billingengine.user;

import com.ciptoning.billingengine.BillingConstants;
import com.ciptoning.billingengine.BillingRepository;

public class BillingSystem {

    static class Loan {
        private final BillingRepository repository;

        Loan(BillingRepository repository) {
            this.repository = repository;
        }

        public void CreateLoan(BillingUser billingUser) {
            if (billingUser.getOutstandingBalance() >= BillingConstants.LOAN_AMOUNT_PAID)
                return;

            billingUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT);
            billingUser.setWeekRemaining(BillingConstants.LOAN_WEEK_DUE);
            repository.save(billingUser);
        }

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

        public void SkipPayment(BillingUser billingUser) {
            Integer delinquentFrequency = billingUser.getDelinquentFrequency();

            if (delinquentFrequency == 0)
                billingUser.setDelinquentFrequency(1);
            else
                billingUser.setDelinquentFrequency(delinquentFrequency+1);

            repository.save(billingUser);
        }

        public Integer GetOutstanding(BillingUser billingUser) {
            return billingUser.getOutstandingBalance();
        }

        public Integer GetWeekRemaining(BillingUser billingUser) {
            return billingUser.getWeekRemaining();
        }

        public Integer GetSkipWeek(BillingUser billingUser) {
            return billingUser.getDelinquentFrequency();
        }

        public boolean IsDelinquent(BillingUser billingUser) {
            if (billingUser.getDelinquentFrequency() != null)
                return billingUser.getDelinquentFrequency() >= BillingConstants.DELINQUENT_THRESHOLD;
            return false;
        }
    }
}
