package answerPrivateQuestion;

import java.sql.SQLException;

public interface AnswerPrivateQuestion {
	String getTextResponse(String clientLogin) throws SQLException;
}
