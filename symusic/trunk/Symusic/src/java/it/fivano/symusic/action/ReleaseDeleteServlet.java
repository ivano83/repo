package it.fivano.symusic.action;

import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReleaseDeleteServlet  extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	public ReleaseDeleteServlet() {
		this.setLogger(getClass());
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		id = "ID "+request.getParameter("id")  +  "   "+request.getParameter("vote");
		if(request.getParameter("id").toString().equals("")){
			id="ID unknown";
		}
		
		System.out.println("ReleaseDeleteServlet con id "+id);
		
		Long idRelease = request.getParameter("id")!=null ? Long.parseLong(request.getParameter("id")) : null;
		
		try {
			if(idRelease!=null && false==true)
				new ReleaseService().deleteReleaseFull(idRelease);
		} catch (BackEndException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
