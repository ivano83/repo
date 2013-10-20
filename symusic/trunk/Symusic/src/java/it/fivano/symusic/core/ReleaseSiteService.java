package it.fivano.symusic.core;

import it.fivano.symusic.core.thread.ReleaseProcessModule;
import it.fivano.symusic.core.thread.ReleaseThreadObject;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;

import java.util.ArrayList;
import java.util.List;

public abstract class ReleaseSiteService extends BaseService {

	protected boolean enableBeatportService;
	protected boolean enableScenelogService;
	protected boolean enableYoutubeService;
	protected boolean excludeRipRelease;
	
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
}
