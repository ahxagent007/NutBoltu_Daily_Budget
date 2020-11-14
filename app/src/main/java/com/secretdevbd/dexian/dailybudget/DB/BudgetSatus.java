package com.secretdevbd.dexian.dailybudget.DB;

public class BudgetSatus {
    String budgetName;
    int budgetAmount;
    int budgetGet;
    int budgetBalance;
    String budgetType;
    int catID;

    public BudgetSatus(String budgetName, int budgetAmount, int budgetGet, int budgetBalance, String budgetType) {
        this.budgetName = budgetName;
        this.budgetAmount = budgetAmount;
        this.budgetGet = budgetGet;
        this.budgetBalance = budgetBalance;
        this.budgetType = budgetType;
    }

    public BudgetSatus() {
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public int getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(int budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public int getBudgetGet() {
        return budgetGet;
    }

    public void setBudgetGet(int budgetGet) {
        this.budgetGet = budgetGet;
    }

    public int getBudgetBalance() {
        return budgetBalance;
    }

    public void setBudgetBalance(int budgetBalance) {
        this.budgetBalance = budgetBalance;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }
}
