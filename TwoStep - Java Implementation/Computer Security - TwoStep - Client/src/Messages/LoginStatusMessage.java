package Messages;

import java.io.Serializable;

/* 2 different uses:
 * 1. Negative/Positive feedback from the server for the entire login process.
 * 2. Timeout occurred during the login process. Must be started anew.
 */
public class LoginStatusMessage implements Serializable
{
	private static final long serialVersionUID = -2747690492270934052L;

	private Boolean login_successful;
	private String additional_information;
	public Boolean getLoginStatus() {
		return login_successful;
	}
	public void setLoginStatus(Boolean login_successful) {
		this.login_successful = login_successful;
	}
	
	public String toString()
	{ 
		return additional_information;
	}
	public void setLoginStatusString(String additional_information)
	{
		this.additional_information = additional_information;
	}
}
