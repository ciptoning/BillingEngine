package com.ciptoning.billingengine.user;

import com.ciptoning.billingengine.BillingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    private final BillingRepository repository;

    public IndexController(BillingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String Index() {
        return "index";
    }

    @PostMapping("/login")
    public String GetOrCreateUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        BillingUser billingUser = repository.findByUsername(username);

        if (billingUser == null) {
            BillingUser bUser = new BillingUser();
            bUser.setUsername(username);
            billingUser = bUser;
            repository.save(billingUser);
        }

        redirectAttributes.addFlashAttribute("username", billingUser.getUsername());
        return "redirect:/user_detail";
    }
}
