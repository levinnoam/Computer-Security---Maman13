package Messages_New;

import java.io.Serializable;
import java.util.ArrayList;

/* 3 different uses:
 * 1. Negative feedback from the server for a single registration step. Will request certain fixes for the specific step.
 * 2. Negative/Positive feedback from the server for the entire registration process. Necessary message in this case.
 * 3. Timeout occurred during the registration process. Must be started anew.
 */
public class RegistrationStatusMessage implements Serializable
{
	private static final long serialVersionUID = -6913112605166815897L;

	private Boolean registration_succesful;
	private ArrayList<Exception> registration_exceptions;
	
	public Boolean getRegistrationStatus() {
		return registration_succesful;
	}
	public void setRegisteratiionStatus(Boolean register_succesful) {
		this.registration_succesful = register_succesful;
	}
	
	public ArrayList<Exception> getRegistrationExceptions()
	{
		return registration_exceptions;
	}
	public void setRegistrationExceptions(ArrayList<Exception> registration_exceptions)
	{
		this.registration_exceptions = registration_exceptions;
	}
}
