package org.wtrader.loader.utils.interfaces;

public interface IDatabase {

	public int backup(String phase);

	public void restore(String fullFilename) throws Exception;

}
