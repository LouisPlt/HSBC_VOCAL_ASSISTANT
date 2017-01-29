package answerPrivateQuestion;

import java.sql.ResultSet;
import java.sql.SQLException;

import config.DatabaseConnector;

public class BankBalance implements AnswerPrivateQuestion {

	@Override
	public String getTextResponse(String clientLogin) throws SQLException {
		ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery("SELECT a.balance\n" +
				"FROM accounts a\n" +
				"JOIN clients c ON a.client_id = c.id\n" +
				"WHERE CAST( c.login AS INTEGER) = " + clientLogin);
		result.next();
		return "You have "+ result.getString("balance") +" euros on your account ";
	}

}
