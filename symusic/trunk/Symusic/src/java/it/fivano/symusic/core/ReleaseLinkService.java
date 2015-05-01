package it.fivano.symusic.core;

import java.io.IOException;

import it.fivano.symusic.core.parser.Mp3TrackzParser;
import it.fivano.symusic.core.parser.MusicDLParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.model.MusicDLParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;

public class ReleaseLinkService {
	
	private ScenelogParser scenelog;
	private MusicDLParser musicDL;
	private Mp3TrackzParser mp3trackz;
	
	private static int MAX_CONSECUTIVE_FAILS = 20;
	private int countScenelogSleep = 0;
	
	public ReleaseLinkService() throws IOException {
		scenelog = new ScenelogParser();
		musicDL = new MusicDLParser();
		mp3trackz = new Mp3TrackzParser();
	}
	
	public ReleaseModel searchlink(ReleaseModel release) throws ParseReleaseException {
		
		String releaseName = release.getNameWithUnderscore();
		
		// SCENELOG
		if(scenelog.getCountFailConnection()<MAX_CONSECUTIVE_FAILS) {
			ScenelogParserModel releaseScenelog = new ScenelogParserModel();
			releaseScenelog.setReleaseName(releaseName);
			releaseScenelog.setUrlReleaseDetails(scenelog.getUrlRelease(releaseName));
			release = scenelog.parseReleaseDetails(releaseScenelog, release);
		}
		else {
			countScenelogSleep++;
			if(countScenelogSleep==MAX_CONSECUTIVE_FAILS) {
				scenelog.resetCountFailConnection();
			}
		}
		
		if(release.getLinks()!=null && !release.getLinks().isEmpty()) {
			return release;
		}
		
		// MUSICDL
		String url = musicDL.getUrlRelease(releaseName, release.getGenre().getName());
		MusicDLParserModel musicDLModel = new MusicDLParserModel();
		musicDLModel.setReleaseName(releaseName);
		musicDLModel.setUrlReleaseDetails(url);
		release = musicDL.parseReleaseDetails(musicDLModel, release);
		
		// MP3 TRACKZ
		release = mp3trackz.parseReleaseDetails(release, releaseName);
		
		if(release.getLinks()!=null && !release.getLinks().isEmpty()) {
			return release;
		}
		
		
		
		return release;
	}
	
	

}
