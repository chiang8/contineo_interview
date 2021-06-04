package com.contineo.inventory.controller;

import java.util.Arrays;
import java.util.List;

public class JsonMessage {
	private boolean isSuccess;
	private Object data;
	private List<String> messages;
	
	public JsonMessage(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public JsonMessage(Object data) {
		this(true, data, null);
	}
	
	public JsonMessage(boolean isSuccess, String message) {
		this(isSuccess, Arrays.asList(message));
	}
	
	public JsonMessage(boolean isSuccess, List<String> messages) {
		this(isSuccess, null, messages);
	}
	
	public JsonMessage(boolean isSuccess, Object data, List<String> messages) {
		super();
		this.isSuccess = isSuccess;
		this.data = data;
		this.messages = messages;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
