package pl.edu.pwr.wordnetloom.systems.enums;

import java.util.HashMap;
import java.util.Map;

public enum EmotionalMarkednes {

	NOT_SET("Nie ustawione"),
	STRONG_POSITIVE("+ m (mocne nacechowanie pozytywne jednostki"),
	WEAK_POSITIVE("+ s (słabe nacechowanie pozytywne jednostki)"),
	STRONG_NEGATIVE("- m ( mocne nacechowanie negatywne jednostki)"),
	WEAK_NEGATIVE("- s (mocne nacechowanie negatywne jednostki);");

	private String caption;

	private static final Map<String, EmotionalMarkednes> lookup = new HashMap<String, EmotionalMarkednes>();

	static {
		for (EmotionalMarkednes abbr : EmotionalMarkednes.values())
			lookup.put(abbr.getCaption(), abbr);
	}

	public static EmotionalMarkednes get(String abbreviation) {
		if(abbreviation == null) return NOT_SET;
		return lookup.get(abbreviation);
	}
	
	private EmotionalMarkednes(String caption) {
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
