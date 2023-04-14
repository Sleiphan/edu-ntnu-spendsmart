package edu.ntnu.g14;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Budget {

    private static final char CSV_DELIMITER = ',';

    private BigDecimal salary = BigDecimal.ZERO;
    private BigDecimal savings = BigDecimal.ZERO;
    private byte age;
    private GenderCategory gender;

    private HouseholdCategory household;
    private final List<BudgetItem> entries = new ArrayList<>();



    /**
     * Creates a new empty budget.
     * @param i The age of the person following this budget.
     * @param ownerGender The gender of the person following this budget.
     */
    public Budget(byte i, GenderCategory ownerGender) {
        this.age = i;
        this.gender = ownerGender;
        this.household = HouseholdCategory.LIVING_ALONE;
    }

    public Budget(HouseholdCategory householdSize) {
        this.household = householdSize;
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

    // BigDecimal salary = BigDecimal.ZERO;
    // BigDecimal savings = BigDecimal.ZERO;
    // byte age;
    // GenderCategory gender;
    // final List<BudgetItem> entries = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Budget))
            return false;

        return o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return salary.hashCode() * savings.hashCode() * age * gender.hashCode() * household.hashCode() * entries.hashCode();
    }

    public String toCSV() {
        StringBuilder allEntries = new StringBuilder();
        for (BudgetItem b : entries)
            allEntries.append(b.toCSV()).append(CSV_DELIMITER);

        // Pop the last element, as it is a lone delimiter.
        allEntries.deleteCharAt(allEntries.length() - 1);

        return  salary .toPlainString() + CSV_DELIMITER +
                savings.toPlainString() + CSV_DELIMITER +
                age                     + CSV_DELIMITER +
                gender                  + CSV_DELIMITER +
                household               + CSV_DELIMITER +
                allEntries.toString();
    }

    public static Budget fromCSV(String data) {
        final String[] fields = data.split(CSV_DELIMITER + "(?!\\s)");
        final BigDecimal salary = new BigDecimal(fields[0]);
        final BigDecimal savings = new BigDecimal(fields[1]);
        final byte age = Byte.parseByte(fields[2]);
        final GenderCategory gender = GenderCategory.valueOf(fields[3]);
        final HouseholdCategory householdCategory = HouseholdCategory.valueOf(fields[4].equals("null") ? null : fields[4]);
        final List<BudgetItem> entries = new ArrayList<>();

        for (int i = 5; i < fields.length; i++) {
            entries.add(BudgetItem.fromCSV(fields[i]));
        }

        Budget budget = new Budget(age, gender);
        budget.setSalary(salary);
        budget.setSavings(savings);
        budget.setSavings(savings);
        budget.setHouseholdCategory(householdCategory);

        for (BudgetItem i : entries) {
            budget.addBudgetItem(i);
            i.setAssociatedBudget(budget);
        }

        return budget;
    }

    private void setHouseholdCategory(HouseholdCategory householdCategory) {
        this.household = householdCategory;
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

    public void setCategory(HouseholdCategory category) {
        household = category;
    }

    public HouseholdCategory getCategory() {
        return household;
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

    public HouseholdCategory getHouseholdCategory() {
        return household;
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
