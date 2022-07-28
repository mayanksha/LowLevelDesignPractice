package com.practice.lld.splitwise;

import com.practice.lld.splitwise.exception.DuplicateExpenseException;
import com.practice.lld.splitwise.exception.DuplicateUserException;
import com.practice.lld.splitwise.exception.ExpenseNotFoundException;
import com.practice.lld.splitwise.exception.UserNotFoundException;

public class SplitwiseMain {
    public static void main(String[] args) throws UserNotFoundException, DuplicateExpenseException, ExpenseNotFoundException, DuplicateUserException {
        Driver driver = new Driver();
        driver.runProgram();
    }
}
