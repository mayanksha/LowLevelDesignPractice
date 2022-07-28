package com.practice.lld.splitwise.service;

import com.practice.lld.splitwise.exception.DuplicateExpenseException;
import com.practice.lld.splitwise.exception.ExpenseNotFoundException;
import com.practice.lld.splitwise.model.Contribution;
import com.practice.lld.splitwise.model.Expense;
import com.practice.lld.splitwise.model.ExpenseStatus;
import com.practice.lld.splitwise.model.User;
import com.practice.lld.splitwise.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ExpenseService {
    private ExpenseRepository expenseRepo = ExpenseRepository.getInstance();

    public String createExpense(String createdBy, Double amount, String description) throws DuplicateExpenseException {
        Expense expense = Expense.builder()
                .createdOn(new Date())
                .createdById(createdBy)
                .status(ExpenseStatus.CREATED)
                .amount(amount)
                .description(description)
                .sharedBy(new HashMap<>())
                .contriHistory(new ArrayList<>())
                .build();

        log.debug("Created expense. createdBy: {}, id: {}, sharedBy: {}", expense.getCreatedById(), expense.getId(), expense.getSharedBy());
        expenseRepo.insertExpenseForId(expense.getId(), expense);
        return expense.getId();
    }

    public void addUsersAndShareAmount(String expenseId, List<User> userList, String strategy, List<Double> strategyInput) throws ExpenseNotFoundException {
        Expense expense = expenseRepo.getExpenseForId(expenseId);
        validateStrategy(expense.getAmount(), strategy, strategyInput);

        assert userList.size() == strategyInput.size();
        Double amount = expense.getAmount();
        Map<String, Double> userShareMap = new HashMap<>();

        for (int i = 0; i < userList.size(); i++) {
            Double currAmount;
            if (Objects.equals(strategy, "EQUAL")) {
                currAmount = amount / (userList.size());
            } else if (Objects.equals(strategy, "EXACT")) {
                currAmount = strategyInput.get(i);
            } else if (Objects.equals(strategy, "PERCENT")) {
                currAmount = (amount * strategyInput.get(i)) / 100.0;
            } else {
                throw new RuntimeException("Strategy is not defined!");
            }

            userShareMap.put(userList.get(i).getId(), currAmount);
        }

        if (userShareMap.containsKey(expense.getCreatedById())) {
            userShareMap.remove(expense.getCreatedById());
        }

        expense.getSharedBy().putAll(userShareMap);
        expense.setStatus(ExpenseStatus.UNSETTLED);
    }

    public void addUserContribution(String expenseId, Contribution contribution) throws ExpenseNotFoundException {
        Expense expense = expenseRepo.getExpenseForId(expenseId);

        if (!contribution.getPaidTo().equals(expense.getCreatedById())) {
            throw new RuntimeException(String.format("The expense with ID: %s cannot take a contribution from user %s", expense.getId(), contribution.getPaidBy()));
        }

        String beingPaidBy = contribution.getPaidBy();
        if (!expense.getSharedBy().containsKey(beingPaidBy)) {
            throw new RuntimeException(String.format("User %s has no due to be paid to %s", beingPaidBy, expense.getCreatedById()));
        }

        Double amountDue = expense.getSharedBy().get(beingPaidBy);
        if (amountDue < contribution.getPaidAmount()) {
            throw new RuntimeException(String.format("User %s has lesser due amount compared to what he's paying right now. due: %s, currBeingPaid: %s",
                    beingPaidBy, amountDue.toString(), contribution.getPaidAmount().toString()));
        }

        if (amountDue.equals(contribution.getPaidAmount())) {
            expense.getSharedBy().remove(beingPaidBy);
        } else
            expense.getSharedBy().put(beingPaidBy, amountDue - contribution.getPaidAmount());

        expense.getContriHistory().add(contribution);
    }

    public Map<String, Double> getCurrentDueAmounts(String expenseId) throws ExpenseNotFoundException {
        Expense expense = expenseRepo.getExpenseForId(expenseId);

        Map<String, Double> nonZeroDues = new HashMap<>();
        for (Map.Entry<String, Double> entry : expense.getSharedBy().entrySet()) {
            if (nonZeroDues.containsKey(entry.getKey())) {
                nonZeroDues.put(entry.getKey(), entry.getValue() + nonZeroDues.get(entry.getKey()));
            } else
                nonZeroDues.put(entry.getKey(), entry.getValue());
        }

        return nonZeroDues;
    }

    private void validateStrategy(Double totalAmount, String strategy, List<Double> strategyInput) {
        if (strategy == "EXACT") {
            Double sum = 0.0;
            for (Double aDouble : strategyInput) {
                sum += aDouble;
            }

            if (!totalAmount.equals(sum)) {
                throw new RuntimeException(
                        String.format("The given strategy \"%s\" and its inputs are not valid. totalAmount: %lf, sum: %lf", strategy, totalAmount, sum)
                );
            }
        } else if (strategy == "PERCENT") {
            Double sum = 0.0;
            for (Double aDouble : strategyInput) {
                sum += aDouble;
            }

            if (!sum.equals(100.0)) {
                throw new RuntimeException(
                        String.format("The given strategy \"%s\" and its inputs are not valid. sharePercents: %lf", strategy, sum)
                );
            }
        }
    }

    private static ExpenseService instance;
    public static ExpenseService getInstance() {
        if (instance == null)
            instance = new ExpenseService();
        return instance;
    }

    private ExpenseService() {

    }
}
