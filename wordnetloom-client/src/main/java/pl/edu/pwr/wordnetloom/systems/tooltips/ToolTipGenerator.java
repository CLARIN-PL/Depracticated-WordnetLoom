/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
*/

package pl.edu.pwr.wordnetloom.systems.tooltips;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.model.yiddish.VariantType;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.systems.common.SynsetsRelationsDC;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.systems.misc.IObjectWrapper;
import pl.edu.pwr.wordnetloom.utils.Common;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;

/**
 * klasa dostarcza metod zwracajacych tooltipy dla jednostki leksyklanej i
 * synsetu
 * 
 * @author Max
 */
public class ToolTipGenerator implements ToolTipGeneratorInterface {
	// elementy wspolne
	protected final static String HTML_HEADER = "<html><body style=\"font-weight:normal\">";
	protected final static String HTML_FOOTER = "</body></html>";
	protected final static String HTML_TAB = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	protected final static String HTML_SPA = "&nbsp;";
	protected final static String HTML_BR = "<br>";
	protected final static String DESC_STR = "<div style=\"text-align:left; margin-left:10px; width:250px;\">%s</div>";
	protected final static String RED_FORMAT = "<font color=\"red\">%s</font>";
	static private volatile ToolTipGenerator generator = null;

	// szablony uzywane do wyswietlania danych
	static String SYNSET_TEMPLATE = HTML_SPA + "<font size=\"4\"><b><i>%s</i></b></font>" + HTML_BR + HTML_SPA + "<b>"
			+ Labels.DEFINITION_COLON + "</b> " + DESC_STR + HTML_BR + HTML_SPA + "<b>" + Labels.ARTIFICIAL_COLON
			+ "</b> %s" + HTML_BR + HTML_SPA + "<b>" + Labels.STATUS_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>"
			+ Labels.DOMAIN_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR
			+ HTML_SPA + "<b>" + Labels.SYNSET_COMMENT_COLON + "</b> " + DESC_STR + HTML_BR + HTML_SPA + "<b>"
			+ Labels.UNIT_COMMENT_COLON + "</b> " + DESC_STR + HTML_BR;;

	static String LEXICALUNIT_TEMPLATE = HTML_SPA + "<font size=\"4\"><b><i>%s</i></b></font>" + HTML_BR + HTML_SPA
			+ "<b>" + Labels.LEXICON_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>" + Labels.DOMAIN_COLON + "</b> %s"
			+ HTML_BR + HTML_SPA + "<b>" + Labels.PARTS_OF_SPEECH_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>"
			+ Labels.REGISTER_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>" + Labels.DEFINITION_COLON + "</b>"
			+ DESC_STR + HTML_SPA + "<b>" + Labels.USE_CASE_COLON + "</b>" + DESC_STR;

	static String SYNSET_RELATION_TEMPLATE = HTML_SPA + "<b>" + Labels.RELATION_COLON + "</b> %s" + HTML_BR + HTML_SPA
			+ "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>" + Labels.CORRECT_COLON + "</b> %s"
			+ HTML_BR + HTML_SPA + "<b>" + Labels.SOURCE_SYNSET_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>"
			+ Labels.TARGET_SYNSET_COLON + "</b> %s" + HTML_BR;

	static String LEXICAL_RELATION_TEMPLATE = HTML_SPA + "<b>" + Labels.RELATION_COLON + "</b> %s" + HTML_BR + HTML_SPA
			+ "<b>" + Labels.OWNER_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>" + Labels.CORRECT_COLON + "</b> %s"
			+ HTML_BR + HTML_SPA + "<b>" + Labels.SOURCE_UNIT_COLON + "</b> %s" + HTML_BR + HTML_SPA + "<b>"
			+ Labels.TARGET_UNIT_COLON + "</b> %s" + HTML_BR;

