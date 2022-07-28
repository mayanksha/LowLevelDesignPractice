package com.practice.lld.splitwise;

import com.practice.lld.splitwise.exception.DuplicateExpenseException;
import com.practice.lld.splitwise.exception.DuplicateUserException;
import com.practice.lld.splitwise.exception.ExpenseNotFoundException;
import com.practice.lld.splitwise.exception.UserNotFoundException;
import com.practice.lld.splitwise.model.ExpenseGroup;
import com.practice.lld.splitwise.model.User;
import com.practice.lld.splitwise.service.ExpenseGroupService;
import com.practice.lld.splitwise.service.ExpenseService;
import com.practice.lld.splitwise.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.*;

@Slf4j
public class Driver {

    private ExpenseGroupService expenseGroupService = ExpenseGroupService.getInstance();
    private UserService userService = UserService.getInstance();
    private ExpenseService expenseService = ExpenseService.getInstance();

    private List<String> userNames = Arrays.asList("u1", "u2", "u3", "u4");
    private Set<String> sharingStrategy = new HashSet<>(Arrays.asList("EQUAL", "EXACT", "PERCENT"));

    public void runProgram() throws UserNotFoundException, ExpenseNotFoundException, DuplicateExpenseException, DuplicateUserException {
        InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream("input.txt");
        Scanner lineScanner = new Scanner(inputStream);

        ExpenseGroup group = expenseGroupService.createExpenseGroup(userNames.get(0), "Dummy Expense Group");
        createDummyUsers();

        while (lineScanner.hasNextLine()) {
            Scanner scanner = new Scanner(lineScanner.nextLine());
            String command = scanner.next();

            if (command.equals("SHOW")) {
                handleShow(group, scanner);
            } else if (command.equals("EXPENSE")) {
                handleExpenseCreation(group, scanner);
            }
        }
    }

    private void handleShow(ExpenseGroup expenseGroup, Scanner scanner) throws ExpenseNotFoundException, UserNotFoundException {
        if (!scanner.hasNext()) {
            expenseGroupService.displayAllUnsettledExpensesForGroup(expenseGroup);
            return;
        }

        String userId = scanner.next();
        expenseGroupService.displayAllUnsettledExpensesForUser(expenseGroup, userId);
    }

    private void handleExpenseCreation(ExpenseGroup expenseGroup, Scanner scanner) throws DuplicateExpenseException, UserNotFoundException, ExpenseNotFoundException {
        String createdBy = scanner.next();
        Double amount = scanner.nextDouble();

        List<User> sharedBy = new ArrayList<>();

        Integer numUsers = scanner.nextInt();
        for (Integer i = 0; i < numUsers; i++) {
            User user = userService.getUser(scanner.next());
            sharedBy.add(user);
        }

        // Create expense
        String expenseId = expenseService.createExpense(createdBy, amount, "BLAH BLAH BLAH");
        expenseGroup.addExpense(expenseId);

        String strategy = scanner.next();
        List<Double> strategyInputs = new ArrayList<>();
        if (Objects.equals(strategy, "EXACT")) {
            while (scanner.hasNextDouble()) {
                strategyInputs.add(scanner.nextDouble());
            }
        } else if (Objects.equals(strategy, "PERCENT")) {
            while (scanner.hasNextDouble()) {
                strategyInputs.add(scanner.nextDouble());
            }
        }

        expenseService.addUsersAndShareAmount(expenseId, sharedBy, strategy, strategyInputs);
    }

    private void createDummyUsers() throws DuplicateUserException {
        for (String name : userNames) {
            String userId = userService.createUser(name);
            log.info("Created user {}", userId);
        }
    }
}
