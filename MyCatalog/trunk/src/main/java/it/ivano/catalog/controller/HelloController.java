package it.ivano.catalog.controller;

import java.util.ArrayList;
import java.util.List;

import it.ivano.catalog.backend.dao.MimeTypeDao;
import it.ivano.catalog.backend.dto.MimeType;
import it.ivano.filecatalog.exception.FileDataException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/hello")
public class HelloController{
	
	@Autowired
	private MimeTypeDao dao;
 
	@RequestMapping(method = RequestMethod.GET)
	public String printHello(ModelMap model) {

		List<MimeType> mtlist = new ArrayList<MimeType>();
		try {
			mtlist = dao.getAllMimeType();
		} catch (FileDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("message", "Hello Spring MVC Framework!");
		model.addAttribute("mtlist", mtlist);
		return "index";
	}
   
}
