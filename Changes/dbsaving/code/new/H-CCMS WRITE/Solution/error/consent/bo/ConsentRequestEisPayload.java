package com.mintstreet.consent.bo;

import com.google.gson.annotations.SerializedName;

public class ConsentRequestEisPayload {
	
	@SerializedName("HEADERS")
	private ConsentRequestPayloadHeaders headers;
	
	@SerializedName("BODY")
	private ConsentRequestPayloadBody body;

	public ConsentRequestPayloadHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(ConsentRequestPayloadHeaders headers) {
		this.headers = headers;
	}

	public ConsentRequestPayloadBody getBody() {
		return body;
	}

	public void setBody(ConsentRequestPayloadBody body) {
		this.body = body;
	}

}