	static String YDDISH_TEMPLATE = "<b>%s</b>" + HTML_BR + HTML_SPA + "<b>Semantic fields:</b> %s" + HTML_BR + HTML_SPA
			+ "<b>Meaning:</b> %s" + HTML_BR + HTML_SPA + "<b>Grammatical Gender:</b> %s" + HTML_BR + HTML_SPA
			+ "<b>Style:</b> %s" + HTML_BR + HTML_SPA + "<b>Lexical Characteristc:</b> %s" + HTML_BR + HTML_SPA
			+ "<b>Status:</b> %s" + HTML_BR + HTML_SPA + "<b>Etymology:</b> %s" + HTML_BR + HTML_SPA + "<b>Age</b> %s"
			+ HTML_BR + HTML_SPA + "<b>Inflection:</b> %s" + HTML_BR + HTML_SPA + "<b>Etymological root:</b> %s"
			+ HTML_BR + HTML_SPA + "<b>Particels:</b> %s" + HTML_BR;

	static String RELATIONS_HEADER = HTML_SPA + "<font size=\"4\"><b><i>" + Labels.RELATIONS_COLON + "</i></b></font>"
			+ HTML_BR;

	final static String RELATIONS_TYPE = HTML_TAB + "<b>%s:</b>" + HTML_BR;
	final static String RELATIONS_ITEM = HTML_TAB + HTML_TAB + "%s" + HTML_BR;

	static String SYNSETS_HEADER = HTML_SPA + "<font size=\"4\"><b><i>" + Labels.SYNSETS_COLON + "</i></b></font>"
			+ HTML_BR;

	final static String SYNSET_ITEM = DESC_STR;
	private boolean isEnabled = true;

	/**
	 * konstrukor prywatny
	 *
	 */
	private ToolTipGenerator() {
		/***/
	}

	/**
	 * odczytanie instancji generatora
	 * 
	 * @return generator
	 */
	static public ToolTipGenerator getGenerator() {
		if (generator == null) {
			synchronized (ToolTipGenerator.class) {
				if (generator == null) {
					generator = new ToolTipGenerator();
				}
			}
		}
		return generator;
	}

	/**
	 * formatowanie wartosci
	 * 
	 * @param value
	 *            - wartosc
	 * @return sformatowana wartosc
	 */
	static private String format(String value) {
		if (value == null || value.equals(""))
			return "...";
		return value;
	}

