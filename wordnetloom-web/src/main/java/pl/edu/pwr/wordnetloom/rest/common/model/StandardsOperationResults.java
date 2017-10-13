package pl.edu.pwr.wordnetloom.rest.common.model;

public final class StandardsOperationResults {

	private StandardsOperationResults() {
	}

	public static OperationResult getOperationResultExistent(final ResourceMessage resourceMessage,
			final String fieldsNames) {
		return OperationResult.error(resourceMessage.getKeyOfResourceExistent(),
				resourceMessage.getMessageOfResourceExistent(fieldsNames));
	}

	public static OperationResult getOperationResultNotFound(final ResourceMessage resourceMessage) {
		return OperationResult.error(resourceMessage.getKeyOfResourceNotFound(),
				resourceMessage.getMessageOfResourceNotFound());
	}

	public static OperationResult getOperationResultDependencyNotFound(final ResourceMessage resourceMessage,
			final String dependencyField) {
		return OperationResult.error(resourceMessage.getKeyOfInvalidField(dependencyField),
				resourceMessage.getMessageNotFound());
	}

}