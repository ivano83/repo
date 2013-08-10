import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/*
 * Created on 29-nov-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OpzioniSms {

	private Language language;
	private JPanel pannelloOpzioniSms;
	private CheckboxGroup cbgSms;
	private Checkbox checkBoxAssociaRubrica;

	public OpzioniSms(Language l) {
		this.language = l;
		//pannello OPZIONI per gli sms
		pannelloOpzioniSms = new JPanel();
		pannelloOpzioniSms.setLayout(new BoxLayout(pannelloOpzioniSms, javax.swing.BoxLayout.Y_AXIS));
		JPanel pannelloFormatoSms = new JPanel(); //primo pannello
		pannelloFormatoSms.setLayout(new GridLayout(2,1,5,5));
		pannelloFormatoSms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), language.getText("86"), 2, 0));
		this.cbgSms = new CheckboxGroup();
		pannelloFormatoSms.add(new Checkbox(language.getText("28") + ".txt", this.cbgSms, true));
		pannelloFormatoSms.add(new Checkbox(language.getText("28") + ".xls", this.cbgSms, false));
		JPanel pannelloAssociaRubricaSms = new JPanel(); //secondo pannello
		pannelloAssociaRubricaSms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Associa Rubrica", 2, 0));
		this.checkBoxAssociaRubrica = new Checkbox("Associa i nomi in rubrica con i numeri di telefono negli sms", null, false);
		pannelloAssociaRubricaSms.add(checkBoxAssociaRubrica);
		
		pannelloOpzioniSms.add(pannelloFormatoSms);
		pannelloOpzioniSms.add(pannelloAssociaRubricaSms);
		
	}
	public JPanel getPannelloOpzioniSms() {
		return this.pannelloOpzioniSms;
	}
	public CheckboxGroup getCheckboxFormato() {
		return this.cbgSms;
	}
	public Checkbox getCheckBoxAssociaRubrica() {
		return this.checkBoxAssociaRubrica;
	}

}
