package Messages_New;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/* 3 different uses:
 * 1. Negative feedback from the server for a single registration step. Will request certain fixes for the specific step.
 * 2. Negative/Positive feedback from the server for the entire registration process. Necessary message in this case.
 * 3. Timeout occurred during the registration process. Must be started anew.
 */
public class RegisterStatusMessage implements Serializable
{
	private static final long serialVersionUID = -6913112605166815897L;

	private Boolean register_succesful;
	private ArrayList<Exception> register_exceptions;
}
