package com.ciptoning.billingengine;

import com.ciptoning.billingengine.user.BillingUser;
import com.ciptoning.billingengine.user.UserDetailController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailControllerTest {

    @Mock
    private BillingRepository repository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Spy
    @InjectMocks
    private UserDetailController controller;

    private BillingUser testUser;

    @BeforeEach
    void SetUp() {
        testUser = new BillingUser();
        testUser.setUsername("testUser");
        testUser.setOutstandingBalance(1000000);

        controller = new UserDetailController(repository);
        controller.loanService = spy(controller.loanService);
    }

    @Test
    void UserDetailWithValidUserReturnsCorrectView() {
        // Arrange
        when(model.getAttribute("username")).thenReturn("testUser");
        when(repository.findByUsername("testUser")).thenReturn(testUser);
        when(controller.loanService.IsDelinquent(testUser)).thenReturn(false);
        when(controller.loanService.GetOutstanding(testUser)).thenReturn(500000);
        when(controller.loanService.GetWeekRemaining(testUser)).thenReturn(2);
        when(controller.loanService.GetSkipWeek(testUser)).thenReturn(1);

        // Act
        String viewName = controller.UserDetail(model);

        // Assert
        assertEquals("user_detail", viewName);
        verify(model).addAttribute("is_delinquent", false);
        verify(model).addAttribute("paid_loan", BillingConstants.LOAN_WEEK_DUE - 2);
        verify(model).addAttribute("skipped_week", 1);
        verify(model).addAttribute("outstanding_balance", 500000);
        verify(model).addAttribute(eq("payment_schedules"), any(List.class));
    }

    @Test
    void UserDetailWithNullUsernameRedirectsToIndex() {
        // Arrange
        when(model.getAttribute("username")).thenReturn(null);

        // Act
        String viewName = controller.UserDetail(model);

        // Assert
        assertEquals("redirect:/", viewName);
    }

    @Test
    void UserDetailUserNotFoundRedirectsToIndex() {
        // Arrange
        when(model.getAttribute("username")).thenReturn("unknownUser");
        when(repository.findByUsername("unknownUser")).thenReturn(null);

        // Act
        String viewName = controller.UserDetail(model);

        // Assert
        assertEquals("redirect:/", viewName);
    }

    @Test
    void CreateLoanValidUserCreatesLoanAndRedirects() {
        // Arrange
        when(repository.findByUsername("testUser")).thenReturn(testUser);

        // Act
        String result = controller.CreateLoanRequest("testUser", redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(controller.loanService).CreateLoan(testUser);
        verify(redirectAttributes).addFlashAttribute("username", "testUser");
    }

    @Test
    void CreateLoanUserNotFoundRedirectsWithoutCreating() {
        // Arrange
        when(repository.findByUsername("unknownUser")).thenReturn(null);

        // Act
        String result = controller.CreateLoanRequest("unknownUser", redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(controller.loanService, never()).CreateLoan(any());
        verify(redirectAttributes).addFlashAttribute("username", "unknownUser");
    }

    @Test
    void MakePaymentValidUserProcessesPayment() {
        // Arrange
        when(repository.findByUsername("testUser")).thenReturn(testUser);

        // Act
        String result = controller.MakePaymentRequest("testUser", redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(controller.loanService).MakePayment(testUser);
        verify(redirectAttributes).addFlashAttribute("username", "testUser");
    }

    @Test
    void SkipPaymentValidUserSkipsPayment() {
        // Arrange
        when(repository.findByUsername("testUser")).thenReturn(testUser);

        // Act
        String result = controller.SkipPaymentRequest("testUser", redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(controller.loanService).SkipPayment(testUser);
        verify(redirectAttributes).addFlashAttribute("username", "testUser");
    }

    @Test
    void LogoutReturnsToIndex() {
        // Act
        String result = controller.Logout();

        // Assert
        assertEquals("index", result);
    }
}