package com.ciptoning.billingengine.user;

import com.ciptoning.billingengine.BillingConstants;
import com.ciptoning.billingengine.BillingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserDetailController {

    private final BillingRepository repository;
    public LoanService loanService;

    // Constructor for LoanService and Memory Storage (repository).
    public UserDetailController(BillingRepository repository) {
        this.repository = repository;
        this.loanService = new LoanService(this.repository);
    }

    @GetMapping("/user_detail")
    public String UserDetail(Model model) {
        String username = (String) model.getAttribute("username");
        BillingUser billingUser = repository.findByUsername(username);
        if (billingUser == null) {
            return "redirect:/";
        }

        boolean delinquency = loanService.IsDelinquent(billingUser);

        if (billingUser.getOutstandingBalance() != 0) {
            int outstandingBalance = loanService.GetOutstanding(billingUser);
            int weekRemaining = loanService.GetWeekRemaining(billingUser);
            int skipWeek = loanService.GetSkipWeek(billingUser);
            int startPayment = (BillingConstants.LOAN_WEEK_DUE - weekRemaining) + 1;
            List<PaymentSchedule> paymentSchedules = new ArrayList<>();

            for (int i = startPayment; i <= BillingConstants.LOAN_WEEK_DUE; i++) {
                paymentSchedules.add(new PaymentSchedule("W"+i, BillingConstants.LOAN_AMOUNT_PAID));
            }

            model.addAttribute("paid_loan", BillingConstants.LOAN_WEEK_DUE - weekRemaining);
            model.addAttribute("skipped_week", skipWeek);
            model.addAttribute("outstanding_balance", outstandingBalance);
            model.addAttribute("payment_schedules", paymentSchedules);
        }

        model.addAttribute("is_delinquent", delinquency);

        return "user_detail";
    }

    @PostMapping("/logout")
    public String Logout() {
        return "index";
    }

    @PostMapping("/create_loan")
    public String CreateLoanRequest(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        BillingUser billingUser = repository.findByUsername(username);

        if (billingUser != null) {
            loanService.CreateLoan(billingUser);
        }

        redirectAttributes.addFlashAttribute("username", username);

        return "redirect:/user_detail";
    }

    @PostMapping("/make_payment")
    public String MakePaymentRequest(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        BillingUser billingUser = repository.findByUsername(username);

        if (billingUser != null) {
            loanService.MakePayment(billingUser);
        }

        redirectAttributes.addFlashAttribute("username", username);

        return "redirect:/user_detail";
    }

    @PostMapping("/skip_payment")
    public String SkipPaymentRequest(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        BillingUser billingUser = repository.findByUsername(username);

        if (billingUser != null) {
            loanService.SkipPayment(billingUser);
        }

        redirectAttributes.addFlashAttribute("username", username);

        return "redirect:/user_detail";
    }
}
