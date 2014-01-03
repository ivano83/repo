package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.UserMapper;
import it.fivano.symusic.backend.model.User;
import it.fivano.symusic.backend.model.UserExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.UserModel;

import java.util.List;

public class UserService extends RootService {
	
	public UserService() {
		this.setLogger(getClass());
	}
	
	private UserMapper getUserMapper() throws BackEndException {
		return this.apriSessione().getMapper(UserMapper.class);
	}
	
	public UserModel getUser(Long id) throws BackEndException {
		
		try {

			UserMapper uDao = this.getUserMapper();
									
			UserModel res = TransformerUtility.transformUserToModel(uDao.selectByPrimaryKey(id));
			
			return res;
			
			
		} finally {
			this.chiudiSessione();
		}
	}
	
	public UserModel getUser(String userName) throws BackEndException {
		
		try {

			UserMapper uDao = this.getUserMapper();
			
			UserExample ex = new UserExample();
			ex.createCriteria().andUserNameEqualTo(userName);
			List<User> res = uDao.selectByExample(ex);
			if(res!=null && !res.isEmpty())
				return TransformerUtility.transformUserToModel(res.get(0));
			else
				return null;
			
			
		} finally {
			this.chiudiSessione();
		}
	}
	

	
}
