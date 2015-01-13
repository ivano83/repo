package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.filecatalog.model.AudioComponent;
import it.ivano.filecatalog.model.VideoFileModel;

import java.io.File;

import uk.co.caprica.vlcj.player.AudioTrackInfo;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;

import com.sun.jna.NativeLibrary;

public class VideoUtils extends CommonUtils {

//	static {
//		NativeLibrary.addSearchPath("libvlc", "vlc");
//	}
	
	public VideoFileModel estaiMetadati(File file) throws FileDataException {
		
		VideoFileModel res = new VideoFileModel();
		MediaPlayer mediaPlayer = null;
		try {
			if(file==null) {
				throw new FileDataException("Il file e' nullo e non puo' essere processato");
			}
			super.estraiMetadatiStandard(file, res);

			NativeLibrary.addSearchPath("libvlc", "vlc");
			
			MediaPlayerFactory factory = new MediaPlayerFactory();
			mediaPlayer = factory.newHeadlessMediaPlayer();
		    mediaPlayer.prepareMedia(file.getAbsolutePath());
		    
		    mediaPlayer.parseMedia();

		    for(TrackInfo tr : mediaPlayer.getTrackInfo()) {
		    	if(tr instanceof VideoTrackInfo) {
		    		VideoTrackInfo vtr = (VideoTrackInfo)tr;
		    		res.setAltezza(vtr.height()+"");
		    		res.setLarghezza(vtr.width()+"");
		    		res.setCodec(vtr.codecName());
		    	} 
		    	else if(tr instanceof AudioTrackInfo) {
		    		AudioTrackInfo atr = (AudioTrackInfo)tr;
		    		AudioComponent audio = new AudioComponent();
		    		audio.setBitrate(atr.bitRate()+"");
		    		audio.setCanali(atr.channels()+"");
		    		audio.setDescrizione(atr.description());
		    		audio.setLinguaggio(atr.language());
		    		audio.setCodec(atr.codecName());
		    		res.addAudio(audio);
		    	}
		    }
		    
		   
			
		} catch (Exception e) {
			throw new FileDataException("Errore nel recupero dei dati per il file '"+file.getName()+"'", e);
		} 
		finally {
			if(mediaPlayer!=null)
				mediaPlayer.release();
		}
		return res;
	}

	public static void main(String[] args) throws FileDataException {
		
		VideoUtils u = new VideoUtils();
		File f = new File("C:\\Users\\ivano\\Downloads\\torrent\\[BluRay Rip 1080p - ITA-ENG-GER AC3-SUB] Dumbo (1941) LiFE [m@rcomem].mkv");
		u.estaiMetadati(f);
		
	}
}
