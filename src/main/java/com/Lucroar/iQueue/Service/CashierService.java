package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Cashier;
import com.Lucroar.iQueue.Repository.CashierRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CashierService {
    private final CashierRepository cashierRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public CashierService(CashierRepository cashierRepository, BCryptPasswordEncoder passwordEncoder) {
        this.cashierRepository = cashierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cashier newCashier(Cashier cashier){
        cashier.setPassword(passwordEncoder.encode(cashier.getPassword()));
        return cashierRepository.save(cashier);
    }

    public Optional<Cashier> findCashier(String id){
        return cashierRepository.findById(id);
    }
}
