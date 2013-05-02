package beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import auth.Util;

import models.User;

@ManagedBean(name = "loginBean")
@SessionScoped
/**
 *
 * @author User
 */
public class LoginBean implements Serializable
{

	private static final long serialVersionUID = 1L;
	private String message, uname;
	private String password;

	public String getMessage()
	{
		return this.message;
	}

	public String getPassword()
	{
		return this.password;
	}

	public String getUname()
	{
		return this.uname;
	}

	public String loginProject()
	{
		boolean result = User.login(this.uname, this.password);
		if (result)
		{
			// get Http Session and store username
			HttpSession session = Util.getSession();
			session.setAttribute("username", this.uname);

			return "home";
		}
		else
		{

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Invalid Login!", "Please Try Again!"));

			// invalidate session, and redirect to other pages

			// message = "Invalid Login. Please Try Again!";
			return "login";
		}
	}

	public String logout()
	{
		HttpSession session = Util.getSession();
		session.invalidate();
		return "login";
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}
}
