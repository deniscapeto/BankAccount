package com.cockroachlabs.application;

import com.cockroachlabs.domain.entities.Account;
import com.cockroachlabs.infraestructure.cockroachdb.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllOperationsUseCase {

    private AccountRepository accountRepository;

    public AllOperationsUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(){

        accountRepository.testRetryHandling();

        var account1 = new Account(1000);
        var account2 = new Account(250);

        int insertedAccounts = accountRepository.insertAccount(account1);
        insertedAccounts += accountRepository.insertAccount(account2);
        System.out.printf("AccountRepository.insertAccount:\n    => %s total inserted accounts\n", insertedAccounts);

        BigDecimal balance1 = accountRepository.getAccountBalance(account1.getId());
        BigDecimal balance2 = accountRepository.getAccountBalance(account2.getId());
        System.out.printf("main:\n    => Account balances at time '%s':\n    ID %s => $%s\n    ID %s => $%s\n", LocalTime.now(), 1, balance1, 2, balance2);


        // Transfer $100 from account 1 to account 2
        UUID fromAccount = UUID.randomUUID();
        UUID toAccount = UUID.randomUUID();
        BigDecimal transferAmount = BigDecimal.valueOf(100);
        int transferredAccounts = accountRepository.transferFunds(fromAccount, toAccount, transferAmount);
        if (transferredAccounts != -1) {
            System.out.printf("BasicExampleDAO.transferFunds:\n    => $%s transferred between accounts %s and %s, %s rows updated\n", transferAmount, fromAccount, toAccount, transferredAccounts);
        }

        balance1 = accountRepository.getAccountBalance(account1.getId());
        balance2 = accountRepository.getAccountBalance(account2.getId());
        System.out.printf("main:\n    => Account balances at time '%s':\n    ID %s => $%s\n    ID %s => $%s\n", LocalTime.now(), 1, balance1, 2, balance2);


        // Bulk insertion example using JDBC's batching support.
        int totalRowsInserted = accountRepository.bulkInsertRandomAccountData();
        System.out.printf("\nBasicExampleDAO.bulkInsertRandomAccountData:\n    => finished, %s total rows inserted\n", totalRowsInserted);

        // Print out 10 account values.
        int accountsRead = accountRepository.readAccounts(10);
    }
}
