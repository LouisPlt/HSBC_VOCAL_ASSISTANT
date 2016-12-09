package answer;

import config.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankBalance implements Answer{

	@Override
	public String getTextResponse() throws SQLException {
		Connection connection = DatabaseConnector.getConnection();
		Statement stat = connection.createStatement();
		ResultSet result = stat.executeQuery("SELECT a.balance\n" +
				"FROM accounts a\n" +
				"JOIN clients c ON a.client_id = c.id\n" +
				"WHERE c.id = 10");
		result.next();
		return "You have "+ result.getString("balance") +" euros on your account ";
	}

}
