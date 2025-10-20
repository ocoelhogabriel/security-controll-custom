package com.ocoelhogabriel.security_control_custom.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class GenericResponseModel {

	@Schema(name = "description", type = "String", description = "Request description.", example = "Description")
	private String desc;
	@Schema(name = "date", type = "Date", description = "Request date.", example = "200")
	private String date;
	@Schema(name = "resultObject", type = "Integer", description = "Request result object.")
	private Object object;

	public GenericResponseModel() {
		super();
	}

	public GenericResponseModel(String desc, String date, Object object) {
		super();
		this.desc = desc;
		this.date = date;
		this.object = object;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenericResponseModel [");
		if (desc != null) {
			builder.append("desc=").append(desc).append(", ");
		}
		if (date != null) {
			builder.append("date=").append(date).append(", ");
		}
		if (object != null) {
			builder.append("object=").append(object);
		}
		builder.append("]");
		return builder.toString();
	}

}
