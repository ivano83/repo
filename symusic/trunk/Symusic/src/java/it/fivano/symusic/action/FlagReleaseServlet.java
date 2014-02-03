package it.fivano.symusic.action;

import it.fivano.symusic.backend.service.ReleaseOptionService;
import it.fivano.symusic.backend.service.UserService;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.ReleaseFlagModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.UserModel;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FlagReleaseServlet  extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String idRelease;
	private String optionType;
	private String optionValue;
	
	public FlagReleaseServlet() {
        this.setLogger(getClass());
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Long idRelease = Long.parseLong(request.getParameter("idRelease"));
		optionType = request.getParameter("optionType");
		optionValue = request.getParameter("optionValue");
		
		try {
			UserModel user = null;
			if(request.getSession().getAttribute("user")!=null)
				user = (UserModel) request.getSession().getAttribute("user");
			else
				user = new UserService().getUser("ivano");

			ReleaseOptionService optServ = new ReleaseOptionService();
			ReleaseFlagModel flag = new ReleaseFlagModel();
			if(optionType!=null && optionType.equals("1")) {
				// preview
				flag.setPreview(true);
				optServ.saveOrUpdateReleaseOption(flag, idRelease, user.getId());
				log.info("Flag 'VISIONATO' per la release con id = "+idRelease);
			} else if(optionType!=null && optionType.equals("2")) {
				// preview
				flag.setDownloaded(true);
				optServ.saveOrUpdateReleaseOption(flag, idRelease, user.getId());
				log.info("Flag 'SCARICATO' per la release con id = "+idRelease);
			}
		} catch (BackEndException e) {
			e.printStackTrace();
		}

		response.setContentType("text/plain");  
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write(idRelease+""); 
		
	}

	public String getIdRelease() {
		return idRelease;
	}

	public void setIdRelease(String idRelease) {
		this.idRelease = idRelease;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	

}
