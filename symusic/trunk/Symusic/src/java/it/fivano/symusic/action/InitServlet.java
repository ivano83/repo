package it.fivano.symusic.action;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.core.BeatportService;
import it.fivano.symusic.core.ReleaseScenelogService;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7330044353124475893L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Set<String> genreList = null;
			if(request.getSession().getAttribute("genreList")!=null) {
				genreList = (Set<String>) request.getSession().getAttribute("genreList");
			} else {
				Map<String,String> res = new BeatportService().getGenreList();
				genreList = res.keySet();
				request.getSession().setAttribute("genreList", genreList);
				request.getSession().setAttribute("genreMap", res);
			}
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
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
	

}
