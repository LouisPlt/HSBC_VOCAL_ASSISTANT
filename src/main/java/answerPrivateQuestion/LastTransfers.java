package answerPrivateQuestion;

import java.sql.ResultSet;

import config.DatabaseConnector;

public class LastTransfers {
	
	public static String getTextResponse(String clientLogin, int nbOfTransfers) throws Exception {
		String query =  "SELECT t.amount, t.description, a.account_type_id, a.id, t.sender_account_id " +
				"FROM transfers t " +
				"JOIN accounts a " +
				"ON t.sender_account_id = a.id OR t.receiver_account_id = a.id " +
				"WHERE a.client_id IN (SELECT id FROM clients WHERE CAST(login AS INTEGER) = " + clientLogin + ")" +
				"ORDER BY t.date DESC";
		ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery(query);
		if(nbOfTransfers == 1){
			return buildResponse(result, "Your last operation is : ", nbOfTransfers);
		}
		return buildResponse(result, "Your last operations are : ", nbOfTransfers);
	}
		
	public static String getTextResponse(String clientLogin, String date) throws Exception {
		String query =  "SELECT t.amount, t.description, a.account_type_id, a.id, t.sender_account_id " +
				"FROM transfers t " +
				"JOIN accounts a " +
				"ON t.sender_account_id = a.id OR t.receiver_account_id = a.id " +
				"WHERE a.client_id IN (SELECT id FROM clients WHERE CAST(login AS INTEGER) = " + clientLogin + ")" +
				"AND t.date::date = date '" + date + "'" +
				"ORDER BY t.date DESC";
		ResultSet result = DatabaseConnector.getConnection().createStatement().executeQuery(query);
		
		return buildResponse(result, "The " + date + ", ", 10);
	}
	
	private static String buildResponse(ResultSet result, String response, int nbOfTransfers) throws Exception {		
		int i = 0;
		while(result.next() && (i < nbOfTransfers) && (i < 10)){	
			String[] description = result.getString("description").split(" ");
			String transfertType = description[0];
		
			switch(transfertType){
				case "CB": 
					response += "Your account has been debited of " + result.getString("amount") + " euros from " +  description[1];
					break;
				case "PRLV": 
					response += "You have been charged " + result.getString("amount") + " euros from " +  description[1];
					break;
				case "RETRAIT":
					response += "You have withdrawn " + result.getString("amount") + " euros";
					break;
				case "VIREMENT":
					if(result.getString("sender_account_id") == result.getString("id")){
						response += "You have received a transfer of  " + result.getString("amount") + " euros";
					}
					else {
						response += "You have made a transfer of " + result.getString("amount") + " euros";
					}
					break;
				default: 
					response += "Your account has been charged of " + result.getString("amount") + " euros";
					break;
			}
			i++;
			response+=". ";
		}
		if(i >= 10){
			response += "If you want to have more than 10 reponses, go check your transfers on the HSBC website.";
		} else if (i == 0) {
			response += "You have no bank operations.";
		}
		return response;
	}
}
