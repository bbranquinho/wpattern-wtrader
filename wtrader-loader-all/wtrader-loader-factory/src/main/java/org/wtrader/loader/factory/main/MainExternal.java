package org.wtrader.loader.factory.main;


class MainExternal extends AbstractMain {

	private MainExternal(String[] args) {
		super(args);
	}

	public static void main(String[] args) {
		System.setProperty("conf.data.loader", "file:./conf/data_loader.properties");
		System.setProperty("wtrader.properties", "file:./conf/data_loader.properties");

		(new  MainExternal(args)).mainAbstract();
	}
}
