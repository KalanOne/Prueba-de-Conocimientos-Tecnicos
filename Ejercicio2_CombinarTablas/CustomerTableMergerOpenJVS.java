import com.olf.openjvs.*;
import com.olf.openjvs.enums.COL_TYPE_ENUM;

/**
 * Utility class for merging two customer-related tables by selecting specific
 * columns from each
 * and combining them into a new table. The resulting table contains 10 columns:
 * 5 from the first
 * table (CustomerProfile) and 5 from the second table (CustomerTransactions),
 * each with prefixed names.
 * <p>
 * Usage:
 * <ul>
 * <li>Select exactly 5 columns from each input table using their column
 * indices.</li>
 * <li>The merge operation combines rows up to the minimum row count of the two
 * tables.</li>
 * <li>Supports columns of types: int, double, string, and date/time.</li>
 * </ul>
 * 
 * <b>Example:</b>
 * 
 * <pre>
 * int[] columnsA = { 1, 2, 3, 4, 5 };
 * int[] columnsB = { 1, 2, 3, 4, 5 };
 * Table merged = CustomerTableMergerOpenJVS.mergeCustomerTables(tableA, tableB, columnsA, columnsB);
 * </pre>
 * 
 * <b>Exceptions:</b>
 * <ul>
 * <li>IllegalArgumentException if tables or column arrays are null, or if
 * column indices are invalid.</li>
 * <li>OException for OpenJVS-specific errors.</li>
 * </ul>
 */
public class CustomerTableMergerOpenJVS {

    /**
     * Merges two customer tables into a single combined table based on the
     * specified columns.
     *
     * <p>
     * This method validates the input parameters, creates a new table with combined
     * columns from both input tables,
     * and merges the rows according to the specified column mappings.
     * </p>
     *
     * @param tableA   The first input table containing customer data.
     * @param tableB   The second input table containing customer data.
     * @param columnsA The indices of columns in tableA to be used for merging.
     * @param columnsB The indices of columns in tableB to be used for merging.
     * @return A new Table instance containing the merged data from both input
     *         tables.
     * @throws OException If any error occurs during validation, column creation, or
     *                    row combination.
     */
    public static Table mergeCustomerTables(
            Table tableA, Table tableB,
            int[] columnsA, int[] columnsB) throws OException {

        validateParameters(tableA, tableB, columnsA, columnsB);

        Table combinedTable = Table.tableNew();
        createCombinedColumns(tableA, tableB, columnsA, columnsB, combinedTable);

        combineRows(tableA, tableB, columnsA, columnsB, combinedTable);

        return combinedTable;
    }

