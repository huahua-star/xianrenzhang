package org.jeecg.modules.zzj.entity.invoice;

import java.util.List;

/**
 * 冲红请求
 */
public class ChParams extends RequestData {
	/**
	 * 原发票编号
	 */
	private String originalCode;

	/**
	 * 作废/红冲原因
	 */
	private String reason;

	/**
	 * 通知方式
	 */
	private List<Notice> notices;

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}
}
