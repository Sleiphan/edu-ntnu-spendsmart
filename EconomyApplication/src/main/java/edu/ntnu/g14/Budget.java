package edu.ntnu.g14;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Budget {

    private BigDecimal salary;
    private BigDecimal savings;
    private byte age;
    private GenderCategory gender;
    private final List<BudgetItem> entries;



    /**
     * Creates a new empty budget.
     * @param ownerAge The age of the person following this budget.
     * @param ownerGender The gender of the person following this budget.
     */
    public Budget(byte ownerAge, GenderCategory ownerGender) {
        this.age = ownerAge;
        this.gender = ownerGender;

        entries = new ArrayList<>();
    }


    /**
     * Adds a new budget item to this budget.
     * @param newBudgetItem The new budget item to be added to this budget.
     */
    public void addBudgetItem(BudgetItem newBudgetItem) {
        entries.add(newBudgetItem);
        newBudgetItem.setAssociatedBudget(this);
    }

    /**
     * Returns the number of budget items currently in this budget.
     * @return the number of budget items currently in this budget.
     */
    public int getNumBudgetItems() {
        return entries.size();
    }

    /**
     * Attempts to remove the submitted budget item from this Budget.
     * @param index the index of the BudgetItem to be removed from this Budget.
     *              The index scheme follows the one present in the List-object returned by Budget::getEntries().
     * @return true if the object was found and removed. Else, returns false.
     */
    public boolean removeBudgetItem(int index) {
        BudgetItem removed = entries.remove(index);
        if (removed == null)
            return false;

        removed.setAssociatedBudget(null);
        return true;
    }



    /**
     * Updates the associated results of this budget. This method should be called every time a change the budget is made.
     */
    public void updateCalculations() {
        // As of now, nothing to update or recalculate.
    }



    /**
     * Sets the salary registered with this budget.
     * @param salary the salary registered with this budget.
     */
    public void setSalary(BigDecimal salary) {
        if (salary == null)
            throw new IllegalArgumentException("Salary cannot be null");
        if (salary.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Salary cannot be a negative number.");
        this.salary = salary;
        updateCalculations();
    }

    /**
     * Sets the savings registered with this budget.
     * @param savings the savings registered with this budget.
     */
    public void setSavings(BigDecimal savings) {
        if (savings == null)
            throw new IllegalArgumentException("Savings cannot be null");
        if (savings.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Savings cannot be a negative number.");
        this.savings = savings;
        updateCalculations();
    }

    /**
     * Sets the age of the person following this budget.
     * @param age the age of the person following this budget.
     */
    public void setAge(byte age) {
        if (age < 0)
            throw new IllegalArgumentException("The age of the person following this budget cannot be negative.");
        this.age = age;
        updateCalculations();
    }

    /**
     * Sets the gender of the person following this budget.
     * @param gender the gender of the person following this budget.
     */
    public void setGender(GenderCategory gender) {
        this.gender = gender;
        updateCalculations();
    }



    /**
     * Returns the salary registered with this budget.
     * @return the salary registered with this budget.
     */
    public BigDecimal getSalary() {
        return salary;
    }

    /**
     * Returns the savings registered with this budget.
     * @return the savings registered with this budget.
     */
    public BigDecimal getSavings() {
        return savings;
    }

    /**
     * Returns the age of the person following this budget.
     * @return the age of the person following this budget.
     */
    public byte getAge() {
        return age;
    }

    /**
     * Returns the gender of the person following this budget.
     * @return the gender of the person following this budget.
     */
    public GenderCategory getGender() {
        return gender;
    }

    /**
     * Returns the budget items of this Budget. The returned object is a copy of the internal List-object containing all the BudgetItems.
     * However, changes made to the existing elements in the returned List-object will make changes to this budget.
     * @return the budget items of this Budget.
     */
    public List<BudgetItem> getEntries() {
        return new ArrayList<>(entries);
    }
}