	/**
	 * odczytanie tooltipu dla jednostki leksykalnej
	 * 
	 * @param object
	 *            jednostka leksykalna
	 * @return hint do wyswietlenia
	 */
	private String getToolTipText(Sense object) {
		if (!isEnabled) {
			return null;
		}

		object = RemoteUtils.lexicalUnitRemote.dbGet(object.getId());

		Domain domain = DomainManager.getInstance().getNormalized(object.getDomain());
		String register = Common.getSenseAttribute(object, Sense.REGISTER);
		String useCase = Common.getSenseAttribute(object, Sense.USE_CASES);
		StringBuilder exampleBuilder = new StringBuilder();
		if (useCase != null) {
			String[] examples = useCase.split("\\|");
			for (String example : examples) {
				exampleBuilder.append(String.format(DESC_STR, example));
			}
		}
		// czy jest bledny
		String global = String.format(LEXICALUNIT_TEMPLATE, object.toString(), object.getLexicon().getName().getText(),
				domain.toString() + " => " + domain.getDescription().toString(),
				PosManager.getInstance().getNormalized(object.getPartOfSpeech()),
				register == null ? Labels.ND : register, format(Common.getSenseAttribute(object, Sense.COMMENT)),
				useCase == null ? Labels.ND : exampleBuilder.toString());

		// odczytanie relacji
		List<SenseRelation> relations = RemoteUtils.unitsRelationsRemote.dbGetRelations(object, null,
				SenseRelation.IS_PARENT, false);
		StringBuilder relString = new StringBuilder();
		if (relations.size() > 0) {
			relString.append(RELATIONS_HEADER);
			// stworzenie opisu relacji
			
			// sortowanie listy według rodzaju relacji
			//TODO można zobaczyć, czy da radę pobierać już posortowane dane z bazy, czy nie będzie to przeszkadzać w innym miejscu
			Collections.sort(relations, (e1, e2) -> e1.getRelation().getId().compareTo(e2.getRelation().getId()));
			// typ relacji w obecnej iteracji
			RelationType currentRelationType = null;
			// ostatni typ relacji (nie jest to typ z ostatniej iteracji, a poprzedni typ który nie jest typem obecnym)
			RelationType lastRelationType = null;
			for(SenseRelation relation : relations)
			{
				currentRelationType = relation.getRelation();
				// jeżeli poprzedni tym i obecny nioe sa zgodne, oznacza to, że wypisujemy kolejny tym relacji. W taim pzypadku wypiosujemy nazwę tej relacji
				if(lastRelationType == null || currentRelationType != lastRelationType)
				{
					relString.append(String.format(RELATIONS_TYPE, currentRelationType.getName()));
					lastRelationType = currentRelationType;
				}
				//wypisujemy wyraz z którym jest w relacji
				relString.append(String.format(RELATIONS_ITEM, relation.getSenseTo().toString()));
			}
		}

		// doczytanie synsetow dla jednostki
		List<Synset> synsets = RemoteUtils.lexicalUnitRemote.dbFastGetSynsets(object,
				LexiconManager.getInstance().getLexicons());
		StringBuilder synsetString = new StringBuilder();
		if (synsets.size() > 0) {
			synsetString.append(SYNSETS_HEADER);
			for (Synset synset : synsets) {
				synsetString.append(String.format(SYNSET_ITEM, "" + RemoteUtils.synsetRemote.dbRebuildUnitsStr(synset,
						LexiconManager.getInstance().getLexicons())));
			}
		}
		// yddish string for variants

		final StringBuilder yy = new StringBuilder();

		if (object.getYiddishSenseExtension().size() > 0) {

			String variants = HTML_SPA + "<b>Variants:</b> ";

			for (YiddishSenseExtension se : object.getYiddishSenseExtension()) {

				if (se.getVariant() == VariantType.Yiddish_Primary_Lemma) {
					StringBuilder sf = new StringBuilder();
					sf.append("");
					if (se.getYiddishDomains() != null) {
						se.getYiddishDomains().forEach(d -> sf.append(d.toString()).append(","));
					}
					StringBuilder infl = new StringBuilder();
					infl.append("");
					if (se.getInflection() != null) {
						se.getInflection().stream().forEach(i -> infl.append(i.toString()).append(","));
					}
					StringBuilder part = new StringBuilder();
					part.append("");
					if (se.getParticels() != null) {
						se.getParticels().stream().forEach(p -> part.append(p.toString()).append(","));
					}
					yy.append(String.format(YDDISH_TEMPLATE, se.getVariant().name(), sf.toString(),
							se.getMeaning() == null ? Labels.ND : se.getMeaning(),
							se.getGrammaticalGender() == null ? Labels.ND : se.getGrammaticalGender().getName(),
							se.getStyle() == null ? Labels.ND : se.getStyle().getName(),
							se.getLexicalCharcteristic() == null ? Labels.ND : se.getLexicalCharcteristic().getName(),
							se.getStatus() == null ? Labels.ND : se.getStatus().getName(),
							se.getEtymology() == null ? Labels.ND : se.getEtymology(),
							se.getAge() == null ? Labels.ND : se.getAge(), infl.toString(),
							se.getEtymologicalRoot() == null ? Labels.ND : se.getEtymologicalRoot(), part.toString()));
				} else {
					variants += se.getLatinSpelling() + " (" + se.getVariant().name() + "), ";
				}

			}
			yy.append(variants);

		}
		return HTML_HEADER + global + relString.toString() + synsetString.toString() + yy.toString() + HTML_FOOTER;
	}

