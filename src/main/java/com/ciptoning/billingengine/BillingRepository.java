package com.ciptoning.billingengine;

import com.ciptoning.billingengine.user.BillingUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<BillingUser, Long>{
    BillingUser findByUsername(String username);
}
