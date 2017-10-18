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

        senseFilter.setPaginationData(extractPaginationData());
        senseFilter.setLemma(getUriInfo().getQueryParameters().getFirst("lemma"));

        final String lexiconIdStr = getUriInfo().getQueryParameters().getFirst("lexiconId");

        if (lexiconIdStr != null) {
            senseFilter.setLexiconId(Long.valueOf(lexiconIdStr));
        }

        final String posIdStr = getUriInfo().getQueryParameters().getFirst("partOfSpeechId");
        if (posIdStr != null) {
            senseFilter.setPartOfSpeechId(Long.valueOf(posIdStr));
        }

        final String domainIdStr = getUriInfo().getQueryParameters().getFirst("domainId");
        if (domainIdStr != null) {
            senseFilter.setDomainId(Long.valueOf(domainIdStr));
        }

        final String relationTypeId = getUriInfo().getQueryParameters().getFirst("relationTypeId");
        if (relationTypeId != null) {
            senseFilter.setRelationTypeId(Long.valueOf(relationTypeId));
        }

        final String grammaticalGenderId = getUriInfo().getQueryParameters().getFirst("grammaticalGenderId");
        if (grammaticalGenderId != null) {
            senseFilter.setGrammaticalGenderId(Long.valueOf(grammaticalGenderId));
        }

        final String sourceId = getUriInfo().getQueryParameters().getFirst("sourceId");
        if (sourceId != null) {
            senseFilter.setSourceId(Long.valueOf(sourceId));
        }

        final String ageId = getUriInfo().getQueryParameters().getFirst("ageId");
        if (ageId != null) {
            senseFilter.setAgeId(Long.valueOf(ageId));
        }

        final String statusId = getUriInfo().getQueryParameters().getFirst("statusId ");
        if (statusId != null) {
            senseFilter.setStatusId(Long.valueOf(statusId));
        }

        final String styleId = getUriInfo().getQueryParameters().getFirst("styleId ");
        if (styleId != null) {
            senseFilter.setStyleId(Long.valueOf(styleId));
        }

        final String yiddishDomainId = getUriInfo().getQueryParameters().getFirst("yiddishDomainId");
        if (yiddishDomainId != null) {
            senseFilter.setYiddishDomainId(Long.valueOf(yiddishDomainId));
        }

        final String lexicalCharacteristicId = getUriInfo().getQueryParameters().getFirst("lexicalCharacteristicId");
        if (lexicalCharacteristicId != null) {
            senseFilter.setLexicalCharacteristicId(Long.valueOf(lexicalCharacteristicId));
        }

        final String domainModifierDictionaryId = getUriInfo().getQueryParameters().getFirst("domainModifierId");
        if (domainModifierDictionaryId != null) {
            senseFilter.setDomainModifierDictionaryId(Long.valueOf(domainModifierDictionaryId));
        }

        senseFilter.setEthymology(getUriInfo().getQueryParameters().getFirst("ethymology"));
        senseFilter.setDefinition(getUriInfo().getQueryParameters().getFirst("definition"));
        senseFilter.setEtymologicalRoot(getUriInfo().getQueryParameters().getFirst("etymologicalRoot"));
        senseFilter.setExample(getUriInfo().getQueryParameters().getFirst("example"));

        return senseFilter;
    }

    @Override
    protected String getDefaultSortField() {
        return "lemma";
    }

}