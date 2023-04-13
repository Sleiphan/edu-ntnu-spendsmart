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
        return salary.hashCode() * savings.hashCode() * age * gender.hashCode() * entries.hashCode();
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
                allEntries.toString();
    }

    public static Budget fromCSV(String data) {
        final String[] fields = data.split(CSV_DELIMITER + "(?!\\s)");//splitIgnoreStringLiterals(data, CSV_DELIMITER);
        final BigDecimal salary = new BigDecimal(fields[0]);
        final BigDecimal savings = new BigDecimal(fields[1]);
        final byte age = Byte.parseByte(fields[2]);
        final GenderCategory gender = GenderCategory.valueOf(fields[3]);
        final List<BudgetItem> entries = new ArrayList<>();

        for (int i = 4; i < fields.length; i++) {
            entries.add(BudgetItem.fromCSV(fields[i]));
        }

        Budget budget = new Budget(age, gender);
        budget.setSalary(salary);
        budget.setSavings(savings);
        for (BudgetItem i : entries) {
            budget.addBudgetItem(i);
            i.setAssociatedBudget(budget);
        }

        return budget;
    }

    /**
     * Performs the same split operation as String::split, but ignores String literals within that string, marked by the character '"'. <br>
     * For example, this operation: <br>
     *     <code>splitIgnoreStringLiterals("54, \"Hello, world!\"", ',');</code> <br>
     * will return this array: <br>
     *     <code>["54", "Hello, world!]</code><br>
     * @param text The string to be split.
     * @param delimiter The character to split this string
     * @return An array of strings containing the result of the split
     */
    private static String[] splitIgnoreStringLiterals(String text, char delimiter) {
        // We need a place to store the position of all the delimiters we find.
        List<Integer> splitIndices = new ArrayList<>();
        // Add an entry to make sure we include the first part of the text.
        splitIndices.add(-1);

        boolean inString = false;
        int index = 0;
        while (index < text.length()) {
            switch (text.charAt(index)) {
                case CSV_DELIMITER:
                    if (!inString)
                        splitIndices.add(index);
                case '"':
                    if (index > 0 && text.charAt(index - 1) != '\\')
                        inString = !inString;
            }
            index++;
        }

        // Add an entry to make sure we include the last part of the text.
        splitIndices.add(text.length() - 1);

        // If no occurrences of the delimiter was found, return an array with the whole text as the only element
        if (splitIndices.size() <= 2)
            return new String[]{text};

        // Split the text based on the indices recorded in 'splitIndices'
        List<String> stringParts = new ArrayList<>();

        for (int i = 1; i < splitIndices.size() - 1; i++) // For every index in 'splitIndices' ...
            stringParts.add(text.substring(splitIndices.get(i) + 1, splitIndices.get(i + 1))); // ... append a substring of the text, based on recorded indices.

        // Return the resulting array
        return stringParts.toArray(String[]::new);
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
