package org.jeecg.modules.zzj.entity.invoice;

/**
 * 通知方式 Created by hongpujun on 15/10/30.
 */
public class Notice {
	public static final String NOTICE_TYPE_SMS = "sms";
	public static final String NOTICE_TYPE_EMAIL = "email";
	/**
	 * 通知类型
	 */
	private String type;
	/**
	 * 通知所需要的值（例如：通知类型为短信时，值为手机号码；通知类型为Email时，值为邮件地址。）
	 */
	private String value;

	public Notice(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
