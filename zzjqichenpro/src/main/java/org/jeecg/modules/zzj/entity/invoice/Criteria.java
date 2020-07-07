package org.jeecg.modules.zzj.entity.invoice;

/**
 * Created by zhangdongqing on 16/3/16.
 */
public class Criteria {

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
	private String value;

	public Criteria(String name, String value) {
		this.name = name;
		this.value = value;
	}

}
