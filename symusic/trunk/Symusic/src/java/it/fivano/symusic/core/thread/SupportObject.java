package it.fivano.symusic.core.thread;

import org.jsoup.select.Elements;

public class SupportObject {

	private boolean enableBeatportService;
	private boolean enableScenelogService;
	private boolean enableYoutubeService;
	
	private Long idUser;
	
	public boolean isEnableBeatportService() {
		return enableBeatportService;
	}
	public void setEnableBeatportService(boolean enableBeatportService) {
		this.enableBeatportService = enableBeatportService;
	}
	public boolean isEnableScenelogService() {
		return enableScenelogService;
	}
	public void setEnableScenelogService(boolean enableScenelogService) {
		this.enableScenelogService = enableScenelogService;
	}
	public boolean isEnableYoutubeService() {
		return enableYoutubeService;
	}
	public void setEnableYoutubeService(boolean enableYoutubeService) {
		this.enableYoutubeService = enableYoutubeService;
	}
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	
}
