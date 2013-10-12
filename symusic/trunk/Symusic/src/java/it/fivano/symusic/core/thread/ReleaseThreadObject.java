package it.fivano.symusic.core.thread;

import it.fivano.symusic.model.ReleaseModel;

import java.util.Map;
import java.util.TreeMap;

public class ReleaseThreadObject extends ThreadObject  {
	
	private Map<Integer,ReleaseModel> releaseResults;
	private SupportObject support;

	public ReleaseThreadObject(int maxActiveThread, Object monitor, SupportObject support) {
		super(maxActiveThread, monitor);
		this.releaseResults = new TreeMap<Integer, ReleaseModel>();
		this.support = support;
	}


	public Map<Integer,ReleaseModel> getReleaseResults() {
		return releaseResults;
	}

	public void addRelease(Integer i, ReleaseModel releaseResult) {
		synchronized (monitor) {
			this.releaseResults.put(i, releaseResult);
		}
	}
	
	public SupportObject getSupport() {
		return this.support;
	}
}
