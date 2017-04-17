package rddl.policy.waterloo;

import java.util.TreeSet;

import sperseus.POMDP;
import sperseus.Solver;

public class InstanceManager {
	/*
	 * This class will be called directly by Client and will manage all decisions made by Symbolic Perseus
	 */
	
	private RoundManager currentRound;
	private String spuddFileName;

	public POMDP solve(String spuddFileName){
		boolean checkIfSaved = true;
		POMDP pomdp = Solver.solveAndSave(spuddFileName, checkIfSaved);
		System.out.println("Solved pomdp");
		return pomdp;
	}
	
	private void testViaSimulation(POMDP pomdp){
		double avRew = pomdp.evaluatePolicyStationary(2, 30, true);
		System.out.println(avRew);
	}
	
	public void initRound(POMDP pomdp){
		this.currentRound = new RoundManager(pomdp);
	}
	
	public String firstTurn(POMDP pomdp){
		return this.currentRound.generateAction();
	}
	
	public String updateBelAndGetAction(TreeSet<String> trueObservations){
		this.currentRound.updateBeliefState(trueObservations);
		return this.currentRound.generateAction();
	}
	
	public void setSpuddFileName(String filename){
		this.spuddFileName = filename;
	}
	
	public String getSpuddFileName(){
		return this.spuddFileName;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//for testing purposes only
		InstanceManager manager = new InstanceManager();
		POMDP pomdp = manager.solve("sysadmin_pomdp.sysp1.sperseus");
		manager.testViaSimulation(pomdp);
		
		System.out.println(manager.firstTurn(pomdp));
		TreeSet<String> testSet = new TreeSet<String>();
		testSet.add("running_obs__c1");
		testSet.add("running_obs__c2");
		System.out.println(manager.updateBelAndGetAction(testSet));
	}

}
