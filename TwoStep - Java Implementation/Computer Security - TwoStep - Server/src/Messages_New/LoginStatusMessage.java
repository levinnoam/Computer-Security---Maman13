package Messages_New;

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
}
