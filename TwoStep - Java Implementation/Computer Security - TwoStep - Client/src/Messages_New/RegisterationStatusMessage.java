package Messages_New;

import java.io.Serializable;
import java.util.ArrayList;

/* 3 different uses:
 * 1. Negative feedback from the server for a single registration step. Will request certain fixes for the specific step.
 * 2. Negative/Positive feedback from the server for the entire registration process. Necessary message in this case.
 * 3. Timeout occurred during the registration process. Must be started anew.
 */
public class RegisterationStatusMessage implements Serializable
{
	private static final long serialVersionUID = -6913112605166815897L;

	private Boolean registeration_succesful;
	private ArrayList<Exception> registeration_exceptions;
	
	public Boolean getRegisterationStatus() {
		return registeration_succesful;
	}
	public void setRegisteratiionStatus(Boolean register_succesful) {
		this.registeration_succesful = register_succesful;
	}
	
	public ArrayList<Exception> getRegisterationExceptions()
	{
		return registeration_exceptions;
	}
	public void setRegisterationExceptions(ArrayList<Exception> registeration_exceptions)
	{
		this.registeration_exceptions = registeration_exceptions;
	}
}
