package answerPrivateQuestion;

import config.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankBalance implements AnswerPrivateQuestion {

	@Override
	public String getTextResponse(String clientLogin) throws SQLException {
		Connection connection = DatabaseConnector.getConnection();
		Statement stat = connection.createStatement();
		ResultSet result = stat.executeQuery("SELECT a.balance\n" +
				"FROM accounts a\n" +
				"JOIN clients c ON a.client_id = c.id\n" +
				"WHERE CAST( c.login AS INTEGER) = " + clientLogin);
		result.next();
		return "You have "+ result.getString("balance") +" euros on your account ";
	}

}
