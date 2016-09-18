/**
 * RDDL: Main state representation and transition function 
 *       computation methods; this class requires everything
 *       to simulate a RDDL domain instance.
 * 
 * @author Scott Sanner (ssanner@gmail.com)
 * @version 10/10/10
 *
 **/

package rddl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import dd.discrete.ADD;
import rddl.RDDL.AGG_EXPR;
import rddl.RDDL.BOOL_CONST_EXPR;
import rddl.RDDL.BOOL_EXPR;
import rddl.RDDL.Bernoulli;
import rddl.RDDL.CPF_DEF;
import rddl.RDDL.DiracDelta;
import rddl.RDDL.ENUM_TYPE_DEF;
import rddl.RDDL.ENUM_VAL;
import rddl.RDDL.EXPR;
import rddl.RDDL.INT_CONST_EXPR;
import rddl.RDDL.KronDelta;
import rddl.RDDL.LCONST;
import rddl.RDDL.LTYPED_VAR;
import rddl.RDDL.LVAR;
import rddl.RDDL.OBJECTS_DEF;
import rddl.RDDL.OPER_EXPR;
import rddl.RDDL.PVARIABLE_ACTION_DEF;
import rddl.RDDL.PVARIABLE_DEF;
import rddl.RDDL.PVARIABLE_INTERM_DEF;
import rddl.RDDL.PVARIABLE_OBS_DEF;
import rddl.RDDL.PVARIABLE_STATE_DEF;
import rddl.RDDL.PVAR_INST_DEF;
import rddl.RDDL.PVAR_NAME;
import rddl.RDDL.REAL_CONST_EXPR;
import rddl.RDDL.TYPE_DEF;
import rddl.RDDL.TYPE_NAME;
import rddl.translate.RDDL2Pomdpx2014.CPFTable;
import util.Pair;
import mc.McState;
import mc.SearchTree;

public class State {

	public final static boolean DISPLAY_UPDATES = false;
	
	public final static int UNDEFINED = 0;
	public final static int STATE     = 1;
	public final static int NONFLUENT = 2;
	public final static int ACTION    = 3;
	public final static int INTERM    = 4;
	public final static int OBSERV    = 5;
	
	// PVariable definitions
	public HashMap<PVAR_NAME,PVARIABLE_DEF> _hmPVariables;
	
	// Type definitions
	public HashMap<TYPE_NAME,TYPE_DEF> _hmTypes;
	
	// CPF definitions
	public HashMap<PVAR_NAME,CPF_DEF> _hmCPFs;
	
	// Object ID lookup... we use IntArrays because hashing and comparison
	// operations will be much more efficient this way than with Strings.
	public HashMap<TYPE_NAME,ArrayList<LCONST>> _hmObject2Consts;
	
	// Lists of variable names
	public ArrayList<PVAR_NAME> _alStateNames = new ArrayList<PVAR_NAME>();
	public ArrayList<PVAR_NAME> _alActionNames = new ArrayList<PVAR_NAME>();
	public TreeMap<Pair,PVAR_NAME> _tmIntermNames = new TreeMap<Pair,PVAR_NAME>();
	public ArrayList<PVAR_NAME> _alIntermNames = new ArrayList<PVAR_NAME>();
	public ArrayList<PVAR_NAME> _alObservNames = new ArrayList<PVAR_NAME>();
	public ArrayList<PVAR_NAME> _alNonFluentNames = new ArrayList<PVAR_NAME>();
	public HashMap<String,ArrayList<PVAR_NAME>> _hmTypeMap = new HashMap<String,ArrayList<PVAR_NAME>>();
	
	// String -> (IntArray -> Object)
	public HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> _state;
	public HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> _nonfluents;
	public HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> _actions;
	public HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> _interm;
	public HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> _observ;

	public HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>> _stateVars;  
	public HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>> _actionVars; 
	public HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>> _observVars;
	
	public McState _stateNum;
	public McState _nextStateNum;
	
	//public CPFTable[][] _cpfTable;
	//public CPFTable[] _obsCpfTable;
	
	public boolean[] _state_act_related;
	public boolean[] _obs_act_related;
	public boolean[] _reward_act_related;
	
	public CPFTable[][] _state_cpts;
	public CPFTable[][] _obs_cpts;
	public CPFTable[][] _reward_cpts;
	public int _reward_component_num;
	
	//public HashMap<PVAR_NAME, HashMap<ArrayList<LCONST>, Integer>> _fluentName2Num;
	public HashMap<McState, HashMap<Integer, Boolean>> _state2FluentValue;
	
	// Constraints
	public ArrayList<BOOL_EXPR> _alConstraints;
	public EXPR _reward;
	public int _nMaxNondefActions = -1;
	
	// Temporarily holds next state while it is being computed
	public HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> _nextState;

	public boolean _ssConst = false;
	public boolean _saConst = false;
	public boolean _aaConst = false;
	
	public ArrayList<BOOL_EXPR> _ssConstraints = new ArrayList<BOOL_EXPR>();
	public ArrayList<BOOL_EXPR> _aaConstraints = new ArrayList<BOOL_EXPR>();
	
	public HashMap<Pair, Integer> _stateName2Num = new HashMap<Pair, Integer>();
	public HashMap<Pair, Integer> _statePrimeName2Num = new HashMap<Pair, Integer>();
	
	public boolean _copied = false;
	
