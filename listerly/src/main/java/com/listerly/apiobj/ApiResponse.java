package com.listerly.apiobj;


public class ApiResponse {
	public static enum ResponseType {
		SUCCESS, FAILURE
	}
	
	private ResponseType responseType;
	private String responseMessage;
	private AbstractApiObject<?> response;
	
	public ApiResponse() {
		setResponseType(ResponseType.SUCCESS);
	}
	
	public ApiResponse(String failureMessage) {
		setResponseType(ResponseType.FAILURE);
		setResponseMessage(failureMessage);
	}
	
	public ApiResponse(AbstractApiObject<?> responseObject) {
		setResponseType(ResponseType.SUCCESS);
		setResponse(responseObject);
	}

	public ResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public AbstractApiObject<?> getResponse() {
		return response;
	}

	public void setResponse(AbstractApiObject<?> response) {
		this.response = response;
	}
}
