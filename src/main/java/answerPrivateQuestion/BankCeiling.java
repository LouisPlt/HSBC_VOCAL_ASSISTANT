package answerPrivateQuestion;

import java.sql.ResultSet;
import java.sql.SQLException;

import config.DatabaseConnector;

/**
 * Created by louis on 05/12/16.
 */
public class BankCeiling implements AnswerPrivateQuestion {

    @Override
    public String getTextResponse(String clientLogin) throws SQLException {
        ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery("SELECT at.kind, at.celling\n" +
                "FROM accounts a\n" +
                "JOIN clients c ON a.client_id = c.id\n" +
                "JOIN account_types at ON a.account_type_id = at.id\n" +
                "WHERE CAST( c.login AS INTEGER) = " + clientLogin);
        result.next();
        return "The ceiling of your "+ result.getString("kind") +" is " + result.getString("celling")+" euros";
    }
}
