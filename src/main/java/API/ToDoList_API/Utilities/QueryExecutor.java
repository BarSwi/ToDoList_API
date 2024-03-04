package API.ToDoList_API.Utilities;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Apparently since the spring framework offers it`s own queryExecutor this whole class won't be used, but I will leave it there only to
 * notice the difference in framework usage later on.
 * Most likely Hibernate would handle connetion to database as well so every connection method would not be used.
 */

public class QueryExecutor {

    //Current placeholder symbol for Prepared Statements
    private static final char PLACEHOLDER_SYMBOL = '?';

    //private static DatabaseHandler db = DatabaseHandler.getInstance();


    /**
     * Executes a SQL statement with the provided parameters.
     *
     * @param sql    The SQL statement to be executed.
     * @param params The parameters to be set in the prepared statement. Method allows to not provide the second argument.
     */
    public static void executeStatement(String sql, @Nullable Object... params){
        Connection connection = getDbConnection();

        //try-with-resources for preparedStatement
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParams(sql, preparedStatement, params);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try{
                connection.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }

    /**
     * Executes a SQL statement with the provided parameters.
     *
     * @param sqlStatements  Array of SQL statements to be executed in transaction.
     * @param paramsList Objects that represent the values to prepared statement. Method allows to not provide the second argument.
     */
    public static void executeStatementsTransaction(String[] sqlStatements, Object[]... paramsList){
        Connection connection = getDbConnection();

        try {
            connection.setAutoCommit(false);  // Start transaction
            Object[] params;
            String sql;
            for (int i = 0; i < sqlStatements.length; i++) {
                sql = sqlStatements[i];
                params = (paramsList != null && i < paramsList.length) ? paramsList[i] : null;


                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    setParams(sql, preparedStatement, params);
                    preparedStatement.execute();
                }
            }

            connection.commit();  // Commit the transaction

        } catch (SQLException e) {
            try {
                connection.rollback();  // Rollback the transaction in case of an exception
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();  // Handle rollback exception
            }
            e.printStackTrace();  // Log or handle the exception
        } finally {
            try {
                connection.setAutoCommit(true);  // Reset auto-commit to true
                connection.close();  // Close the connection (or return it to the connection pool, if applicable)
            } catch (SQLException closeException) {
                closeException.printStackTrace();  // Handle close exception
            }
        }
    }


    /**
     * Sets parameters in a PreparedStatement, ensuring the length of params matches the number of placeholders.
     *
     * @param sql String that represents preparedStatement for the validation.
     * @param preparedStatement The prepared statement to set parameters for.
     * @param params            The parameters to set.
     * @throws IllegalArgumentException If the length of params does not match the number of placeholders.
     */
    private static void setParams(String sql, PreparedStatement preparedStatement, Object... params) throws IllegalArgumentException{

        //No need to set params if the array is empty
        if(params==null || params.length==0) return;

        if(countPlaceholders(sql) != params.length){
            throw new IllegalArgumentException(
                    "Invalid number of parameters. Expected: " + countPlaceholders(sql) + ", Actual: " + params.length
            );
        }

        for(int i =0; i<params.length;++i){
            try{
                preparedStatement.setObject(i + 1, params[i]);
            }
            catch(SQLException e){
                e.printStackTrace();
                break;
            }
        }
    }


    /**
     * Counts the number of placeholders in a prepared statement's SQL.
     * Placeholders are represented by the '?' symbol. This count is important for ensuring that the
     * number of parameters provided matches the number of placeholders when setting values in a
     * PreparedStatement using the setParams method.
     *
     * @param sql String of prepared statement to count placeholders for.
     * @return The number of placeholders in the SQL.
     */
    private static int countPlaceholders(String sql){
        int count = 0;

        //Count Placeholder Symbols in String
        for(int i = 0; i < sql.length(); ++i){
            if(sql.charAt(i) == PLACEHOLDER_SYMBOL) count++;
        }
        return count;
    }

    /**
     * Gets a database connection from the DatabaseHandler singleton.
     *
     * @return The database connection.
     */

    private static Connection getDbConnection(){
        //db.connect();
        //return db.getConnection();
        return null;
    }
}
