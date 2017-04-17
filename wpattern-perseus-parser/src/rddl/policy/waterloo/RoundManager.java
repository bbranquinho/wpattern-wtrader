package rddl.policy.waterloo;

import java.util.TreeSet;

import sperseus.DD;
import sperseus.POMDP;

public class RoundManager {
	/*
	 * InstanceManager will use this class to deal with a single round
	 */
	private POMDP pomdp;
	private DD beliefState;
	private int recentAction;
	public static final String TRUE = "true";
	
	public RoundManager(POMDP pomdp){
		this.pomdp = pomdp;
		this.beliefState = pomdp.initialBelState;
		
		if (pomdp.adjunctNames != null) {
			for (int i = 0; i < pomdp.adjunctNames.length; i++)
				if (pomdp.adjunctNames[i].startsWith("init")) {
					System.out.println("Using "
							+ pomdp.adjunctNames[i]
							+ " adjunct dd as initial state");
					this.beliefState = pomdp.adjuncts[i];
				}
		}
	}
	
	public String generateAction(){
		//print belief, calculate action, print action
		System.out.println("current belief state: ");
		pomdp.printBeliefState(this.beliefState);
		int actId = pomdp.policyQuery(this.beliefState, false);
		String returnAction = pomdp.actions[actId].name;
		System.out.println("action used: " + actId
				+ " which is " + returnAction);
		
		this.recentAction = actId; //will be used to update belief
		return returnAction;
	}
	
	public void updateBeliefState(TreeSet<String> trueObservations){
		assert trueObservations != null;
		
		//translate true observations into observation list of "true" "false"
		String[] observations = new String[pomdp.nObsVars];
		for (int o = 0; o < pomdp.nObsVars; o++) {
			//set a default (false)
			observations[o] = pomdp.obsVars[o].valNames[1];
			assert observations[o].equalsIgnoreCase("false");
			
			//determine observation name string
			String observationName = pomdp.obsVars[o].name;
			if (trueObservations.contains(observationName)){
				System.out.println("true obs " + observationName);
				
				//make sure "true" is a possible observation
				int possibleValues = pomdp.obsVars[o].arity;
				assert possibleValues == 2; //better be boolean
				for (int k = 0; k < possibleValues; k++) {
					if (TRUE.equalsIgnoreCase(pomdp.obsVars[o].valNames[k])){
						observations[o] = TRUE;
					}
				}
			}
		}
		
		//use observations to update belief
		System.out.print("observations: ");
		for (int o = 0; o < pomdp.nObsVars; o++)
			System.out.print(" " + observations[o]);
		System.out.println();
		this.beliefState = pomdp.beliefUpdate(this.beliefState, this.recentAction,
				observations);
	}
	
}
