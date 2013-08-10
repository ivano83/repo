package it.ivano.backupsync.view;

import it.ivano.backupsync.business.FileSyncService;
import it.ivano.backupsync.model.CompareItem;
import it.ivano.backupsync.model.FileSyncModel;
import it.ivano.utility.ByteUtility;
import it.ivano.utility.FileUtility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

//VS4E -- DO NOT REMOVE THIS LINE!
public class BackupSyncGui extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel jPanel0;
	private JLabel jLabel0;
	private JTextField jTextFieldPath;
	private JLabel jLabel1;
	private JTextField jTextFieldPathBackup;
	private JButton jButtonSubmit;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JTabbedPane jTabbedPane0;

	private JTable jTable0;
	private JScrollPane jScrollPane0;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JButton jButton1;
	private JButton jButton2;
	private JPopupMenu popup;
	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
	public void init() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					initComponents();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initComponents() {
		setTitle("BackupSync");
		add(getJPanel0(), BorderLayout.CENTER);
		setSize(1025, 547);
	}

	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setIcon(new ImageIcon("images/apri.png"));
			jButton2.addMouseListener(new MouseAdapter() {
	
				public void mouseClicked(MouseEvent event) {
					jButton2MouseMouseClicked(event);
				}
			});
		}
		return jButton2;
	}

	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton(new ImageIcon("images/apri.png"));
			jButton1.addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent event) {
					jButton1MouseMouseClicked(event);
				}
			});
		}
		return jButton1;
	}

	private JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Copyright © 2012 - Ivano Fiorini");
		}
		return jLabel3;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("-");
		}
		return jLabel2;
	}

	private JScrollPane getJScrollPane0() {
		if (jScrollPane0 == null) {
			jScrollPane0 = new JScrollPane();
			jScrollPane0.setViewportView(getJTable0());
			
		}
		return jScrollPane0;
	}

	private JTable getJTable0() {
		if (jTable0 == null) {
			jTable0 = new JTable();
			
			MyModel dm = new MyModel(new String[][]{}); 
			jTable0.setModel(dm);
			
			jTable0.addMouseListener(new MouseAdapter() {
				@Override
		        public void mouseReleased(MouseEvent event) {
					
					jTable0MouseMouseReleased(event);
				}

			});
			
//			jTable0.setCellSelectionEnabled(true);
//			if(data!=null && data.length!=0)
//			dm.setValueAt("ccaaaaa",0,0);

//			jTable0.setModel(new DefaultTableModel(data, titles) {
//				private static final long serialVersionUID = 1L;
//				Class<?>[] types = new Class<?>[] { Object.class, Object.class, };
//	
//				public Class<?> getColumnClass(int columnIndex) {
//					return types[columnIndex];
//				}
//				
//			});
		}
		jTable0.add(getPopup());
//			jTable0.repaint();
//			jTabbedPane0.repaint();
//			jPanel0.repaint();
//			this.repaint();
			
		return jTable0;
	}

	private JTabbedPane getJTabbedPane0() {
		if (jTabbedPane0 == null) {
			jTabbedPane0 = new JTabbedPane();
			jTabbedPane0.addTab("Risultati", getJPanel2());
			jTabbedPane0.addTab("Configurazione", getJPanel1());
		}
		return jTabbedPane0;
	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GroupLayout());
			jPanel2.add(getJScrollPane0(), new Constraints(new Trailing(6, 982, 10, 10), new Leading(9, 362, 10, 10)));
		}
		return jPanel2;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GroupLayout());
		}
		return jPanel1;
	}

	private JButton getJButtonSubmit() {
		if (jButtonSubmit == null) {
			jButtonSubmit = new JButton();
			jButtonSubmit.setText("Start");
			jButtonSubmit.addMouseListener(new MouseAdapter() {
	
				public void mouseClicked(MouseEvent event) {
					try {
						jButtonSubmitMouseMouseClicked(event);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		return jButtonSubmit;
	}

	private JTextField getJTextFieldPathBackup() {
		if (jTextFieldPathBackup == null) {
			jTextFieldPathBackup = new JTextField();
		}
		return jTextFieldPathBackup;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Path Backup");
		}
		return jLabel1;
	}

	private JTextField getJTextFieldPath() {
		if (jTextFieldPath == null) {
			jTextFieldPath = new JTextField();
		}
		return jTextFieldPath;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("Path Originale");
		}
		return jLabel0;
	}

	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new JPanel();
			jPanel0.setLayout(new GroupLayout());
			jPanel0.add(getJLabel1(), new Constraints(new Leading(12, 98, 12, 12), new Leading(65, 10, 419)));
			jPanel0.add(getJTextFieldPathBackup(), new Constraints(new Leading(125, 262, 10, 10), new Leading(59, 10, 413)));
			jPanel0.add(getJLabel0(), new Constraints(new Leading(12, 98, 12, 12), new Leading(30, 18, 10, 452)));
			jPanel0.add(getJTextFieldPath(), new Constraints(new Leading(125, 262, 12, 12), new Leading(25, 324, 303)));
			jPanel0.add(getJButtonSubmit(), new Constraints(new Leading(572, 10, 10), new Leading(63, 20, 12, 12)));
			jPanel0.add(getJButton1(), new Constraints(new Leading(399, 27, 12, 12), new Leading(26, 27, 12, 12)));
			jPanel0.add(getJButton2(), new Constraints(new Leading(399, 27, 10, 225), new Leading(59, 27, 12, 12)));
			jPanel0.add(getJLabel3(), new Constraints(new Leading(826, 183, 6, 6), new Leading(517, 20, 10, 10)));
			jPanel0.add(getJLabel2(), new Constraints(new Leading(18, 362, 10, 10), new Leading(519, 447, 451)));
			jPanel0.add(getJTabbedPane0(), new Constraints(new Leading(14, 997, 10, 10), new Leading(101, 413, 6, 6)));
		}
		return jPanel0;
	}

	public BackupSyncGui() {
		initComponents();
	}

	
	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BackupSyncGui frame = new BackupSyncGui();
				frame.setDefaultCloseOperation(BackupSyncGui.EXIT_ON_CLOSE);
				frame.setTitle("BackupSync");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setResizable(false);
			}
		});
	}

	private void jButton1MouseMouseClicked(MouseEvent event) {
		String currDir = "";
		JFileChooser fc = new JFileChooser(currDir);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JFrame f1 = new JFrame("Apri");
		int scelta = fc.showOpenDialog(f1);
		
		if (scelta == JFileChooser.APPROVE_OPTION) {
			File fileScelto = fc.getSelectedFile();
			if(fileScelto.exists()) {
				fc.resetChoosableFileFilters(); //resetta i tipi di formato da filtrare
				getJTextFieldPath().setText(fileScelto.getPath());
			}
			else {
				fc.resetChoosableFileFilters();
				//new frameAlert("attenzione");
			}
		}
		else if (scelta == JFileChooser.CANCEL_OPTION) {
			fc.resetChoosableFileFilters();
		}
	}

	private void jButton2MouseMouseClicked(MouseEvent event) {
		String currDir = "";
		JFileChooser fc = new JFileChooser(currDir);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		JFrame f1 = new JFrame("Apri");
		int scelta = fc.showOpenDialog(f1);
		
		if (scelta == JFileChooser.APPROVE_OPTION) {
			File fileScelto = fc.getSelectedFile();
			if(fileScelto.exists()) {
				fc.resetChoosableFileFilters(); //resetta i tipi di formato da filtrare
				getJTextFieldPathBackup().setText(fileScelto.getPath());
			}
			else {
				fc.resetChoosableFileFilters();
				//new frameAlert("attenzione");
			}
		}
		else if (scelta == JFileChooser.CANCEL_OPTION) {
			fc.resetChoosableFileFilters();
		}
	}

	private void jButtonSubmitMouseMouseClicked(MouseEvent event) throws Exception {
		
		if(!this.notEmpty(getJTextFieldPath().getText()) || !this.notEmpty(getJTextFieldPathBackup().getText())) {
			alert("Impostare correttamente i percorsi delle cartelle per la comparazione");
		}
		
		FileSyncService fs = new FileSyncService();
		FileSyncModel input = new FileSyncModel();
		input.setInitialPath(getJTextFieldPath().getText());
		input.setInitialPathBackup(getJTextFieldPathBackup().getText());
		fs.comparazioneFile(input);
		
		int size = input.getListaDifferenze().size();
		String[][] righe = new String[size][5];
		int x = 0;
		for(CompareItem c : input.getListaDifferenze()) {
			righe[x][0] = c.getFileName();
			if(c.getRelativePath()==null || c.getRelativePath().length()==0)
				righe[x][1] = "/";
			else
				righe[x][1] = c.getRelativePath();
			if(c.getSize()==0)
				righe[x][2] = "--";
			else
				righe[x][2] = ByteUtility.convertByte(c.getSize());
			if(c.getOtherItem()!=null && c.getOtherItem().getSize()!=0)
				righe[x][3] = ByteUtility.convertByte(c.getOtherItem().getSize());
			else
				righe[x][3] = "--";
			righe[x][4] = c.getDescrizioneDifferenza();

			x++;
		}
		
		MyModel dm = new MyModel(righe); 
//	    jTable0.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
	    jTable0.setModel(dm);
	    dm = (MyModel) jTable0.getModel();
	    TableColumnModel colModel = jTable0.getColumnModel();
	    for(int j = 0; j < colModel.getColumnCount(); j++)
	    	colModel.getColumn(j).setCellRenderer(new RowRenderer(jTextFieldPath.getText()));
	    
	   
	    colModel.getColumn(2).setPreferredWidth(30);
	    colModel.getColumn(3).setPreferredWidth(30); 
	}
	
	private int rigaSelezionata;
	
	private void jTable0MouseMouseReleased(MouseEvent e)  {
		
		System.out.println("mouse rel e");
        int r = jTable0.rowAtPoint(e.getPoint());
        if (r >= 0 && r < jTable0.getRowCount()) {
        	jTable0.setRowSelectionInterval(r, r);
        } else {
        	jTable0.clearSelection();
        }

        rigaSelezionata = jTable0.getSelectedRow();
        if (rigaSelezionata < 0)
            return;
        if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
//        	getPopup();
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
	}
	
	private void alert(String text) {
		JOptionPane option = new JOptionPane (text, JOptionPane.ERROR_MESSAGE, JOptionPane.CLOSED_OPTION);
		JDialog dialog = option.createDialog(new JFrame(),"Alert!");
		dialog.setVisible(true);
	}
	
	private boolean notEmpty(String text) {
		if(text!=null && text.length()!=0)
			return true;
		return false;
	}
	
	private JMenuItem anItem1;
	private JMenuItem anItem2;
	public JMenuItem getAnItem1() {
		return anItem1;
	}
	public JMenuItem getAnItem2() {
		return anItem2;
	}
    public JPopupMenu getPopup(){
    	popup = new JPopupMenu();
        anItem1 = new JMenuItem("Apri cartelle file");
        anItem1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					apriCartellaMouseClicked(ev);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        popup.add(anItem1);
        
        anItem2 = new JMenuItem("Sincronizza file");
        anItem2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					sincronizzaFileMouseClicked(ev);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        popup.add(anItem2);
        
        return popup;
    }
    
    public void apriCartellaMouseClicked(ActionEvent event) {
		String relativePath = (String) jTable0.getModel().getValueAt(rigaSelezionata, 1);
		String path = jTextFieldPath.getText()+relativePath;
		
		String pathBackup = jTextFieldPathBackup.getText()+relativePath;
		
		try {
			Desktop.getDesktop().open(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Desktop.getDesktop().open(new File(pathBackup));
		} catch (IOException e) {
		}
		
	}
    public void sincronizzaFileMouseClicked(ActionEvent event) {
		String relativePath = (String) jTable0.getModel().getValueAt(rigaSelezionata, 1);
		String fileName = (String) jTable0.getModel().getValueAt(rigaSelezionata, 0);
		String path = jTextFieldPath.getText()+relativePath+"/"+fileName;
		
		String pathBackup = jTextFieldPathBackup.getText()+relativePath;
		File fileDaCopiare = new File(path);
		File fileOutput =  new File(pathBackup+"/"+fileName);
		
		try {
			FileUtility.copiaFile(fileDaCopiare, fileOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


class RowRenderer extends DefaultTableCellRenderer {
	private String filePath;
	public RowRenderer(String filePath) {
		this.filePath = filePath;
	}
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected,
                                            hasFocus, row, column);
        setToolTipText((String)table.getValueAt(row, column));
//        try {
//        	if(column==0) {
//        		//Create a temporary file with the specified extension  
//        		File file = File.createTempFile("icon", filePath.split("\\.")[filePath.split("\\.").length-1]);
//
//        		FileSystemView view = FileSystemView.getFileSystemView();      
//        		Icon icon = view.getSystemIcon(file);      
//        		//Delete the temporary file  
//        		file.delete(); 
//
//        	}
//        } catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
        
        return this;
    }
}

class ImageRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, String fileName) throws IOException {
    	//Create a temporary file with the specified extension  
	    File file = File.createTempFile("icon", fileName.split("\\.")[fileName.split("\\.").length-1]);  
	          
	    FileSystemView view = FileSystemView.getFileSystemView();      
	    Icon icon = view.getSystemIcon(file);      
	      
	    //Delete the temporary file  
	    file.delete(); 

        this.setIcon(icon);
        return this;
    }
}

