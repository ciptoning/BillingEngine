package com.ciptoning.billingengine;

import com.ciptoning.billingengine.user.BillingUser;
import com.ciptoning.billingengine.user.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private BillingRepository repository;

    private LoanService loanService;
    private BillingUser testUser;

    @BeforeEach
    void SetUp() {
        loanService = new LoanService(repository);
        testUser = new BillingUser();
    }

    @Test
    void CreateLoanWhenBalanceBelowThresholdCreatesNewLoan() {
        // Arrange
        testUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT_PAID - 1);

        // Act
        loanService.CreateLoan(testUser);

        // Assert
        assertEquals(BillingConstants.LOAN_AMOUNT, testUser.getOutstandingBalance());
        assertEquals(BillingConstants.LOAN_WEEK_DUE, testUser.getWeekRemaining());
        verify(repository).save(testUser);
    }

    @Test
    void CreateLoanWhenBalanceAboveThresholdDoesNothing() {
        // Arrange
        testUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT_PAID);

        // Act
        loanService.CreateLoan(testUser);

        // Assert
        assertEquals(BillingConstants.LOAN_AMOUNT_PAID, testUser.getOutstandingBalance());
        verify(repository, never()).save(testUser);
    }

    @Test
    void MakePaymentWithSufficientBalanceProcessesPayment() {
        // Arrange
        testUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT_PAID);
        testUser.setWeekRemaining(5);
        testUser.setDelinquentFrequency(1);

        // Act
        loanService.MakePayment(testUser);

        // Assert
        assertEquals(0, testUser.getOutstandingBalance());
        assertEquals(4, testUser.getWeekRemaining());
        assertEquals(0, testUser.getDelinquentFrequency());
        verify(repository).save(testUser);
    }

    @Test
    void MakePaymentWithMultipleDelinquenciesProcessesCorrectPayment() {
        // Arrange
        testUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT_PAID * 3);
        testUser.setWeekRemaining(5);
        testUser.setDelinquentFrequency(2);

        // Act
        loanService.MakePayment(testUser);

        // Assert
        assertEquals(BillingConstants.LOAN_AMOUNT_PAID, testUser.getOutstandingBalance());
        assertEquals(3, testUser.getWeekRemaining());
        assertEquals(0, testUser.getDelinquentFrequency());
        verify(repository).save(testUser);
    }

    @Test
    void MakePaymentWithInsufficientBalanceDoesNothing() {
        // Arrange
        testUser.setOutstandingBalance(BillingConstants.LOAN_AMOUNT_PAID - 1);

        // Act
        loanService.MakePayment(testUser);

        // Assert
        assertEquals(BillingConstants.LOAN_AMOUNT_PAID - 1, testUser.getOutstandingBalance());
        verify(repository, never()).save(testUser);
    }

    @Test
    void SkipPaymentFirstTimeSetsDelinquencyToOne() {
        // Arrange
        testUser.setDelinquentFrequency(0);

        // Act
        loanService.SkipPayment(testUser);

        // Assert
        assertEquals(1, testUser.getDelinquentFrequency());
        verify(repository).save(testUser);
    }

    @Test
    void SkipPaymentSubsequentTimesIncrementsDelinquency() {
        // Arrange
        testUser.setDelinquentFrequency(2);

        // Act
        loanService.SkipPayment(testUser);

        // Assert
        assertEquals(3, testUser.getDelinquentFrequency());
        verify(repository).save(testUser);
    }

    @Test
    void GetOutstandingReturnsCorrectValue() {
        // Arrange
        testUser.setOutstandingBalance(500000);

        // Act & Assert
        assertEquals(500000, loanService.GetOutstanding(testUser));
    }

    @Test
    void GetWeekRemainingReturnsCorrectValue() {
        // Arrange
        testUser.setWeekRemaining(3);

        // Act & Assert
        assertEquals(3, loanService.GetWeekRemaining(testUser));
    }

    @Test
    void GetSkipWeekReturnsCorrectValue() {
        // Arrange
        testUser.setDelinquentFrequency(2);

        // Act & Assert
        assertEquals(2, loanService.GetSkipWeek(testUser));
    }

    @Test
    void IsDelinquentWhenAboveThresholdReturnsTrue() {
        // Arrange
        testUser.setDelinquentFrequency(BillingConstants.DELINQUENT_THRESHOLD);

        // Act & Assert
        assertTrue(loanService.IsDelinquent(testUser));
    }

    @Test
    void IsDelinquentWhenBelowThresholdReturnsFalse() {
        // Arrange
        testUser.setDelinquentFrequency(BillingConstants.DELINQUENT_THRESHOLD - 1);

        // Act & Assert
        assertFalse(loanService.IsDelinquent(testUser));
    }

    @Test
    void IsDelinquentWhenNullFrequencyReturnsFalse() {
        // Arrange
        testUser.setDelinquentFrequency(null);

        // Act & Assert
        assertFalse(loanService.IsDelinquent(testUser));
    }
}