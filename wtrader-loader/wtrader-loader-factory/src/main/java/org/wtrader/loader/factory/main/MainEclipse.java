package org.wtrader.loader.factory.main;


class MainEclipse extends AbstractMain {

	private MainEclipse(String[] args) {
		super(args);
	}

	public static void main(String[] args) {
		(new  MainEclipse(args)).mainAbstract();
	}

}
