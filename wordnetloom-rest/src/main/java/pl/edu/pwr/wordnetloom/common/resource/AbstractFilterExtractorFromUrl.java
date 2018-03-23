package pl.edu.pwr.wordnetloom.common.resource;


import javax.ws.rs.core.UriInfo;
import pl.edu.pwr.wordnetloom.common.model.PaginationData;

public abstract class AbstractFilterExtractorFromUrl {
	private UriInfo uriInfo;
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PER_PAGE = 50;

	protected abstract String getDefaultSortField();

	public AbstractFilterExtractorFromUrl(final UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	protected UriInfo getUriInfo() {
		return uriInfo;
	}

	protected PaginationData extractPaginationData() {
		final int perPage = getPerPage();
		final int firstResult = getPage() * perPage;

		String orderField;
		PaginationData.OrderMode orderMode;
		final String sortField = getSortField();

		if (sortField.startsWith("+")) {
			orderField = sortField.substring(1);
			orderMode = PaginationData.OrderMode.ASCENDING;
		} else if (sortField.startsWith("-")) {
			orderField = sortField.substring(1);
			orderMode = PaginationData.OrderMode.DESCENDING;
		} else {
			orderField = sortField;
			orderMode = PaginationData.OrderMode.ASCENDING;
		}

		return new PaginationData(firstResult, perPage, orderField, orderMode);
	}

	protected String getSortField() {
		final String sortField = uriInfo.getQueryParameters().getFirst("sort");
		if (sortField == null) {
			return getDefaultSortField();
		}
		return sortField;
	}

	private Integer getPage() {
		final String page = uriInfo.getQueryParameters().getFirst("page");
		if (page == null) {
			return DEFAULT_PAGE;
		}
		return Integer.parseInt(page);
	}

	private Integer getPerPage() {
		final String perPage = uriInfo.getQueryParameters().getFirst("per_page");
		if (perPage == null) {
			return DEFAULT_PER_PAGE;
		}
		return Integer.parseInt(perPage);
	}

}