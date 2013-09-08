package it.fivano.symusic.backend;

import java.util.ArrayList;
import java.util.List;

import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseTrack;
import it.fivano.symusic.backend.model.ReleaseVideo;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.VideoModel;

public class TransformerUtility {
	
	
	public static Release transformRelease(ReleaseModel model) {
		if(model==null)
			return null;
		
		Release res = new Release();
		
		res.setIdRelease(model.getId());
		res.setReleaseName(model.getNameWithUnderscore());
		res.setAuthor(model.getArtist());
		res.setCrew(model.getCrew());
		res.setSongName(model.getSong());
		res.setYears(model.getYear());
				
		return res;
		
	}
	
	public static ReleaseVideo transformVideo(VideoModel video, Long idRelease) {
		if(video==null)
			return null;
		ReleaseVideo res = new ReleaseVideo();
		
		res.setIdRelease(idRelease);
		res.setVideoLink(video.getLink());
		
		return res;
	}
	
	public static List<ReleaseVideo> transformVideos(List<VideoModel> video, Long idRelease) {
		List<ReleaseVideo> res = new ArrayList<ReleaseVideo>();
		
		if(video==null)
			return res;
		
		for(VideoModel v : video) {
			res.add(transformVideo(v,idRelease));
		}
		return res;
	}
	
	public static ReleaseLink transformLink(LinkModel link, Long idRelease) {
		if(link==null)
			return null;
		ReleaseLink res = new ReleaseLink();
		
		res.setIdRelease(idRelease);
		res.setReleaseLink(link.getLink());
		
		return res;
	}
	
	public static List<ReleaseLink> transformLinks(List<LinkModel> video, Long idRelease) {
		List<ReleaseLink> res = new ArrayList<ReleaseLink>();
		
		if(video==null)
			return res;
		
		for(LinkModel v : video) {
			res.add(transformLink(v,idRelease));
		}
		return res;
	}
	
	public static ReleaseTrack transformTrack(TrackModel link, Long idRelease) {
		if(link==null)
			return null;
		ReleaseTrack res = new ReleaseTrack();
		
		res.setIdRelease(idRelease);
		res.setTrackName(link.getTrackName());
		res.setTrackBpm(link.getBpm());
		res.setTrackNumber((link.getTrackNumber()==null)? null : link.getTrackNumber().toString());
		res.setTrackGenere(link.getGenere());
		res.setTrackTime(link.getTime());
		
		return res;
	}
	
	public static List<ReleaseTrack> transformTracks(List<TrackModel> link, Long idRelease) {
		List<ReleaseTrack> res = new ArrayList<ReleaseTrack>();
		
		if(link==null)
			return res;
		
		for(TrackModel v : link) {
			res.add(transformTrack(v,idRelease));
		}
		return res;
	}
	
	
	
	
	public static ReleaseModel transformReleaseToModel(Release rel) {
		if(rel==null)
			return null;
		
		ReleaseModel res = new ReleaseModel();
		
		res.setId(rel.getIdRelease());
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
	
	public static TrackModel transformTrackToModel(ReleaseTrack track) {
		if(track==null)
			return null;
		
		TrackModel res = new TrackModel();
		
		res.setTrackName(track.getTrackName());
		res.setBpm(track.getTrackBpm());
		res.setGenere(track.getTrackGenere());
		res.setTime(track.getTrackTime());
		res.setTrackNumber((track.getTrackNumber()==null)?null : Integer.parseInt(track.getTrackNumber()));
		
		return res;
	}
	
	public static List<TrackModel> transformTracksToModel(List<ReleaseTrack> video) {
		List<TrackModel> res = new ArrayList<TrackModel>();
		
		if(video==null)
			return res;
		
		for(ReleaseTrack v : video) {
			res.add(transformTrackToModel(v));
		}
		return res;
	}

}
