package it.fivano.symusic.core;

import it.fivano.symusic.core.thread.ReleaseProcessModule;
import it.fivano.symusic.core.thread.ReleaseThreadObject;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class ReleaseSiteService extends BaseService {

	
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
	
	
	@Override
	protected abstract String applyFilterSearch(String result);
	
}
