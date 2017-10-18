package org.wtrader.loader.utils.beans;


public class PairBean<T, K> extends BaseBean {

	private static final long serialVersionUID = 201401121815L;

	private T first;

	private K second;

	public PairBean(T first, K second) {
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return this.first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public K getSecond() {
		return this.second;
	}

	public void setSecond(K second) {
		this.second = second;
	}

}
