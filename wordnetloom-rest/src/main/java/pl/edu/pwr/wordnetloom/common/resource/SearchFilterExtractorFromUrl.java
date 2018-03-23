package pl.edu.pwr.wordnetloom.common.resource;

import pl.edu.pwr.wordnetloom.common.filter.SearchFilter;

import javax.ws.rs.core.UriInfo;

public class SearchFilterExtractorFromUrl extends AbstractFilterExtractorFromUrl {

    public SearchFilterExtractorFromUrl(final UriInfo uriInfo) {
        super(uriInfo);
    }

    public SearchFilter getFilter() {

        final SearchFilter searchFilter = new SearchFilter();

        searchFilter.setPaginationData(extractPaginationData());
        searchFilter.setLemma(getUriInfo().getQueryParameters().getFirst("lemma"));

        final String lexiconIdStr = getUriInfo().getQueryParameters().getFirst("lexiconId");
        if (lexiconIdStr != null) {
            searchFilter.setLexicon(Long.valueOf(lexiconIdStr));
        }

        final String posIdStr = getUriInfo().getQueryParameters().getFirst("partOfSpeechId");
        if (posIdStr != null) {
            searchFilter.setPartOfSpeechId(Long.valueOf(posIdStr));
        }

        final String domainIdStr = getUriInfo().getQueryParameters().getFirst("domainId");
        if (domainIdStr != null) {
            searchFilter.setDomainId(Long.valueOf(domainIdStr));
        }

        final String relationTypeId = getUriInfo().getQueryParameters().getFirst("relationTypeId");
        if (relationTypeId != null) {
            searchFilter.setRelationTypeId(Long.valueOf(relationTypeId));
        }

        final String statusId = getUriInfo().getQueryParameters().getFirst("statusId");
        if (statusId != null) {
            searchFilter.setStatusId(Long.valueOf(statusId));
        }

        final String registerId = getUriInfo().getQueryParameters().getFirst("registerId");
        if (registerId != null) {
            searchFilter.setRegisterId(Long.valueOf(registerId));
        }

        final String isAbstractStr = getUriInfo().getQueryParameters().getFirst("isAbstract");
        if(isAbstractStr != null){
            searchFilter.setAbstract(Boolean.valueOf(isAbstractStr));
        }

        searchFilter.setDefinition(getUriInfo().getQueryParameters().getFirst("definition"));

        searchFilter.setExample(getUriInfo().getQueryParameters().getFirst("example"));

        return searchFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "lemma";
    }

}