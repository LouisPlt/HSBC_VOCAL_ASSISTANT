package answerPrivateQuestion;

import config.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankAdvisor implements AnswerPrivateQuestion {

	@Override
	public String getTextResponse(String clientLogin) throws SQLException {
		Connection connection =DatabaseConnector.getConnection();
		Statement stat = connection.createStatement();
		ResultSet result = stat.executeQuery("SELECT ad.lastname, ad.firstname\n" +
				"FROM advisors ad\n" +
				"JOIN agencies a ON ad.agency_id = a.id\n" +
				"JOIN clients c ON a.id = c.agency_id\n" +
				"WHERE CAST( c.login AS INTEGER) = " + clientLogin);
		result.next();
		return "Your bank advisor is "+ result.getString("lastname") +' ' + result.getString("firstname");
	}

}
