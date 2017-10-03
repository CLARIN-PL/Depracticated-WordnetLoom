package pl.edu.pwr.wordnetloom.systems.enums;

import java.util.HashMap;
import java.util.Map;

import pl.edu.pwr.wordnetloom.utils.Labels;

public enum YesNo {

	YES(Labels.YES),
	NO(Labels.NO);

	private String caption;
	
	private static final Map<String, YesNo> lookup = new HashMap<String, YesNo>();

	static {
		for (YesNo abbr : YesNo.values())
			lookup.put(abbr.getCaption(), abbr);
	}

	public static YesNo get(String abbreviation) {
		if(abbreviation == null) return NO;
		return lookup.get(abbreviation);
	}
	
	private YesNo(String caption) {
		this.caption = caption;
	}

	public String getCaption(){
		return caption;
	}

	@Override
	public String toString(){
		return getCaption();
	}
}
