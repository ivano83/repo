package it.fivano.symusic.action;

import javax.servlet.http.HttpServlet;

import it.fivano.symusic.MyLogger;

public class BaseAction extends HttpServlet {

	private static final long serialVersionUID = 6364939581740746342L;
	protected MyLogger log;
	
	protected void setLogger(Class<?> classe) {
		log = new MyLogger(classe);
	}
	
}
