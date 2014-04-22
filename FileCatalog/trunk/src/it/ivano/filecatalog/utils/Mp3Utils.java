package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.filecatalog.model.Mp3FileModel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;

public class Mp3Utils extends CommonUtils {
	
	public Mp3FileModel extraiMetadati(File file) throws FileDataException {
		
		Mp3FileModel res = new Mp3FileModel();
		try {
			if(file==null) {
				throw new FileDataException("Il file e' nullo e non puo' essere processato");
			}
			super.estraiMetadati(file, res);
			
			// create an MP3File object representing our chosen file
			MediaFile oMediaFile = new MP3File(file);

			ID3V2Tag tag = oMediaFile.getID3V2Tag();
			ID3V1Tag tag2 = oMediaFile.getID3V1Tag();
						
			if(tag!=null) {
				res.setAlbum(tag.getAlbum());
				res.setAnno(tag.getYear()+"");
				res.setArtista(tag.getArtist());
				res.setNumeroTraccia(tag.getTrackNumber()+"");
				res.setGenere(tag.getGenre());
			} else if(tag2!=null) {
				res.setAlbum(tag2.getAlbum());
				res.setAnno(tag2.getYear());
				res.setArtista(tag2.getArtist());
				res.setGenere(tag2.getGenre().toString());
			} else {
				
			}
			
		} catch (ID3Exception e) {
			throw new FileDataException("Errore nel recupero dei dati per il file mp3 '"+file.getName()+"'",e);
		} catch (Exception e) {
			throw new FileDataException("Errore nel recupero dei dati per il file '"+file.getName()+"'", e);
		} 
		return res;
		
	}

	public static void main(String[] args) throws FileDataException {
		
		Mp3Utils u = new Mp3Utils();
		File f = new File("C:\\Users\\ivano\\Downloads\\prova.mp3");
		u.extraiMetadati(f);
		
	}
}
