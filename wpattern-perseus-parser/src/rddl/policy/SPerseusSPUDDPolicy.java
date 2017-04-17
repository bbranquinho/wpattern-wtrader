/**
 * RDDL: This policy is for use with the Symbolic Perseus and SPUDD
 *       translations.  The planner displays state (MDP) or observations
 *       (POMDP) as true fluents as well as possible actions and
 *       then randomly takes an action.  State-action constraints are
 *       not checked.
 * 
 * @author Scott Sanner (ssanner [at] gmail.com)
 * @version 12/18/10
 *
 **/

package rddl.policy;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import rddl.*;
import rddl.RDDL.*;
import rddl.policy.waterloo.InstanceManager;
import rddl.translate.RDDL2Format;
import sperseus.POMDP;

///////////////////////////////////////////////////////////////////////////
//                             Helper Functions
///////////////////////////////////////////////////////////////////////////

public class SPerseusSPUDDPolicy extends Policy {
	
	public final static boolean SHOW_STATE   = true;
	public final static boolean SHOW_ACTIONS = true;
	public final static boolean SHOW_ACTION_TAKEN = true;
	//public final static boolean ALLOW_NOOP   = false;
	
	public final static String PROBLEM_FILE_PATH = "files/test_comp/spudd_sperseus/";
	public final static String PROBLEM_FILE_EXT = "sperseus";
	
	// Just use the default random seed
	public Random _rand = new Random();
	
	//manages all actions and belief updates
	private InstanceManager manager;
	
	public SPerseusSPUDDPolicy () { }
	
	public SPerseusSPUDDPolicy(String instance_name) {
		super(instance_name);
	}
	
	private InstanceManager createManager(String instance_name){
		InstanceManager manager = new InstanceManager();
		String spuddFileName = findProblemFile(instance_name);
		manager.setSpuddFileName(spuddFileName);
		return manager;
	}
	
	private String findProblemFile(String instance_name){
		File problemDir = new File(PROBLEM_FILE_PATH);
		File[] problemFiles = problemDir.listFiles();
		String stringPattern = ".*" + instance_name + "\\..*";
		for (File problemFile : problemFiles){
			if(problemFile.isFile() //is a file
			&& problemFile.length() > 0L //exists and has data
			&& problemFile.getName().endsWith(PROBLEM_FILE_EXT) //is right type
			&& problemFile.getName().matches(stringPattern)){ //is the correct instance
				String problemFilePath = PROBLEM_FILE_PATH + problemFile.getName();
				System.out.println("file: " + problemFilePath);
				return problemFilePath;
				
			}
		}
		assert false; //should always return before here
		return null;
	}
	///////////////////////////////////////////////////////////////////////////
	//                      Main Action Selection Method
	//
	// If you're using Java and the SPUDD / Symbolic Perseus Format, this 
	// method is the only client method you need to understand to implement
	// your own custom policy.
	///////////////////////////////////////////////////////////////////////////

	public ArrayList<PVAR_INST_DEF> getActions(State s) throws EvalException {

		if (s == null) {
			if (manager == null){
				assert this._sInstanceName != null;
				this.manager = createManager(this._sInstanceName);
			}
			POMDP pomdp = manager.solve(manager.getSpuddFileName());
			manager.initRound(pomdp); //initilization procedure
//			Map<String,ArrayList<PVAR_INST_DEF>> action_map = getLegalActionMap(s);
//			if (SHOW_STATE) {
//				System.out.println("\nLegal action names:");
//				for (String action_name : action_map.keySet())
//					System.out.println(" - " + action_name);
//			}
			return new ArrayList<PVAR_INST_DEF>(); //return noop for now
		}
		
		// If the domain is partially observed, we only see observations,
		// otherwise if it is fully observed, we see the state
		String fluent_type = s._alObservNames.size() > 0 ? "observ" : "states";
		
		//System.out.println("FULL STATE:\n\n" + getStateDescription(s));
		
		// Get a set of all true observation or state variables
		TreeSet<String> true_vars = getTrueFluents(s, fluent_type);
		if (SHOW_STATE) {
			System.out.println("\n==============================================");
			System.out.println("\nTrue state/observation variables:");
			for (String prop_var : true_vars)
				System.out.println(" - " + prop_var);
		}
		
		// Get a map of { legal action names -> RDDL action definition }  
		Map<String,ArrayList<PVAR_INST_DEF>> action_map = 
			ActionGenerator.getLegalBoolActionMap(s);

		if (SHOW_STATE) {
			System.out.println("\nLegal action names:");
			for (String action_name : action_map.keySet())
				System.out.println(" - " + action_name);
		}
		
		//generate the new actions
		ArrayList<String> actions = new ArrayList<String>(action_map.keySet());
//		String action_taken = actions.get(_rand.nextInt(actions.size()));  //use for testing random strategy
		String action_taken = manager.updateBelAndGetAction(true_vars);
		if (SHOW_ACTION_TAKEN){
			System.out.println("\n--> Action taken: " + action_taken);
		}
		
		return action_map.get(action_taken);
	}

	///////////////////////////////////////////////////////////////////////////
	//                             Helper Methods
	//
	// You likely won't need to understand the code below, only the above code.
	///////////////////////////////////////////////////////////////////////////
	
	public TreeSet<String> getTrueFluents(State s, String fluent_type) {
				
		// Go through all variable types (state, interm, observ, action, nonfluent)
		TreeSet<String> true_fluents = new TreeSet<String>();
		for (PVAR_NAME p : (ArrayList<PVAR_NAME>)s._hmTypeMap.get(fluent_type)) {
			
			try {
				// Go through all term groundings for variable p
				ArrayList<ArrayList<LCONST>> gfluents = s.generateAtoms(p);										
				for (ArrayList<LCONST> gfluent : gfluents) {
					if ((Boolean)s.getPVariableAssign(p, gfluent)) {
						true_fluents.add(RDDL2Format.CleanFluentName(p._sPVarName + gfluent));
					}
				}
			} catch (Exception ex) {
				System.err.println("SPerseusSPUDDPolicy: could not retrieve assignment for " + p + "\n");
			}
		}
				
		return true_fluents;
	}

	public String getStateDescription(State s) {
		StringBuilder sb = new StringBuilder();
		
		// Go through all variable types (state, interm, observ, action, nonfluent)
		for (Map.Entry<String,ArrayList<PVAR_NAME>> e : s._hmTypeMap.entrySet()) {
			
			if (e.getKey().equals("nonfluent"))
				continue;
			
			// Go through all variable names p for a variable type
			for (PVAR_NAME p : e.getValue()) {
				sb.append(p + "\n");
				try {
					// Go through all term groundings for variable p
					ArrayList<ArrayList<LCONST>> gfluents = s.generateAtoms(p);										
					for (ArrayList<LCONST> gfluent : gfluents)
						sb.append("- " + e.getKey() + ": " + p + 
								(gfluent.size() > 0 ? gfluent : "") + " := " + 
								s.getPVariableAssign(p, gfluent) + "\n");
						
				} catch (EvalException ex) {
					sb.append("- could not retrieve assignment " + s + " for " + p + "\n");
				}
			}
		}
				
		return sb.toString();
	}

}
