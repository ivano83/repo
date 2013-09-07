package it.fivano.symusic.backend;

import java.util.ArrayList;
import java.util.List;

import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseVideo;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;

public class TransformerUtility {
	
	
	public static Release transformRelease(ReleaseModel model) {
		if(model==null)
			return null;
		
		Release res = new Release();
		
		res.setReleaseName(model.getNameWithUnderscore());
		res.setAuthor(model.getArtist());
		res.setCrew(model.getCrew());
		res.setSongName(model.getSong());
		res.setYears(model.getYear());
		
//		res.setReleaseVideos(transformVideos(model.getVideos()));
		
		return res;
		
	}
	
	public static ReleaseVideo transformVideo(VideoModel video) {
		if(video==null)
			return null;
		ReleaseVideo res = new ReleaseVideo();
		
		res.setVideoLink(video.getLink());
		
		return res;
	}
	
	public static List<ReleaseVideo> transformVideos(List<VideoModel> video) {
		List<ReleaseVideo> res = new ArrayList<ReleaseVideo>();
		
		if(video==null)
			return res;
		
		for(VideoModel v : video) {
			res.add(transformVideo(v));
		}
		return res;
	}
	
	
	
	
	
	
	
	public static ReleaseModel transformReleaseToModel(Release rel) {
		if(rel==null)
			return null;
		
		ReleaseModel res = new ReleaseModel();
		
		res.setNameWithUnderscore(rel.getReleaseName());
		res.setArtist(rel.getAuthor());
		res.setCrew(rel.getCrew());
		res.setSong(rel.getSongName());
		res.setYear(rel.getYears());
		
//		res.setVideos(transformVideosToModel(rel.getReleaseVideos()));
		
		return res;
		
	}
	
	public static VideoModel transformVideoToModel(ReleaseVideo video) {
		if(video==null)
			return null;
		
		VideoModel res = new VideoModel();
		
		res.setLink(video.getVideoLink());
		
		return res;
	}
	
	public static List<VideoModel> transformVideosToModel(List<ReleaseVideo> video) {
		List<VideoModel> res = new ArrayList<VideoModel>();
		
		if(video==null)
			return res;
		
		for(ReleaseVideo v : video) {
			res.add(transformVideoToModel(v));
		}
		return res;
	}
	
	public static LinkModel transformLinkToModel(ReleaseLink video) {
		if(video==null)
			return null;
		
		LinkModel res = new LinkModel();
		
		res.setLink(video.getReleaseLink());
		
		return res;
	}
	
	public static List<LinkModel> transformLinksToModel(List<ReleaseLink> video) {
		List<LinkModel> res = new ArrayList<LinkModel>();
		
		if(video==null)
			return res;
		
		for(ReleaseLink v : video) {
			res.add(transformLinkToModel(v));
		}
		return res;
	}

}
