package it.fivano.symusic.action;

import it.fivano.symusic.backend.service.UserService;
import it.fivano.symusic.core.BeatportService;
import it.fivano.symusic.core.ReleaseBeatportService;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ZeroDayMusicServlet
 */

public class BeatportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String urlPrecedente;
	private String urlSuccessivo;

    /**
     * Default constructor. 
     */
    public BeatportServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			UserModel user = null;
			if(request.getSession().getAttribute("user")==null)
				user = (UserModel) request.getSession().getAttribute("user");
			else
				user = new UserService().getUser("ivano");

			
			Map<String,String> genreMap = null;
			if(request.getSession().getAttribute("genreMap")!=null) {
				genreMap = (Map<String,String>) request.getSession().getAttribute("genreMap");
			} else {
				genreMap = new BeatportService().getGenreList();
				request.getSession().setAttribute("genreMap", genreMap);
			}

			String genre = request.getParameter("genre");
			
			if(genre==null) {
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
				rd.forward(request, response);
			}
			
			String urlGenre = genreMap.get(genre);
			
			List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
			ReleaseBeatportService beatport = new ReleaseBeatportService(user.getId());
			
			listRelease = beatport.parseBeatportRelease(urlGenre, genre);
			
			request.getSession().setAttribute("listRelease", listRelease);
			
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/release_result.jsp");
			rd.forward(request, response);
			
			
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

	public String getUrlPrecedente() {
		return urlPrecedente;
	}

	public void setUrlPrecedente(String urlPrecedente) {
		this.urlPrecedente = urlPrecedente;
	}

	public String getUrlSuccessivo() {
		return urlSuccessivo;
	}

	public void setUrlSuccessivo(String urlSuccessivo) {
		this.urlSuccessivo = urlSuccessivo;
	}
	
	

}
