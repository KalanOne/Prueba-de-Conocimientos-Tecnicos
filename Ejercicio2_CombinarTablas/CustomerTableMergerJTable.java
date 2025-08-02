import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Utility class for merging selected columns from two JTable instances into a
 * single DefaultTableModel.
 * <p>
 * The merged table will contain 10 columns: 5 selected from the first table
 * (Customer Profile)
 * and 5 selected from the second table (Customer Transactions). The headers of
 * the merged table
 * are prefixed to indicate their origin.
 * </p>
 *
 * <p>
 * Usage example:
 * 
 * <pre>
 * int[] columnsA = { 0, 1, 2, 3, 4 };
 * int[] columnsB = { 0, 1, 2, 3, 4 };
 * DefaultTableModel mergedModel = CustomerTableMergerJTable.mergeCustomerTables(tableA, tableB, columnsA, columnsB);
 * </pre>
 * </p>
 *
 * <ul>
 * <li>Both input tables must not be null.</li>
 * <li>Exactly 5 columns must be selected from each table.</li>
 * <li>The number of rows in the merged table will be the minimum of the row
 * counts of the two input tables.</li>
 * </ul>
 *
 */
public class CustomerTableMergerJTable {

    /**
     * Merges selected columns from two JTable instances into a single
     * DefaultTableModel.
     * <p>
     * The method combines the specified columns from both input tables row by row,
     * creating a new table model with combined headers and data.
     * </p>
     *
     * @param tableA   the first JTable to merge
     * @param tableB   the second JTable to merge
     * @param columnsA the indices of columns to select from tableA
     * @param columnsB the indices of columns to select from tableB
     * @return a DefaultTableModel containing the merged data from both tables
     * @throws IllegalArgumentException if the parameters are invalid or the tables
     *                                  have different row counts
     */
    public static DefaultTableModel mergeCustomerTables(
            JTable tableA, JTable tableB,
            int[] columnsA, int[] columnsB) {

        validateParameters(tableA, tableB, columnsA, columnsB);

        String[] combinedHeaders = createCombinedHeaders(tableA, tableB, columnsA, columnsB);

        DefaultTableModel combinedModel = new DefaultTableModel(combinedHeaders, 0);

        combineRows(tableA, tableB, columnsA, columnsB, combinedModel);

        return combinedModel;
    }

    /**
     * Validates the parameters for merging two JTable instances by checking for
     * null values,
     * ensuring the correct number of columns are selected from each table, and
     * verifying that
     * the specified column indices are within valid bounds for each table.
     *
     * @param tableA   the first JTable to validate (must not be null)
     * @param tableB   the second JTable to validate (must not be null)
     * @param columnsA the array of selected column indices from tableA (must not be
     *                 null and must have length 5)
     * @param columnsB the array of selected column indices from tableB (must not be
     *                 null and must have length 5)
     * @throws IllegalArgumentException if any table or column array is null,
     *                                  if the column arrays do not have exactly 5
     *                                  elements,
     *                                  or if any column index is out of range for
     *                                  its respective table
     */
    private static void validateParameters(
            JTable tableA, JTable tableB,
            int[] columnsA, int[] columnsB) {

        if (tableA == null || tableB == null) {
            throw new IllegalArgumentException("Tables cannot be null.");
        }
        if (columnsA == null || columnsB == null) {
            throw new IllegalArgumentException("Column arrays cannot be null.");
        }
        if (columnsA.length != 5 || columnsB.length != 5) {
            throw new IllegalArgumentException("You must select exactly 5 columns from each table.");
        }

        for (int colIndex : columnsA) {
            if (colIndex < 0 || colIndex >= tableA.getColumnCount()) {
                throw new IllegalArgumentException("Column index A out of range: " + colIndex);
            }
        }
        for (int colIndex : columnsB) {
            if (colIndex < 0 || colIndex >= tableB.getColumnCount()) {
                throw new IllegalArgumentException("Column index B out of range: " + colIndex);
            }
        }
    }

    /**
     * Creates a combined array of header names for a merged table by concatenating
     * column names from two source JTables. The first half of the headers are
     * prefixed
     * with "CustomerProfile_" and taken from tableA using columnsA indices, while
     * the
     * second half are prefixed with "CustomerTransactions_" and taken from tableB
     * using columnsB indices.
     *
     * @param tableA   the first JTable containing customer profile data
     * @param tableB   the second JTable containing customer transaction data
     * @param columnsA the indices of columns to include from tableA (length 5)
     * @param columnsB the indices of columns to include from tableB (length 5)
     * @return a String array of length 10 containing the combined headers
     */
    private static String[] createCombinedHeaders(
            JTable tableA, JTable tableB,
            int[] columnsA, int[] columnsB) {

        String[] headers = new String[10];
        for (int i = 0; i < 5; i++) {
            headers[i] = "CustomerProfile_" + tableA.getColumnName(columnsA[i]);
            headers[5 + i] = "CustomerTransactions_" + tableB.getColumnName(columnsB[i]);
        }
        return headers;
    }

    /**
     * Combines rows from two JTables into a single table model by selecting
     * specific columns from each table.
     *
     * <p>
     * For each row up to the minimum row count of the two tables, this method
     * extracts values from the specified
     * columns of both {@code tableA} and {@code tableB}, and merges them into a new
     * row, which is then added to
     * the provided {@code combinedModel}. The first five elements of each combined
     * row are taken from {@code tableA}
     * using {@code columnsA}, and the next five elements are taken from
     * {@code tableB} using {@code columnsB}.
     * </p>
     *
     * @param tableA        the first JTable to combine rows from
     * @param tableB        the second JTable to combine rows from
     * @param columnsA      the indices of columns to extract from {@code tableA}
     *                      (length must be 5)
     * @param columnsB      the indices of columns to extract from {@code tableB}
     *                      (length must be 5)
     * @param combinedModel the DefaultTableModel to which the combined rows will be
     *                      added
     */
    private static void combineRows(
            JTable tableA, JTable tableB,
            int[] columnsA, int[] columnsB,
            DefaultTableModel combinedModel) {

        int numRows = Math.min(tableA.getRowCount(), tableB.getRowCount());

        for (int row = 0; row < numRows; row++) {
            Object[] combinedRow = new Object[10];

            for (int i = 0; i < 5; i++) {
                combinedRow[i] = tableA.getValueAt(row, columnsA[i]);
                combinedRow[5 + i] = tableB.getValueAt(row, columnsB[i]);
            }

            combinedModel.addRow(combinedRow);
        }
    }
}
