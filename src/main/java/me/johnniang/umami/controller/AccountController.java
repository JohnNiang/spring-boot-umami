package me.johnniang.umami.controller;

import me.johnniang.umami.entity.Account;
import me.johnniang.umami.model.AccountRequest;
import me.johnniang.umami.repository.AccountRepository;
import me.johnniang.umami.security.SecurityContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Account controller.
 *
 * @author johnniang
 */
@RestController
@RequestMapping("/api")
class AccountController {

    private final AccountRepository accountRepository;

    AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/accounts")
    List<Account> getAllAccounts(@SortDefault(sort = "id") Sort sort) {
        ensureAdminAccess();
        return accountRepository.findAll(sort);
    }

    @PostMapping("/account")
    Account updateOrCreateAccount(@RequestBody @Valid AccountRequest accountRequest) {
        // get current login account
        if (accountRequest.getUserId() == null) {
            ensureAdminAccess();
            // create account
            // validate password
            if (StringUtils.hasText(accountRequest.getPassword())) {
                throw new IllegalArgumentException("Account password must be present.");
            }
            // validate username
            if (accountRepository.existsByUsername(accountRequest.getUsername())) {
                throw new IllegalArgumentException("Account already exists.");
            }
            // build account
            Account account = new Account();
            account.setUsername(accountRequest.getUsername());
            account.setPassword(accountRequest.getPassword());
            account.setAdmin(false);
            accountRepository.save(account);
            return account;
        } else {
            Account currentAccount = SecurityContextHolder.getContext().getAccount();
            // update account
            boolean isAdmin = currentAccount.isAdmin();
            if (!currentAccount.getId().equals(accountRequest.getUserId()) && !isAdmin) {
                throw new IllegalStateException("Permission denied.");
            }
            // update account
            if (isAdmin && currentAccount.getId().equals(accountRequest.getUserId())) {
                // if admin want to update himself/herself
                throw new IllegalArgumentException("Do not try to update adminself.");
            }
            // update username
            Account updatingAccount = accountRepository.getById(accountRequest.getUserId());
            if (StringUtils.hasText(accountRequest.getUsername()) && !accountRequest.getUsername().equals(updatingAccount.getUsername())) {
                // validate username
                if (accountRepository.existsByUsername(accountRequest.getUsername())) {
                    throw new IllegalArgumentException("Account already exists.");
                }
            }
            // update account
            updatingAccount.setUsername(accountRequest.getUsername());
            return accountRepository.save(updatingAccount);
        }
    }

    @GetMapping("/account/{accountId}")
    Account getAccount(@PathVariable("accountId") Integer accountId) {
        ensureAdminAccess();
        return accountRepository.getById(accountId);
    }

    @DeleteMapping("/account/{accountId}")
    Account deleteAccount(@PathVariable("accountId") Integer accountId) {
        ensureAdminAccess();
        Account account = accountRepository.getById(accountId);
        accountRepository.delete(account);
        return account;
    }

    void ensureAdminAccess() {
        // ensure that current account role is admin
        Account currentAccount = SecurityContextHolder.getContext().getAccount();
        if (!currentAccount.isAdmin()) {
            throw new IllegalStateException("Permission denied.");
        }
    }
}
