package mc;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import dd.discrete.ADD;
import rddl.RDDL.*;
import rddl.State;
import rddl.State.CPFTable;
import rddl.competition.Server;
import rddl.*;
import util.Pair;

public class SearchTree implements Runnable{
	
	public static int SIMULATION_NUM = 5000;
	public static int PARTICLE_NUM = 5000;
	public static Random _rand = new Random();
	//public static Random _ucbRand = new Random();
	//public static Random _rolloutRand = new Random();
	public static long START_TIME;
	public static long TOTAL_TIME = 18 * 60 * 1000;
	public static long TIME_PER_ROUND = 36000;
	public static long TIME_PER_STEP = 900;
	public static long SEARCH_TIME = 900 * 3/4;
	public static long UPDATE_TIME;
	public static int HORIZON;
	public static double DISCOUNT;
	public static long STATE_NUM;
	public static int STATE_FLUENT_NUM;
	public static long OBSERV_NUM;
	public static int OBSERV_FLUENT_NUM;
	public static int ACTION_NUM;
	public static int ACTION_FLUENT_NUM;
	
	public final int CONSTANT_SIMULATION_NUM = 50;
	public double _ucbConstant;
	
	public Node _defRoot;
	public Node _root;
	public int _rootDepth;
	public State _state;
	public State _stateT;
	
	public Node _defRootT;
	public Node _rootT;
	public int _threadState = 0;
	public int _actionT;
	public long _observationT;
	
	public HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>> _stateVars;  
	public HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>> _actionVars; 
	public HashMap<PVAR_NAME,ArrayList<ArrayList<LCONST>>> _observVars;
	
	//public static ArrayList<ArrayList<PVAR_INST_DEF>> _actionList;
	
	//for testing
	public int _simulDepth;
	public long _lastSimulTime;
	public long _simulStartTime;
	public ArrayList<Integer> test;
	public int _searchTimeout = 0;
	
