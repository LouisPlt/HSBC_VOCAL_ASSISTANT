package application;


import java.sql.SQLException;

import org.joda.time.DateTime;

import config.DatabaseConnector;

public class Authentification {
	private String login;
	private String password;
	private String sessionStartTime;
	private Boolean succeeded = false;
	private String reasonOfFailure = "";
	
	public Authentification(String login, String password){
		this.login = login;
		this.password = password;
	}
	public void checkPassword() throws SQLException{
	 if(login != null ){
	 	String passwordFromDB = DatabaseConnector.getClientPassword(login);
	 	System.out.println("Password from DB ="+passwordFromDB);
		if(passwordFromDB.equals(password)){
			sessionStartTime = DateTime.now().getYear()+"."+ DateTime.now().getDayOfYear() +"."+ DateTime.now().getSecondOfDay();
			succeeded = true;
		} else
			reasonOfFailure = "Your password is incorrect";
		
	   } else
		   reasonOfFailure = "I need your login first";
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSessionStartTime() {
		return sessionStartTime;
	}
	public void setSessionStartTime(String sessionStartTime) {
		this.sessionStartTime = sessionStartTime;
	}
	public Boolean isSucceeded() {
		return succeeded;
	}
	public void setSucceeded(Boolean succeeded) {
		this.succeeded = succeeded;
	}
	public String getReasonOfFailure() {
		return reasonOfFailure;
	}
	public void setReasonOfFailure(String reasonOfFailure) {
		this.reasonOfFailure = reasonOfFailure;
	}
	
	


	

}
