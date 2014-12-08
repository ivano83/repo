package it.fivano.symusic.backend;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.model.Genre;
import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseExtraction;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseTrack;
import it.fivano.symusic.backend.model.ReleaseUserOption;
import it.fivano.symusic.backend.model.ReleaseVideo;
import it.fivano.symusic.backend.model.User;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseFlagModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.UserModel;
import it.fivano.symusic.model.VideoModel;

public class TransformerUtility {
	
	
	public static Release transformRelease(ReleaseModel model) throws ParseException {
		if(model==null)
			return null;
		
		Release res = new Release();
		
		res.setIdRelease(model.getId());
		res.setReleaseName(model.getNameWithUnderscore());
		res.setAuthor(model.getArtist());
		res.setCrew(model.getCrew());
		res.setSongName(model.getSong());
		res.setYears(model.getYear());
		res.setReleaseDate(SymusicUtility.getStandardDate(model.getReleaseDate()));
		
		if(model.getGenre()!=null)
			res.setIdGenre(model.getGenre().getId());
				
		return res;
		
	}
	
	public static ReleaseVideo transformVideo(VideoModel video, Long idRelease) {
		if(video==null)
			return null;
		ReleaseVideo res = new ReleaseVideo();
		
		res.setIdRelease(idRelease);
		res.setVideoLink(video.getLink());
		res.setVideoName(video.getName());
		
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
	
	public static ReleaseTrack transformTrack(TrackModel track, Long idRelease) {
		if(track==null)
			return null;
		ReleaseTrack res = new ReleaseTrack();
		
		res.setIdRelease(idRelease);
		res.setTrackName(track.getTrackName());
		res.setTrackBpm(track.getBpm());
		res.setTrackNumber((track.getTrackNumber()==null)? null : track.getTrackNumber().toString());
		res.setTrackGenere(track.getGenere());
		res.setTrackTime(track.getTime());
		res.setIdReleaseTrack(track.getIdTrack());
		
		return res;
	}
	
	public static List<Genre> transformGenres(List<GenreModel> genre) {
		List<Genre> res = new ArrayList<Genre>();
		
		if(genre==null)
			return res;
		
		for(GenreModel v : genre) {
			res.add(transformGenre(v));
		}
		return res;
	}
	
	public static Genre transformGenre(GenreModel genre) {
		if(genre==null)
			return null;
		Genre res = new Genre();
		
		res.setIdGenre(genre.getId());
		res.setName(genre.getName());
		
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
	
	public static ReleaseExtraction transformReleaseExtraction(ReleaseExtractionModel relExtr) {
		
		if(relExtr==null)
			return null;
		
		ReleaseExtraction res = new ReleaseExtraction();
		res.setIdRelease(relExtr.getIdRelease());
		res.setBeatport(relExtr.getBeatport());
		res.setBeatportDateExtraction(relExtr.getBeatportDate());
		res.setYoutube(relExtr.getYoutube());
		res.setYoutubeDateExtraction(relExtr.getYoutubeDate());
		res.setScenelog(relExtr.getScenelog());
		res.setScenelogDateExtraction(relExtr.getScenelogDate());
		return res;
	}
	
	
	
	public static ReleaseModel transformReleaseToModel(Release rel) throws ParseException {
		if(rel==null)
			return null;
		
		ReleaseModel res = new ReleaseModel();
		
		res.setId(rel.getIdRelease());
		res.setNameWithUnderscore(rel.getReleaseName());
		res.setArtist(rel.getAuthor());
		res.setCrew(rel.getCrew());
		res.setSong(rel.getSongName());
		res.setYear(rel.getYears());
		res.setReleaseDate(SymusicUtility.getStandardDate(rel.getReleaseDate()));
		if(rel.getIdGenre()!=null) {
			GenreModel genre = new GenreModel();
			genre.setId(rel.getIdGenre());
			res.setGenre(genre);
		}
		
 
		return res;
		
	}
	
	public static List<ReleaseModel> transformReleasesToModel(List<Release> in) throws ParseException {
		List<ReleaseModel> res = new ArrayList<ReleaseModel>();
		
		for(Release r : in) {
			res.add(transformReleaseToModel(r));
		}
		
		return res;
		
	}
	
	public static VideoModel transformVideoToModel(ReleaseVideo video) {
		if(video==null)
			return null;
		
		VideoModel res = new VideoModel();
		
		res.setLink(video.getVideoLink());
		res.setName(video.getVideoName());
		
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
		res.setIdTrack(track.getIdReleaseTrack());
		
		return res;
	}
	
	public static List<TrackModel> transformTracksToModel(List<ReleaseTrack> tr) {
		List<TrackModel> res = new ArrayList<TrackModel>();
		
		if(tr==null)
			return res;
		
		for(ReleaseTrack v : tr) {
			res.add(transformTrackToModel(v));
		}
		return res;
	}

	public static List<GenreModel> transformGenreToModel(List<Genre> genres) {
		List<GenreModel> res = new ArrayList<GenreModel>();
		
		if(genres==null)
			return res;
		
		for(Genre g : genres) {
			res.add(transformGenreToModel(g));
		}
		return res;
	}
	
	public static GenreModel transformGenreToModel(Genre genre) {
		
		if(genre==null)
			return null;
		
		GenreModel res = new GenreModel();
		res.setId(genre.getIdGenre());
		res.setName(genre.getName());
		
		return res;
	}

	public static ReleaseExtractionModel transformReleaseExtractionToModel(ReleaseExtraction relExtr) {
		
		if(relExtr==null)
			return null;
		
		ReleaseExtractionModel res = new ReleaseExtractionModel();
		res.setIdRelease(relExtr.getIdRelease());
		res.setBeatport(relExtr.getBeatport());
		res.setBeatportDate(relExtr.getBeatportDateExtraction());
		res.setYoutube(relExtr.getYoutube());
		res.setYoutubeDate(relExtr.getYoutubeDateExtraction());
		res.setScenelog(relExtr.getScenelog());
		res.setScenelogDate(relExtr.getScenelogDateExtraction());
		return res;
	}

	public static UserModel transformUserToModel(User user) {
		if(user==null)
			return null;
		
		UserModel res = new UserModel();
		res.setId(user.getIdUser());
		res.setUserName(user.getUserName());
		
		return res;
	}

	public static ReleaseFlagModel transformReleaseUserOptionToModel(ReleaseUserOption input) {
		if(input==null)
			return null;
		
		ReleaseFlagModel res = new ReleaseFlagModel();
		res.setDownloaded(input.getReleaseDownloaded());
		res.setPreview(input.getReleasePreview());
		
		return res;
	}

	public static ReleaseUserOption transformReleaseUserOption(ReleaseFlagModel input, Long idRelease, Long idUser) {
		
		if(input==null)
			return null;
		
		ReleaseUserOption res = new ReleaseUserOption();
		res.setIdRelease(idRelease);
		res.setIdUser(idUser);
		res.setReleaseDownloaded(input.getDownloaded());
		res.setReleasePreview(input.getPreview());
		
		return res;
	}

}
