package it.ivano.catalog.controller;

import java.util.List;

import javax.validation.Valid;

import it.ivano.catalog.backend.dao.MimeTypeDao;
import it.ivano.catalog.backend.dto.MimeType;
import it.ivano.catalog.backend.form.SearchMimeForm;
import it.ivano.filecatalog.exception.FileDataException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SearchMimeType extends BaseController {

	@Autowired
	private MimeTypeDao daoMime;
	
	public SearchMimeType() {
		super(SearchMimeType.class);
	}
	
	@RequestMapping(value="search-mime", method=RequestMethod.GET)
	public String searchMimeHome(@ModelAttribute("mimeForm") SearchMimeForm mimeForm, BindingResult result, Model model) {
		model.addAttribute("mimeForm", mimeForm);
	    return "search_mime";
	}
	
	@RequestMapping(value="/search-mime-res", method=RequestMethod.POST)
	public String searchMime(@Valid @ModelAttribute("mimeForm") SearchMimeForm mimeForm, BindingResult result, Model model) {
		
		
		try {
			logger.info("Searching... "+mimeForm.getText());
			
			if(result.hasErrors()) {
				model.addAttribute("mimeForm", mimeForm);
				return "search_mime";
			}
			
			List<MimeType> mtList = daoMime.getListaMimeTypeByFreeText(mimeForm.getText());
			logger.info("Result Size: "+mtList.size());
			
			
		} catch (FileDataException e) {
			model.addAttribute("mimeForm", mimeForm);
			return "redirect:/search-mime";
		}
		
	    
	    return "redirect:/search-mime";
	}
}