	public static void initParam(State state, int horizon, double discount)
	{
		STATE_FLUENT_NUM = 0;
		OBSERV_FLUENT_NUM = 0;
		ACTION_FLUENT_NUM = 0;
		HORIZON = horizon;
		DISCOUNT = discount;
		//SEARCH_TIME = TIME_PER_ROUND*3/4/((HORIZON + 1)*HORIZON/2);
		SEARCH_TIME = TIME_PER_ROUND/HORIZON*3/4;
		UPDATE_TIME = TIME_PER_ROUND/HORIZON*1/4;
				
		try
		{
			for (PVAR_NAME p : state._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = state._stateVars.get(p);
				STATE_FLUENT_NUM += gfluents.size();
			}
			STATE_NUM = (long)Math.pow(2, STATE_FLUENT_NUM);
			McState.STATE_FLUENT_NUM = STATE_FLUENT_NUM;
			
			for (PVAR_NAME p : state._alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = state._observVars.get(p);
				OBSERV_FLUENT_NUM += gfluents.size();
			}
			OBSERV_NUM = (long)Math.pow(2, OBSERV_FLUENT_NUM);

			for (PVAR_NAME p : state._alActionNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = state._actionVars.get(p);
				ACTION_FLUENT_NUM += gfluents.size();
			}
			
			if(state._nMaxNondefActions == 1)
			{
				ACTION_NUM = ACTION_FLUENT_NUM + 1;
			}
			else
			{
				//_hmActionMap = ActionGenerator.getLegalBoolActionMap(state);
				int per = (int)ACTION_FLUENT_NUM/state._nMaxNondefActions;
				ACTION_NUM = (int)Math.pow(per + 1, state._nMaxNondefActions);
				//ACTION_NUM = _hmActionMap.entrySet().size();
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void init(State state)
	{
		//STATE_FLUENT_NUM = 0;
		//OBSERV_FLUENT_NUM = 0;
		//ACTION_FLUENT_NUM = 0;
		_state = state;
		//HORIZON = horizon;
		//DISCOUNT = discount;
		//SEARCH_TIME = TIME_PER_ROUND/HORIZON*3/4;
		//UPDATE_TIME = TIME_PER_ROUND/HORIZON*1/4;
		_stateVars = _state._stateVars;
		_actionVars = _state._actionVars;
		_observVars = _state._observVars;
		
		try
		{
			/*for (PVAR_NAME p : _state._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				STATE_FLUENT_NUM += gfluents.size();
			}
			STATE_NUM = (long)Math.pow(2, STATE_FLUENT_NUM);
			McState.STATE_FLUENT_NUM = STATE_FLUENT_NUM;*/
			state._stateNum = new McState();
			state._nextStateNum = new McState();
			//state._cpfTable = new State.CPFTable[ACTION_NUM][STATE_FLUENT_NUM];
			state._state_cpts = new State.CPFTable[ACTION_NUM][STATE_FLUENT_NUM];
			for(int i = 0; i < ACTION_NUM; i++)
				for(int j = 0; j < STATE_FLUENT_NUM; j++)
					//state._cpfTable[i][j] = new State.CPFTable();
					state._state_cpts[i][j] = new State.CPFTable();
			
			/*for (PVAR_NAME p : _state._alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
				OBSERV_FLUENT_NUM += gfluents.size();
			}
			OBSERV_NUM = (long)Math.pow(2, OBSERV_FLUENT_NUM);*/
//			state._obsCpfTable = new State.CPFTable[OBSERV_FLUENT_NUM];
			state._obs_cpts = new State.CPFTable[ACTION_NUM][OBSERV_FLUENT_NUM];
			for(int i = 0; i < ACTION_NUM; i++)
				for(int j = 0; j < OBSERV_FLUENT_NUM; j++)
//					state._obsCpfTable[i] = new State.CPFTable();
				state._obs_cpts[i][j] = new State.CPFTable();
			state._reward_cpts = new State.CPFTable[ACTION_NUM][];
			state._state_act_related = new boolean[STATE_FLUENT_NUM];
			state._obs_act_related = new boolean[OBSERV_FLUENT_NUM];
			/*for (PVAR_NAME p : _state._alActionNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _actionVars.get(p);
				ACTION_FLUENT_NUM += gfluents.size();
			}*/
			state.buildCPTs();
			/*System.out.println(state._alStateNames);
			System.out.println(_stateVars);
			System.out.println();
			for(int jj = 0; jj < state._cpfTable.length; jj++)
			{
				CPFTable table = state._cpfTable[jj];
				System.out.println(table._relevants);
				System.out.println(table._table.toString());
				System.out.println();
			}*/
			/*if(_state._nMaxNondefActions == 1)
			{
				ACTION_NUM = ACTION_FLUENT_NUM + 1;
			}
			else
			{
				int per = (int)ACTION_FLUENT_NUM/_state._nMaxNondefActions;
				ACTION_NUM = (int)Math.pow(per + 1, _state._nMaxNondefActions);
			}*/
				
			/*test = new ArrayList<Integer>();
			for(int j = 0; j < ACTION_NUM; j++)
			{
				test.add(new Integer(0));
			}*/
			_stateT = new State();
			_stateT.copy(state);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void initRoot()
	{
		_root = new Node(ACTION_NUM, 0);
		//_defRoot = _root;
		
		_rootT = new Node(ACTION_NUM, 0);
		//_defRootT = _rootT;
		
		McState initStateNum = stateToInt();
		for(int i = 0; i < PARTICLE_NUM; i++)
		{
			_root._belief.addSample(initStateNum);
			_rootT._belief.addSample(initStateNum);
		}
		_rootDepth = 0;
		setUcbConstant();
	}
	
	public void reset()
	{
		_root = new Node(ACTION_NUM, 0);
		_rootT = new Node(ACTION_NUM, 0);
		McState initStateNum = stateToInt();
		for(int i = 0; i < PARTICLE_NUM; i++)
		{
			_root._belief.addSample(initStateNum);
			_rootT._belief.addSample(initStateNum);
		}
		

		_rootDepth = 0;
		//_root = _defRoot;
		//_rootT = _defRootT;
	}
	
	public boolean update(int action, long observation)
	{
		//if(_rootDepth <= 1)
		//{
			//printObserv(observation);
			//printAction(action);
		//}
		/*ANode testNode = _root._branches.get(action);
		for(Integer in: testNode._children.keySet())
		{
			System.out.print(in + "-" + testNode._children.get(in)._belief._size + " ");
		}*/
		//System.out.println("obs:" + observation);
		UPDATE_TIME = TIME_PER_STEP/4;
		
		_threadState = 1;
		_actionT = action;
		_observationT = observation;
		Thread thread = new Thread(this);
		thread.start();
		
		Node node = _root._branches.get(action)._children.get(new Long(observation));
		if(node == null)
		{
			//System.out.println("new node in depth: " + _root._depth);
			node = new Node(ACTION_NUM, _root._depth + 1);
			//_root._branches.get(action)._children.put(new Long(observation), node);
		}
		//Node node = new Node(ACTION_NUM, 0);
		HashMap<McState, Double> weightBuf = new HashMap<McState, Double>();
		long startTime = System.currentTimeMillis();
		int i = 0;
		
		for(i = 0; i < PARTICLE_NUM; i++)
		{
			McState curState = _root._belief.generateState();
			//System.out.println("sample from: " + curState);
			stateInit(curState);
			//SampleResult result = sample(action);
			McState nextState = sampleForUpdate(action);
			//if(_rootDepth <= 1)
			//printState(nextState);
			double weight;
			Double weightD = weightBuf.get(nextState);
			if(weightD == null)
			{
				weight = weight(nextState, action, observation);
				weightBuf.put(nextState, new Double(weight));
			}
			else
				weight = weightD.doubleValue();
			clearNextState();
			//if(_rootDepth <= 1)
			//System.out.println(i + ":" + weight);
			node._belief.addSampleWithWeight(nextState, weight);
			if(System.currentTimeMillis() - startTime > UPDATE_TIME)
				break;
		}
		//System.out.println("update: " + i + " ;time: " + (System.currentTimeMillis() - startTime));
		if(_rootDepth != 0)
			_root._belief.clear();
		_root = node;
		_rootDepth++;
		boolean not_out_of_particle = node._belief.resample();
		try
		{
			while(thread.isAlive());
		}
		catch(Exception e)
		{
			
		}
		return not_out_of_particle;
		
		/*Node node = _root._branches.get(action)._children.get(new Integer(observation));
		if(node == null)
			node = new Node(ACTION_NUM, _root._depth + 1);
		for(int i = 0; i < SIMULATION_NUM; i++)
		{
			int stateNum = _root._belief.generateState();
			stateInit(stateNum);
			SampleResult result = sample(action);
			if(similarity(result.observation, observation) >= OBSERV_NUM * 0.8)
				node._belief.addSample(result.nextState);
		}
		_root = node;
		_rootDepth++;
		return true;*/
	}
	
	public int search()
	{	
		if(_rootDepth == 0)
			SEARCH_TIME = TIME_PER_STEP;
		else
			SEARCH_TIME = TIME_PER_STEP * 3/4;
		
		_threadState = 0;
		Thread thread = new Thread(this);
		thread.start();
		
		long startTime = System.currentTimeMillis();
		int i = 0;
		for(i = 0; i < SIMULATION_NUM; i++)
		{
			//int stateNum = _root._belief._particles.get(i).intValue();
			McState stateNum = _root._belief.generateState();
			stateInit(stateNum);
			//simulate(_root, 0);
			//_simulDepth = 0;
			_simulStartTime = System.currentTimeMillis();
			simulate(_root, _rootDepth);
			//if(_searchTimeout >= 4)
			//{
				//if(_rootDepth == 0)
				//{
					//System.out.println("simulDepth: " + _simulDepth);
			long simulT = System.currentTimeMillis() - _simulStartTime;
			if(simulT >= 2000)
				System.out.println("time for simulation " + i + ": " + simulT);
				//}
			//}
			//if(System.currentTimeMillis() - startTime > SEARCH_TIME * (HORIZON - _rootDepth))
			if(System.currentTimeMillis() - startTime > SEARCH_TIME)
				break;
		}
		long searchT = System.currentTimeMillis() - startTime;
		//if(searchT > 2000)
			//_searchTimeout++;
		System.out.println("search: " + i + " ;time: " + searchT);
		//System.out.println("search: " + i + " ;time: " + (System.currentTimeMillis() - startTime));
		try
		{
			while(thread.isAlive());
		}
		catch(Exception e)
		{
			
		}
		int action = ucbSelectForWhole(_root, _rootT);
		/*System.out.println(test.toString() + "action:" + action);
		for(int i = 0; i < ACTION_NUM; i++)
			test.set(i, new Integer(0));*/
		return action;
	}
	
	public Node search2()
	{	
		long startTime = System.currentTimeMillis();
		int i = 0;
		if(_rootDepth == 0)
			SEARCH_TIME = TIME_PER_STEP;
		else
			SEARCH_TIME = TIME_PER_STEP * 3/4;
		for(i = 0; i < SIMULATION_NUM; i++)
		{
			McState stateNum = _root._belief.generateState();
			stateInit(stateNum);
			_simulStartTime = System.currentTimeMillis();
			simulate(_root, _rootDepth);
			long simulT = System.currentTimeMillis() - _simulStartTime;
			if(simulT >= 2000)
				System.out.println("time for simulation " + i + ": " + simulT);
			if(System.currentTimeMillis() - startTime > SEARCH_TIME)
				break;
		}
		long searchT = System.currentTimeMillis() - startTime;
		System.out.println("search: " + i + " ;time: " + searchT);
		//int action = ucbSelect(_root, false);
		return _root;
	}
	
	public void run()
	{
		if(_threadState == 0)
		{
			long startTime = System.currentTimeMillis();
			int i = 0;
			for(i = 0; i < SIMULATION_NUM; i++)
			{
				McState stateNum = _rootT._belief.generateState();
				stateInitForThread(stateNum);
				simulateForThread(_rootT, _rootDepth);		
				if(System.currentTimeMillis() - startTime > SEARCH_TIME)
					break;
			}
			long searchT = System.currentTimeMillis() - startTime;
			System.out.println("search2: " + i + " ;time: " + searchT);
		}
		else
		{
			Node node = _rootT._branches.get(_actionT)._children.get(new Long(_observationT));
			if(node == null)
			{
				//System.out.println("new node in depth: " + _root._depth);
				node = new Node(ACTION_NUM, _rootT._depth + 1);
				//_root._branches.get(action)._children.put(new Long(observation), node);
			}
			//Node node = new Node(ACTION_NUM, 0);
			HashMap<McState, Double> weightBuf = new HashMap<McState, Double>();
			long startTime = System.currentTimeMillis();
			int i = 0;
			
			for(i = 0; i < PARTICLE_NUM; i++)
			{
				McState curState = _rootT._belief.generateState();
				//System.out.println("sample from: " + curState);
				stateInitForThread(curState);
				//SampleResult result = sample(action);
				McState nextState = sampleForUpdateForThread(_actionT);
				//if(_rootDepth <= 1)
				//printState(nextState);
				double weight;
				Double weightD = weightBuf.get(nextState);
				if(weightD == null)
				{
					weight = weightForThread(nextState, _actionT, _observationT);
					weightBuf.put(nextState, new Double(weight));
				}
				else
					weight = weightD.doubleValue();
				clearNextStateForThread();
				//if(_rootDepth <= 1)
				//System.out.println(i + ":" + weight);
				node._belief.addSampleWithWeight(nextState, weight);
				if(System.currentTimeMillis() - startTime > UPDATE_TIME)
					break;
			}
			//System.out.println("update2: " + i + " ;time: " + (System.currentTimeMillis() - startTime));
			if(_rootDepth != 0)
				_rootT._belief.clear();
			_rootT = node;
			//_rootDepth++;
			node._belief.resample();
		}
	}
	
	public double simulate(Node node, int depth)
	{
		
		//if(depth == 1)
		//if(depth != _rootDepth)
			//node._belief.addSample(stateToInt());
		if(depth >= HORIZON)
			return 0;
		int action = ucbSelect(node, true);
		//if(depth == _rootDepth)
			//test.set(action, new Integer(test.get(action).intValue() + 1));
		//if(_state._state2FluentValue.get(_state._stateNum) != null)
			//System.out.println("simulate hit in depth: " + depth + " stateNum: " + _state._stateNum);
		//else
			//System.out.println("simulate not hit in depth: " + depth + " stateNum: " + _state._stateNum);
		SampleResult result = sample(action);
		double totReward = simulateA(node._branches.get(action), action, result, depth);
		node._count++;
		return totReward;
	}
	
	public double simulateA(ANode aNode, int action, SampleResult result, int depth)
	{
		//_simulDepth++;
		Node node = aNode._children.get(new Long(result.observation));
		double reward = result.reward;
		if(node != null)
			reward += DISCOUNT * simulate(node, depth + 1);
		else
		{
			node = new Node(ACTION_NUM, depth + 1);
			aNode._children.put(new Long(result.observation), node);
			//if(_root._depth == 0)
				//System.out.println("time for simulate: " + (System.currentTimeMillis() - _simulStartTime));
			long timeRStart = System.currentTimeMillis();
			reward += DISCOUNT * rollout(result.nextState, depth + 1);
			long timeR = System.currentTimeMillis() - timeRStart;
			if(timeR > 1500)
				System.out.println("time for rollout: " + timeR);
		}
			
		aNode._value = (aNode._value * aNode._count + reward)/(aNode._count + 1);
		aNode._count++;
		
		return reward;
	}
	
	public double simulateForThread(Node node, int depth)
	{
		if(depth >= HORIZON)
			return 0;
		int action = ucbSelect(node, true);
		SampleResult result = sampleForThread(action);
		double totReward = simulateAForThread(node._branches.get(action), action, result, depth);
		//if(depth == _rootDepth)
		node._count++;
		return totReward;
	}
	
	public double simulateAForThread(ANode aNode, int action, SampleResult result, int depth)
	{
		Node node = aNode._children.get(new Long(result.observation));
		double reward = result.reward;
		if(node != null)
			reward += DISCOUNT * simulateForThread(node, depth + 1);
		else
		{
			node = new Node(ACTION_NUM, depth + 1);
			aNode._children.put(new Long(result.observation), node);
			reward += DISCOUNT * rolloutForThread(result.nextState, depth + 1);
		}
		
		//if(depth == _rootDepth)
		//{
		aNode._value = (aNode._value * aNode._count + reward)/(aNode._count + 1);
		aNode._count++;
		//}
		
		return reward;
	}
	
	public double rollout(McState state, int depth)
	{
		/*if(depth >= HORIZON)
			return 0;
		int action = randomSelect();
		//if(_state._state2FluentValue.get(_state._stateNum) != null)
			//System.out.println("simulate hit in depth: " + depth + " stateNum: " + _state._stateNum);
		//else
			//System.out.println("depth: " + depth + " stateNum: " + _state._stateNum);
		SampleResult result = sampleForRollout(action);
		return result.reward + DISCOUNT * rollout(result.nextState, depth + 1);*/
		
		double roReward = 0;
		double discount = 1.0;
		for(; depth < HORIZON; depth++)
		{
			int action = randomSelect();
			SampleResult result = sampleForRollout(action);
			roReward += discount * result.reward; 
			discount *= DISCOUNT;
		}
		return roReward;
	}
	
	public double rolloutForThread(McState state, int depth)
	{
		double roReward = 0;
		double discount = 1.0;
		for(; depth < HORIZON; depth++)
		{
			int action = randomSelect();
			SampleResult result = sampleForRolloutForThread(action);
			roReward += discount * result.reward; 
			discount *= DISCOUNT;
		}
		return roReward;
	}
	
	public int ucbSelect(Node node, boolean confidence)
	{
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		double bestValue = Double.NEGATIVE_INFINITY;
		double value;
		double constant = _ucbConstant * (HORIZON - node._depth) / HORIZON;
		
		for(int i = 0; i < node._branches.size(); i++)
		{
			if(confidence)
			{
				if(node._branches.get(i)._count == 0)
					value = Double.POSITIVE_INFINITY;
				else
					value = node._branches.get(i)._value + constant * Math.sqrt(Math.log(node._count)/node._branches.get(i)._count);
			}
			else
				value = node._branches.get(i)._value;
			if(value >= bestValue)
			{
				if(value > bestValue)
					bestActions.clear();
				bestActions.add(new Integer(i));
				bestValue = value;
			}
		}
		
		return bestActions.get(_rand.nextInt(bestActions.size()));
	}
	
	public int ucbSelectForWhole(Node node1, Node node2)
	{
		ArrayList<Integer> bestActions = new ArrayList<Integer>();
		double bestValue = Double.NEGATIVE_INFINITY;
		double value1;
		double value2;
		
		for(int i = 0; i < node1._branches.size(); i++)
		{
			value1 = node1._branches.get(i)._value;
			value2 = node2._branches.get(i)._value;
			if(value1 + value2 >= bestValue)
			{
				if(value1 + value2 > bestValue)
					bestActions.clear();
				bestActions.add(new Integer(i));
				bestValue = value1 + value2;
			}
		}
		
		return bestActions.get(_rand.nextInt(bestActions.size()));
	}
	
	public int randomSelect()
	{
		return _rand.nextInt(ACTION_NUM);
	}
	
	public SampleResult sample(int action)
	{
		SampleResult result = new SampleResult();
		try
		{
			_state.computeNextState(getAction(action), action, _rand);
			result.observation = observToInt();
			//result.reward = ((Number)_state._reward.sample(new HashMap<LVAR,LCONST>(), _state, _rand, new BooleanPair())).doubleValue();
			_state.computeReward(action);
			_state.advanceNextState();
			//result.nextState = stateToInt();
			result.nextState.assign(_state._stateNum);
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	
	public SampleResult sampleForRollout(int action)
	{
		SampleResult result = new SampleResult();
		try
		{
			_state.computeNextStateWithoutObserv(getAction(action), action, _rand);
			result.observation = -1;
//			result.reward = ((Number)_state._reward.sample(new HashMap<LVAR,LCONST>(), _state, _rand, new BooleanPair())).doubleValue();
			result.reward = _state.computeReward(action);
			_state.advanceNextState();
			//result.nextState = stateToInt();
			result.nextState.assign(_state._stateNum);
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	
	public McState sampleForUpdate(int action)
	{
		McState nextState = new McState();
		try
		{
			_state.computeNextStateWithoutObserv(getAction(action), action, _rand);
			//nextState = nextStateToInt();
			nextState.assign(_state._nextStateNum);
		}
		catch(Exception e)
		{
			
		}
		return nextState;
	}
	
	public SampleResult sampleForThread(int action)
	{
		SampleResult result = new SampleResult();
		try
		{
			_stateT.computeNextState(getAction(action), action, _rand);
			result.observation = observToIntForThread();
//			result.reward = ((Number)_stateT._reward.sample(new HashMap<LVAR,LCONST>(), _stateT, _rand, new BooleanPair())).doubleValue();
			result.reward = _stateT.computeReward(action);
			_stateT.advanceNextState();
			//result.nextState = stateToInt();
			result.nextState.assign(_stateT._stateNum);
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	
	public SampleResult sampleForRolloutForThread(int action)
	{
		SampleResult result = new SampleResult();
		try
		{
			_stateT.computeNextStateWithoutObserv(getAction(action), action, _rand);
			result.observation = -1;
//			result.reward = ((Number)_stateT._reward.sample(new HashMap<LVAR,LCONST>(), _stateT, _rand, new BooleanPair())).doubleValue();
			result.reward = _stateT.computeReward(action);
			_stateT.advanceNextState();
			//result.nextState = stateToInt();
			result.nextState.assign(_stateT._stateNum);
		}
		catch(Exception e)
		{
			
		}
		return result;
	}

	public McState sampleForUpdateForThread(int action)
	{
		McState nextState = new McState();
		try
		{
			_stateT.computeNextStateWithoutObserv(getAction(action), action, _rand);
			//nextState = nextStateToInt();
			nextState.assign(_stateT._nextStateNum);
		}
		catch(Exception e)
		{
			
		}
		return nextState;
	}
	
	public void clearNextState()
	{
		for (PVAR_NAME p : _state._nextState.keySet())
			_state._nextState.get(p).clear();
	}
	
	public void clearNextStateForThread()
	{
		for (PVAR_NAME p : _stateT._nextState.keySet())
			_stateT._nextState.get(p).clear();
	}
	
	public McState stateToInt()
	{
		McState state = new McState();
		try
		{
			int i = 0;
			for (PVAR_NAME p : _state._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					Boolean value = (Boolean)_state.getPVariableAssign(p, assign);
					state._fValues[i] = value.booleanValue();
					i++;
				}
			}
			_state._stateNum.assign(state);
		}
		catch(Exception e)
		{
			
		}
		return state;
	}
	
	public McState nextStateToInt()
	{
		McState state = new McState();
		try
		{
			int i = 0;
			for (PVAR_NAME p : _state._alStateNames) 
			{	
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				PVAR_NAME p1 = new PVAR_NAME(p.toString());
				p1._bPrimed = true;
				for(ArrayList<LCONST> assign: gfluents)
				{
					Boolean value = (Boolean)_state.getPVariableAssign(p1, assign);
					state._fValues[i] = value.booleanValue();
					i++;
				}
			}
		}
		catch(Exception e)
		{
			
		}
		return state;
	}
	
	public long observToInt()
	{
		long base = 1;
		long sum = 0;
		try
		{
			for (PVAR_NAME p : _state._alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					Boolean value = (Boolean)_state.getPVariableAssign(p, assign);
					sum += (value.booleanValue()?1:0) * base;
					base *= 2;
				}
			}
			return sum;
		}
		catch(Exception e)
		{
			
		}
		return 0;
	}
	
	public long observToIntForThread()
	{
		long base = 1;
		long sum = 0;
		try
		{
			for (PVAR_NAME p : _stateT._alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					Boolean value = (Boolean)_stateT.getPVariableAssign(p, assign);
					sum += (value.booleanValue()?1:0) * base;
					base *= 2;
				}
			}
			return sum;
		}
		catch(Exception e)
		{
			
		}
		return 0;
	}
	
	public void stateInit(McState stateNum)
	{
		_state._stateNum.assign(stateNum);
		try
		{
			int i = 0;
			for (PVAR_NAME p : _state._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					_state.setPVariableAssign(p, assign, new Boolean(stateNum._fValues[i]));
					i++;
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void stateInitForThread(McState stateNum)
	{
		try
		{
			_stateT.advanceNextState();
			for (PVAR_NAME p : _stateT._state.keySet())
				_stateT._state.get(p).clear();
			int i = 0;
			for (PVAR_NAME p : _stateT._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					_stateT.setPVariableAssign(p, assign, new Boolean(stateNum._fValues[i]));
					i++;
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	/*public void nextStateInit(McState stateNum)
	{
		try
		{
			for (PVAR_NAME p : _state._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					long value = stateNum % 2;
					boolean bValue;
					if(value == 0)
						bValue = false;
					else
						bValue = true;
					PVAR_NAME p1 = new PVAR_NAME(p.toString());
					p1._bPrimed = true;
					_state.setPVariableAssign(p1, assign, new Boolean(bValue));
					stateNum = stateNum/2;
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}*/
	
	public ArrayList<PVAR_INST_DEF> getAction(int action)
	{
		ArrayList<PVAR_INST_DEF> actions = new ArrayList<PVAR_INST_DEF>();
		if(_state._nMaxNondefActions == 1)
		{
			int index = 0;
			for (PVAR_NAME p : _state._alActionNames) {
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
			int base = ACTION_FLUENT_NUM/_state._nMaxNondefActions + 1;
			ArrayList<Integer> actionNum = new ArrayList<Integer>();
			for(int k = 0; k < _state._nMaxNondefActions; k++)
			{
				actionNum.add(new Integer(action%base));
				action = action/base;
			}
			int index = 0;
			for (PVAR_NAME p : _state._alActionNames) 
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
	
	public void setUcbConstant()
	{
		Node root = new Node(ACTION_NUM, 0);
		for(int i = 0; i < CONSTANT_SIMULATION_NUM; i++)
			root._belief.addSample(_root._belief.generateState());
		double lReward = Double.POSITIVE_INFINITY;
		long startTime = System.currentTimeMillis();
		double rRAve = 0;
		for(int i = 0; i < CONSTANT_SIMULATION_NUM; i++)
		//for(int i = 0; i < 30; i++)
		{
			McState stateNum = root._belief.generateState();
			stateInit(stateNum);
			double reward = rollout(stateNum, 0);
			//System.out.println("random reward: " + reward);
			rRAve = (rRAve * i + reward)/(i + 1);
			if(reward < lReward)
				lReward = reward;
			//if(System.currentTimeMillis() - startTime > SEARCH_TIME * HORIZON/2)
			if(System.currentTimeMillis() - startTime > SEARCH_TIME)
				break;
		}
		
		/*try{
			FileWriter writer = new FileWriter("ramdon_reward.txt", true);
			writer.write(String.valueOf(rRAve) + "\r\n\r\n");
			writer.close();
		}
		catch(Exception e)
		{
			
		}
		System.exit(0);*/
		
		//System.out.println("average: " + rRAve);
		//System.out.println("small memory");
		double hReward = Double.NEGATIVE_INFINITY;
		startTime = System.currentTimeMillis();
		for(int i = 0; i < CONSTANT_SIMULATION_NUM; i++)
		{
			McState stateNum = root._belief.generateState();
			stateInit(stateNum);
			double reward = simulateC(root, 0);
			if(reward > hReward)
				hReward = reward;
			//if(System.currentTimeMillis() - startTime > SEARCH_TIME * HORIZON/2)
			if(System.currentTimeMillis() - startTime > SEARCH_TIME)
				break;
		}
		_ucbConstant = hReward - lReward;
	}
	
	public double simulateC(Node node, int depth)
	{
		//if(depth == 1)
		//if(depth != _rootDepth)
			//node._belief.addSample(stateToInt());
		if(depth >= HORIZON)
			return 0;
		int action = ucbSelect(node, false);
		//if(depth == _rootDepth)
			//test.set(action, new Integer(test.get(action).intValue() + 1));
		SampleResult result = sample(action);
		node._count++;
		return simulateA(node._branches.get(action), action, result, depth);
	}
	
	public double simulateCA(ANode aNode, int action, SampleResult result, int depth)
	{
		Node node = aNode._children.get(new Long(result.observation));
		double reward = result.reward;
		if(node != null)
			reward += DISCOUNT * simulateC(node, depth + 1);
		else
		{
			node = new Node(ACTION_NUM, depth + 1);
			aNode._children.put(new Long(result.observation), node);
			reward += DISCOUNT * rollout(result.nextState, depth + 1);
		}
			
		aNode._value = (aNode._value * aNode._count + reward)/(aNode._count + 1);
		aNode._count++;
		
		return reward;
	}
	
	public int similarity(long obs1, long obs2)
	{
		int similarity = 0;
		for(int i = 0; i < OBSERV_FLUENT_NUM; i++)
		{
			if(obs1 % 2 == obs2 % 2)
				similarity++;
			obs1 = obs1/2;
			obs2 = obs2/2;
		}
		return similarity;
	}
	
	public double weight(McState state, int action, long observation)
	{
		/*double weight = 1.0;
		try
		{
			HashMap<LVAR, LCONST> subs = new HashMap<LVAR, LCONST>();
			for (PVAR_NAME p : _state._alObservNames) 
			{
				CPF_DEF cpf = _state._hmCPFs.get(p);
				ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) {
						LVAR v = (LVAR)cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST)assign.get(i);
						subs.put(v,c);
					}
					EXPR e = cpf._exprEquals.getDist(subs, _state);
					double prob_true = -1d;
					if (e instanceof KronDelta) 
					{
						EXPR e2 = ((KronDelta)e)._exprIntValue;
						if (e2 instanceof BOOL_CONST_EXPR)
							prob_true = ((BOOL_CONST_EXPR)e2)._bValue ? 1d : 0d;
					} 
					else if (e instanceof Bernoulli) 
					{
						prob_true = ((REAL_CONST_EXPR)((Bernoulli)e)._exprProb)._dValue;
					}
					
					long value = observation % 2;
					if(value == 0)
						weight *= 1 - prob_true;
					else
						weight *= prob_true;
					observation = observation/2;
				}
			}
			
		}
		catch(Exception e)
		{
			
		}*/
		return _state.weight(state, action, observation);
	}
	
	public double weightForThread(McState state, int action, long observation)
	{
		return _stateT.weight(state, action, observation);
	}
	
	public void printState(McState state)
	{
		try
		{
			int i = 0;
			System.out.print("[");
			for (PVAR_NAME p : _state._alStateNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _stateVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					
					if(state._fValues[i] == false)
						System.out.print("~" + p.toString() + assign.toString() + " ");
					else
						System.out.print(p.toString() + assign.toString() + " ");
					i++;
				}
			}
			System.out.println("]");
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void printObserv(long observ)
	{
		try
		{
			System.out.print("[");
			for (PVAR_NAME p : _state._alObservNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _observVars.get(p);
				for(ArrayList<LCONST> assign: gfluents)
				{
					long value = observ % 2;
					if(value == 0)
						System.out.print("~" + p.toString() + assign.toString() + " ");
					else
						System.out.print(p.toString() + assign.toString() + " ");
					observ = observ/2;
				}
			}
			System.out.println("]");
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void printAction(int action)
	{
		System.out.print("[");
		if(_state._nMaxNondefActions == 1)
		{
			int index = 0;
			for (PVAR_NAME p : _state._alActionNames) {
				ArrayList<ArrayList<LCONST>> gfluents = _actionVars.get(p);
				for (ArrayList<LCONST> assign : gfluents) {
					if (index == action)
						System.out.print(p.toString() + assign.toString());
					index++;
				}
			}
		}
		else
		{
			int base = ACTION_FLUENT_NUM/_state._nMaxNondefActions + 1;
			ArrayList<Integer> actionNum = new ArrayList<Integer>();
			for(int k = 0; k < _state._nMaxNondefActions; k++)
			{
				actionNum.add(new Integer(action%base));
				action = action/base;
			}
			int index = 0;
			for (PVAR_NAME p : _state._alActionNames) 
			{
				ArrayList<ArrayList<LCONST>> gfluents = _actionVars.get(p);
				int i = 0;
				for(ArrayList<LCONST> assign: gfluents)
				{
					if(actionNum.get(i).intValue() == index)
					{
						System.out.print(p.toString() + assign.toString() + " ");
					}
					i++;
				}
				index++;
			}
		}
		System.out.println("]");
	}
	
	public static class Node
	{
		public ArrayList<ANode> _branches;
		public Belief _belief;
		public int _count;
		public int _depth;
		
		public Node(int actionNum, int depth)
		{
			_branches = new ArrayList<ANode>();
			for(int i = 0; i < actionNum; i++)
				_branches.add(new ANode());
			_count = 0;
			_belief = new Belief();
			_depth = depth;
		}
	}
	
	public static class ANode
	{
		public int _count;
		public double _value;
		public HashMap<Long, Node> _children;
		
		public ANode()
		{
			_children = new HashMap<Long, Node>();
			_count = 0;
			_value = 0;
		}
	}
	
	public static class SampleResult
	{
		public McState nextState = new McState();
		public long observation;
		public double reward;
	}
}