	public void init(HashMap<TYPE_NAME,OBJECTS_DEF> nonfluent_objects,
					 HashMap<TYPE_NAME,OBJECTS_DEF> instance_objects,
					 HashMap<TYPE_NAME,TYPE_DEF> typedefs,
					 HashMap<PVAR_NAME,PVARIABLE_DEF> pvariables,
					 HashMap<PVAR_NAME,CPF_DEF> cpfs,
					 ArrayList<PVAR_INST_DEF> init_state,
					 ArrayList<PVAR_INST_DEF> nonfluents,
					 ArrayList<BOOL_EXPR> state_action_constraints,
					 EXPR reward, 
					 int max_nondef_actions) {
		
		_hmPVariables = pvariables;
		_hmTypes = typedefs;
		_hmCPFs = cpfs;
		_alConstraints = state_action_constraints;
		_reward = reward;
		_nMaxNondefActions = max_nondef_actions;
		
		// Map object class name to list
		_hmObject2Consts = new HashMap<TYPE_NAME,ArrayList<LCONST>>();
		if (nonfluent_objects != null)
			for (OBJECTS_DEF obj_def : nonfluent_objects.values())
				_hmObject2Consts.put(obj_def._sObjectClass, obj_def._alObjects);
		if (instance_objects != null)
			for (OBJECTS_DEF obj_def : instance_objects.values())
				_hmObject2Consts.put(obj_def._sObjectClass, obj_def._alObjects);
		for (Map.Entry<TYPE_NAME,TYPE_DEF> e : typedefs.entrySet()) {
			if (e.getValue() instanceof ENUM_TYPE_DEF) {
				ENUM_TYPE_DEF etd = (ENUM_TYPE_DEF)e.getValue();
				ArrayList<LCONST> values = new ArrayList<LCONST>();
				for (ENUM_VAL v : etd._alPossibleValues)
					values.add(v);
				_hmObject2Consts.put(etd._sName, values);
			}
		}

		// Initialize assignments (missing means default)
		_state      = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_interm     = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_nextState  = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_observ     = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_actions    = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_nonfluents = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();

		// Initialize variable lists
		_alStateNames.clear();
		_alNonFluentNames.clear();
		_alActionNames.clear();
		_alObservNames.clear();
		_alIntermNames.clear();
		for (Map.Entry<PVAR_NAME,PVARIABLE_DEF> e : _hmPVariables.entrySet()) {
			PVAR_NAME pname   = e.getKey();
			PVARIABLE_DEF def = e.getValue();
			if (def instanceof PVARIABLE_STATE_DEF && !((PVARIABLE_STATE_DEF)def)._bNonFluent) {
				_alStateNames.add(pname);
				_state.put(pname, new HashMap<ArrayList<LCONST>,Object>());
				_nextState.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_STATE_DEF && ((PVARIABLE_STATE_DEF)def)._bNonFluent) {
				_alNonFluentNames.add(pname);
				_nonfluents.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_ACTION_DEF) {
				_alActionNames.add(pname);
				_actions.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_OBS_DEF) {
				_alObservNames.add(pname);
				_observ.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_INTERM_DEF) {
				_alIntermNames.add(pname);
				_tmIntermNames.put(new Pair(((PVARIABLE_INTERM_DEF)def)._nLevel, pname), pname);
				_interm.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			}
		}
		_hmTypeMap.put("states", _alStateNames);
		_hmTypeMap.put("nonfluent", _alNonFluentNames);
		_hmTypeMap.put("action", _alActionNames);
		_hmTypeMap.put("observ", _alObservNames);
		_hmTypeMap.put("interm", _alIntermNames);

		// Set initial state and pvariables
		setPVariables(_state, init_state);
		if (nonfluents != null)
			setPVariables(_nonfluents, nonfluents);
		
		_state2FluentValue = new HashMap<McState, HashMap<Integer, Boolean>>();
		//_fluentName2Num =  new HashMap<PVAR_NAME, HashMap<ArrayList<LCONST>, Integer>>();
		
		_stateVars = new HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>>();
		_actionVars = new HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>>();
		_observVars = new HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>>();
		try
		{
			int stateNum = 0;
			for (PVAR_NAME p : _alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
				_stateVars.put(p, gfluents);
				for(ArrayList<LCONST> gfluent: gfluents)
				{
					PVAR_NAME p_prime = new PVAR_NAME(p.toString() + "'");
					_stateName2Num.put(new Pair(p, gfluent), new Integer(stateNum));
					_statePrimeName2Num.put(new Pair(p_prime, gfluent), new Integer(stateNum));
					stateNum++;
				}
			}
			
			for (PVAR_NAME p : _alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
				_observVars.put(p, gfluents);
				
			}
			
			for (PVAR_NAME p : _alActionNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
				_actionVars.put(p, gfluents);
				
			}
			
			for(BOOL_EXPR expr: state_action_constraints)
			{
				HashSet<Pair> relevant_vars = new HashSet<Pair>();
				HashMap<LVAR, LCONST> empty_sub = new HashMap<LVAR, LCONST>();
				expr.collectGFluents(empty_sub, this, relevant_vars);
				boolean aa = true;
				boolean ss = true;
				for (Pair p : relevant_vars) {
					if (getPVariableType((PVAR_NAME) p._o1) == State.ACTION)
						ss = false;
					else
						aa = false;
				}
				if(aa)
					_aaConstraints.add(expr);
				else if(ss)
					_ssConstraints.add(expr);
			}
			if(_aaConstraints.size() > 0)
				_aaConst = true;
			if(_ssConstraints.size() > 0)
				_ssConst = true;
		}
		catch(Exception ex)
		{
			
		}
	}

	public void copy(State state)
	{
		_copied = true;
		
		_hmPVariables = state._hmPVariables;
		_hmTypes = state._hmTypes;
		
		_hmCPFs = state._hmCPFs;
		
		_hmObject2Consts = state._hmObject2Consts;
		
		// Lists of variable names
		_alStateNames = state._alStateNames;
		_alActionNames = state._alActionNames;
		_tmIntermNames = state._tmIntermNames;
		_alIntermNames = state._alIntermNames;
		_alObservNames = state._alObservNames;
		_alNonFluentNames = state._alNonFluentNames;
		_hmTypeMap = state._hmTypeMap;
		
		// String -> (IntArray -> Object)
		_state      = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_interm     = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_nextState  = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_observ     = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_actions    = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();
		_nonfluents = new HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>>();

		for (Map.Entry<PVAR_NAME,PVARIABLE_DEF> e : _hmPVariables.entrySet()) {
			PVAR_NAME pname   = e.getKey();
			PVARIABLE_DEF def = e.getValue();
			if (def instanceof PVARIABLE_STATE_DEF && !((PVARIABLE_STATE_DEF)def)._bNonFluent) {
				_state.put(pname, new HashMap<ArrayList<LCONST>,Object>());
				_nextState.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_STATE_DEF && ((PVARIABLE_STATE_DEF)def)._bNonFluent) {
				_nonfluents.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_ACTION_DEF) {
				_actions.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_OBS_DEF) {
				_observ.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			} else if (def instanceof PVARIABLE_INTERM_DEF) {
				_tmIntermNames.put(new Pair(((PVARIABLE_INTERM_DEF)def)._nLevel, pname), pname);
				_interm.put(pname, new HashMap<ArrayList<LCONST>,Object>());
			}
		}
		
		_stateVars = state._stateVars;  
		_actionVars = state._actionVars; 
		_observVars = state._observVars;
		
		_stateNum = new McState();
		_nextStateNum = new McState();
		
		//_cpfTable = state._cpfTable;
		//_obsCpfTable = state._obsCpfTable;
		_state_cpts = state._state_cpts;
		_obs_cpts = state._obs_cpts;
		_reward_cpts = state._reward_cpts;
		
		_state2FluentValue = state._state2FluentValue;
		
		_alConstraints = state._alConstraints;
		_reward = state._reward;
		_nMaxNondefActions = state._nMaxNondefActions;
		
		_ssConst = state._ssConst;
		_saConst = state._saConst;
		_aaConst = state._aaConst;
		
		_ssConstraints = state._ssConstraints;
		_aaConstraints = state._aaConstraints;
	}
	
	public void initNewRound( ArrayList<PVAR_INST_DEF> init_state)
	{
		for (PVAR_NAME p : _state.keySet())
			_state.get(p).clear();
		setPVariables(_state, init_state);
	}
	
