package org.wtrader.cep.utils.ta.beans.utils;

import java.util.ArrayList;
import java.util.List;

import org.wtrader.cep.utils.beans.BaseBean;

public class TARequestBean extends BaseBean {

	private static final long serialVersionUID = -7734413754695275003L;

	private final List<AbstractRequestBean> taRequests;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////////////////////////////////////////

	public TARequestBean() {
		this.taRequests = new ArrayList<AbstractRequestBean>();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public void addRequest(AbstractRequestBean... requestsBean) {
		for (AbstractRequestBean request : requestsBean) {
			this.taRequests.add(request);
		}
	}

	public boolean containsRequest(AbstractRequestBean requestBean) {
		return this.taRequests.contains(requestBean);
	}

	public void removeRequest(AbstractRequestBean requestBean) {
		this.taRequests.remove(requestBean);
	}

}
