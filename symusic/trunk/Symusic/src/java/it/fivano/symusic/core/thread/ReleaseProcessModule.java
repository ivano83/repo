package it.fivano.symusic.core.thread;

import it.fivano.symusic.LoggerSync;
import it.fivano.symusic.MyLogger;
import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.ReleaseExtractionService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.core.BeatportService;
import it.fivano.symusic.core.GoogleService;
import it.fivano.symusic.core.ScenelogService;
import it.fivano.symusic.core.YoutubeService;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;

public class ReleaseProcessModule extends Thread {
	
	private MyLogger log;
	private LoggerSync loggerSync; // RACCOGLIE LE RIGHE DI LOG PER POI STAMPARLE IN UN'UNICA VOLTA
	
	private ReleaseModel release;
	private ReleaseThreadObject threadObject;
	private int count;
	
	public ReleaseProcessModule(ReleaseModel release, ReleaseThreadObject threadObject, int count) {
		this.release = release;
		this.threadObject = threadObject;
		this.count = count;
		log = new MyLogger(getClass());
		loggerSync = new LoggerSync();
	}
	
	public void processaRelease() {
		
		this.start();
		
	}
	
	@Override
	public void run() {
		
		try {
			
			// ATTIVA IL LOG SINCRONIZZATO
			log.startSyncLog(loggerSync);
			
			SupportObject supp = threadObject.getSupport();
			boolean enableYoutubeService = supp.isEnableYoutubeService();
			boolean enableScenelogService = supp.isEnableScenelogService();
			boolean enableBeatportService = supp.isEnableBeatportService();
			

			// CONTROLLA SE LA RELEASE E' GIA' PRESENTE
			boolean isRecuperato = false;
			ReleaseService relServ = new ReleaseService();
			ReleaseModel relDb = relServ.getReleaseFull(release.getNameWithUnderscore(), threadObject.getSupport().getIdUser());
			if(relDb!=null) {
				log.info(release+" e' gia' presente nel database con id = "+relDb.getId());
				
				// controlla sul db se le varie estrazioni hanno avuto successo
				ReleaseExtractionModel extr = relDb.getReleaseExtraction();
				
				if(extr!=null ){
					enableScenelogService = !extr.getScenelog();
					enableYoutubeService = !extr.getYoutube();
//					enableBeatportService = extr.getBeatport();
				}
				isRecuperato = true;
				release = relDb; // SOSTITUISCE I DATI FINO AD ORA ESTRATTI CON QUELLI DEL DB
			}
			

			// SALVA SUL DB SE NON C'ERA
			if(!isRecuperato) {
				ReleaseModel r = relServ.saveRelease(release);
				release.setId(r.getId());
				log.info(release+" e' stata salvata sul database con id = "+r.getId());
			}
			
			// recupero e inserimento dati sul DB
			// TODO recupero e inserimento dati sul DB
//			double rounded = (double) Math.round(new Double(new Random().nextInt(5)*0.8) * 100) / 100;
//			release.setVoteAverage(rounded);
//			int i = 0;
//			if(i==0 || i==3) {
//				release.setVoted(true);
//				release.setVoteValue(new Random().nextInt(4));
//			}
//			i++;
			
			// OGGETTO CHE CONTIENE I FLAG PER ESTRARRE O MENO DETERMINATI DATI
			ReleaseExtractionModel relExtr = (release.getReleaseExtraction()==null)? new ReleaseExtractionModel() : release.getReleaseExtraction();
			
			// ########## SCENELOG ############
			try {
				ScenelogService scenelog = new ScenelogService();
				if(enableScenelogService) {
					// recupera dati da Scenelog per la tracklist e link download
					boolean res = scenelog.parseScenelog(release);
					
					SymusicUtility.updateReleaseExtraction(relExtr,res,AreaExtraction.SCENELOG);
				}
			} catch (ParseReleaseException e1) {
				log.warn("ScenelogService fallito! " + e1.getMessage());
				relExtr.setScenelog(false);
			}
			
			
			// ########## BEATPORT ############
			try {
				if(enableBeatportService) {
					// recupera dati da beatport per il dettaglio della release
					BeatportService beatport = new BeatportService();
					boolean res = beatport.parseBeatport(release);
					
					SymusicUtility.updateReleaseExtraction(relExtr,res,AreaExtraction.BEATPORT);
				}
				
			} catch (ParseReleaseException e1) {
				log.warn("BeatportService fallito! " + e1.getMessage());
				relExtr.setBeatport(false);
			}
			
			// ########## YOUTUBE ############
			YoutubeService youtube = new YoutubeService();
			try {
				if(enableYoutubeService) {
					// recupera dati da youtube per i video
					boolean res = youtube.extractYoutubeVideo(release);
					
					SymusicUtility.updateReleaseExtraction(relExtr,res,AreaExtraction.YOUTUBE);
				}
				
			} catch (ParseReleaseException e1) {
				log.warn("YoutubeService fallito! " + e1.getMessage());
				relExtr.setYoutube(false);
			}

			log.info("Salvataggio del release extraction con idRelease="+release.getId());
			// AGGIORNA/SALVA I FLAG DI ESTRAZIONE
			relExtr.setIdRelease(release.getId());
			new ReleaseExtractionService().saveReleaseExtraction(relExtr);
			
			
			// AGGIUNGE I LINK DI RICERCA MANUALE (DIRETTAMENTE SU GOOGLE E YOUTUBE)
			GoogleService google = new GoogleService();
			google.addManualSearchLink(release);
			youtube.addManualSearchLink(release); // link a youtube per la ricerca manuale
			
			threadObject.addRelease(count, release);
			log.info("Arricchimento release '"+release.getNameWithUnderscore()+"' eseguita");
			
		} catch(Exception e) {
			log.error("Errore",e);
		} finally {
			threadObject.decrement(); // IL THREAD HA FINITO E DECREMENTA IL CONTATORE
			// VENGONO STAMPATI ATOMICAMENTE TUTTI I LOG DEL THREAD
			synchronized (threadObject.getMonitor()) {
				log.stopSyncLog();
				loggerSync.printLog(log);
			}
		}
	}


	private boolean verificaAbilitazioneYoutube(ReleaseModel release) {
		
		String name = release.getName().toUpperCase();
		if(name.startsWith("VA-") || name.startsWith("VA -"))
			return false;
		
		return true;
	}
	
}
