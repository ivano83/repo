import java.awt.Checkbox;
import java.awt.CheckboxGroup;

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
public class OpzioniRubrica {

	private Language language;
	private JPanel pannelloOpzioniRubrica;
	private CheckboxGroup cbgRubrica;

	/**
	 * @param language
	 */
	public OpzioniRubrica(Language l) {
		this.language = l;
//		pannello OPZIONI per la rubrica
		pannelloOpzioniRubrica = new JPanel();
		pannelloOpzioniRubrica.setLayout(new BoxLayout(pannelloOpzioniRubrica, javax.swing.BoxLayout.Y_AXIS));
		pannelloOpzioniRubrica.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), language.getText("86"), 2, 0));
		this.cbgRubrica = new CheckboxGroup();
		pannelloOpzioniRubrica.add(new Checkbox(language.getText("28") + ".txt", this.cbgRubrica, true));
		pannelloOpzioniRubrica.add(new Checkbox(language.getText("28") + ".xls", this.cbgRubrica, false));

	}

	/**
	 * @return
	 */
	public JPanel getPannelloOpzioniRubrica() {
		return this.pannelloOpzioniRubrica;
	}
	/**
	 * @return
	 */
	public CheckboxGroup getCheckboxFormato() {
		return this.cbgRubrica;
	}
}
