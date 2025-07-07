package com.ciptoning.billingengine;

import com.ciptoning.billingengine.user.BillingUser;
import org.springframework.data.jpa.repository.JpaRepository;

// Memory Storage using JPA and extending BillingUser
public interface BillingRepository extends JpaRepository<BillingUser, Long>{

    // Custom query to select based on Username.
    BillingUser findByUsername(String username);
}