class MyModel extends AbstractTableModel {  
 
	private static final long serialVersionUID = 4204092414548984705L;
	private String[] columnData = new String[5];  
    private Object[][] data = new String[20][5];
    public MyModel(String[][] data)  
    {  
    	this.data = data;
    	this.columnData = new String[] {"File", "Path", "Dimensione", "Dimensione Backup", "Informazioni"};
		
//    	this.data[0][0] = "aaaa";
//    	this.data[0][1] = "aaaa";
//    	this.data[0][2] = "aaaa";
//    	setValueAt("fdsgsdg000000000000", 0, 3);
//    	this.data[0][4] = "aaaa";
    }  
    public int getColumnCount() {  
        return columnData.length;  
    }  
    public int getRowCount() {  
        return data.length;  
    }  
    public String getColumnName(int col) {  
        return columnData[col];  
    }  
    public Object getValueAt(int row, int col) {  
        return data[row][col];  
    }  
    public Class getColumnClass(int c) {  
        return getValueAt(0, c).getClass();  
    }  
    /* 
     * Don't need to implement this method unless your table's 
     * editable. 
     */  
    public boolean isCellEditable(int row, int col)  
    {  
        //Note that the data/cell address is constant,  
        //no matter where the cell appears onscreen.  
        if (col < 2) {  
            return false;  
        } else {  
            return true;  
        }  
    }  
    /* 
     * Don't need to implement this method unless your table's 
     * data can change. 
     */  
    public void setValueAt(Object value, int row, int col)  
    {  
        //...//debugging code not shown...  
        //...//ugly special handling of Integers not shown...  
        data[row][col] = value;  
        fireTableCellUpdated(row, col);  
        //...//debugging code not shown...  
    }  
    public void setValueAt(Object value, int row, int col, boolean isFolder)  
    {  
    	Class thisClass = BackupSyncGui.class;
    	if(isFolder) {
    		data[row][col] = new ImageIcon("images/apri.png");
    	}
    	else {
    		data[row][col] = value;
    	}
    	fireTableCellUpdated(row, col);  
    } 

}  
