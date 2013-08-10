package it.ivano.backupsync.util;

public class UidUtility {

	private static UidUtility instance;
	
	private int count;
	
	private UidUtility() {
		count = 1;
	}
	
	public static UidUtility getInstance() {
		if(instance==null)
			instance = new UidUtility();
		
		return instance;
	}
	
	public int getUid() {
		int i = count;
		count++;
		return i;
	}
}
