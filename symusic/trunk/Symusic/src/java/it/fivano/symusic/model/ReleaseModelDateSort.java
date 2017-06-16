package it.fivano.symusic.model;

import java.text.ParseException;
import java.util.Comparator;

import it.fivano.symusic.SymusicUtility;

public class ReleaseModelDateSort implements Comparator<ReleaseModel> {

	@Override
	public int compare(ReleaseModel o1, ReleaseModel o2) {
		try {
			return SymusicUtility.getStandardDate(o1.getReleaseDate()).compareTo(SymusicUtility.getStandardDate(o2.getReleaseDate()));
		} catch (ParseException e) {
			return 0;
		} catch (NullPointerException e) {
			return 0;
		}
	}



}
