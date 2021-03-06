package it.fivano.symusic.action;

import it.fivano.symusic.SymusicUtility;
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
	
	private String urlPrecedente;
	private String urlSuccessivo;

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
			
			String enableBeatport = request.getParameter("enableBeatport");
			
			// se non presente le date sono inizializzate alla data corrente
			initDate = (iDate==null || iDate.isEmpty())? sdf.parse(sdf.format(new Date())) : sdf.parse(iDate);
			endDate = (eDate==null || eDate.isEmpty())? sdf.parse(sdf.format(new Date())) : sdf.parse(eDate);

			urlPrecedente = request.getRequestURI()+"?site="+site+"&genre="+genre+"&initDate="+sdf.format(SymusicUtility.sottraiData(initDate, 2))+"&endDate="+sdf.format(SymusicUtility.sottraiData(initDate, 1));
			urlSuccessivo = request.getRequestURI()+"?site="+site+"&genre="+genre+"&initDate="+sdf.format(SymusicUtility.aggiungiData(endDate, 1))+"&endDate="+sdf.format(SymusicUtility.aggiungiData(endDate, 2));

			request.setAttribute("urlPrecedente", urlPrecedente);
			request.setAttribute("urlSuccessivo", urlSuccessivo);
			
			List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
			if(site.equals("1")) {
				Release0DayMusicService zeroDay = new Release0DayMusicService();
				if(enableBeatport!=null && enableBeatport.equalsIgnoreCase("true"))
					zeroDay.setEnableBeatportService(true);
				else
					zeroDay.setEnableBeatportService(false);
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
