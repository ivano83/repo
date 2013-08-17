import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class Test {

	private JFileChooser fc;
	private File dirScelta;

	public Test() {
		fc = new JFileChooser();
		this.scegliDir();
		if(dirScelta != null)
			this.start();
		else
			System.out.println("Non hai selezionato niente");
	}

private void scegliDir() {
		JFrame f1 = new JFrame();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int scelta = fc.showOpenDialog(f1);
		
		if (scelta == JFileChooser.APPROVE_OPTION) {
			dirScelta = fc.getSelectedFile();
		}
	}

private void start() {
	String path = dirScelta.getAbsolutePath();
	File[] lista = dirScelta.listFiles();
	int i;
	String nomeCorrente;
	for(i=0; i<lista.length; i++) {
		try {
			File fileCorrente = lista[i];
			nomeCorrente = fileCorrente.getName();
			File newFile = new File("XXX/" + nomeCorrente + "111");
			
			fileCorrente.renameTo(newFile);
			
			//FileOutputStream discoFileStream = new FileOutputStream(newFile);
			//PrintStream disco = new PrintStream(discoFileStream); //Oggetto che punta al File
			//disco.println(nomeCorrente);

			//discoFileStream.close();
			
		} catch (Exception e) { e.printStackTrace(); }
		
		
	}
	System.out.println("Operazione eseguita con successo!");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test l = new Test();

	}

}
