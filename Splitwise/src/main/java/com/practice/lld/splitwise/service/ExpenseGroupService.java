package com.practice.lld.splitwise.service;

import com.practice.lld.splitwise.exception.ExpenseNotFoundException;
import com.practice.lld.splitwise.exception.UserNotFoundException;
import com.practice.lld.splitwise.model.Expense;
import com.practice.lld.splitwise.model.ExpenseGroup;
import com.practice.lld.splitwise.model.User;
import com.practice.lld.splitwise.repository.ExpenseRepository;
import com.practice.lld.splitwise.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ExpenseGroupService {

    private ExpenseService expenseService = ExpenseService.getInstance();
    private ExpenseRepository expenseRepository = ExpenseRepository.getInstance();
    private UserRepository userRepository = UserRepository.getInstance();

    public ExpenseGroup createExpenseGroup(String createdBy, String expenseName) {
        return ExpenseGroup.builder()
                .createdBy(createdBy)
                .createdOn(new Date())
                .name(expenseName)
                .Id(UUID.randomUUID().toString())
                .expenseIds(new ArrayList<>())
                .build();
    }

    public void displayAllUnsettledExpensesForGroup(ExpenseGroup group) throws ExpenseNotFoundException {
        if (group.getExpenseIds().isEmpty()) {
            log.info("No balances");
            return;
        }

        Map<String, Double> totalDue = resolvePayments(group);

        for (Map.Entry<String, Double> entry : totalDue.entrySet()) {
            String[] split = entry.getKey().split("\\|");
            log.info("{} owes {}: {}", split[0], split[1], entry.getValue());
        }

        log.debug("SHOWALL ENDED ------------------- ");
    }

    private Map<String, Double> resolvePayments(ExpenseGroup group) throws ExpenseNotFoundException {
        Map<String, Double> totalDue = new HashMap<>();
        for (String expenseId : group.getExpenseIds()) {
            Expense expense = expenseRepository.getExpenseForId(expenseId);

            Map<String, Double> currentDueAmounts = expenseService.getCurrentDueAmounts(expenseId);
            for (Map.Entry<String, Double> entry : currentDueAmounts.entrySet()) {
                if (expense.getCreatedById().equals(entry.getKey()))
                    continue;

                // Means entry.getKey() owes money to expense.getCreatedById()
                String hashKey = entry.getKey() + "|" + expense.getCreatedById();

                if (totalDue.containsKey(hashKey)) {
                    totalDue.put(hashKey, entry.getValue() + totalDue.get(hashKey));
                } else {
                    // Before putting it, we need to check for reverse owing as well
                    String revHashKey = expense.getCreatedById() + "|" + entry.getKey();
                    if (totalDue.containsKey(revHashKey)) {
                        Double revAmount = totalDue.get(revHashKey);
                        Double curr = entry.getValue();

                        if (revAmount > curr) {
                            totalDue.put(revHashKey, revAmount - curr);
                        } else if (revAmount < curr) {
                            totalDue.remove(revHashKey);
                            totalDue.put(hashKey, curr - revAmount);
                        } else {
                            totalDue.remove(revHashKey);
                        }
                    } else {
                        totalDue.put(hashKey, entry.getValue());
                    }
                }
            }
        }

        return totalDue;
    }

    public void displayAllUnsettledExpensesForUser(ExpenseGroup group, String userId) throws ExpenseNotFoundException, UserNotFoundException {
        if (group.getExpenseIds().isEmpty()) {
            log.info("No balances");
            return;
        }

        User user = userRepository.getUserForId(userId);

        Map<String, Double> totalDue = resolvePayments(group);

        for (Map.Entry<String, Double> entry : totalDue.entrySet()) {
            String[] strings = entry.getKey().split("\\|");
            if (strings[0].equals(userId) || strings[1].equals(userId))
                log.info("{} owes {}: {}", strings[0], strings[1], entry.getValue());
        }

        log.debug("SHOW USER ENDED ------------------- ");
    }

    private static ExpenseGroupService instance;

    private ExpenseGroupService() {

    }

    public static ExpenseGroupService getInstance() {
        if (instance == null)
            instance = new ExpenseGroupService();
        return instance;
    }
}
