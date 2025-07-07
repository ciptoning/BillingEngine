package com.ciptoning.billingengine;

import com.ciptoning.billingengine.user.BillingUser;
import com.ciptoning.billingengine.user.IndexController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    private BillingRepository repository;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private IndexController indexController;

    // Happy Path - Existing user
    @Test
    void LoginExistingUserRedirectsWithUsername() {
        // Arrange
        String username = "existingUser";
        BillingUser mockUser = new BillingUser();
        mockUser.setUsername(username);

        when(repository.findByUsername(username)).thenReturn(mockUser);

        // Act
        String result = indexController.GetOrCreateUser(username, redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(repository, never()).save(any());
        verify(redirectAttributes).addFlashAttribute("username", username);
    }

    // Negative Path - New user creation
    @Test
    void LoginNewUserCreatesAndRedirects() {
        // Arrange
        String username = "newUser";
        when(repository.findByUsername(username)).thenReturn(null);
        when(repository.save(any(BillingUser.class))).thenAnswer(invocation -> {
            BillingUser user = invocation.getArgument(0);
            user.setUsername(username);
            return user;
        });

        // Act
        String result = indexController.GetOrCreateUser(username, redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(repository).save(argThat(user -> username.equals(user.getUsername())));
        verify(redirectAttributes).addFlashAttribute("username", username);
    }

    // Edge Case - Empty username
    @Test
    void LoginEmptyUsernameThrowsException() {
        // Test empty string
        assertThrows(IllegalArgumentException.class, () ->
            indexController.GetOrCreateUser("", redirectAttributes)
        );

        // Test whitespace-only string
        assertThrows(IllegalArgumentException.class, () ->
            indexController.GetOrCreateUser("   ", redirectAttributes)
        );

        // Test null
        assertThrows(IllegalArgumentException.class, () ->
            indexController.GetOrCreateUser(null, redirectAttributes)
        );

        verifyNoInteractions(repository);
        verifyNoInteractions(redirectAttributes);
    }

    // Edge Case - Very long username
    @Test
    void LoginVeryLongUsernameHandlesCorrectly() {
        // Arrange
        String longUsername = "a".repeat(500);
        BillingUser mockUser = new BillingUser();
        mockUser.setUsername(longUsername);

        when(repository.findByUsername(longUsername)).thenReturn(mockUser);

        // Act
        String result = indexController.GetOrCreateUser(longUsername, redirectAttributes);

        // Assert
        assertEquals("redirect:/user_detail", result);
        verify(repository, never()).save(any());
        verify(redirectAttributes).addFlashAttribute("username", longUsername);
    }
}