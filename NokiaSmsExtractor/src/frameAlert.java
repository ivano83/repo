import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Ivano
 *
 * Frame di allarme
 */
public class frameAlert {
	String title;
	
	public frameAlert(String title) {
		this.title = title;
		this.creaFrame();
	}

	private void creaFrame() {
		JOptionPane option = new JOptionPane (title, JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION);
		JDialog dialog = option.createDialog(new JFrame(),"Allert!");
		dialog.setVisible(true);
	}
}
