package actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import GUI.PrincipalGUI;

public class OpenAction implements ActionListener{

	private JFileChooser fc;
	private PrincipalGUI gui;

	public OpenAction(PrincipalGUI gui) {
		this.gui = gui;
	}
	
	public void actionPerformed(ActionEvent e) {
		JFrame f1 = new JFrame();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int scelta = fc.showOpenDialog(f1);
		
		if (scelta == JFileChooser.APPROVE_OPTION) {
			File dirScelta = fc.getSelectedFile();
			//creare classe per il rename e metterci tutti i campi che servono
			//es: dirScelta serve x rename!
			gui.getPathField().setText(dirScelta.getPath());
		}

	}
}
