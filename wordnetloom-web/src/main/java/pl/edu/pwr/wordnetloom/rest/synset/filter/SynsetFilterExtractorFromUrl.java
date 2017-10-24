package pl.edu.pwr.wordnetloom.rest.synset.filter;

import pl.edu.pwr.wordnetloom.common.resource.AbstractFilterExtractorFromUrl;
import pl.edu.pwr.wordnetloom.dto.SynsetFilter;

import javax.ws.rs.core.UriInfo;

public class SynsetFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public SynsetFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public SynsetFilter getFilter() {

        final SynsetFilter synsetFilter = new SynsetFilter();

        synsetFilter.setPaginationData(extractPaginationData());
        synsetFilter.setLemma(getUriInfo().getQueryParameters().getFirst("lemma"));

        final String lexiconIdStr = getUriInfo().getQueryParameters().getFirst("lexiconId");

        if (lexiconIdStr != null) {
            synsetFilter.setLexiconId(Long.valueOf(lexiconIdStr));
        }

        final String posIdStr = getUriInfo().getQueryParameters().getFirst("partOfSpeechId");
        if (posIdStr != null) {
            synsetFilter.setPartOfSpeechId(Long.valueOf(posIdStr));
        }

        final String domainIdStr = getUriInfo().getQueryParameters().getFirst("domainId");
        if (domainIdStr != null) {
            synsetFilter.setDomainId(Long.valueOf(domainIdStr));
        }

        return synsetFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "lemma";
    }

}