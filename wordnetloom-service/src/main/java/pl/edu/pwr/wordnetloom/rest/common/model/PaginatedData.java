package pl.edu.pwr.wordnetloom.rest.common.model;

import java.util.List;

public class PaginatedData<T> {
	private final int numberOfRows;
	private final List<T> rows;

	public PaginatedData(final int numberOfRows, final List<T> rows) {
		this.numberOfRows = numberOfRows;
		this.rows = rows;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public List<T> getRows() {
		return rows;
	}

	public T getRow(final int index) {
		if (index >= rows.size()) {
			return null;
		}
		return rows.get(index);
	}

	@Override
	public String toString() {
		return "PaginatedData [numberOfRows=" + numberOfRows + ", rows=" + rows + "]";
	}

}