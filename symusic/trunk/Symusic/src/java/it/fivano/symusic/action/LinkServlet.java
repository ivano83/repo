package it.fivano.symusic.action;

import it.fivano.symusic.backend.service.UserService;
import it.fivano.symusic.core.parser.Mp3TrackzParser;
import it.fivano.symusic.core.parser.MusicDLParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.model.UserModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LinkServlet extends BaseAction {

	private static final long serialVersionUID = 4439822158288675730L;

	public LinkServlet() {
		this.setLogger(getClass());
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			UserModel user = null;
			if(request.getSession().getAttribute("user")!=null)
				user = (UserModel) request.getSession().getAttribute("user");
			else
				user = new UserService().getUser("ivano");

			String releaseName = request.getParameter("release");
			String site = request.getParameter("site");
			
			String urlOut = "";
			if("SCENELOG".equalsIgnoreCase(site)) {
				urlOut = new ScenelogParser().getUrlRelease(releaseName);
			} else if("MP3_TRACKZ".equalsIgnoreCase(site)) {
				urlOut = new Mp3TrackzParser().getUrlRelease(releaseName);
			} else if("MUSIC_DL".equalsIgnoreCase(site)) {
				urlOut = new MusicDLParser().getUrlRelease(releaseName, request.getParameter("genre"));
			}
			
			
			response.sendRedirect(urlOut);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	
}
