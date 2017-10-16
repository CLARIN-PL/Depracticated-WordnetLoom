package pl.edu.pwr.wordnetloom.rest.sense.resource;

import pl.edu.pwr.wordnetloom.common.resource.AbstractFilterExtractorFromUrl;
import pl.edu.pwr.wordnetloom.dto.SenseFilter;

import javax.ws.rs.core.UriInfo;

public class SenseFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

	public SenseFilterExtractorFromUrl(final UriInfo uriInfo) {
		super(uriInfo);
	}

	public SenseFilter getFilter() {

		final SenseFilter senseFilter = new SenseFilter();

		//senseFilter.setPaginationData(extractPaginationData());
		senseFilter.setLemma(getUriInfo().getQueryParameters().getFirst("lemma"));

		final String categoryIdStr = getUriInfo().getQueryParameters().getFirst("lexiconId");
		if (categoryIdStr != null) {
			senseFilter.setLexiconId(Long.valueOf(categoryIdStr));
		}

		return senseFilter;
	}

	@Override
	protected String getDefaultSortField() {
		return "lemma";
	}

}