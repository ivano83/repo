package it.ivano.catalog.controller;

import it.ivano.catalog.backend.dao.UtenteDao;
import it.ivano.catalog.backend.dto.Utente;
import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.utility.logging.MyLogger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class Home {
	
	@Autowired
	private UtenteDao dao;
	
	private static String mailStatic = "ivano83@tiscali.it";
	
	private MyLogger logger = new MyLogger(getClass());
 
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView homeLogin(HttpSession session) throws FileDataException {

		ModelAndView mv = new ModelAndView();
		Utente utente = null;
		try {
			utente = dao.getUtente(mailStatic);
			
			session.setAttribute("utente", utente);
			
			mv.addObject("message", "Benvenuto");
			mv.setViewName("home");
			
			
		} catch (FileDataException e) {
			mv.setViewName("error");
			logger.error("Errore nel recupero dell'utente cercato", e);
		}
		return mv;
		
	}
   
}
