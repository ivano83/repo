package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseExtractionMapper;
import it.fivano.symusic.backend.model.ReleaseExtraction;
import it.fivano.symusic.backend.model.ReleaseExtractionExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.ReleaseExtractionModel;

public class ReleaseExtractionService extends RootService {
	
	public ReleaseExtractionService() {
		this.setLogger(getClass());
	}
	
	private ReleaseExtractionMapper getReleaseExtractionMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseExtractionMapper.class);
	}
	

	public ReleaseExtractionModel getReleaseExtraction(Long idRelease) throws BackEndException {
		
		try {
			ReleaseExtractionMapper releaseExtrDao = this.getReleaseExtractionMapper();
			
			return this.getReleaseExtraction(idRelease, releaseExtrDao);
			
		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
	
	private ReleaseExtractionModel getReleaseExtraction(Long idRelease, ReleaseExtractionMapper releaseExtrDao) throws BackEndException {
		
		ReleaseExtractionExample input = new ReleaseExtractionExample();
		input.createCriteria().andIdReleaseEqualTo(idRelease);

		ReleaseExtraction res = releaseExtrDao.selectByPrimaryKey(idRelease);

//		if(res==null) {
//			log.warn("La ricerca delle estrazioni per id release = '"+idRelease+"' non ha restituito niente");
//			ReleaseExtractionModel extr = new ReleaseExtractionModel();
//			extr.setIdRelease(idRelease);
//			extr = this.saveReleaseExtraction(extr);
//			return extr;
//		}

		return TransformerUtility.transformReleaseExtractionToModel(res);
			
	}
	
	public ReleaseExtractionModel saveReleaseExtraction(ReleaseExtractionModel releaseExtr) throws BackEndException {
		
		try {
			ReleaseExtraction rel = TransformerUtility.transformReleaseExtraction(releaseExtr);
			
			if(rel!=null && rel.getIdRelease()!=null) {
				ReleaseExtractionMapper releaseExtrDao = this.getReleaseExtractionMapper();
				
				ReleaseExtractionExample input = new ReleaseExtractionExample();
				input.createCriteria().andIdReleaseEqualTo(releaseExtr.getIdRelease());
				ReleaseExtraction inputRes = releaseExtrDao.selectByPrimaryKey(releaseExtr.getIdRelease());
				if(inputRes!=null) {
					releaseExtrDao.updateByPrimaryKeySelective(rel);
				}
				else {
					releaseExtrDao.insert(rel);
				}
				
			}
			else {
				throw new BackEndException("Non ci sono dati sufficienti per salvare la release: "+rel);
			}
			return TransformerUtility.transformReleaseExtractionToModel(rel);
		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public int deleteReleaseExtraction(Long idrelease) throws BackEndException {
		
		try {
			
			ReleaseExtractionMapper releaseExtrDao = this.getReleaseExtractionMapper();
			ReleaseExtractionExample input = new ReleaseExtractionExample();
			input.createCriteria().andIdReleaseEqualTo(idrelease);
			
			int result = releaseExtrDao.deleteByExample(input);
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
}
