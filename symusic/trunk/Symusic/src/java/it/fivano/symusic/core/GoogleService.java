package it.fivano.symusic.core;

import it.fivano.symusic.conf.GoogleConf;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;

public class GoogleService extends BaseService {

	private GoogleConf conf;
		
	public GoogleService() throws IOException {
		conf = new GoogleConf();
		this.setLogger(getClass());
	}
	
	private String getUrlConnection(ReleaseModel release, int tentativi) {		
		// pagina di inizio
		return conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", release.getNameWithUnderscore());
	}
	
	public void addManualSearchLink(ReleaseModel release) {
		
		LinkModel l = new LinkModel();
		l.setLink(this.getUrlConnection(release, 0));
		l.setName("[......CERCA SU GOOGLE......]");
		release.addLink(l);
		
	}
	
	protected String applyFilterSearch(String t) {
		return t.replace(" ", "+");
	}
	
}
