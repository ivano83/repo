package it.fivano.symusic.action;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.UserService;
import it.fivano.symusic.core.Release0DayMp3Service;
import it.fivano.symusic.core.Release0DayMusicService;
import it.fivano.symusic.core.ReleaseFromPreDbService;
import it.fivano.symusic.core.ReleaseFromPresceneService;
import it.fivano.symusic.core.ReleaseMusicDLService;
import it.fivano.symusic.core.ReleaseSiteService.SearchType;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.UserModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class ZeroDayMusicServlet extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String urlPrecedente;
	private String urlSuccessivo;

	private String reload;

    /**
     * Default constructor.
     */
    public ZeroDayMusicServlet() {
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


			String site = request.getParameter("site");
			String genre = request.getParameter("genre");
			String crew = request.getParameter("crew");
			Date initDate = null;
			Date endDate = null;

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String iDate = request.getParameter("initDate");
			String eDate = request.getParameter("endDate");

			String annoDa = request.getParameter("annoDa");
			String annoAl = request.getParameter("annoAl");

			String enableBeatport = request.getParameter("enableBeatport");
			String excludeReleaseRip = request.getParameter("excludeRelaseRip");
			String excludeReleaseVA = request.getParameter("excludeVA");
			boolean flagBeatport = (enableBeatport!=null && enableBeatport.equalsIgnoreCase("true"))?true:false;
			boolean flagRip = (excludeReleaseRip!=null && excludeReleaseRip.equalsIgnoreCase("true"))?true:false;
			boolean flagVA = (excludeReleaseVA!=null && excludeReleaseVA.equalsIgnoreCase("true"))?true:false;

			// se non presente le date sono inizializzate alla data corrente
			initDate = (iDate==null || iDate.isEmpty())? sdf.parse(sdf.format(new Date())) : sdf.parse(iDate);
			endDate = (eDate==null || eDate.isEmpty())? sdf.parse(sdf.format(new Date())) : sdf.parse(eDate);

			urlPrecedente = request.getRequestURI()+"?site="+site+"&genre="+genre+"&initDate="+sdf.format(SymusicUtility.sottraiData(initDate, 2))+"&endDate="+sdf.format(SymusicUtility.sottraiData(initDate, 1));
			urlPrecedente += "&enableBeatport="+flagBeatport+"&excludeRelaseRip="+flagRip;
			urlSuccessivo = request.getRequestURI()+"?site="+site+"&genre="+genre+"&initDate="+sdf.format(SymusicUtility.aggiungiData(endDate, 1))+"&endDate="+sdf.format(SymusicUtility.aggiungiData(endDate, 2));
			urlSuccessivo += "&enableBeatport="+flagBeatport+"&excludeRelaseRip="+flagRip;

			request.setAttribute("urlPrecedente", urlPrecedente);
			request.setAttribute("urlSuccessivo", urlSuccessivo);

			List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();

			reload = request.getParameter("reload");
			if(reload!=null && reload.length()>0) {
				listRelease = (List<ReleaseModel>) request.getSession().getAttribute("listRelease");
			} else {

				if(site.equals("1")) {
					Release0DayMusicService zeroDay = new Release0DayMusicService(user.getId());
					zeroDay.setEnableBeatportService(flagBeatport);
					zeroDay.setExcludeRipRelease(flagRip);
					zeroDay.setExcludeVA(flagVA);
					zeroDay.setAnnoDa(annoDa);
					zeroDay.setAnnoAl(annoAl);
					listRelease = zeroDay.parse0DayMusicRelease(genre, initDate, endDate);
				} else if(site.equals("2")) {
					Release0DayMp3Service zeroDay = new Release0DayMp3Service(user.getId());
					zeroDay.setEnableBeatportService(flagBeatport);
					zeroDay.setExcludeRipRelease(flagRip);
					zeroDay.setExcludeVA(flagVA);
					zeroDay.setAnnoDa(annoDa);
					zeroDay.setAnnoAl(annoAl);
					listRelease = zeroDay.parse0DayMp3Release(genre, initDate, endDate);
				} else if(site.equals("3")) {
					ReleaseMusicDLService musicDL = new ReleaseMusicDLService(user.getId());
					musicDL.setEnableBeatportService(flagBeatport);
					musicDL.setExcludeRipRelease(flagRip);
					musicDL.setExcludeVA(flagVA);
					musicDL.setAnnoDa(annoDa);
					musicDL.setAnnoAl(annoAl);
					listRelease = musicDL.parseMusicDLRelease(genre, initDate, endDate);
				} else if(site.equals("4") || site.equals("4a")) {
					ReleaseFromPresceneService musicDL = new ReleaseFromPresceneService(user.getId());
					musicDL.setEnableBeatportService(flagBeatport);
					musicDL.setExcludeRipRelease(flagRip);
					musicDL.setExcludeVA(flagVA);
					musicDL.setAnnoDa(annoDa);
					musicDL.setAnnoAl(annoAl);
					if(site.equals("4"))
						listRelease = musicDL.parsePresceneRelease(genre, initDate, endDate, SearchType.SEARCH_GENRE);
					else
						listRelease = musicDL.parsePresceneRelease(crew, initDate, endDate, SearchType.SEARCH_CREW);
				} else if(site.equals("5")) {
					ReleaseFromPreDbService musicDL = new ReleaseFromPreDbService(user.getId());
					musicDL.setEnableBeatportService(flagBeatport);
					musicDL.setExcludeRipRelease(flagRip);
					musicDL.setExcludeVA(flagVA);
					musicDL.setAnnoDa(annoDa);
					musicDL.setAnnoAl(annoAl);
					listRelease = musicDL.parsePreDbRelease(crew, initDate, endDate, SearchType.SEARCH_CREW);
				}
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

	public String getReload() {
		return reload;
	}

	public void setReload(String reload) {
		this.reload = reload;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(new String("Ferry Tayle ft. Sarah Shields – The Most Important Thing (Club".getBytes(),"ISO-8859-1"));
	}

}
