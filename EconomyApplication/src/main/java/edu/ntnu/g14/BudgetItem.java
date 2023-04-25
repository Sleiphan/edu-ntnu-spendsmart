package edu.ntnu.g14;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.json.JSONObject;
import java.math.BigDecimal;

/**
 * This class represents the individual entries of a complete budget.
 * Every Budget consists of multiple BudgetItems, each with their own financial value and description, along with a categorisation.
 * @author Håkon F. Fjellanger
 */
public class BudgetItem {
    private static final char CSV_DELIMITER = ';';

    private BigDecimal financialValue;
    private BudgetCategory category;
    private String description;

    private Budget budgetRef = null;



    /**
     * Creates a new instance of BudgetItem, with the default values pre-defined in this class.
     */
    public BudgetItem() {
        this(BigDecimal.ZERO, "", BudgetCategory.OTHER);
    }

    /**
     * Creates a new instance of BudgetItem, a budget item to be used in setting up a budget.
     * @param financialValue The financial value of this budget item.
     * @param description The description of this budget item,
     *                    describing e.g. the purpose of this transaction, where the money is coming from or going, etc.
     * @param category The category this budget item belongs.
     */
    public BudgetItem(BigDecimal financialValue, String description, BudgetCategory category) {
        if (financialValue == null)
            throw new IllegalArgumentException("The financial value in a budget item cannot be null");
        if (description == null)
            throw new IllegalArgumentException("The financial value in a budget item cannot be null");

        this.financialValue = financialValue;
        this.description = description;
        this.category = category;
    }
    /**
     * Creates a BudgetItem object with the specified budget category and financial value.
     * @param category the budget category of the item
     * @param financialValue the financial value of the item
     */
    public BudgetItem(BudgetCategory category, BigDecimal financialValue){
     this(financialValue, "", category);
    }

    /**
     * Returns a string representation of the BudgetItem object in CSV format.
     * @return a string in CSV format with financialValue, description, and category separated by CSV_DELIMITER
     */
    public String toCSV() {
        return  financialValue.toPlainString() + CSV_DELIMITER +
                "\"" + description + "\"" + CSV_DELIMITER +
                category;
    }

    /**
     * Creates a BudgetItem object from a CSV string representation.
     * @param data a string in CSV format with financialValue, description, and category separated by CSV_DELIMITER
     * @return a BudgetItem object with the specified financialValue, description, and category
     */
    public static BudgetItem fromCSV(String data) {
        String[] fields = data.split(CSV_DELIMITER + "(?!\\s)");
        fields[1] = fields[1].substring(1, fields[1].length() - 1);

        BigDecimal financialValue = new BigDecimal(fields[0]);
        String description = fields[1];
        BudgetCategory category = BudgetCategory.valueOf(fields[2]);

        return new BudgetItem(financialValue, description, category);
    }

    /**
     * A method for the Budget to call when this BudgetItem is added to a budget.
     * @param budgetRef The Budget this BudgetItem is a part of.
     */
    void setAssociatedBudget(Budget budgetRef) {
        this.budgetRef = budgetRef;
    }

    /**
     * Triggers a recalculation in this BudgetItem's associated budget.
     * Called every time a recalculation should happen in the budget this BudgetItem is a part of.
     */
    private void updateBudgetCalculations() {
        if (budgetRef != null)
            budgetRef.updateCalculations();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof BudgetItem))
            return false;

        return o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return getFinancialValue().hashCode() * getDescription().hashCode() * getCategory().hashCode();
    }

    /**
     * Sets the financial value of this budget item.
     * Since BigDecimal is immutable, no changes can be made to the parameter-object, thus making the internal value safe from such unexpected changes.
     * @param financialValue The new financial value of this budget item.
     * @see BigDecimal
     */
    public void setFinancialValue(BigDecimal financialValue) {
        this.financialValue = financialValue;
        updateBudgetCalculations();
    }

    /**
     * Parses the submitted text as a value, and sets the financial value of this budget item to the parsed value.
     * @param financialValueText The text containing the new financial value of this budget item.
     * @throws NumberFormatException if the submitted String could not be parsed to a financial value.
     */
    public void setFinancialValue(String financialValueText) {
        this.financialValue = new BigDecimal(financialValueText);
    }

    /**
     * Parses the submitted text as a value, and sets the amount of this budget item to the parsed value.
     * This method tries to achieve high performance in situations where many successive parsing failures are expected,
     * e.g. checking the input of a user as they type in a value. This is done by avoiding the use of catching a NumberFormatException,
     * by using a validator from the Apache Commons library.
     * @param financialValueText A String containing the new amount of this budget item.
     * @return true if the submitted text could be successfully parsed, or false if the parse failed.
     */
    public boolean setFinancialValue_tryParse(String financialValueText) {
        BigDecimal newValue = BigDecimalValidator.getInstance().validate(financialValueText);
        if (newValue == null)
            return false;

        setFinancialValue(newValue);
        return true;
    }

    /**
     * Sets the financial value of this budget item.
     * @param financialValue The new financial value of this budget item.
     */
    public void setFinancialValue(int financialValue) {
        setFinancialValue(new BigDecimal(financialValue));
    }

    /**
     * Sets the financial value of this budget item.
     * @param financialValue The new financial value of this budget item.
     */
    public void setFinancialValue(long financialValue) {
        setFinancialValue(new BigDecimal(financialValue));
    }

    /**
     * Sets the financial value of this budget item.
     * @param financialValue The new financial value of this budget item.
     */
    public void setFinancialValue(float financialValue) {
        setFinancialValue(new BigDecimal(financialValue));
    }

    /**
     * Sets the financial value of this budget item.
     * @param financialValue The new financial value of this budget item.
     */
    public void setFinancialValue(double financialValue) {
        setFinancialValue(new BigDecimal(financialValue));
    }

    /**
     * Returns the financial value of this budget item, as BigDecimal.
     * @return the financial value of this budget item, as BigDecimal.
     * @see BigDecimal
     */
    public BigDecimal getFinancialValue() {
        return financialValue;
    }

    /**
     * Returns the financial value of this budget item as an int-value. <br>
     * <i>WARNING: Returns the result of BigDecimal::intValue(), which cuts away certain pieces of the original value.</i>
     * @return the financial value of this budget item, as an int-value.
     * @see BigDecimal
     */
    public int getFinancialValue_int() {
        return financialValue.intValue();
    }

    /**
     * Returns the financial value of this budget item as a double-value. <br>
     * <i>WARNING: Returns the result of BigDecimal::intValue(), which cuts away certain pieces of the original value.</i>
     * @return the financial value of this budget item, as a double-value.
     * @see BigDecimal
     */
    public double getFinancialValue_double() {
        return financialValue.doubleValue();
    }

    /**
     * Sets the description of this budget item.
     * @param newDescription The new description of this budget item.
     */
    public void setDescription(String newDescription) {
        description = newDescription;
    }

    /**
     * Sets the category of this budget item.
     * @param newCategory The new category of this budget item.
     */
    public void setCategory(BudgetCategory newCategory) {
        category = newCategory;
        updateBudgetCalculations();
    }

    /**
     * Returns the description of this budget item.
     * @return the description of this budget item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the category of this budget item.
     * @return the category of this budget item.
     */
    public BudgetCategory getCategory() {
        return category;
    }
}