package pl.edu.pwr.wordnetloom.rest.common.model;

public class ResourceMessage {
	private final String resource;

	private static final String KEY_EXISTENT = "%s.existent";
	private static final String MESSAGE_EXISTENT = "There is already a %s for the given %s";
	private static final String MESSAGE_INVALID_FIELD = "%s.invalidField.%s";
	private static final String KEY_NOT_FOUND = "%s.NotFound";
	private static final String MESSAGE_NOT_FOUND = "%s not found";
	private static final String NOT_FOUND = "Not found";

	public ResourceMessage(final String resource) {
		this.resource = resource;
	}

	public String getKeyOfResourceExistent() {
		return String.format(KEY_EXISTENT, resource);
	}

	public String getMessageOfResourceExistent(final String fieldsNames) {
		return String.format(MESSAGE_EXISTENT, resource, fieldsNames);
	}

	public String getKeyOfInvalidField(final String invalidField) {
		return String.format(MESSAGE_INVALID_FIELD, resource, invalidField);
	}

	public String getKeyOfResourceNotFound() {
		return String.format(KEY_NOT_FOUND, resource);
	}

	public String getMessageOfResourceNotFound() {
		return String.format(MESSAGE_NOT_FOUND, resource);
	}

	public String getMessageNotFound() {
		return NOT_FOUND;
	}

}