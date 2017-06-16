package it.fivano.symusic.core;

import java.io.IOException;

import it.fivano.symusic.core.parser.AlbumDLParser;
import it.fivano.symusic.core.parser.Mp3TrackzParser;
import it.fivano.symusic.core.parser.MusicDLParser;
import it.fivano.symusic.core.parser.Rave2NiteParser;
import it.fivano.symusic.core.parser.SceneMusicParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BaseMusicParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;

public class ReleaseLinkService {

	private ScenelogParser scenelog;
	private MusicDLParser musicDL;
	private Rave2NiteParser rave2nite;
	private SceneMusicParser sceneMusic;
	private Mp3TrackzParser mp3trackz;

	private static int MAX_CONSECUTIVE_FAILS = 20;
	private int countScenelogSleep = 0;

	public ReleaseLinkService() throws IOException {
		scenelog = new ScenelogParser();
		musicDL = new MusicDLParser();
		rave2nite = new Rave2NiteParser();
		mp3trackz = new Mp3TrackzParser();
		sceneMusic = new SceneMusicParser();
	}

	public ReleaseModel searchlink(ReleaseModel release) throws ParseReleaseException {

		String releaseName = release.getNameWithUnderscore();

		// SCENELOG
		if(scenelog.getCountFailConnection()<MAX_CONSECUTIVE_FAILS && !release.getReleaseExtraction().getScenelog()) {
			BaseReleaseParserModel releaseScenelog = new BaseReleaseParserModel();
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

		// SCENEMUSIC
		BaseReleaseParserModel sceneMusicModel = new BaseReleaseParserModel();
		sceneMusicModel.setReleaseName(releaseName);
		sceneMusicModel.setUrlReleaseDetails(sceneMusic.getUrlRelease(releaseName, null));
		release = sceneMusic.parseReleaseDetails(sceneMusicModel, release);

		// RAVE2NITE
		if(release.getTracks()==null || release.getTracks().isEmpty()) {
			release = rave2nite.parseReleaseDetails(release, releaseName);
		}


		if(release.getLinks()!=null && !release.getLinks().isEmpty()) {
			return release;
		}

		// MUSICDL
//		String url = musicDL.getUrlRelease(releaseName, release.getGenre().getName());
//		BaseMusicParserModel musicDLModel = new BaseMusicParserModel();
//		musicDLModel.setReleaseName(releaseName);
//		musicDLModel.setUrlReleaseDetails(url);
//		release = musicDL.parseReleaseDetails(musicDLModel, release);

		// MP3 TRACKZ
		release = mp3trackz.parseReleaseDetails(release, releaseName);

		if(release.getLinks()!=null && !release.getLinks().isEmpty()) {
			return release;
		}



		return release;
	}



}
