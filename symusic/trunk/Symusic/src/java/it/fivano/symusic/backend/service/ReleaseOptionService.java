package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseExtractionMapper;
import it.fivano.symusic.backend.dao.ReleaseUserOptionMapper;
import it.fivano.symusic.backend.dao.UserMapper;
import it.fivano.symusic.backend.model.ReleaseExtraction;
import it.fivano.symusic.backend.model.ReleaseExtractionExample;
import it.fivano.symusic.backend.model.ReleaseUserOption;
import it.fivano.symusic.backend.model.ReleaseUserOptionExample;
import it.fivano.symusic.backend.model.ReleaseUserOptionKey;
import it.fivano.symusic.backend.model.User;
import it.fivano.symusic.backend.model.UserExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseFlagModel;
import it.fivano.symusic.model.UserModel;

import java.util.List;

public class ReleaseOptionService extends RootService {
	
	public ReleaseOptionService() {
		this.setLogger(getClass());
	}
	
	private ReleaseUserOptionMapper getUserOptionMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseUserOptionMapper.class);
	}
	
	public ReleaseFlagModel getReleaseOption(Long idRelease, Long idUser) throws BackEndException {
		
		try {

			ReleaseUserOptionMapper uDao = this.getUserOptionMapper();
							
			ReleaseUserOptionExample ex = new ReleaseUserOptionExample();
			ex.createCriteria().andIdReleaseEqualTo(idRelease).andIdUserEqualTo(idUser);
			
			List<ReleaseUserOption> res = uDao.selectByExample(ex);
			if(res!=null && !res.isEmpty())
				return TransformerUtility.transformReleaseUserOptionToModel(res.get(0));
			else
				return null;
			
			
		} finally {
			this.chiudiSessione();
		}
	}
	
	
	public ReleaseFlagModel saveOrUpdateReleaseOption(ReleaseFlagModel input, Long idRelease, Long idUser) throws BackEndException {
		
		try {
			if(input==null)
				return null;
			
			ReleaseUserOption rel = TransformerUtility.transformReleaseUserOption(input, idRelease, idUser);
			rel.setIdRelease(idRelease);
			rel.setIdUser(idUser);
			
			if(idRelease!=null && idUser!=null) {
				ReleaseUserOptionMapper releaseExtrDao = this.getUserOptionMapper();
				
				ReleaseUserOptionKey key = new ReleaseUserOptionKey();
				key.setIdRelease(idRelease);
				key.setIdUser(idUser);
				ReleaseUserOption res = releaseExtrDao.selectByPrimaryKey(key);
				if(res!=null) {
					releaseExtrDao.updateByPrimaryKeySelective(rel);
				}
				else {
					if(rel.getReleaseDownloaded()==null)
						rel.setReleaseDownloaded(false);
					if(rel.getReleasePreview()==null)
						rel.setReleasePreview(false);
					releaseExtrDao.insert(rel);
				}

				
			}
			else {
				throw new BackEndException("Non ci sono dati sufficienti per salvare la release: "+rel);
			}
			return TransformerUtility.transformReleaseUserOptionToModel(rel);
		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
}
