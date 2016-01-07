package record_indexer.shared.communication;

public class HttpURLResponse {

	private int responseCode;
	private int responseLength;
	private Object responseBody;
	
	public HttpURLResponse() {
		this.responseCode = 0;
		this.responseLength = 0;
		this.responseBody = null;
	}
	
	public HttpURLResponse (int responseCode, int responseLength, Object responseBody) {
		this.responseCode = responseCode;
		this.responseLength = responseLength;
		this.responseBody = responseBody;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public int getResponseLength() {
		return responseLength;
	}

	public Object getResponseBody() {
		return responseBody;
	}
	
	public boolean equals(Object response) {
		boolean result = response != null &&
				response instanceof HttpURLResponse;
		if(result) {
			HttpURLResponse httpURLResponse = (HttpURLResponse)response;
			result = httpURLResponse.responseCode == responseCode &&
					 httpURLResponse.responseLength == responseLength &&
					 (
							 (httpURLResponse.responseBody == null && responseBody == null) ||
							 (httpURLResponse.responseBody != null && responseBody != null &&
							 	httpURLResponse.responseBody.equals(responseBody)
							 )
					 );
		}
		return result;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseLength(int responseLength) {
		this.responseLength = responseLength;
	}

	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}

}