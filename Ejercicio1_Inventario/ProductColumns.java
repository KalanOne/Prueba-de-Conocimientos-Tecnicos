/**
 * Enum representing the columns of a Product entity.
 * Each enum constant maps to a specific column name used in data storage or processing.
 *
 * <ul>
 *   <li>{@link #PRODUCT_ID} - Represents the product's unique identifier.</li>
 *   <li>{@link #PRODUCT_NAME} - Represents the name of the product.</li>
 *   <li>{@link #CATEGORY} - Represents the category to which the product belongs.</li>
 *   <li>{@link #QUANTITY} - Represents the quantity of the product available.</li>
 *   <li>{@link #UNIT_PRICE} - Represents the unit price of the product.</li>
 * </ul>
 *
 * Use {@link #getColName()} to retrieve the string representation of the column name.
 */
public enum ProductColumns {
    PRODUCT_ID("ProductID"),
    PRODUCT_NAME("ProductName"),
    CATEGORY("Category"),
    QUANTITY("Quantity"),
    UNIT_PRICE("UnitPrice");

    private final String colName;

    ProductColumns(String colName) {
        this.colName = colName;
    }

    public String getColName() {
        return colName;
    }
}