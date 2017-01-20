package answerPrivateQuestion;

import config.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by louis on 05/12/16.
 */
public class BankCeiling implements AnswerPrivateQuestion {

    @Override
    public String getTextResponse(String clientLogin) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        Statement stat = connection.createStatement();
        ResultSet result = stat.executeQuery("SELECT at.kind, at.celling\n" +
                "FROM accounts a\n" +
                "JOIN clients c ON a.client_id = c.id\n" +
                "JOIN account_types at ON a.account_type_id = at.id\n" +
                "WHERE CAST( c.login AS INTEGER) = " + clientLogin);
        result.next();
        return "The ceiling of your "+ result.getString("kind") +" is " + result.getString("celling")+" euros";
    }
}
