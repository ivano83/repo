package it.fivano.symusic.action;

import it.fivano.symusic.core.Release0DayMusicService;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ZeroDayMusicServlet
 */

public class ZeroDayMusicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ZeroDayMusicServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String site = request.getParameter("site");
			String genre = request.getParameter("genre");
			Date initDate = null;
			Date endDate = null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String iDate = request.getParameter("initDate");
			String eDate = request.getParameter("endDate");
			
			// se non presente le date sono inizializzate alla data corrente
			initDate = (iDate==null || iDate.isEmpty())? new Date() : sdf.parse(iDate);
			endDate = (eDate==null || eDate.isEmpty())? new Date() : sdf.parse(eDate);

			List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
			if(site.equals("1")) {
				Release0DayMusicService zeroDay = new Release0DayMusicService();
				listRelease = zeroDay.parse0DayMusicRelease(genre, initDate, endDate);
			}
			
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

}
