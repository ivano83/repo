package it.fivano.symusic.core;

import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.backend.service.LinkService;
import it.fivano.symusic.backend.service.ReleaseExtractionService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.backend.service.VideoService;
import it.fivano.symusic.core.thread.ReleaseProcessModule;
import it.fivano.symusic.core.thread.ReleaseThreadObject;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class ReleaseSiteService extends BaseService {

	protected boolean enableBeatportService;
	protected boolean enableScenelogService;
	protected boolean enableYoutubeService;
	protected boolean excludeRipRelease;
	protected boolean excludeVA;
	
	protected String annoDa;
	protected String annoAl;
	
	protected Long idUser;
	
	protected List<ReleaseModel> arricchimentoRelease(List<ReleaseModel> releases, SupportObject supp) throws ParseReleaseException {
		
		// INIT OGGETTO THREAD
		Object monitor = new Object();
		int maxThread = Integer.parseInt(generalConf.MAX_ACTIVE_THREAD);
		ReleaseThreadObject threadObject = new ReleaseThreadObject(maxThread,monitor, supp);

		int count = 0;
		for(ReleaseModel currRel : releases) {

			// AVVIA THREAD RELATIVO ALLA SINGOLA RELEASE
			ReleaseProcessModule thread = new ReleaseProcessModule(currRel, threadObject, count);
			// IL METODO VERIFICA SE IL THREAD CORRENTE PUO' PROSEGUIRE O MENO.
			// IN CASO CI SIANO GIA' TROPPI THREAD ATTIVI, QUESTO THREAD SI METTE
			// IN PAUSA PER TOT MS. SE SUPERA IL TIMEOUT IMPOSTATO IMPOSTATO ALLORA VIENE ESEGUITO ALL'ISTANTE
			// SE IL THREAD PUO' PARTIRE VIENE INCREMENTATO IL CONTATORE DEI THREAD ATTIVI
			threadObject.canThreadStart(log, thread);

			thread.processaRelease();

			count++;
		}

		// FINE TUTTI THREAD
		synchronized (monitor) {
			try {
				while(count>threadObject.getNumFinishThreads()) {
					monitor.wait();
				}
			} catch (InterruptedException e1) {
				throw new ParseReleaseException("Errore gestione thread",e1);
			}
		}
		
		return new ArrayList<ReleaseModel>(threadObject.getReleaseResults().values());
	}
	
	protected boolean isRadioRipRelease(ReleaseModel release) {
		for(String rip : generalConf.RELEASE_EXCLUSION) {
			if(release.getNameWithUnderscore().contains(rip))
				return true;
		}
		return false;
	}
	
	protected boolean isVARelease(ReleaseModel release) {
		for(String rip : generalConf.RELEASE_VA) {
			if(release.getNameWithUnderscore().startsWith(rip))
				return true;
		}
		return false;
	}
	
	protected void saveOrUpdateRelease(ReleaseModel release, boolean isRecuperato) throws BackEndException {
		
		if(!isRecuperato) {
			// SALVA O RECUPERA IL GENERE
			if(release.getGenre()!=null)
				release.setGenre(new GenreService().saveGenre(release.getGenre()));
			
			release.getReleaseFlag().setNewRelease(true);
			
			ReleaseService relServ = new ReleaseService();
			ReleaseModel r = relServ.saveRelease(release);
			release.setId(r.getId());
			log.info(release+" e' stata salvata sul database con id = "+r.getId());
		}
		else {
			
		}
		if(enableYoutubeService) {
			VideoService vidServ = new VideoService();
			vidServ.saveVideos(release.getVideos(), release.getId());
		} if(enableScenelogService) {
			TrackService traServ = new TrackService();
			traServ.saveTracks(release.getTracks(), release.getId());
		}
		LinkService linkServ = new LinkService();
		linkServ.saveLinks(release.getLinks(), release.getId());
		
		// AGGIORNA/SALVA I FLAG DI ESTRAZIONE
		ReleaseExtractionModel extr = release.getReleaseExtraction();
		extr.setIdRelease(release.getId());
		new ReleaseExtractionService().saveReleaseExtraction(extr);
		log.info("Salvataggio del release extraction con idRelease="+release.getId());
		
	}
	

	@Override
	protected abstract String applyFilterSearch(String result);
	
	public boolean isEnableBeatportService() {
		return enableBeatportService;
	}


	public void setEnableBeatportService(boolean enableBeatportService) {
		this.enableBeatportService = enableBeatportService;
	}


	public boolean isExcludeRipRelease() {
		return excludeRipRelease;
	}


	public void setExcludeRipRelease(boolean excludeRipRelease) {
		this.excludeRipRelease = excludeRipRelease;
	}

	public boolean isExcludeVA() {
		return excludeVA;
	}

	public void setExcludeVA(boolean excludeVA) {
		this.excludeVA = excludeVA;
	}

	public String getAnnoDa() {
		return annoDa;
	}

	public void setAnnoDa(String annoDa) {
		this.annoDa = annoDa;
	}

	public String getAnnoAl() {
		return annoAl;
	}

	public void setAnnoAl(String annoAl) {
		this.annoAl = annoAl;
	}
	
	
}
