package answerPrivateQuestion;

import java.sql.ResultSet;
import java.sql.SQLException;

import config.DatabaseConnector;

public class MaxBankOverdraft implements AnswerPrivateQuestion {

	@Override
	public String getTextResponse(String clientLogin) throws SQLException {
        ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery("SELECT a.overdraft_value_max\n" +
                "FROM accounts a\n" +
                "JOIN clients c ON a.client_id = c.id\n" +
                "WHERE CAST( c.login AS INTEGER) = " + clientLogin);
        result.next();
        return "Your maximum overdraft is "+ result.getString("overdraft_value_max")+" euros";
	}
}