	/**
	 * odczytanie tooltipu dla synsetu
	 * 
	 * @param object
	 *            synsetu
	 * @return hint do wyswietlenia
	 */
	private String getToolTipText(Synset object) {
		if (!isEnabled) {
			return null;
		}

		object = RemoteUtils.synsetRemote.dbGet(object.getId());
		String domain_str = "";
		Domain domain = RemoteUtils.synsetRemote.dbGetDomain(object, LexiconManager.getInstance().getLexicons());
		if (domain != null) {
			domain_str = domain.toString();
		}

		Iterator<Sense> itr = RemoteUtils.lexicalUnitRemote
				.dbFullGetUnits(object, 999, LexiconManager.getInstance().getLexicons()).iterator();

		String lu_comment = "brak";
		if (itr.hasNext())
			lu_comment = Common.getSenseAttribute(itr.next(), Sense.COMMENT);

		String global = String.format(SYNSET_TEMPLATE, object.toString(),
				format(Common.getSynsetAttribute(object, Synset.DEFINITION)),
				Synset.isAbstract(Common.getSynsetAttribute(object, Synset.ISABSTRACT))
						? String.format(RED_FORMAT, "tak") : "nie",
				"OK", domain_str, "", format(Common.getSynsetAttribute(object, Synset.COMMENT)), format(lu_comment));

		// odczytanie relacji
		Collection<SynsetsRelationsDC> relations = SynsetsRelationsDC.dbFastGetRelations(object,
				SynsetsRelationsDC.IS_PARENT, false);
		StringBuilder relString = new StringBuilder();
		if (relations.size() > 0) {
			relString.append(RELATIONS_HEADER);
			// stworzenie opisu relacji
			for (SynsetsRelationsDC synsetsRelations : relations) {
				relString.append(String.format( // dodanie typu relacji
						RELATIONS_TYPE, synsetsRelations.getRelationType().toString()));

				int size = synsetsRelations.getSize();
				for (int i = 0; i < size; i++) {
					// zabezpieczenie przed nullami
					SynsetRelation relTmp = synsetsRelations.getRelation(i);
					String text = null;
					if (relTmp != null && relTmp.getSynsetTo() != null)
						text = relTmp.getSynsetTo().toString();

					// dodanie konkretnej relacji
					relString.append(String.format(RELATIONS_ITEM, text != null ? text : ""));
				}
			}
		}

		return HTML_HEADER + global + relString.toString() + HTML_FOOTER;
	}

	/**
	 * Get tooltip for synset relation
	 * 
	 * @param object
	 *            - synset relation
	 * @return hint to show
	 */
	private String getToolTipText(SynsetRelation object) {
		if (!isEnabled) {
			return null;
		}

		object = RemoteUtils.synsetRelationRemote.dbGet(object.getId());
		;

		String global = String.format(SYNSET_RELATION_TEMPLATE, object, "", "tak", object.getSynsetFrom(),
				object.getSynsetTo());

		return HTML_HEADER + global + HTML_FOOTER;
	}

	/**
	 * Get tooltip for lexical relation
	 * 
	 * @param object
	 *            - lexical relation
	 * @return hint to show
	 */
	private String getToolTipText(SenseRelation object) {
		if (!isEnabled) {
			return null;
		}

		object = RemoteUtils.lexicalRelationRemote.dbGet(object.getId());

		String global = String.format(LEXICAL_RELATION_TEMPLATE, object, "", "tak", object.getSenseFrom(),
				object.getSenseTo());

		return HTML_HEADER + global + HTML_FOOTER;
	}

	/**
	 * wygenerowanie tooltipu da obiektu
	 * 
	 * @param object
	 *            - obiekt dla ktorego ma zostać wygenerowany opis
	 * @return @return hint do wyswietlenia
	 */
	public String getToolTipText(Object object) {
		if (!isEnabled) {
			return null;
		}

		try {
			if (object instanceof IObjectWrapper) {
				return getToolTipText(((IObjectWrapper) object).getObject());
			} else if (object instanceof Synset) {
				return getToolTipText((Synset) object);
			} else if (object instanceof Sense) {
				return getToolTipText((Sense) object);
			} else if (object instanceof SynsetRelation) {
				return getToolTipText((SynsetRelation) object);
			} else if (object instanceof SenseRelation) {
				return getToolTipText((SenseRelation) object);
			}
		} catch (Exception e) {
			Logger.getLogger(ToolTipGenerator.class).log(Level.ERROR, "While getting tooltip " + e);
		}
		return "";
	}

	/**
	 * wlacza/wylacza pokazywanie rozszerzonych tooltipow
	 * 
	 * @param isEnabled
	 *            - TRUE wlacza pokazywanie, FALSE wylacza
	 */
	public void setEnabledTooltips(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * odczytuje czy pokazywanie podpowiedzi jest aktualnie wlaczone lub
	 * wylaczone
	 * 
	 * @return TRUE jesli podpowiedzi sa pokazywane
	 *
	 */
	@Override
	public boolean hasEnabledTooltips() {
		return this.isEnabled;
	}

}
