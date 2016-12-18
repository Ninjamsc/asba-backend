package com.technoserv.rest.request;

//TODO DTO from Compare module ...
public class StopListElement {

	private String Photo;

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	@Override
public String toString() {
	return "StopListElement  [photo=" + Photo + "]";
}
}
