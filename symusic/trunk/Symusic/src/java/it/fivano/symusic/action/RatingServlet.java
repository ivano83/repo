package it.fivano.symusic.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RatingServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	public RatingServlet() {
        // TODO Auto-generated constructor stub
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		id = "ID "+request.getParameter("id");
		if(request.getParameter("id").toString().equals("")){
		id="ID unknown";
		}
		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(id); 
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
