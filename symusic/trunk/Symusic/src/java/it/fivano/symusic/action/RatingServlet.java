package it.fivano.symusic.action;

import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RatingServlet  extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	public RatingServlet() {
		this.setLogger(getClass());
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		id = "ID "+request.getParameter("id")  +  "   "+request.getParameter("vote");
		if(request.getParameter("id").toString().equals("")){
		id="ID unknown";
		}
		
		List<ReleaseModel> listRelease = (List<ReleaseModel>) request.getSession().getAttribute("listRelease");
		for(ReleaseModel r : listRelease) {
			if(r.getId().longValue()==Long.parseLong(request.getParameter("id"))){
				r.setVoteAverage((r.getVoteAverage()+Integer.parseInt(request.getParameter("vote"))/2));
			}
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
