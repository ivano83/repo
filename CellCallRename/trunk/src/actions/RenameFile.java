package actions;

import it.ivano.utility.FileUtility;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.CallFile;

public class RenameFile {

	private String pattern;
	private String urlFolder;
	private List<CallFile> listaChiamate;
	
	
	
	public boolean modificaNomiFile(String pattern, String urlFolder, String urlRubrica, String urlOutput) {
		
		try {
			// CARICO IL FILE DELLA RUBRICA
			File f = null;
			Set<String> l = new TreeSet<String>();
			if(urlRubrica!=null) {
				f = FileUtility.caricaFile(urlRubrica).get(0);
				l = FileUtility.estraiRigheDaFile(f);
			}			
			Map<String,String> rubrica = new TreeMap<String,String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd][HH.mm.ss");

			// eseguo azioni in base al tipo di pattern in input
			List<File> fileList = FileUtility.getFilesByPattern(FileUtility.caricaFile(urlFolder),pattern);
			
			// costruisco la rubrica
			String[] x;
			for(String tmp : l) {
				x = tmp.split("-");
				System.out.println(x[0]);
				rubrica.put(x[1].trim(), x[0].trim());
			}
			
			// Esempio:
			// P[+39065924916][Out][13-01-2010]-[12-08-04]
			
			File tmp1;
			String[] pezzi;
			String oldName, newName, num;
			Date data;
			CallFile cf;
			for(File fi : fileList) {
				if(pattern.equals("[][][][]")) {

					oldName = fi.getName();
					cf = new CallFile();
					cf.setOriginalPath(oldName);
					pezzi = oldName.split("\\[");
					if(pezzi[1].split("\\]").length==0)
						num = "ANONIMO";
					else
						num = pezzi[1].split("\\]")[0]; // prende quello che c'è tra le prime [ ]
					if(rubrica.containsKey(num)) {
						cf.setNumber(num);
						cf.setName(rubrica.get(num));
					}
					else if(rubrica.containsKey("+39"+num)) {
						cf.setNumber("+39"+num);
						cf.setName(rubrica.get("+39"+num));
					}
					else if(rubrica.containsKey(num.replaceAll("\\+39", ""))) {
						cf.setNumber(num.replaceAll("\\+39", ""));
						cf.setName(rubrica.get(num.replaceAll("\\+39", "")));
					}
					else {
						cf.setNumber(num);
					}
					
					if(cf.getName()!=null)
						cf.setName(cf.getName().toUpperCase());
					
					cf.setInOut(pezzi[2].split("\\]")[0].toUpperCase());
					
					String[] data1 = pezzi[3].split("\\]")[0].split("-");
					String[] data2 = pezzi[4].split("\\]")[0].split("-");
					cf.setData(Integer.parseInt(data1[2]), Integer.parseInt(data1[1]), 
							Integer.parseInt(data1[0]), Integer.parseInt(data2[0]), 
							Integer.parseInt(data2[1]), Integer.parseInt(data2[2]));
				
					String[] finale = oldName.split("\\.");
					
					if(cf.getName()!=null)
						newName = "["+sdf.format(cf.getData().getTime())+"]"+"["+cf.getName()+"]"+"["+cf.getInOut()+"]."+finale[finale.length-1];
					else
						newName = "["+sdf.format(cf.getData().getTime())+"]"+"["+cf.getNumber()+"]"+"["+cf.getInOut()+"]."+finale[finale.length-1];

//					System.out.println(data1[0]+data1[1]+data1[2]);
//					System.out.println(data2[0]+data2[1]+data2[2]);
					System.out.println("new name " + newName);
					System.out.println(fi.getParent());
					
					
					
					File newFile = new File(urlOutput);
					if(!newFile.exists())
						newFile.mkdir();
					if(urlOutput!=null)
						newFile = new File(urlOutput+"\\"+newName);
					else
						newFile = new File(fi.getParent()+"\\"+newName);
					fi.renameTo(newFile);
					
					
				}
				else if(pattern.equals("__")) {
					
					oldName = fi.getName();
					cf = new CallFile();
					cf.setOriginalPath(oldName);
					String finale = oldName.split("\\.")[oldName.split("\\.").length-1];
					pezzi = oldName.replace("."+finale, "").split("_");
					
					Collection<String> nomiRubrica = rubrica.values();
					String tmp = null;
					for(String n : nomiRubrica) {
						tmp = n.trim().replace(" ", "").replace(" ", "");
						if(tmp.equalsIgnoreCase(pezzi[0]))
							cf.setName(n.toUpperCase());
					}
					if(cf.getName()==null)
						cf.setName(pezzi[0].toUpperCase());
					
					String[] data1 = pezzi[1].split("-");
					String[] data2 = pezzi[2].split("-");
					cf.setData(Integer.parseInt(data1[2]), Integer.parseInt(data1[1]), 
							Integer.parseInt(data1[0]), Integer.parseInt(data2[0]), 
							Integer.parseInt(data2[1]), Integer.parseInt(data2[2]));
					
					
					newName = "["+sdf.format(cf.getData().getTime())+"]"+"["+cf.getName()+"]."+finale;
					
					
					File newFile = new File(urlOutput);
					if(!newFile.exists())
						newFile.mkdir();
					if(urlOutput!=null)
						newFile = new File(urlOutput+"\\"+newName);
					else
						newFile = new File(fi.getParent()+"\\"+newName);
					fi.renameTo(newFile);
					
					
				}
			}
			
			// carico i file dentro la url --> (List<CallFile> listaChiamate)
			
			// quando setto gli oggetti, verifico che il num è presente in rubrica
			
			// applico le modifiche a tutti gli oggetti seguendo un pattern comune
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	

}