    /**
     * Validates the input parameters for merging two tables.
     *
     * @param tableA   The first table to be validated. Must not be null.
     * @param tableB   The second table to be validated. Must not be null.
     * @param columnsA An array of column indices selected from tableA. Must not be
     *                 null and must contain exactly 5 elements.
     * @param columnsB An array of column indices selected from tableB. Must not be
     *                 null and must contain exactly 5 elements.
     * @throws IllegalArgumentException if any of the tables or column arrays are
     *                                  null,
     *                                  if the column arrays do not contain exactly
     *                                  5 elements,
     *                                  or if any column index is out of the valid
     *                                  range for its respective table.
     * @throws OException               if an OpenJVS-specific error occurs during
     *                                  validation.
     */
    private static void validateParameters(
            Table tableA, Table tableB,
            int[] columnsA, int[] columnsB) throws OException {

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
            if (colIndex <= 0 || colIndex > tableA.getNumCols()) {
                throw new IllegalArgumentException("Column index A out of range: " + colIndex);
            }
        }
        for (int colIndex : columnsB) {
            if (colIndex <= 0 || colIndex > tableB.getNumCols()) {
                throw new IllegalArgumentException("Column index B out of range: " + colIndex);
            }
        }
    }

    /**
     * Creates and adds combined columns to the specified combinedTable by merging
     * columns from tableA and tableB.
     * For each of the first 5 columns specified in columnsA and columnsB, this
     * method:
     * <ul>
     * <li>Adds a column to combinedTable with the prefix "CustomerProfile_"
     * followed by the original column name from tableA,
     * using the corresponding column type from tableA.</li>
     * <li>Adds a column to combinedTable with the prefix "CustomerTransactions_"
     * followed by the original column name from tableB,
     * using the corresponding column type from tableB.</li>
     * </ul>
     *
     * @param tableA        The first source table containing customer profile
     *                      columns.
     * @param tableB        The second source table containing customer transaction
     *                      columns.
     * @param columnsA      An array of column indices from tableA to be included in
     *                      the combined table.
     * @param columnsB      An array of column indices from tableB to be included in
     *                      the combined table.
     * @param combinedTable The table to which the combined columns will be added.
     * @throws OException If an error occurs while adding columns to the combined
     *                    table.
     */
    private static void createCombinedColumns(
            Table tableA, Table tableB,
            int[] columnsA, int[] columnsB,
            Table combinedTable) throws OException {

        for (int i = 0; i < 5; i++) {
            String originalNameA = tableA.getColName(columnsA[i]);
            combinedTable.addCol("CustomerProfile_" + originalNameA, tableA.getColType(columnsA[i]));

            String originalNameB = tableB.getColName(columnsB[i]);
            combinedTable.addCol("CustomerTransactions_" + originalNameB, tableB.getColType(columnsB[i]));
        }

        for (int i = 0; i < 5; i++) {
        }
    }

    /**
     * Combines rows from two tables into a single combined table by copying
     * specified columns.
     *
     * For each row up to the minimum number of rows in tableA and tableB, this
     * method:
     * <ul>
     * <li>Adds a new row to the combinedTable.</li>
     * <li>Copies five columns from tableA (specified by columnsA) into the first
     * five columns of the new row.</li>
     * <li>Copies five columns from tableB (specified by columnsB) into the next
     * five columns of the new row.</li>
     * </ul>
     *
     * @param tableA        The first source table.
     * @param tableB        The second source table.
     * @param columnsA      Array of column indices to copy from tableA.
     * @param columnsB      Array of column indices to copy from tableB.
     * @param combinedTable The table where combined rows will be added.
     * @throws OException If an error occurs during table operations.
     */
    private static void combineRows(
            Table tableA, Table tableB,
            int[] columnsA, int[] columnsB,
            Table combinedTable) throws OException {

        int numRows = Math.min(tableA.getNumRows(), tableB.getNumRows());

        for (int row = 1; row <= numRows; row++) {
            int newRow = combinedTable.addRow();

            for (int i = 0; i < 5; i++) {
                int colTypeA = tableA.getColType(columnsA[i]);
                copyCell(tableA, columnsA[i], row, combinedTable, i + 1, newRow, colTypeA);

                int colTypeB = tableB.getColType(columnsB[i]);
                copyCell(tableB, columnsB[i], row, combinedTable, i + 6, newRow, colTypeB);
            }
        }
    }

    /**
     * Copies a cell value from a source table to a target table, handling different
     * column types.
     *
     * @param source  The source {@link Table} from which to copy the cell value.
     * @param srcCol  The column index in the source table.
     * @param srcRow  The row index in the source table.
     * @param target  The target {@link Table} to which the cell value will be
     *                copied.
     * @param tgtCol  The column index in the target table.
     * @param tgtRow  The row index in the target table.
     * @param colType The type of the column, as an integer corresponding to
     *                {@link COL_TYPE_ENUM}.
     * @throws OException If an error occurs during the copy operation.
     */
    private static void copyCell(
            Table source, int srcCol, int srcRow,
            Table target, int tgtCol, int tgtRow,
            int colType) throws OException {

        switch (COL_TYPE_ENUM.fromInt(colType)) {
            case COL_INT:
                target.setInt(tgtCol, tgtRow, source.getInt(srcCol, srcRow));
                break;
            case COL_DOUBLE:
                target.setDouble(tgtCol, tgtRow, source.getDouble(srcCol, srcRow));
                break;
            case COL_STRING:
                target.setString(tgtCol, tgtRow, source.getString(srcCol, srcRow));
                break;
            case COL_DATE_TIME:
                target.setDateTime(tgtCol, tgtRow, source.getDateTime(srcCol, srcRow));
                break;
            default:
                target.setString(tgtCol, tgtRow, source.getString(srcCol, srcRow));
                break;
        }
    }
}
