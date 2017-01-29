package answerPrivateQuestion;

import java.sql.ResultSet;
import java.sql.SQLException;

import config.DatabaseConnector;

public class BankAdvisor implements AnswerPrivateQuestion {

	@Override
	public String getTextResponse(String clientLogin) throws SQLException {
		ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery("SELECT ad.lastname, ad.firstname\n" +
				"FROM advisors ad\n" +
				"JOIN agencies a ON ad.agency_id = a.id\n" +
				"JOIN clients c ON a.id = c.agency_id\n" +
				"WHERE CAST( c.login AS INTEGER) = " + clientLogin);
		result.next();
		return "Your bank advisor is "+ result.getString("lastname") +' ' + result.getString("firstname");
	}

}
