package it.fivano.symusic.action;

import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.backend.service.UserService;
import it.fivano.symusic.core.parser.Mp3TrackzParser;
import it.fivano.symusic.core.parser.MusicDLParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.UserModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LastReleaseDateServlet extends BaseAction {

	private static final long serialVersionUID = -9223053923179439047L;
	
	
	public LastReleaseDateServlet() {
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

			
			String genreInput = request.getParameter("genre");
			
			GenreService genreServ = new GenreService();
			GenreModel genre = genreServ.getGenreByName(genreInput);
			
			Date lastReleaseDate = new ReleaseService().getLastReleaseDate(genre.getId());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			response.setContentType("text/plain");  
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().write(sdf.format(lastReleaseDate)); 
			
			
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
