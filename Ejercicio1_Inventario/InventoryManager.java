import com.olf.openjvs.*;
import com.olf.openjvs.enums.*;
import java.util.*;

public class InventoryManager implements IScript {

    /**
     * Executes the main logic for managing the inventory of products.
     * <p>
     * This method performs the following steps:
     * <ul>
     * <li>Initializes a list of {@code ProductA} objects representing the
     * inventory.</li>
     * <li>Creates a new {@code Table} to store product data.</li>
     * <li>Initializes the product table structure using
     * {@code initProductTable}.</li>
     * <li>Populates the table with the product list using
     * {@code fillTableFromList}.</li>
     * <li>Sorts the table by the quantity column using {@code sortByColumn}.</li>
     * <li>Updates the quantity of a product by its ID and by row index using
     * {@code updateQuantityByProductId} and {@code updateQuantityByRow}.</li>
     * <li>Handles exceptions related to table initialization and validation errors,
     * logging messages to the console.</li>
     * <li>Destroys the product table to release resources.</li>
     * </ul>
     *
     * @param context the container context in which this execution occurs
     * @throws OException if an error occurs during table initialization
     */
    @Override
    public void execute(IContainerContext context) throws OException {
        List<Product> products = Arrays.asList(
                new Product(101, "Laptop", "Electronics", 5, 1200.0),
                new Product(102, "Mouse", "Electronics", 20, 15.5),
                new Product(103, "Chair", "Furniture", 10, 85.0),
                new Product(104, "Desk", "Furniture", 5, 150.0));

        Table productTable = Table.tableNew();

        try {
            initProductTable(productTable);
        } catch (OException e) {
            OConsole.oprint("Error initializing product table: " + e.getMessage() + "\n");
            return;
        }

        fillTableFromList(productTable, products);

        sortByColumn(productTable, ProductColumns.QUANTITY);

        productTable.viewTable();

        try {
            updateQuantityByProductId(productTable, 101, 8);
            sortByColumn(productTable, ProductColumns.QUANTITY);
        } catch (IllegalArgumentException e) {
            OConsole.oprint("Validation error updating product ID 101: " + e.getMessage() + "\n");
        }

        try {
            updateQuantityByRow(productTable, 2, 25);
            sortByColumn(productTable, ProductColumns.QUANTITY);
        } catch (IllegalArgumentException | IllegalStateException e) {
            OConsole.oprint("Validation error updating row 2: " + e.getMessage() + "\n");
        }

        productTable.destroy();
    }

    /**
     * Initializes the given table with columns for managing product inventory.
     * The table will have columns for Product ID, Product Name, Category, Quantity
     * in Stock, and Unit Price.
     *
     * @param tbl the Table object to be initialized with product columns
     * @throws OException if an error occurs while adding columns to the table
     */
    public void initProductTable(Table tbl) throws OException {
        tbl.addCol(ProductColumns.PRODUCT_ID.getColName(), COL_TYPE_ENUM.COL_INT, "Product ID");
        tbl.addCol(ProductColumns.PRODUCT_NAME.getColName(), COL_TYPE_ENUM.COL_STRING, "Product Name");
        tbl.addCol(ProductColumns.CATEGORY.getColName(), COL_TYPE_ENUM.COL_STRING, "Category");
        tbl.addCol(ProductColumns.QUANTITY.getColName(), COL_TYPE_ENUM.COL_INT, "Quantity in Stock");
        tbl.addCol(ProductColumns.UNIT_PRICE.getColName(), COL_TYPE_ENUM.COL_DOUBLE, "Unit Price");
    }

    /**
     * Populates the specified table with data from a list of ProductA objects.
     *
     * <p>
     * For each product in the provided list, a new row is added to the table and
     * the product's
     * properties are set in the corresponding columns: "ProductID", "ProductName",
     * "Category",
     * "Quantity", and "UnitPrice".
     * </p>
     *
     * @param tbl      the table to be filled with product data
     * @param products the list of ProductA objects whose data will populate the
     *                 table
     * @throws OException if an error occurs while adding rows or setting values in
     *                    the table
     */
    public void fillTableFromList(Table tbl, List<Product> products) throws OException {
        if (products == null || products.isEmpty()) {
            OConsole.oprint("Product list is empty.\n");
            return;
        }

        for (Product p : products) {
            addProduct(tbl, p);
        }
    }

