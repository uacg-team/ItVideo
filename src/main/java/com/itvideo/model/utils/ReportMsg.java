package com.itvideo.model.utils;
/**
 * report to js if there is problem,and what action is coming next
 */
public class ReportMsg {
	private String typeError;
	private String msg;
	private String action;

	public ReportMsg(String typeError, String report, String action) {
		this.msg = report;
		this.typeError = typeError;
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public String getMsg() {
		return msg;
	}

	public String getTypeError() {
		return typeError;
	}
}