	public void splitConstraints()
	{
		try
		{
			for(BOOL_EXPR expr: _alConstraints)
			{
				HashSet<Pair> relevant_vars = new HashSet<Pair>();
				HashMap<LVAR, LCONST> empty_sub = new HashMap<LVAR, LCONST>();
				expr.collectGFluents(empty_sub, this, relevant_vars);
				boolean aa = true;
				boolean ss = true;
				for (Pair p : relevant_vars) {
					if (getPVariableType((PVAR_NAME) p._o1) == State.ACTION)
						ss = false;
					else
						aa = false;
				}
				if(aa)
					_aaConstraints.add(expr);
				else if(ss)
					_ssConstraints.add(expr);
			}
			if(_aaConstraints.size() > 0)
				_aaConst = true;
			if(_ssConstraints.size() > 0)
				_ssConst = true;
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void checkStateActionConstraints(ArrayList<PVAR_INST_DEF> actions)  
		throws EvalException {
		
		// Clear then set the actions
		for (PVAR_NAME p : _actions.keySet())
			_actions.get(p).clear();
		int non_def = setPVariables(_actions, actions);

		// Check max-nondef actions
		if (non_def > _nMaxNondefActions)
			throw new EvalException("Number of non-default actions (" + non_def + 
					") exceeds limit (" + _nMaxNondefActions + ")");
		
		// Check state-action constraints
		HashMap<LVAR,LCONST> subs = new HashMap<LVAR,LCONST>();
		//for (BOOL_EXPR constraint : _alConstraints) {
		for(BOOL_EXPR constraint : _aaConstraints){
		// satisfied must be true if get here
			try {
				if (! (Boolean)constraint.sample(subs, this, null, new BooleanPair()))
					throw new EvalException("Violated state-action constraint: " + constraint);
			} catch (NullPointerException e) {
				System.out.println("\n***SIMULATOR ERROR EVALUATING: " + constraint);
				throw e;
			} catch (ClassCastException e) {
				System.out.println("\n***SIMULATOR ERROR EVALUATING: " + constraint);
				throw e;
			}
		}
	}
		
	public void computeNextState(ArrayList<PVAR_INST_DEF> actions, int actionNum, Random r) 
		throws EvalException {

		// Clear then set the actions
		for (PVAR_NAME p : _actions.keySet())
			_actions.get(p).clear();
		setPVariables(_actions, actions);
		
		//System.out.println("Starting state: " + _state + "\n");
		//System.out.println("Starting nonfluents: " + _nonfluents + "\n");
		
		// First compute intermediate variables, level-by-level
		HashMap<LVAR,LCONST> subs = new HashMap<LVAR,LCONST>();
		if (DISPLAY_UPDATES) System.out.println("Updating intermediate variables");
		for (Map.Entry<Pair, PVAR_NAME> e : _tmIntermNames.entrySet()) {
			int level   = (Integer)e.getKey()._o1;
			PVAR_NAME p = e.getValue();
			
			// Generate updates for each ground fluent
			//System.out.println("Updating interm var " + p + " @ level " + level + ":");
			ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
			
			for (ArrayList<LCONST> gfluent : gfluents) {
				if (DISPLAY_UPDATES) System.out.print("- " + p + gfluent + " @ " + level + " := ");
				CPF_DEF cpf = _hmCPFs.get(p);
				
				subs.clear();
				for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) {
					LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
					LCONST c = (LCONST)gfluent.get(i);
					subs.put(v,c);
				}
				
				Object value = cpf._exprEquals.sample(subs, this, r, new BooleanPair());
				if (DISPLAY_UPDATES) System.out.println(value);
				
				// Update value
				HashMap<ArrayList<LCONST>,Object> pred_assign = _interm.get(p);
				pred_assign.put(gfluent, value);
			}
		}
		
		// Do same for next-state (keeping in mind primed variables)
		if (DISPLAY_UPDATES) System.out.println("Updating next state");
		
		int fluentNum = 0;
		/*HashMap<Integer, Boolean> fluent2Value = _state2FluentValue.get(_stateNum);
		if(fluent2Value == null)
		{
			fluent2Value = new HashMap<Integer, Boolean>();
		}*/
			
		for (PVAR_NAME p : _alStateNames) {
						
			// Get default value
			Object def_value = null;
			PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
			if (!(pvar_def instanceof PVARIABLE_STATE_DEF) ||
				((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
				throw new EvalException("Expected state variable, got nonfluent: " + p);
			def_value = ((PVARIABLE_STATE_DEF)pvar_def)._oDefValue;
			
			// Generate updates for each ground fluent
			PVAR_NAME primed = new PVAR_NAME(p._sPVarName + "'");
			//System.out.println("Updating next state var " + primed + " (" + p + ")");
			//ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
			ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
			
			for (ArrayList<LCONST> gfluent : gfluents) {
				
				/*Boolean bValue = fluent2Value.get(new Integer(fluentNum));
				if(bValue != null)
				{
					_nextStateNum._fValues[fluentNum] = bValue.booleanValue();
					if (!((Object)bValue).equals(def_value)) {
						HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
						pred_assign.put(gfluent, bValue);
					}
				}
				else
				{
					if (DISPLAY_UPDATES) System.out.print("- " + primed + gfluent + " := ");
					CPF_DEF cpf = _hmCPFs.get(primed);
				
					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) {
						LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST)gfluent.get(i);
						subs.put(v,c);
					}
				
					BooleanPair bp = new BooleanPair();
					Object value = cpf._exprEquals.sample(subs, this, r, bp);
					if(!bp.a && bp.d)
						fluent2Value.put(new Integer(fluentNum), (Boolean)value);
					if (DISPLAY_UPDATES) System.out.println(value);
					_nextStateNum._fValues[fluentNum] = ((Boolean)value).booleanValue();
					if (!value.equals(def_value)) {
						HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
						pred_assign.put(gfluent, value);
					}
				
				}*/
				//CPFTable table = _cpfTable[actionNum][fluentNum];
				CPFTable table = _state_cpts[actionNum][fluentNum];
				ArrayList<Boolean> key = new ArrayList<Boolean>();
				for(int i = 0; i < table._relevants.size(); i++)
				{
					//Pair pair = table._relevants.get(i);
					//key.add((Boolean)getPVariableAssign((PVAR_NAME)pair._o1, (ArrayList<LCONST>)pair._o2));
					key.add(_stateNum._fValues[table._relevants.get(i).intValue()]);
				}
				double prob = table._table.get(key).doubleValue();
				Boolean value = null;
				if(prob == 1.0)
					value = new Boolean(true);
				else if(prob == 0.0)
					value = new Boolean(false);
				else
				{
					if (r.nextDouble() < prob)
					{
						value = new Boolean(true);
					}
					else 
						value = new Boolean(false);
				}
				_nextStateNum._fValues[fluentNum] = value.booleanValue();
				if (!((Object)value).equals(def_value)) {
					HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
					pred_assign.put(gfluent, value);
				}
				fluentNum++;
			}
		}
		if(_ssConst)
		{
			int ss = 0;
			while(true)
			{
				if(ss == 0)
					break;
				ss++;
				HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> temp = _state;
				_state = _nextState;
				_nextState = temp;
				boolean satisfied = true;
				for(BOOL_EXPR expr: _ssConstraints)
				{
					if (!(Boolean)expr.sample(subs, this, null, new BooleanPair()))
					{
						System.out.println(expr.toString());
						System.out.println(_state.toString());
						satisfied = false;
						break;
					}
				}
				temp = _state;
				_state = _nextState;
				_nextState = temp;
				if(satisfied)
					break;
				else
				{
					for (PVAR_NAME p : _nextState.keySet())
						_nextState.get(p).clear();
					fluentNum = 0;
					for (PVAR_NAME p : _alStateNames) {
						
						// Get default value
						Object def_value = null;
						PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
						if (!(pvar_def instanceof PVARIABLE_STATE_DEF) ||
							((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
							throw new EvalException("Expected state variable, got nonfluent: " + p);
						def_value = ((PVARIABLE_STATE_DEF)pvar_def)._oDefValue;
						
						// Generate updates for each ground fluent
						PVAR_NAME primed = new PVAR_NAME(p._sPVarName + "'");
						//System.out.println("Updating next state var " + primed + " (" + p + ")");
						//ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
						ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
						
						for (ArrayList<LCONST> gfluent : gfluents) {
							//CPFTable table = _cpfTable[actionNum][fluentNum];
							CPFTable table = _state_cpts[actionNum][fluentNum];
							ArrayList<Boolean> key = new ArrayList<Boolean>();
							for(int i = 0; i < table._relevants.size(); i++)
							{
								//Pair pair = table._relevants.get(i);
								//key.add((Boolean)getPVariableAssign((PVAR_NAME)pair._o1, (ArrayList<LCONST>)pair._o2));
								key.add(_stateNum._fValues[table._relevants.get(i).intValue()]);
							}
							double prob = table._table.get(key).doubleValue();
							Boolean value = null;
							if(prob == 1.0)
								value = new Boolean(true);
							else if(prob == 0.0)
								value = new Boolean(false);
							else
							{
								if (r.nextDouble() < prob)
									value = new Boolean(true);
								else 
									value = new Boolean(false);
							}
							_nextStateNum._fValues[fluentNum] = value.booleanValue();
							if (!((Object)value).equals(def_value)) {
								HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
								pred_assign.put(gfluent, value);
							}
							fluentNum++;
						}
					}
				}
			}
			if(ss > 1)
			System.out.println("ssConst:" + ss);
		}
		
		//_state2FluentValue.put(_stateNum, fluent2Value);
		// Do same for observations... note that this occurs after the next state
		// update because observations in a POMDP may be modeled on the current
		// and next state, i.e., P(o|s,a,s').
		if (DISPLAY_UPDATES) System.out.println("Updating observations");
		fluentNum = 0;
		for (PVAR_NAME p : _alObservNames) {
			
			// Generate updates for each ground fluent
			//System.out.println("Updating observation var " + p);
			//ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
			ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
			
			for (ArrayList<LCONST> gfluent : gfluents) {
				if (DISPLAY_UPDATES) System.out.print("- " + p + gfluent + " := ");
				/*CPF_DEF cpf = _hmCPFs.get(p);
				
				subs.clear();
				for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) {
					LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
					LCONST c = (LCONST)gfluent.get(i);
					subs.put(v,c);
				}
				
				BooleanPair bp = new BooleanPair();
				Object value = cpf._exprEquals.sample(subs, this, r, bp);
				if (DISPLAY_UPDATES) System.out.println(value);
				
				// Update value
				HashMap<ArrayList<LCONST>,Object> pred_assign = _observ.get(p);
				pred_assign.put(gfluent, value);*/
				
				//CPFTable table = _obsCpfTable[fluentNum];
				CPFTable table = _obs_cpts[actionNum][fluentNum];
				ArrayList<Boolean> key = new ArrayList<Boolean>();
				for(int i = 0; i < table._relevants.size(); i++)
				{
					//Pair pair = table._relevants.get(i);
					//key.add((Boolean)getPVariableAssign((PVAR_NAME)pair._o1, (ArrayList<LCONST>)pair._o2));
					key.add(_nextStateNum._fValues[table._relevants.get(i).intValue()]);
				}
				double prob = table._table.get(key).doubleValue();
				Boolean value = null;
				if(prob == 1.0)
					value = new Boolean(true);
				else if(prob == 0.0)
					value = new Boolean(false);
				else
				{
					if (r.nextDouble() < prob)
						value = new Boolean(true);
					else 
						value = new Boolean(false);
				}
				HashMap<ArrayList<LCONST>,Object> pred_assign = _observ.get(p);
				pred_assign.put(gfluent, value);
				fluentNum++;
			}
		}
	}
	
	public void computeNextStateWithoutObserv(ArrayList<PVAR_INST_DEF> actions, int actionNum, Random r) throws EvalException
	{
		for (PVAR_NAME p : _actions.keySet())
			_actions.get(p).clear();
		setPVariables(_actions, actions);
		
		HashMap<LVAR,LCONST> subs = new HashMap<LVAR,LCONST>();
		
		int fluentNum = 0;
		/*HashMap<Integer, Boolean> fluent2Value = _state2FluentValue.get(_stateNum);
		if(fluent2Value == null)
		{
			fluent2Value = new HashMap<Integer, Boolean>();
		}*/
			
		//int bad = 0;
		//int good = 0;
		for (PVAR_NAME p : _alStateNames) {
						
			// Get default value
			Object def_value = null;
			PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
			if (!(pvar_def instanceof PVARIABLE_STATE_DEF) ||
				((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			def_value = ((PVARIABLE_STATE_DEF)pvar_def)._oDefValue;
			
			// Generate updates for each ground fluent
			PVAR_NAME primed = new PVAR_NAME(p._sPVarName + "'");
			//System.out.println("Updating next state var " + primed + " (" + p + ")");
			//ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
			ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
			
			for (ArrayList<LCONST> gfluent : gfluents) {
				
				/*Boolean bValue = fluent2Value.get(new Integer(fluentNum));
				if(bValue != null)
				{
					_nextStateNum._fValues[fluentNum] = bValue.booleanValue();
					if (!((Object)bValue).equals(def_value)) {
						HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
						pred_assign.put(gfluent, bValue);
					}
				}
				else
				{
					if (DISPLAY_UPDATES) System.out.print("- " + primed + gfluent + " := ");
					CPF_DEF cpf = _hmCPFs.get(primed);
				
					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) {
						LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST)gfluent.get(i);
						subs.put(v,c);
					}
				
					BooleanPair bp = new BooleanPair();
					Object value = cpf._exprEquals.sample(subs, this, r, bp);
					if(!bp.a && bp.d)
						fluent2Value.put(new Integer(fluentNum), (Boolean)value);
					if (DISPLAY_UPDATES) System.out.println(value);
					_nextStateNum._fValues[fluentNum] = ((Boolean)value).booleanValue();
					if (!value.equals(def_value)) {
						HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
						pred_assign.put(gfluent, value);
					}
				
				}*/
				//CPFTable table = _cpfTable[actionNum][fluentNum];
				CPFTable table = _state_cpts[actionNum][fluentNum];
				ArrayList<Boolean> key = new ArrayList<Boolean>();
				for(int i = 0; i < table._relevants.size(); i++)
				{
					//Pair pair = (Pair)table._relevants.get(i);
					//key.add((Boolean)getPVariableAssign((PVAR_NAME)pair._o1, (ArrayList<LCONST>)pair._o2));
					key.add(_stateNum._fValues[table._relevants.get(i).intValue()]);
				}
				double prob = table._table.get(key).doubleValue();
				Boolean value = null;
				if(prob == 1.0)
					value = new Boolean(true);
				else if(prob == 0.0)
					value = new Boolean(false);
				else
				{
					if (r.nextDouble() < prob)
					{
						value = new Boolean(true);
					}
					else 
					{
						value = new Boolean(false);
					}
				}
				_nextStateNum._fValues[fluentNum] = value.booleanValue();
				if (!((Object)value).equals(def_value)) {
					HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
					pred_assign.put(gfluent, value);
				}
				fluentNum++;
			}
		}
		//System.out.println("good fluent sample: " + ((double)good/(good + bad)));
		//_state2FluentValue.put(_stateNum, fluent2Value);
		if(_ssConst)
		{
			int ss = 0;
			while(true)
			{
				if(ss == 0)
					break;
				ss++;
				HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> temp = _state;
				_state = _nextState;
				_nextState = temp;
				boolean satisfied = true;
				for(BOOL_EXPR expr: _ssConstraints)
				{
					if (!(Boolean)expr.sample(subs, this, null, new BooleanPair()))
					{
						System.out.println(_state.toString());
						satisfied = false;
						break;
					}
				}
				temp = _state;
				_state = _nextState;
				_nextState = temp;
				if(satisfied)
					break;
				else
				{
					for (PVAR_NAME p : _nextState.keySet())
						_nextState.get(p).clear();
					fluentNum = 0;
					for (PVAR_NAME p : _alStateNames) {
						
						// Get default value
						Object def_value = null;
						PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
						if (!(pvar_def instanceof PVARIABLE_STATE_DEF) ||
							((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
							throw new EvalException("Expected state variable, got nonfluent: " + p);
						def_value = ((PVARIABLE_STATE_DEF)pvar_def)._oDefValue;
						
						// Generate updates for each ground fluent
						PVAR_NAME primed = new PVAR_NAME(p._sPVarName + "'");
						//System.out.println("Updating next state var " + primed + " (" + p + ")");
						//ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);
						ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
						
						for (ArrayList<LCONST> gfluent : gfluents) {
							//CPFTable table = _cpfTable[actionNum][fluentNum];
							CPFTable table = _state_cpts[actionNum][fluentNum];
							ArrayList<Boolean> key = new ArrayList<Boolean>();
							for(int i = 0; i < table._relevants.size(); i++)
							{
								//Pair pair = table._relevants.get(i);
								//key.add((Boolean)getPVariableAssign((PVAR_NAME)pair._o1, (ArrayList<LCONST>)pair._o2));
								key.add(_stateNum._fValues[table._relevants.get(i).intValue()]);
							}
							double prob = table._table.get(key).doubleValue();
							Boolean value = null;
							if(prob == 1.0)
								value = new Boolean(true);
							else if(prob == 0.0)
								value = new Boolean(false);
							else
							{
								if (r.nextDouble() < prob)
									value = new Boolean(true);
								else 
									value = new Boolean(false);
							}
							_nextStateNum._fValues[fluentNum] = value.booleanValue();
							if (!((Object)value).equals(def_value)) {
								HashMap<ArrayList<LCONST>,Object> pred_assign = _nextState.get(p);
								pred_assign.put(gfluent, value);
							}
							fluentNum++;
						}
					}
				}
			}
			if(ss > 1)
				System.out.println("ssConst:" + ss);
		}
	}
	
	public double computeReward(int action)
	{
		double value = 0;
		for (int index = 0; index < _reward_component_num; index++) {
	
			CPFTable table = _reward_cpts[action][index];
			ArrayList<Boolean> key = new ArrayList<Boolean>();
			for(int i = 0; i < table._relevants.size(); i++)
			{
				key.add(_stateNum._fValues[table._relevants.get(i).intValue()]);
			}
			value += table._table.get(key).doubleValue();	
		}
		return value;
	}
	
	public void advanceNextState() throws EvalException {
		HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> temp = _state;
		_state = _nextState;
		_nextState = temp;
		_stateNum = _nextStateNum;
		_nextStateNum = new McState();
		
		// Clear the non-state, non-constant, non-action variables
		for (PVAR_NAME p : _nextState.keySet())
			_nextState.get(p).clear();
		for (PVAR_NAME p : _interm.keySet())
			_interm.get(p).clear();
		for (PVAR_NAME p : _observ.keySet())
			_observ.get(p).clear();
	}
	
	public void clearPVariables(HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> assign) {
		for (HashMap<ArrayList<LCONST>,Object> pred_assign : assign.values())
			pred_assign.clear();
	}
	
	public int setPVariables(HashMap<PVAR_NAME,HashMap<ArrayList<LCONST>,Object>> assign, 
							  ArrayList<PVAR_INST_DEF> src) {

		int non_def = 0;
		for (PVAR_INST_DEF def : src) {
			
			// Get the assignments for this PVAR
			HashMap<ArrayList<LCONST>,Object> pred_assign = assign.get(def._sPredName);
			
			// Get default value if it exists
			Object def_value = null;
			PVARIABLE_DEF pvar_def = _hmPVariables.get(def._sPredName);
			if (pvar_def instanceof PVARIABLE_STATE_DEF) // state & non_fluents
				def_value = ((PVARIABLE_STATE_DEF)pvar_def)._oDefValue;
			else if (pvar_def instanceof RDDL.PVARIABLE_ACTION_DEF) // actions
				def_value = ((PVARIABLE_ACTION_DEF)pvar_def)._oDefValue;
			
			// Set value if non-default
			if (def_value != null && !def_value.equals(def._oValue)) {
				pred_assign.put(def._alTerms, def._oValue);
				++non_def;
			} else if ( pvar_def instanceof PVARIABLE_OBS_DEF ) {
				pred_assign.put(def._alTerms, def._oValue);
			}
		}
		
		return non_def;
	}

	/////////////////////////////////////////////////////////////////////////////
	//             Methods for Querying and Setting Fluent Values
	/////////////////////////////////////////////////////////////////////////////
	
	public Object getPVariableDefault(PVAR_NAME p) {
		PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
		if (pvar_def instanceof PVARIABLE_STATE_DEF) // state & non_fluents
			return ((PVARIABLE_STATE_DEF) pvar_def)._oDefValue;
		else if (pvar_def instanceof RDDL.PVARIABLE_ACTION_DEF) // actions
			return ((PVARIABLE_ACTION_DEF) pvar_def)._oDefValue;
		return null;
	}
	
	public int getPVariableType(PVAR_NAME p) {
		
		PVARIABLE_DEF pvar_def = _hmPVariables.get(p);

		if (pvar_def instanceof PVARIABLE_STATE_DEF && ((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			return NONFLUENT;
		else if (pvar_def instanceof PVARIABLE_STATE_DEF && !((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			return STATE;
		else if (pvar_def instanceof PVARIABLE_ACTION_DEF)
			return ACTION;
		else if (pvar_def instanceof PVARIABLE_INTERM_DEF)
			return INTERM;
		else if (pvar_def instanceof PVARIABLE_OBS_DEF)
			return OBSERV;
		
		return UNDEFINED;
	}
	
	public Object getDefaultValue(PVAR_NAME p) {
		
		Object def_value = null;
		PVARIABLE_DEF pvar_def = _hmPVariables.get(new PVAR_NAME(p._sPVarName));
		if (pvar_def instanceof PVARIABLE_STATE_DEF) // state & non_fluents
			def_value = ((PVARIABLE_STATE_DEF) pvar_def)._oDefValue;
		else if (pvar_def instanceof RDDL.PVARIABLE_ACTION_DEF) // actions
			def_value = ((PVARIABLE_ACTION_DEF) pvar_def)._oDefValue;	
		
		return def_value;
	}
	
	public Object getPVariableAssign(PVAR_NAME p, ArrayList<LCONST> terms) {

		// Get default value if it exists
		Object def_value = null;
		boolean primed = p._bPrimed;
		p = new PVAR_NAME(p._sPVarName);
		PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
		if (pvar_def instanceof PVARIABLE_STATE_DEF) // state & non_fluents
			def_value = ((PVARIABLE_STATE_DEF) pvar_def)._oDefValue;
		else if (pvar_def instanceof RDDL.PVARIABLE_ACTION_DEF) // actions
			def_value = ((PVARIABLE_ACTION_DEF) pvar_def)._oDefValue;

		// Get correct variable assignments
		HashMap<ArrayList<LCONST>,Object> var_src = null;
		if (pvar_def instanceof PVARIABLE_STATE_DEF && ((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			var_src = _nonfluents.get(p);
		else if (pvar_def instanceof PVARIABLE_STATE_DEF && !((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			var_src = primed ? _nextState.get(p) : _state.get(p); // Note: (next) state index by non-primed pvar
		else if (pvar_def instanceof PVARIABLE_ACTION_DEF)
			var_src = _actions.get(p);
		else if (pvar_def instanceof PVARIABLE_INTERM_DEF)
			var_src = _interm.get(p);
		else if (pvar_def instanceof PVARIABLE_OBS_DEF)
			var_src = _observ.get(p);
		else
			System.out.println("ERROR: getPVariableAssign, unhandled pvariable " + p + terms);
			
		if (var_src == null)
			return null;
		
		// Lookup value, return default (if available) if value not found
		Object ret = var_src.get(terms);
		if (ret == null)
			ret = def_value;
		return ret;
	}	
		
	public boolean setPVariableAssign(PVAR_NAME p, ArrayList<LCONST> terms, 
			Object value) {
		
		// Get default value if it exists
		Object def_value = null;
		boolean primed = p._bPrimed;
		p = new PVAR_NAME(p._sPVarName);
		PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
		if (pvar_def instanceof PVARIABLE_STATE_DEF) // state & non_fluents
			def_value = ((PVARIABLE_STATE_DEF) pvar_def)._oDefValue;
		else if (pvar_def instanceof RDDL.PVARIABLE_ACTION_DEF) // actions
			def_value = ((PVARIABLE_ACTION_DEF) pvar_def)._oDefValue;

		// Get correct variable assignments
		HashMap<ArrayList<LCONST>,Object> var_src = null;
		if (pvar_def instanceof PVARIABLE_STATE_DEF && ((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			var_src = _nonfluents.get(p);
		else if (pvar_def instanceof PVARIABLE_STATE_DEF && !((PVARIABLE_STATE_DEF)pvar_def)._bNonFluent)
			var_src = primed ? _nextState.get(p) : _state.get(p); // Note: (next) state index by non-primed pvar
		else if (pvar_def instanceof PVARIABLE_ACTION_DEF)
			var_src = _actions.get(p);
		else if (pvar_def instanceof PVARIABLE_INTERM_DEF)
			var_src = _interm.get(p);
		else if (pvar_def instanceof PVARIABLE_OBS_DEF)
			var_src = _observ.get(p);
		
		if (var_src == null)
			return false;

		// Set value (or remove if default)... n.b., def_value could be null if not s,a,s'
		if (value == null || value.equals(def_value)) {
			var_src.remove(terms);			
		} else {
			var_src.put(terms, value);
		}
		return true;
	}
			
	//////////////////////////////////////////////////////////////////////
	
	public ArrayList<ArrayList<LCONST>> generateAtoms(PVAR_NAME p) throws EvalException {
		ArrayList<ArrayList<LCONST>> list = new ArrayList<ArrayList<LCONST>>();
		PVARIABLE_DEF pvar_def = _hmPVariables.get(p);
		//System.out.print("Generating pvars for " + pvar_def + ": ");
		if (pvar_def == null) {
			System.out.println("Error, could not generate atoms for unknown variable name.");
			new Exception().printStackTrace();
		}
		generateAtoms(pvar_def, 0, new ArrayList<LCONST>(), list);
		//System.out.println(list);
		return list;
	}
	
	private void generateAtoms(PVARIABLE_DEF pvar_def, int index, 
			ArrayList<LCONST> cur_assign, ArrayList<ArrayList<LCONST>> list) throws EvalException {
		if (index >= pvar_def._alParamTypes.size()) {
			// No more indices to generate
			list.add(cur_assign);
		} else {
			// Get the object list for this index
			TYPE_NAME type = pvar_def._alParamTypes.get(index);
			ArrayList<LCONST> objects = _hmObject2Consts.get(type);
			for (LCONST obj : objects) {
				ArrayList<LCONST> new_assign = (ArrayList<LCONST>)cur_assign.clone();
				new_assign.add(obj);
				generateAtoms(pvar_def, index+1, new_assign, list);
			}
		}
	}
	
	public ArrayList<ArrayList<LCONST>> generateAtoms(ArrayList<LTYPED_VAR> tvar_list) {
		ArrayList<ArrayList<LCONST>> list = new ArrayList<ArrayList<LCONST>>();
		generateAtoms(tvar_list, 0, new ArrayList<LCONST>(), list);
		return list;
	}
	
	private void generateAtoms(ArrayList<LTYPED_VAR> tvar_list, int index, 
			ArrayList<LCONST> cur_assign, ArrayList<ArrayList<LCONST>> list) {
		if (index >= tvar_list.size()) {
			// No more indices to generate
			list.add(cur_assign);
		} else {
			// Get the object list for this index
			TYPE_NAME type = tvar_list.get(index)._sType;
			ArrayList<LCONST> objects = _hmObject2Consts.get(type);
			if (objects == null) {
				System.out.println("Object type '" + type + "' did not have any objects or enumerated values defined.");
			}
			//System.out.println(type + " : " + objects);
			for (LCONST obj : objects) {
				ArrayList<LCONST> new_assign = (ArrayList<LCONST>)cur_assign.clone();
				new_assign.add(obj);
				generateAtoms(tvar_list, index+1, new_assign, list);
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// Go through all variable types (state, interm, observ, action, nonfluent)
		for (Map.Entry<String,ArrayList<PVAR_NAME>> e : _hmTypeMap.entrySet()) {
			
			if (e.getKey().equals("nonfluent"))
				continue;
			
			// Go through all variable names p for a variable type
			for (PVAR_NAME p : e.getValue()) 
				try {
					// Go through all term groundings for variable p
					ArrayList<ArrayList<LCONST>> gfluents = generateAtoms(p);										
					for (ArrayList<LCONST> gfluent : gfluents)
						sb.append("- " + e.getKey() + ": " + p + 
								(gfluent.size() > 0 ? gfluent : "") + " := " + 
								getPVariableAssign(p, gfluent) + "\n");
						
				} catch (EvalException ex) {
					sb.append("- could not retrieve assignment" + e.getKey() + " for " + p + "\n");
				}
		}
				
		return sb.toString();
	}
	
	public void buildCPTs()
	{	
		try
		{
			int index = 0;
			for(PVAR_NAME p: _alStateNames)
			{
				ArrayList<ArrayList<LCONST>> assignments = _stateVars.get(p);
					
				CPF_DEF cpf = _hmCPFs.get(new PVAR_NAME(p.toString() + "'"));
				
				HashMap<LVAR,LCONST> subs = new HashMap<LVAR,LCONST>();
				for (ArrayList<LCONST> assign : assignments) 
				{
					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) 
					{
						LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST)assign.get(i);
						subs.put(v,c);
					}
						
					HashSet<Pair> relevant_vars = new HashSet<Pair>();
					EXPR cpf_expr = cpf._exprEquals;				
						
					cpf_expr.collectGFluents(subs, this, relevant_vars);
					int before_size = relevant_vars.size();
					relevant_vars = filterOutActionVars(relevant_vars);
					int after_size = relevant_vars.size();
					if(after_size < before_size)
						_state_act_related[index] = true;
					else
						_state_act_related[index] = false;
					//System.out.println("relevants num: " + relevant_vars.size());
					
					for(int actionNum = 0; actionNum < SearchTree.ACTION_NUM; actionNum++)
					{
						if(!_state_act_related[index] && actionNum != 0)
						{
							_state_cpts[actionNum][index] = _state_cpts[0][index];
							continue;
						}
						
						ArrayList<PVAR_INST_DEF> actions = getAction(actionNum);
						for (PVAR_NAME p_a : _actions.keySet())
							_actions.get(p_a).clear();
						setPVariables(_actions, actions);
					
						//CPFTable table = _cpfTable[actionNum][index];
						CPFTable table = _state_cpts[actionNum][index];
						
						ArrayList<Pair> nameRelevants = new ArrayList<Pair>(relevant_vars);
						ArrayList<Integer> numRelevants = new ArrayList<Integer>();
						for(Pair nameR: nameRelevants)
							numRelevants.add(_stateName2Num.get(nameR));
						table._relevants = numRelevants;
						
						ArrayList<Boolean> key = new ArrayList<Boolean>();
						for(int i = 0; i < table._relevants.size(); i++)
							key.add(Boolean.FALSE);
						enumerateAssignments(nameRelevants, cpf_expr, subs, 0, key, table);
					//String testS = table._table.toString();
					//int testSize = table._table.entrySet().size();
					}
					index++;
				}
				for (PVAR_NAME p_a : _actions.keySet())
					_actions.get(p_a).clear();
			}
			index = 0;
			for(PVAR_NAME p: _alObservNames)
			{
				ArrayList<ArrayList<LCONST>> assignments = _observVars.get(p);
					
				CPF_DEF cpf = _hmCPFs.get(p);
					
				HashMap<LVAR,LCONST> subs = new HashMap<LVAR,LCONST>();
				for (ArrayList<LCONST> assign : assignments) 
				{
					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) 
					{
						LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST)assign.get(i);
						subs.put(v,c);
					}
						
					HashSet<Pair> relevant_vars = new HashSet<Pair>();
					EXPR cpf_expr = cpf._exprEquals;				
						
					cpf_expr.collectGFluents(subs, this, relevant_vars);
					int before_size = relevant_vars.size();
					relevant_vars = filterOutActionVars(relevant_vars);
					int after_size = relevant_vars.size();
					if(after_size < before_size)
						_obs_act_related[index] = true;
					else
						_obs_act_related[index] = false;	 
					for(int actionNum = 0; actionNum < SearchTree.ACTION_NUM; actionNum++)
					{
						if(!_obs_act_related[index] && actionNum != 0)
						{
							_obs_cpts[actionNum][index] = _obs_cpts[0][index];
							continue;
						}
						
						ArrayList<PVAR_INST_DEF> actions = getAction(actionNum);
						for (PVAR_NAME p_a : _actions.keySet())
							_actions.get(p_a).clear();
						setPVariables(_actions, actions);
					
						CPFTable table = _obs_cpts[actionNum][index];
						
						ArrayList<Pair> nameRelevants = new ArrayList<Pair>(relevant_vars);
						ArrayList<Integer> numRelevants = new ArrayList<Integer>();
						for(Pair nameR: nameRelevants)
							numRelevants.add(_statePrimeName2Num.get(nameR));
						table._relevants = numRelevants;
						
						ArrayList<Boolean> key = new ArrayList<Boolean>();
						for(int i = 0; i < table._relevants.size(); i++)
							key.add(Boolean.FALSE);
						enumerateAssignments(nameRelevants, cpf_expr, subs, 0, key, table);
					}
					/*for (PVAR_NAME action_name: _alActionNames) 
					{
						ArrayList<ArrayList<LCONST>> action_assignments = _actionVars.get(action_name);
		
						for (ArrayList<LCONST> action_assign : action_assignments)
							setPVariableAssign(action_name, action_assign, RDDL.BOOL_CONST_EXPR.FALSE);
					}
					
					CPFTable table = _obsCpfTable[index];
					
					//table._relevants = new ArrayList<Pair>(relevant_vars);
					ArrayList<Pair> nameRelevants = new ArrayList<Pair>(relevant_vars);
					ArrayList<Integer> numRelevants = new ArrayList<Integer>();
					for(Pair nameR: nameRelevants)
						numRelevants.add(_statePrimeName2Num.get(nameR));
					table._relevants = numRelevants;
					
					ArrayList<Boolean> key = new ArrayList<Boolean>();
				    for(int i = 0; i < table._relevants.size(); i++)
				    	key.add(Boolean.FALSE);
					enumerateAssignments(nameRelevants, cpf_expr, subs, 0, key, table);*/
					//String testS = table._table.toString();
					//int testSize = table._table.entrySet().size();
					index++;
				}
			}
			index = 0;
			ArrayList<Pair> exprs = getAdditiveComponents(_reward);
			_reward_component_num = exprs.size();
			for(int i = 0; i < _reward_cpts.length; i++)
			{
				_reward_cpts[i] = new CPFTable[_reward_component_num];
				for(int j = 0; j < _reward_component_num; j++)
					_reward_cpts[i][j] = new CPFTable();
			}
			_reward_act_related = new boolean[_reward_component_num];
			for(Pair pair: exprs)
			{
				EXPR expr = new DiracDelta((EXPR)pair._o2);
				HashMap<LVAR, LCONST> subs = (HashMap<LVAR, LCONST>)pair._o1;
				
				HashSet<Pair> relevant_vars = new HashSet<Pair>();
				expr.collectGFluents(subs, this, relevant_vars);
				int before_size = relevant_vars.size();
				relevant_vars = filterOutActionVars(relevant_vars);
				int after_size = relevant_vars.size();
				if(after_size < before_size)
					_reward_act_related[index] = true;
				else
					_reward_act_related[index] = false;
				
				for(int actionNum = 0; actionNum < SearchTree.ACTION_NUM; actionNum++)
				{
					if(!_reward_act_related[index] && actionNum != 0)
					{
						_reward_cpts[actionNum][index] = _reward_cpts[0][index];
						continue;
					}
					
					ArrayList<PVAR_INST_DEF> actions = getAction(actionNum);
					for (PVAR_NAME p_a : _actions.keySet())
						_actions.get(p_a).clear();
					setPVariables(_actions, actions);
				
					CPFTable table = _reward_cpts[actionNum][index];
					
					ArrayList<Pair> nameRelevants = new ArrayList<Pair>(relevant_vars);
					ArrayList<Integer> numRelevants = new ArrayList<Integer>();
					for(Pair nameR: nameRelevants)
						numRelevants.add(_stateName2Num.get(nameR));
					table._relevants = numRelevants;
					
					ArrayList<Boolean> key = new ArrayList<Boolean>();
					for(int i = 0; i < table._relevants.size(); i++)
						key.add(Boolean.FALSE);
					enumerateAssignments(nameRelevants, expr, subs, 0, key, table);
				}
				index++;
			}
		}
		catch(Exception ex)
		{
			
		}
	}
	
	public void enumerateAssignments(ArrayList<Pair> vars,
			EXPR cpf_expr, HashMap<LVAR,LCONST> subs, int index, ArrayList<Boolean> key, CPFTable table) 
		throws EvalException {

		if (index >= vars.size()) {
			
			RDDL.EXPR e = cpf_expr.getDist(subs, this);
			double prob_true = -1d;
			if (e instanceof KronDelta) {
				EXPR e2 = ((KronDelta)e)._exprIntValue;
				if (e2 instanceof INT_CONST_EXPR)
					// Should either be true (1) or false (0)... same as prob_true
					prob_true = (double)((INT_CONST_EXPR)e2)._nValue;
				else if (e2 instanceof BOOL_CONST_EXPR)
					prob_true = ((BOOL_CONST_EXPR)e2)._bValue ? 1d : 0d;
				else
					throw new EvalException("Unhandled KronDelta argument: " + e2.getClass()); 
			} else if (e instanceof Bernoulli) {
				prob_true = ((REAL_CONST_EXPR)((Bernoulli)e)._exprProb)._dValue;
			} else if (e instanceof DiracDelta) {
				prob_true = ((REAL_CONST_EXPR)((DiracDelta)e)._exprRealValue)._dValue;
			} else
				throw new EvalException("Unhandled distribution type: " + e.getClass());
			
			table._table.put((ArrayList<Boolean>)key.clone(), new Double(prob_true));

		} else {
			PVAR_NAME p = (PVAR_NAME)vars.get(index)._o1;
			ArrayList<LCONST> terms = (ArrayList<LCONST>)vars.get(index)._o2;

			setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.TRUE);
			key.set(index, new Boolean(true));
			enumerateAssignments(vars, cpf_expr, subs, index + 1, key, table);

			setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.FALSE);
			key.set(index, new Boolean(false));
			enumerateAssignments(vars, cpf_expr, subs, index + 1, key, table);
			
			setPVariableAssign(p, terms, null);
		}
	}
	
	public double weight(McState state, int action, long observation)
	{
		double weight = 1.0;
		try
		{
			int fluentNum = 0;
			for (PVAR_NAME p : _alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					//CPFTable table = _obsCpfTable[fluentNum];
					CPFTable table = _obs_cpts[action][fluentNum];
					ArrayList<Boolean> key = new ArrayList<Boolean>();
					for(int i = 0; i < table._relevants.size(); i++)
					{
						//Pair pair = table._relevants.get(i);
						//key.add((Boolean)getPVariableAssign((PVAR_NAME)pair._o1, (ArrayList<LCONST>)pair._o2));
						key.add(_nextStateNum._fValues[table._relevants.get(i).intValue()]);
					}
					double prob_true = table._table.get(key).doubleValue();
					
					long value = observation % 2;
					
					double factor = 0;
					
					if(value == 0)
						factor = 1 - prob_true;
					else
						factor = prob_true;
				
					if(factor == 0)
						factor += 0.0001;
					
					weight *= factor;
					observation = observation/2;
					
					fluentNum++;
				}
			}
			
		}
		catch(Exception e)
		{
			
		}
		
		return weight;
	}
	
	public ArrayList<PVAR_INST_DEF> getAction(int action)
	{
		ArrayList<PVAR_INST_DEF> actions = new ArrayList<PVAR_INST_DEF>();
		if(_nMaxNondefActions == 1)
		{
			int index = 0;
			for (PVAR_NAME p : _alActionNames) {
				ArrayList<ArrayList<LCONST>> gfluents = _actionVars.get(p);
				for (ArrayList<LCONST> assign : gfluents) {
					if (index == action)
						actions.add(new PVAR_INST_DEF(p.toString(),
							new Boolean(true), assign));
					//else
						//actions.add(new PVAR_INST_DEF(p.toString(),
								//new Boolean(false), assign));
					index++;
				}
			}
		}
		else
		{
			int base = SearchTree.ACTION_FLUENT_NUM/_nMaxNondefActions + 1;
			ArrayList<Integer> actionNum = new ArrayList<Integer>();
			for(int k = 0; k < _nMaxNondefActions; k++)
			{
				actionNum.add(new Integer(action%base));
				action = action/base;
			}
			int index = 0;
			for (PVAR_NAME p : _alActionNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _actionVars.get(p);
				int i = 0;
				for(ArrayList<LCONST> assign: gfluents)
				{
					if(actionNum.get(i).intValue() == index)
					{
						actions.add(new PVAR_INST_DEF(p.toString(), new Boolean(true), assign));
					}
					i++;
				}
				index++;
			}
		}
		return actions;
	}
	
	public HashSet<Pair> filterOutActionVars(HashSet<Pair> relevant_vars) {
		HashSet<Pair> new_vars = new HashSet<Pair>();
		for (Pair p : relevant_vars)
			if (getPVariableType((PVAR_NAME)p._o1) != State.ACTION)
				new_vars.add(p);
		return new_vars;
	}
	
	public ArrayList<Pair> getAdditiveComponents(EXPR e) throws Exception {

		ArrayList<Pair> ret = new ArrayList<Pair>();
		
		if (e instanceof OPER_EXPR && ((OPER_EXPR)e)._op == OPER_EXPR.PLUS) {

			OPER_EXPR o = (OPER_EXPR)e;

			//System.out.println("\n- Oper Processing " + o._e1);
			//System.out.println("\n- Oper Processing " + o._e2);
			
			ret.addAll(getAdditiveComponents(o._e1));
			ret.addAll(getAdditiveComponents(o._e2));
						
		} else if (e instanceof AGG_EXPR && ((AGG_EXPR)e)._op == AGG_EXPR.SUM) {
			
			AGG_EXPR a = (AGG_EXPR)e;
			
			ArrayList<ArrayList<LCONST>> possible_subs = generateAtoms(a._alVariables);
			HashMap<LVAR,LCONST> subs = new HashMap<LVAR,LCONST>();

			//System.out.println("\n- Sum Processing " + a);

			// Evaluate all possible substitutions
			for (ArrayList<LCONST> sub_inst : possible_subs) {
				for (int i = 0; i < a._alVariables.size(); i++) {
					subs.put(a._alVariables.get(i)._sVarName, sub_inst.get(i));
				}
				
				// Note: we are not currently decomposing additive structure below a sum aggregator
				ret.add(new Pair(subs.clone(), a._e));			
				
				subs.clear();
			}

		} else {
			//System.out.println("\n- General Processing " + e);
			HashMap<LVAR,LCONST> empty_subs = new HashMap<LVAR,LCONST>();
			ret.add(new Pair(empty_subs, e));
		}
		
		return ret;
	}
	
	public static class CPFTable
	{
		//public ArrayList<Pair> _relevants;
		public ArrayList<Integer> _relevants;
		public HashMap<ArrayList<Boolean>, Double> _table = new HashMap<ArrayList<Boolean>, Double>();
	}
	
	public static class ConstTable
	{
		public ArrayList<Pair> _relevants;
		public HashMap<ArrayList<Boolean>, Boolean> _table = new HashMap<ArrayList<Boolean>, Boolean>();
	}
}
