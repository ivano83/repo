package it.ivano.filecatalog;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.filecatalog.utils.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class FileCatalogCore extends BaseCatalog {

	public FileCatalogCore() {
		super(FileCatalogCore.class);
	}

	public ScanResult scanFiles(List<String> listFiles) throws FileDataException, FileNotFoundException, IOException, TikaException {
		ScanResult res = new ScanResult();
		
		CommonUtils utils = new CommonUtils();
		for(String f : listFiles) {
			File file = new File(f);
			// check estensione
			String ext = utils.getEstensioneFile(f);

			TikaConfig tika = new TikaConfig();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.toString());
			MediaType mimetype = tika.getDetector().detect(
					TikaInputStream.get(file), metadata);
			System.out.println("File " + f + " is " + mimetype);

			
			// extract metadati, check file type
			
			if(file.isDirectory()) {
				res.addNextFiles(file.list());
			}
			
		}
		return res;
	}
	
	public static void main(String[] args) throws FileDataException, FileNotFoundException, IOException, TikaException {
		
		FileCatalogCore c = new FileCatalogCore();
		File f = new File("C:\\Users\\ivano\\Downloads\\torrent");
		List<String> fi = new ArrayList<String>();
		
		for(String x : f.list()) {
			System.out.println(f.getPath()+"\\"+x);
			if(!new File(f.getPath()+"\\"+x).isDirectory()) {
				fi.add(f.getPath()+"\\"+x);
			}
		}
		
		c.scanFiles(fi);
	}
	
}
