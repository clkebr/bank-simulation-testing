package com.banksimulation.repository;

import com.banksimulation.entity.Account;
import com.banksimulation.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByAccountStatus(AccountStatus active);

}
