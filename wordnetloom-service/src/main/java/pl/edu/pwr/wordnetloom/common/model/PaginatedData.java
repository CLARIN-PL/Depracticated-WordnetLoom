package pl.edu.pwr.wordnetloom.common.model;

import java.util.List;

public class PaginatedData<T> {
    private final long numberOfRows;
	private final List<T> rows;

	public PaginatedData(final long numberOfRows, final List<T> rows) {
		this.numberOfRows = numberOfRows;
		this.rows = rows;
	}

	public long getNumberOfRows() {
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