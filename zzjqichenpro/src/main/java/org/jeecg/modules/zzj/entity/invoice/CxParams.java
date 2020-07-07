package org.jeecg.modules.zzj.entity.invoice;

import java.util.List;

/**
 * 查询请求
 */
public class CxParams extends RequestData {

	public List<Criteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<Criteria> criteria) {
		this.criteria = criteria;
	}

	/**
	 * 查询条件
	 */
	private List<Criteria> criteria;
}