    /**
     * Adds a new product to the specified table with the given details.
     *
     * @param tbl      The table to which the product will be added.
     * @param id       The unique identifier for the product.
     * @param name     The name of the product. Must not be null or empty.
     * @param category The category of the product.
     * @param quantity The quantity of the product. Must not be negative.
     * @param price    The unit price of the product. Must not be negative.
     * @return {@code true} if the product was successfully added; {@code false}
     *         otherwise.
     * @throws OException If an error occurs while adding the product to the table.
     */
    public boolean addProduct(Table tbl, int id, String name, String category, int quantity, double price)
            throws OException {
        if (tbl == null) {
            OConsole.oprint("Error: Product table is null.\n");
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            OConsole.oprint("Invalid product (ID: " + id + "): Name cannot be empty.\n");
            return false;
        }
        if (quantity < 0) {
            OConsole.oprint("Invalid product (ID: " + id + "): Quantity cannot be negative.\n");
            return false;
        }
        if (price < 0) {
            OConsole.oprint("Invalid product (ID: " + id + "): Price cannot be negative.\n");
            return false;
        }

        int newRow = tbl.addRow();
        tbl.setInt(ProductColumns.PRODUCT_ID.getColName(), newRow, id);
        tbl.setString(ProductColumns.PRODUCT_NAME.getColName(), newRow, name);
        tbl.setString(ProductColumns.CATEGORY.getColName(), newRow, category);
        tbl.setInt(ProductColumns.QUANTITY.getColName(), newRow, quantity);
        tbl.setDouble(ProductColumns.UNIT_PRICE.getColName(), newRow, price);

        OConsole.oprint("Product added: " + name + " (ID: " + id + ")\n");
        return true;
    }

    /**
     * Adds a product to the specified table.
     *
     * @param tbl     The table to which the product will be added.
     * @param product The product to add. Must not be {@code null}.
     * @return {@code true} if the product was added successfully; {@code false}
     *         otherwise.
     * @throws OException If an error occurs during the addition process.
     */
    public boolean addProduct(Table tbl, Product product) throws OException {
        if (product == null) {
            OConsole.oprint("Invalid product: null object.\n");
            return false;
        }
        return addProduct(tbl, product.getId(), product.getName(), product.getCategory(), product.getQuantity(),
                product.getPrice());
    }

    /**
     * Updates the quantity of a product in the given table.
     *
     * @param tbl         The table containing product information.
     * @param productId   The ID of the product to update.
     * @param newQuantity The new quantity to set for the product. Must be
     *                    non-negative.
     * @throws OException               If an error occurs while accessing the
     *                                  table.
     * @throws IllegalArgumentException If the new quantity is negative.
     *
     *                                  If the product with the specified ID is not
     *                                  found, a message is printed to the console.
     */
    public void updateQuantityByProductId(Table tbl, int productId, int newQuantity) throws OException {
        if (tbl == null || tbl.getNumRows() == 0) {
            throw new IllegalStateException("Product table is empty or not initialized.");
        }

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        sortByColumn(tbl, ProductColumns.PRODUCT_ID);
        int row = tbl.findInt(ProductColumns.PRODUCT_ID.getColName(), productId, SEARCH_ENUM.FIRST_IN_GROUP);

        if (row > 0) {
            tbl.setInt(ProductColumns.QUANTITY.getColName(), row, newQuantity);
            OConsole.oprint("Updated quantity for Product ID " + productId + " to " + newQuantity + "\n");
        } else {
            OConsole.oprint("Product ID " + productId + " not found. No update performed.\n");
        }
    }

    /**
     * Updates the quantity of a product in the specified row of the given table.
     *
     * @param tbl         The table containing product data.
     * @param row         The row number (1-based index) where the quantity should
     *                    be updated.
     * @param newQuantity The new quantity to set for the product.
     * @throws OException               If an error occurs while updating the table.
     * @throws IllegalStateException    If the table is null or contains no rows.
     * @throws IllegalArgumentException If the row number is invalid or the new
     *                                  quantity is negative.
     */
    public void updateQuantityByRow(Table tbl, int row, int newQuantity) throws OException {
        if (tbl == null || tbl.getNumRows() == 0) {
            throw new IllegalStateException("Product table is empty or not initialized.");
        }

        if (row <= 0 || row > tbl.getNumRows()) {
            throw new IllegalArgumentException("Row number " + row + " is invalid.");
        }

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        tbl.setInt(ProductColumns.QUANTITY.getColName(), row, newQuantity);
        OConsole.oprint("Updated quantity at row " + row + " to " + newQuantity + "\n");
    }

    /**
     * Sorts the specified table by the given product column.
     *
     * @param tbl    the table to be sorted
     * @param column the column by which to sort the table
     * @throws OException if an error occurs during sorting
     */
    public void sortByColumn(Table tbl, ProductColumns column) throws OException {
        tbl.sortCol(column.getColName());
    }

}
