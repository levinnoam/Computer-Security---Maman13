/*
 * Noam Levin 	- 	308334424
 * Kfir Fleischer - 311601140
 */

package Messages;

import java.io.Serializable;

public class UsernamePasswordMessage implements Serializable
{
	private static final long serialVersionUID = -168514867542181336L;

	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
