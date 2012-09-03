package webFrame.app.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ListenerApp implements ServletContextListener {
	public void contextInitialized(ServletContextEvent arg0) {
		contextInit(arg0);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	protected void contextInit(ServletContextEvent arg0) {
	}
}
