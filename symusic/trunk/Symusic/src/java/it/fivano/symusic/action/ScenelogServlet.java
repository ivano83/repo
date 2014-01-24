package it.fivano.symusic.action;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.ReleaseOptionService;
import it.fivano.symusic.backend.service.UserService;
import it.fivano.symusic.core.ReleaseScenelogService;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.UserModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class ScenelogServlet extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String urlPrecedente;
	private String urlSuccessivo;

    /**
     * Default constructor. 
     */
    public ScenelogServlet() {
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

			String[] genre = request.getParameterValues("genre");
			List<String> genreList = null;
			if(genre!=null) {
				genreList = Arrays.asList(genre);
			}
			Date initDate = null;
			Date endDate = null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String iDate = request.getParameter("initDate");
			String eDate = request.getParameter("endDate");
			
			String excludeReleaseRip = request.getParameter("excludeRelaseRip");
			boolean flagRip = (excludeReleaseRip!=null && excludeReleaseRip.equalsIgnoreCase("true"))?true:false;
			
			// se non presente le date sono inizializzate alla data corrente
			initDate = (iDate==null || iDate.isEmpty())? sdf.parse(sdf.format(new Date())) : sdf.parse(iDate);
			endDate = (eDate==null || eDate.isEmpty())? sdf.parse(sdf.format(new Date())) : sdf.parse(eDate);

			urlPrecedente = request.getRequestURI()+"?genre="+genre+"&initDate="+sdf.format(SymusicUtility.sottraiData(initDate, 2))+"&endDate="+sdf.format(SymusicUtility.sottraiData(initDate, 1));
			urlPrecedente += "&excludeRelaseRip="+flagRip;
			urlSuccessivo = request.getRequestURI()+"?genre="+genre+"&initDate="+sdf.format(SymusicUtility.aggiungiData(endDate, 1))+"&endDate="+sdf.format(SymusicUtility.aggiungiData(endDate, 2));
			urlSuccessivo += "&excludeRelaseRip="+flagRip;
			
			request.setAttribute("urlPrecedente", urlPrecedente);
			request.setAttribute("urlSuccessivo", urlSuccessivo);
			
			List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
			ReleaseScenelogService scenelog = new ReleaseScenelogService(genreList, user.getId());
			
			listRelease = scenelog.parseScenelogRelease(initDate, endDate);
			
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
