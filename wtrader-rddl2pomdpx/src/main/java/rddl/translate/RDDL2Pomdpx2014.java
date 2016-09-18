/*

 * Some of the code is derived from RDDL2Format.java 
 * 		by Scott Sanner and Sungwook Yoon
 */

package rddl.translate;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import rddl.*;
import rddl.RDDL.*;
import rddl.parser.*;
import mc.McState;
import util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class RDDL2Pomdpx2014 {

	public final static int STATE_ITER = 0;
	public final static int OBSERV_ITER = 1;

	public State _state;
	public INSTANCE _i;
	public NONFLUENTS _n;
	public DOMAIN _d;

	public Document _dom;

	public ArrayList<ArrayList<String>> instances;
	public ArrayList<Double> probs;

	public boolean _succeed;
	public long _timer;
	
	public boolean ACTION_RELATED = true;
	public boolean MOMDP = false;
	public boolean EXPR_LABEL = false;
	public boolean STATE_CONSTRAINT = false;
	
	public static class CPFTable
	{
		public ArrayList<Pair> _relevants;
		public HashMap<ArrayList<Boolean>, Double> values = new HashMap<ArrayList<Boolean>, Double>();
	}

	HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> state_vars;
	HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> action_vars;
	
	HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> observ_vars;

	public ArrayList<String> _alActionVars;
	public Map<String,ArrayList<PVAR_INST_DEF>> _hmActionMap;

	public ArrayList<String> _reward_vars;
	
	public HashMap<Pair, Integer> _state_index;
	
	public HashMap<Pair, ArrayList<Pair>> _state_related_vars;
	public HashMap<Pair, ArrayList<Pair>> _obs_related_vars;
	public HashMap<String, ArrayList<Pair>> _reward_related_vars;
	
	public HashMap<Pair, Boolean> _state_act_related;
	public HashMap<Pair, Boolean> _obs_act_related;
	public HashMap<String, Boolean> _reward_act_related;
	
	public HashMap<Pair, CPFTable[]> _state_act_cpts;
	public HashMap<Pair, CPFTable> _state_cpts;
	public HashMap<Pair, CPFTable> _obs_cpts;
	public HashMap<Pair, CPFTable[]> _obs_act_cpts;
	public HashMap<String, CPFTable[]> _reward_act_cpts;
	public HashMap<String, CPFTable> _reward_cpts;
	
	public boolean _state_constrainted;
	public ArrayList<McState> _state_list;
	public String state_description;
	
	public int STATE_CONSTRAINT_SIZE = 500; 

	public RDDL2Pomdpx2014(RDDL rddl, String instance_name) throws Exception {

		_dom = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.newDocument();

		_state = new State();

		_i = rddl._tmInstanceNodes.get(instance_name);
		if (_i == null)
			throw new Exception("Instance '" + instance_name
					+ "' not found, choices are "
					+ rddl._tmInstanceNodes.keySet());

		_n = null;
		if (_i._sNonFluents != null)
			_n = rddl._tmNonFluentNodes.get(_i._sNonFluents);
		_d = rddl._tmDomainNodes.get(_i._sDomain);
		if (_d == null)
			throw new Exception("Could not get domain '" + _i._sDomain
					+ "' for instance '" + instance_name + "'");
		if (_n != null && !_i._sDomain.equals(_n._sDomain))
			throw new Exception(
					"Domain name of instance and fluents do not match: "
							+ _i._sDomain + " vs. " + _n._sDomain);

		_state.init(_n != null ? _n._hmObjects : null, _i._hmObjects,
				_d._hmTypes, _d._hmPVariables, _d._hmCPF, _i._alInitState,
				_n == null ? null : _n._alNonFluents, _d._alStateConstraints,
						_d._exprReward, _i._nNonDefActions);

		_state.splitConstraints();
		instances = new ArrayList<ArrayList<String>>();
		probs = new ArrayList<Double>();

		_succeed = buildCPTs();
	}

	public void export(String directory) throws Exception {

		if(!_succeed)
			return;

		File dir =new File(directory);
		if (!(dir.exists())||!(dir.isDirectory()))
			dir.mkdirs();

		String filename = directory + File.separator
				+ _i._sName;

		filename = filename + ".pomdpx";
		PrintWriter pw = new PrintWriter(new FileWriter(filename));

		try {
			Source source = new DOMSource(_dom);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
			pw.print(stringWriter.getBuffer().toString());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		pw.close();
	}

	public void addRoot() {
		Element root = _dom.createElement("pomdpx");
		_dom.appendChild(root);
	}

	public void addDescription() {
		Element desc = _dom.createElement("Description");
		//Text t = _dom.createTextNode(s);
		//desc.appendChild(t);
		Element root = _dom.getDocumentElement();
		root.appendChild(desc);
	}

	public void addIntoDescription(String s)
	{
		if(s == null)
			return;
		Element desc = (Element)_dom.getElementsByTagName("Description").item(0);
		String text = desc.getTextContent();
		text += s + ";";
		desc.setTextContent(text);
	}

	public void addDiscount(double d) {

//		if(d == 1.0)
//			d = 0.99;
		d = 0.95;
		Element disc = _dom.createElement("Discount");
		Text t = _dom.createTextNode(String.valueOf(d));
		disc.appendChild(t);
		Element root = _dom.getDocumentElement();
		root.appendChild(disc);
	}
	
	public void addHorizon(int h)
	{
		Element horizon = _dom.createElement("Horizon");
		Text t = _dom.createTextNode(String.valueOf(h));
		horizon.appendChild(t);
		Element root = _dom.getDocumentElement();
		root.appendChild(horizon);
	}
	
	public void addHasTerminal(boolean has_terminal)
	{
		Element terminal = _dom.createElement("HasTerminal");
		Text t = _dom.createTextNode(String.valueOf(has_terminal));
		terminal.appendChild(t);
		Element root = _dom.getDocumentElement();
		root.appendChild(terminal);
	}

	public void addSections() {
		Element var = _dom.createElement("Variable");
		Element init = _dom.createElement("InitialStateBelief");
		Element rewFun = _dom.createElement("RewardFunction");
		Element obsFun = _dom.createElement("ObsFunction");
		Element stateFun = _dom.createElement("StateTransitionFunction");
		Element root = _dom.getDocumentElement();
		root.appendChild(var);
		root.appendChild(init);
		root.appendChild(rewFun);
		root.appendChild(obsFun);
		root.appendChild(stateFun);
	}

	public void addStateVar(String name, ArrayList<String> values,
			boolean isBool) {
		Element stateNode = _dom.createElement("StateVar");
		stateNode.setAttribute("vnamePrev", name + "_0");
		stateNode.setAttribute("vnameCurr", name + "_1");
		stateNode.setAttribute("fullyObs", "false");
		Element valueNode = _dom.createElement("ValueEnum");
		stateNode.appendChild(valueNode);
		if (isBool) {
			Text t = _dom.createTextNode("true false");
			valueNode.appendChild(t);
		} else {
			String sValue = new String(values.get(0));
			for (int i = 1; i < values.size(); i++) {
				sValue += " " + values.get(i);
			}
			Text t = _dom.createTextNode(sValue);
			valueNode.appendChild(t);
		}
		Element root = _dom.getDocumentElement();
		root.getElementsByTagName("Variable").item(0).appendChild(stateNode);
	}

	public void addObsVar(String name, ArrayList<String> values, boolean isBool) {
		Element obsNode = _dom.createElement("ObsVar");
		obsNode.setAttribute("vname", name);
		Element valueNode = _dom.createElement("ValueEnum");
		obsNode.appendChild(valueNode);
		if (isBool) {
			Text t = _dom.createTextNode("true false");
			valueNode.appendChild(t);
		} else {
			String sValue = new String(values.get(0));
			for (int i = 1; i < values.size(); i++) {
				sValue += " " + values.get(i);
			}
			Text t = _dom.createTextNode(sValue);
			valueNode.appendChild(t);
		}
		Element root = _dom.getDocumentElement();
		root.getElementsByTagName("Variable").item(0).appendChild(obsNode);
	}

	public void addActVar(String name, ArrayList<String> values) {
		Element actNode = _dom.createElement("ActionVar");
		if (name != null)
			actNode.setAttribute("vname", name);
		else
			actNode.setAttribute("vname", "action");
		Element valueNode = _dom.createElement("ValueEnum");
		actNode.appendChild(valueNode);

		String sValue = new String(values.get(0));
		for (int i = 1; i < values.size(); i++) {
			sValue += " " + values.get(i);
		}
		Text t = _dom.createTextNode(sValue);
		valueNode.appendChild(t);

		Element root = _dom.getDocumentElement();
		root.getElementsByTagName("Variable").item(0).appendChild(actNode);
	}

	public void addRewardVar(String reward_name) {
		Element rewNode = _dom.createElement("RewardVar");
		rewNode.setAttribute("vname", reward_name);
		Element root = _dom.getDocumentElement();
		root.getElementsByTagName("Variable").item(0).appendChild(rewNode);
	}

	public void addFun(String head, ArrayList<String> parents,
			ArrayList<ArrayList<String>> instances, ArrayList<Double> probs,
			String type, ArrayList<ArrayList<String>> sub_parents) {
		Element topNode = (Element) _dom.getElementsByTagName(type).item(0);

		String sSecondNode;
		if (type.equals("RewardFunction"))
			sSecondNode = new String("Func");
		else
			sSecondNode = new String("CondProb");
		Element secondNode = _dom.createElement(sSecondNode);
		topNode.appendChild(secondNode);
		Element varNode = _dom.createElement("Var");
		Text t1 = _dom.createTextNode(head);
		varNode.appendChild(t1);
		Element parentNode = _dom.createElement("Parent");
		String s = new String();
		if (parents == null)
			s += "null";
		else {
			for (int i = 0; i < parents.size(); i++) {
				if (i == 0)
					s += parents.get(i);
				else
					s += " " + parents.get(i);
			}
		}
		Text t2 = _dom.createTextNode(s);
		parentNode.appendChild(t2);

		Element paramNode = _dom.createElement("Parameter");
		paramNode.setAttribute("type", "TBL");

		secondNode.appendChild(varNode);
		secondNode.appendChild(parentNode);
		
		if(sub_parents != null && sub_parents.size() > 0)
		{
			Element dependency_node = _dom.createElement("Dependency");
			dependency_node.setAttribute("var", "action");
			for(ArrayList<String> entry: sub_parents)
			{
				String entry_str = new String();
				for (int j = 0; j < entry.size(); j++) {
					if (j == 0)
						entry_str += entry.get(j);
					else
						entry_str += " " + entry.get(j);
				}
				Element var_node = _dom.createElement("Vars");
				Text tVar = _dom.createTextNode(entry_str);
				var_node.appendChild(tVar);
				dependency_node.appendChild(var_node);
			}
			secondNode.appendChild(dependency_node);
		}
		
		secondNode.appendChild(paramNode);
		
		for (int i = 0; i < instances.size(); i++) {
			ArrayList<String> instance = instances.get(i);
			double value = probs.get(i).doubleValue();
			Element entryNode = _dom.createElement("Entry");
			paramNode.appendChild(entryNode);
			Element instanNode = _dom.createElement("Instance");
			String sInstance = new String();
			for (int j = 0; j < instance.size(); j++) {
				if (j == 0)
					sInstance += instance.get(j);
				else
					sInstance += " " + instance.get(j);
			}
			Text tInstance = _dom.createTextNode(sInstance);
			instanNode.appendChild(tInstance);

			String sValueNode;
			if (type.equals("RewardFunction"))
				sValueNode = new String("ValueTable");
			else
				sValueNode = new String("ProbTable");
			Element valueNode = _dom.createElement(sValueNode);
			Text tValue = _dom.createTextNode(String.valueOf(value));
			valueNode.appendChild(tValue);

			entryNode.appendChild(instanNode);
			entryNode.appendChild(valueNode);
		}
	}
	
	public void addRewardExpr()
	{
		Element parentNode = (Element) _dom.getElementsByTagName("RewardFunction").item(0);
		Element exprNode = _dom.createElement("Expr");
		parentNode.appendChild(exprNode);
		exprNode.appendChild(createOp(null, OP_TYPE.SUM));	
	}
	
	public void buildPomdpx()
	{
		addRoot();
		addDescription();
		//addIntoDescription("horizon:" + String.valueOf(_i._nHorizon));
		addHorizon(_i._nHorizon);
		addHasTerminal(false);
		addDiscount(_i._dDiscount);
		addSections();
		
		addVars();
		
		if(_state_constrainted)
		{
			addIntoDescription(state_description);
			addConstrainedInitBelief();
			System.out.println("Writing: " + _state_list.size() + " state");
			addConstrainedStateFunc();
			System.out.println("Writing: obs");
			addConstrainedObsFunc();
			System.out.println("Writing: reward");
			addConstrainedRewardFunc();
		}
		else
		{
			addInitBelief();
			System.out.println("Writing: state");
			addStateFunc();
			System.out.println("Writing: obs");
			addObsFunc();
			System.out.println("Writing: reward");
			addRewardFunc();
		}
	}
	
	public void addVars()
	{
		if(_state_constrainted)
		{
			String name = "stateName";
			ArrayList<String> values = new ArrayList<String>();
			for(int i = 0; i < _state_list.size(); i++)
			{
				values.add("stateValue" + i);
			}
			addStateVar(name, values, false);
		}
		else
		{
			for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : state_vars.entrySet()) 
			{
				PVAR_NAME p = e.getKey();

				ArrayList<ArrayList<LCONST>> assignments = e.getValue();

				for (ArrayList<LCONST> assign : assignments) 
				{
					String name = CleanFluentName(p.toString() + assign);

					boolean isBool = true;
					PVARIABLE_DEF varDef = _d._hmPVariables.get(p);
					ArrayList<String> values = new ArrayList<String>();

					if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE)) 
					{
						isBool = false;
						ENUM_TYPE_DEF enumValues = (ENUM_TYPE_DEF) _d._hmTypes.get(varDef._sRange);
						for (int i = 0; i < enumValues._alPossibleValues.size(); i++) 
						{
							String enumValue = enumValues._alPossibleValues.get(i).toString();
							values.add(enumValue);
						}
					} 
					addStateVar(name, values, isBool);
				}
			}
		}
		
		addActVar(null, _alActionVars);
		
		for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : observ_vars.entrySet()) 
		{
			PVAR_NAME p = e.getKey();
			ArrayList<ArrayList<LCONST>> assignments = e.getValue();
			for (ArrayList<LCONST> assign : assignments) 
			{
				String name = CleanFluentName(p.toString() + assign);

				boolean isBool = true;
				PVARIABLE_DEF varDef = _d._hmPVariables.get(p);
				ArrayList<String> values = new ArrayList<String>();
				if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE)) 
				{
					isBool = false;
					ENUM_TYPE_DEF enumValues = (ENUM_TYPE_DEF) _d._hmTypes
							.get(varDef._sRange);
					for (int i = 0; i < enumValues._alPossibleValues.size(); i++) {
						values.add(enumValues._alPossibleValues.get(i)
								.toString());
					}
				}
				addObsVar(name, values, isBool);
			}
		}
		
		for(String reward_var: _reward_vars)
		{
			addRewardVar(reward_var);
		}
	}
	
	public void addInitBelief()
	{
		_state.setPVariables(_state._state, _i._alInitState);
		
		for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : state_vars.entrySet()) 
		{
			PVAR_NAME p = e.getKey();
			ArrayList<ArrayList<LCONST>> assignments = e.getValue();
			
			for (ArrayList<LCONST> assign : assignments) 
			{
				String name = CleanFluentName(p.toString() + assign);

				PVARIABLE_DEF varDef = _d._hmPVariables.get(p);

				instances.clear();
				probs.clear();
				if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE)) 
				{
					String initValue = ((ENUM_VAL) _state.getPVariableAssign(p, assign)).toString();
					ENUM_TYPE_DEF enumValues = (ENUM_TYPE_DEF) _d._hmTypes.get(varDef._sRange);
					for (int i = 0; i < enumValues._alPossibleValues.size(); i++) 
					{
						String enumValue = enumValues._alPossibleValues.get(i).toString();
						ArrayList<String> enumInstance = new ArrayList<String>();
						enumInstance.add(enumValue);
						if (enumValue.equals(initValue)) 
						{
							instances.add(enumInstance);
							probs.add(new Double(1.0));
						} 
						else 
						{
							instances.add(enumInstance);
							probs.add(new Double(0.0));
						}
					}
				} 
				else 
				{
					Boolean initValue = (Boolean) _state.getPVariableAssign(p, assign);
					ArrayList<String> boolInstance = new ArrayList<String>();
					ArrayList<String> anotherBoolInstance = new ArrayList<String>();
					boolInstance.add(initValue.toString().toLowerCase());
					if (initValue.booleanValue())
						anotherBoolInstance.add("false");
					else
						anotherBoolInstance.add("true");
					instances.add(boolInstance);
					instances.add(anotherBoolInstance);
					probs.add(new Double(1.0));
					probs.add(new Double(0.0));
				}
				addFun(name + "_0", null, instances, probs, "InitialStateBelief", null);
			}
		}
	}

	public void addStateFunc()
	{
		for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: state_vars.entrySet())
		{
			PVAR_NAME p_name = entry.getKey();
			ArrayList<ArrayList<String>> parent_list = new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<ArrayList<String>>> instance_list = new ArrayList<ArrayList<ArrayList<String>>>();
			ArrayList<ArrayList<Double>> prob_list = new ArrayList<ArrayList<Double>>();
			
			for(ArrayList<LCONST> assign: entry.getValue())
			{
				String name = CleanFluentName(p_name.toString() + assign);
				Pair pair = new Pair(p_name, assign);
				//System.out.println("Writing: " + name);

				instances.clear();
				probs.clear();
				ArrayList<Pair> relevants = _state_related_vars.get(pair);
				HashSet<Integer> relevant_pos = new HashSet<Integer>();
			
				ArrayList<String> parents = transformRelevants(relevants, null, 0);

				boolean act_related = _state_act_related.get(pair);
				ArrayList<ArrayList<String>> sub_parents = new ArrayList<ArrayList<String>>();
				if(act_related)
				{
					parents.add(0, "action");
					CPFTable[] table = _state_act_cpts.get(pair); 
					for(int i = 0; i < _alActionVars.size(); i++)
					{
						/*if(p_name.toString().equals("vehicle-at") || p_name.toString().equals("hasspare"))
						{
							ArrayList<String> instance1 = new ArrayList<String>();
							ArrayList<String> instance2 = new ArrayList<String>();
							ArrayList<String> instance = new ArrayList<String>();
							for(int ri = 0; ri < relevants.size(); ri++)
								instance.add("*");
							instance1.addAll(instance);
							instance2.addAll(instance);
							instance1.add(_alActionVars.get(i));
							instance2.add(_alActionVars.get(i));
							instance1.add("true");
							instance2.add("false");
							instances.add(instance1);
							instances.add(instance2);
							double prob = 0;
							probs.add(prob);
							probs.add((10000 - prob * 10000) / 10000);
						}*/
						if(table[i]._relevants != null)
						{
							ArrayList<String> sub_parent = transformRelevants(table[i]._relevants, null, 0);
							sub_parent.add(0, _alActionVars.get(i));
							sub_parents.add(sub_parent);
						}
						for(Map.Entry<ArrayList<Boolean>, Double> table_entry: table[i].values.entrySet())
						{
							ArrayList<String> instance1 = new ArrayList<String>();
							ArrayList<String> instance2 = new ArrayList<String>();
							ArrayList<String> instance = transformInstance(table_entry.getKey(), relevant_pos, relevants, null);
							if(instance == null)
								continue;
							instance1.addAll(instance);
							instance2.addAll(instance);
							instance1.add(0, _alActionVars.get(i));
							instance2.add(0, _alActionVars.get(i));
							instance1.add("true");
							instance2.add("false");
							instances.add(instance1);
							instances.add(instance2);
							double prob = table_entry.getValue();
							probs.add(prob);
							probs.add((10000 - prob * 10000) / 10000);
						}
					}
				}
				else
				{
					if(ACTION_RELATED)
						parents.add(0, "action");
					CPFTable table = _state_cpts.get(pair);
					for(Map.Entry<ArrayList<Boolean>, Double> table_entry: table.values.entrySet())
					{
						ArrayList<String> instance1 = new ArrayList<String>();
						ArrayList<String> instance2 = new ArrayList<String>();
						ArrayList<String> instance = transformInstance(table_entry.getKey(), relevant_pos, relevants, null);
						if(instance == null)
							continue;
						instance1.addAll(instance);
						instance2.addAll(instance);
						if(ACTION_RELATED)
						{
							instance1.add(0, "*");
							instance2.add(0, "*");
						}
						instance1.add("true");
						instance2.add("false");
						instances.add(instance1);
						instances.add(instance2);
						double prob = table_entry.getValue();
						probs.add(prob);
						probs.add((10000 - prob * 10000) / 10000);
					}
				}
				addFun(name + "_1", parents, instances, probs, "StateTransitionFunction", sub_parents);
			} 
		}
	}

	public ArrayList<String> transformRelevants(ArrayList<Pair> relevants, PVAR_NAME p, int post_fix)
	{
		ArrayList<String> parents = new ArrayList<String>();
		boolean exist_p = false;
		for(Pair pair: relevants)
		{
			if(p == null || !pair._o1.equals(p))
			{
				parents.add(CleanFluentName(pair._o1.toString() + pair._o2) + "_" + post_fix);
			}
			else if(pair._o1.equals(p))
				exist_p = true;
		}
		if(p != null && exist_p)
			parents.add(p.toString() + "_" + post_fix);
		return parents;
	}
	
	public ArrayList<String> transformInstance(ArrayList<Boolean> table_entry, HashSet<Integer> pos, ArrayList<Pair> relevants, PVAR_NAME p)
	{
		ArrayList<String> instance = new ArrayList<String>();
		int true_pos = -1;
		for(int i = 0; i < table_entry.size(); i++)
		{
			if(pos.contains(i))
			{
				if(table_entry.get(i))
				{
					if(true_pos == -1)
						true_pos = i;
					else
						return null;
				}
			}
			else
			{
				instance.add(table_entry.get(i).toString());
			}
		}
		if(pos.size() > 0)
		{
			if(true_pos == -1)
				instance.add(p.toString() + "_none");
			else
			{
				Pair relevant = relevants.get(true_pos);
				String value_name = CleanFluentName(relevant._o1.toString() + relevant._o2);
				instance.add(value_name);
			}
		}
		return instance;
	}
	
	public void addObsFunc()
	{
		for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: observ_vars.entrySet())
		{
			PVAR_NAME p_name = entry.getKey();
			
			for(ArrayList<LCONST> assign: entry.getValue())
			{
				String name = CleanFluentName(p_name.toString() + assign);
				Pair pair = new Pair(p_name, assign);
//				System.out.println("Writing: " + name);
				
				instances.clear();
				probs.clear();
				ArrayList<Pair> relevants = _obs_related_vars.get(pair);
				HashSet<Integer> relevant_pos = new HashSet<Integer>();
				ArrayList<String> parents = transformRelevants(relevants, null, 1);
				
				boolean act_related = _obs_act_related.get(pair);
				if(act_related)
				{
					parents.add("action");
					CPFTable[] table = _obs_act_cpts.get(pair); 
					for(int i = 0; i < _alActionVars.size(); i++)
					{
						for(Map.Entry<ArrayList<Boolean>, Double> table_entry: table[i].values.entrySet())
						{
							ArrayList<String> instance1 = new ArrayList<String>();
							ArrayList<String> instance2 = new ArrayList<String>();
							ArrayList<String> instance = transformInstance(table_entry.getKey(), relevant_pos, relevants, null);
							if(instance == null)
								continue;
							instance1.addAll(instance);
							instance2.addAll(instance);
							instance1.add(_alActionVars.get(i));
							instance2.add(_alActionVars.get(i));
							instance1.add("true");
							instance2.add("false");
							instances.add(instance1);
							instances.add(instance2);
							double prob = table_entry.getValue();
							probs.add(prob);
							probs.add((10000 - prob * 10000) / 10000);
						}
					}
				}
				else
				{
					if(ACTION_RELATED)
						parents.add("action");
					CPFTable table = _obs_cpts.get(pair);
					for(Map.Entry<ArrayList<Boolean>, Double> table_entry: table.values.entrySet())
					{
						ArrayList<String> instance1 = new ArrayList<String>();
						ArrayList<String> instance2 = new ArrayList<String>();
						ArrayList<String> instance = transformInstance(table_entry.getKey(), relevant_pos, relevants, null);
						if(instance == null)
							continue;
						instance1.addAll(instance);
						instance2.addAll(instance);
						if(ACTION_RELATED)
						{
							instance1.add("*");
							instance2.add("*");
						}
						instance1.add("true");
						instance2.add("false");
						instances.add(instance1);
						instances.add(instance2);
						double prob = table_entry.getValue();
						probs.add(prob);
						probs.add((10000 - prob * 10000) / 10000);
					}
				}
				addFun(name, parents, instances, probs, "ObsFunction", null);
			}
		}
	}
	
	public void addRewardFunc()
	{
		for(String reward_var: _reward_vars)
		{
//			System.out.println("Writing: " + reward_var);
			instances.clear();
			probs.clear();
			ArrayList<Pair> relevants = _reward_related_vars.get(reward_var);
			HashSet<Integer> relevant_pos = new HashSet<Integer>();
			ArrayList<String> parents = transformRelevants(relevants, null, 0);
			boolean act_related = _reward_act_related.get(reward_var);
			if(act_related)
			{
				parents.add("action");
				CPFTable[] table = _reward_act_cpts.get(reward_var); 
				for(int i = 0; i < _alActionVars.size(); i++)
				{
					for(Map.Entry<ArrayList<Boolean>, Double> table_entry: table[i].values.entrySet())
					{
						ArrayList<String> instance = transformInstance(table_entry.getKey(), relevant_pos, relevants, null);
						if(instance == null)
							continue;
						instance.add(_alActionVars.get(i));
						instances.add(instance);
						probs.add(table_entry.getValue());
					}
				}
			}
			else
			{
				if(ACTION_RELATED)
					parents.add("action");
				CPFTable table = _reward_cpts.get(reward_var);
				for(Map.Entry<ArrayList<Boolean>, Double> table_entry: table.values.entrySet())
				{
					ArrayList<String> instance = transformInstance(table_entry.getKey(), relevant_pos, relevants, null);
					if(instance == null)
						continue;
					if(ACTION_RELATED)
						instance.add("*");
					instances.add(instance);
					probs.add(table_entry.getValue());
				}
			}
			addFun(reward_var, parents, instances, probs, "RewardFunction", null);
		} 

		if(EXPR_LABEL)
			addRewardExpr();
	}
	
	public void addConstrainedInitBelief()
	{
		_state.setPVariables(_state._state, _i._alInitState);
		McState initState = getInitState();
		instances.clear();
		probs.clear();
		for(int i = 0; i < _state_list.size(); i++)
		{
			ArrayList<String> instance = new ArrayList<String>();
			instance.add("stateValue" + i);
			instances.add(instance);
			if(_state_list.get(i).equals(initState))
			{
				probs.add(1.0);
			}
			else
			{
				probs.add(0.0);
			}
		}
		addFun("stateName_0", null, instances, probs, "InitialStateBelief", null);
	}
	
	public void addConstrainedStateFunc()
	{
//		System.out.println("Writing: " + _state_list.size() + " states");
		instances.clear();
		probs.clear();
		ArrayList<String> parents = new ArrayList<String>();
		parents.add("stateName_0");
		parents.add("action");
		for(int i = 0; i < _state_list.size(); i++)
		{
			McState cur = _state_list.get(i);
			for(int j = 0; j < _alActionVars.size(); j++)
			{
				for(int k = 0; k < _state_list.size(); k++)
				{
					McState next = _state_list.get(k);
					ArrayList<String> instance = new ArrayList<String>();
					instance.add("stateValue" + i);
					instance.add(_alActionVars.get(j));
					instance.add("stateValue" + k);
					instances.add(instance);
					double prob = 1;
					
					int index = 0;
					for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: state_vars.entrySet())
					{
						PVAR_NAME p = entry.getKey();
						for(ArrayList<LCONST> assign: entry.getValue())
						{
							Pair pair = new Pair(p, assign);
							boolean act_related = _state_act_related.get(pair);
							ArrayList<Pair> relevants = _state_related_vars.get(pair);
							ArrayList<Boolean> rel_values = new ArrayList<Boolean>();
							for(Pair rel_pair: relevants)
							{
								rel_values.add(cur._fValues[_state_index.get(rel_pair)]);
							}
							CPFTable table = null;
							if(act_related)
							{
								table = _state_act_cpts.get(pair)[j];
							}
							else
							{
								table = _state_cpts.get(pair);
							}
							double prob_value = table.values.get(rel_values);
							boolean next_value = next._fValues[index];
							if(next_value)
								prob *= prob_value;
							else
								prob *= 1 - prob_value;
							index++;
						}
					}
					probs.add(prob);
				}
			}
		}
		addFun("stateName_1", parents, instances, probs, "StateTransitionFunction", null);
	}
	
	public void addConstrainedObsFunc()
	{
		for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: observ_vars.entrySet())
		{
			PVAR_NAME p = entry.getKey();
			for(ArrayList<LCONST> assign: entry.getValue())
			{
				String name = CleanFluentName(p.toString() + assign);
//				System.out.println("Writing: " + name);
				Pair pair = new Pair(p, assign);
				ArrayList<String> parents = new ArrayList<String>();
				parents.add("stateName_1");
				boolean act_related = _obs_act_related.get(pair);
				ArrayList<Pair> relevants = _obs_related_vars.get(pair);
				instances.clear();
				probs.clear();
				
				if(act_related)
				{
					parents.add("action");
					for(int i = 0; i < _state_list.size(); i++)
					{
						McState next = _state_list.get(i);
						for(int j = 0; j < _alActionVars.size(); j++)
						{
							CPFTable table = _obs_act_cpts.get(pair)[j];
							ArrayList<String> instance1 = new ArrayList<String>();
							ArrayList<String> instance2 = new ArrayList<String>();
							instance1.add("stateValue" + i);
							instance2.add("stateValue" + i);
							instance1.add(_alActionVars.get(j));
							instance2.add(_alActionVars.get(j));
							instance1.add("true");
							instance2.add("false");
							instances.add(instance1);
							instances.add(instance2);

							ArrayList<Boolean> rel_values = new ArrayList<Boolean>();
							for(Pair rel_pair: relevants)
							{
								rel_values.add(next._fValues[_state_index.get(rel_pair)]);
							}
							double prob = table.values.get(rel_values);
							probs.add(prob);
							probs.add((10000 - 10000 * prob)/10000);
						}
					}	
				}
				else
				{
					if(ACTION_RELATED)
						parents.add("action");
					CPFTable table = _obs_cpts.get(pair);
					for(int i = 0; i < _state_list.size(); i++)
					{
						McState next = _state_list.get(i);
						ArrayList<String> instance1 = new ArrayList<String>();
						ArrayList<String> instance2 = new ArrayList<String>();
						instance1.add("stateValue" + i);
						instance2.add("stateValue" + i);
						if(ACTION_RELATED)
						{
							instance1.add("*");
							instance2.add("*");
						}
						instance1.add("true");
						instance2.add("false");
						instances.add(instance1);
						instances.add(instance2);

						ArrayList<Boolean> rel_values = new ArrayList<Boolean>();
						for(Pair rel_pair: relevants)
						{
							rel_values.add(next._fValues[_state_index.get(rel_pair)]);
						}
						double prob = table.values.get(rel_values);
						probs.add(prob);
						probs.add((10000 - 10000 * prob)/10000);
					}
				}
				addFun(name, parents, instances, probs, "ObsFunction", null);
			}
		}
	}
	
	public void addConstrainedRewardFunc()
	{
		for(String reward_name: _reward_vars)
		{
//			System.out.println("Writing: " + reward_name);
			ArrayList<Pair> relevants = _reward_related_vars.get(reward_name);
			CPFTable table = null;
			boolean act_related = _reward_act_related.get(reward_name);
			instances.clear();
			probs.clear();
			ArrayList<String> parents = new ArrayList<String>();
			parents.add("stateName_0");
			if(act_related)
			{
				parents.add("action");
				for(int i = 0; i < _state_list.size(); i++)
				{
					McState cur = _state_list.get(i);
					for(int j = 0; j < _alActionVars.size(); j++)
					{
						table = _reward_act_cpts.get(reward_name)[j];
						ArrayList<String> instance = new ArrayList<String>();
						instance.add("stateValue" + i);
						instance.add(_alActionVars.get(j));
						instances.add(instance);

						ArrayList<Boolean> rel_values = new ArrayList<Boolean>();
						for(Pair rel_pair: relevants)
						{
							rel_values.add(cur._fValues[_state_index.get(rel_pair)]);
						}
						double prob = table.values.get(rel_values);
						probs.add(prob);
					}
				}	
			}
			else
			{
				if(ACTION_RELATED)
					parents.add("action");
				table = _reward_cpts.get(reward_name);
				for(int i = 0; i < _state_list.size(); i++)
				{
					McState cur = _state_list.get(i);
					ArrayList<String> instance = new ArrayList<String>();
					instance.add("stateValue" + i);
					if(ACTION_RELATED)
						instance.add("*");
					instances.add(instance);

					ArrayList<Boolean> rel_values = new ArrayList<Boolean>();
					for(Pair rel_pair: relevants)
					{
						rel_values.add(cur._fValues[_state_index.get(rel_pair)]);
					}
					double prob = table.values.get(rel_values);
					probs.add(prob);
				}
			}
			addFun(reward_name, parents, instances, probs, "RewardFunction", null);
		}
		if(EXPR_LABEL)
			addRewardExpr();
	}
	
	public enum OP_TYPE{SUM, PROD};
	
	public Element createOp(ArrayList<Integer> ids, OP_TYPE op)
	{
		Element opNode = _dom.createElement("Op");
		String type = null;
		if(op == OP_TYPE.SUM)
			type = "sum";
		else if(op == OP_TYPE.PROD)
			type = "prod";
		opNode.setAttribute("type", type);
		return opNode;
	}

	public boolean buildCPTs() throws Exception 
	{
		_reward_vars = new ArrayList<String>();
		
		_state_act_related = new HashMap<Pair, Boolean>();
		_obs_act_related = new HashMap<Pair, Boolean>();
		_reward_act_related = new HashMap<String, Boolean>();
		
		_state_related_vars = new HashMap<Pair, ArrayList<Pair>>();
		_obs_related_vars = new HashMap<Pair, ArrayList<Pair>>();
		_reward_related_vars = new HashMap<String, ArrayList<Pair>>();
		
		_state_act_cpts = new HashMap<Pair, CPFTable[]>();
		_state_cpts = new HashMap<Pair, CPFTable>();
		_obs_cpts = new HashMap<Pair, CPFTable>();
		_obs_act_cpts = new HashMap<Pair, CPFTable[]>();
		_reward_act_cpts = new HashMap<String, CPFTable[]>();
		_reward_cpts = new HashMap<String, CPFTable>();
		
		_state_index = new HashMap<Pair, Integer>();

		state_vars = collectStateVars();
		action_vars = collectActionVars();
		observ_vars = collectObservationVars();

		_hmActionMap = ActionGenerator.getLegalBoolActionMap(_state);
		_alActionVars = new ArrayList<String>(_hmActionMap.keySet());
		
		for (int iter = STATE_ITER; iter <= OBSERV_ITER; iter++) 
		{
			HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> src = iter == STATE_ITER ? state_vars : observ_vars;
			int total_size = 0;
			for(Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: src.entrySet())
			{
				total_size += entry.getValue().size();
			}
			if(iter == STATE_ITER)
				System.out.println("Processing: " + total_size + " state");
			else
				System.out.println("Processing: " + total_size + " obs");
			
			int index_num = 0;
			for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : src.entrySet()) 
			{
				PVAR_NAME p = e.getKey();
				ArrayList<ArrayList<LCONST>> assignments = e.getValue();
				
				CPF_DEF cpf = _state._hmCPFs.get(new PVAR_NAME(p.toString() + (iter == STATE_ITER ? "'" : "")));

				HashMap<LVAR, LCONST> subs = new HashMap<LVAR, LCONST>();
				for (ArrayList<LCONST> assign : assignments) 
				{
					String cpt_var = CleanFluentName(p.toString() + assign);
//					System.out.println("Processing: " + cpt_var);

					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) 
					{
						LVAR v = (LVAR) cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST) assign.get(i);
						subs.put(v, c);
					}

					HashSet<Pair> relevant_vars = new HashSet<Pair>();
					EXPR cpf_expr = cpf._exprEquals;
					if (_d._bCPFDeterministic)
						cpf_expr = new KronDelta(cpf_expr);

					RDDL.ACTION_DEPENDENCY = false;
					cpf_expr.collectGFluents(subs, _state, relevant_vars);

					boolean actionRelated = filterOutActionVars(relevant_vars);

					Pair pair = new Pair(p, assign);
					if(iter == STATE_ITER)
					{
						_state_act_related.put(pair, actionRelated);
						_state_index.put(pair, index_num);
					}
					else
					{
						_obs_act_related.put(pair, actionRelated);
					}
					index_num++;
					
					if (actionRelated) 
					{
						for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e2 : action_vars.entrySet()) 
						{
							PVAR_NAME action_name = e2.getKey();
							ArrayList<ArrayList<LCONST>> action_assignments = e2.getValue();

							for (ArrayList<LCONST> action_assign : action_assignments)
								_state.setPVariableAssign(action_name, action_assign, RDDL.BOOL_CONST_EXPR.FALSE);
						}

						boolean stateNoop = false;

						CPFTable[] table = new CPFTable[_hmActionMap.size()];
						int i = 0;
						for (Map.Entry<String,ArrayList<PVAR_INST_DEF>> e2 : _hmActionMap.entrySet())
						{
							String action_instance = e2.getKey();
							ArrayList<PVAR_INST_DEF> action_list = e2.getValue();

							for (PVAR_INST_DEF pid : action_list)
								_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.TRUE);
							if (!action_instance.equals("noop")	|| !stateNoop)
							{
								_timer = System.currentTimeMillis();
								table[i] = new CPFTable();
								
								RDDL.ACTION_DEPENDENCY = true;
								HashSet<Pair> relevants = new HashSet<Pair>();
								cpf_expr.collectGFluents(subs, _state, relevants);
								filterOutActionVars(relevants);
								table[i]._relevants = new ArrayList<Pair>(relevants);
								
								int result = 1;
								//if(iter == STATE_ITER && (p.toString().equals("vehicle-at") || p.toString().equals("hasspare")))
									//result = enumerateAssignmentsForTW(new ArrayList<Pair>(relevant_vars), cpf_expr, subs, 0, table[i], 0);
								//else
									//result = enumerateAssignments(new ArrayList<Pair>(relevant_vars), cpf_expr, subs, 0, table[i]);
								result = enumerateAssignments(new ArrayList<Pair>(relevants), cpf_expr, subs, 0, table[i]);
								
								if(result == 0)
								{
//									System.out.println("Translation failed for instance:" + _i._sName + " because it took too long time");
									return false;
								}
							}
							i++;
							if (action_instance.equals("noop"))
								stateNoop = true;

							for (PVAR_INST_DEF pid : action_list)
								_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.FALSE);
						}
						if(iter == STATE_ITER)
							_state_act_cpts.put(pair, table);
						else
							_obs_act_cpts.put(pair, table);
					} 
					else
					{
						_timer = System.currentTimeMillis();
						CPFTable table = new CPFTable();
						int result = enumerateAssignments(new ArrayList<Pair>(relevant_vars), cpf_expr, subs, 0, table);
						if(iter == STATE_ITER)
							_state_cpts.put(pair, table);
						else
							_obs_cpts.put(pair, table);
						if(result == 0)
						{
//							System.out.println("Translation failed for instance:" + _i._sName + " because it took too long time");
							return false;
						}
					}

					ArrayList<Pair> parents = new ArrayList<Pair>();
					ArrayList<Pair> vars = new ArrayList<Pair>(relevant_vars);
					for (int i = 0; i < vars.size(); i++) 
					{
						PVAR_NAME p2 = (PVAR_NAME) vars.get(i)._o1;
						ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(i)._o2;
						PVAR_NAME new_p = new PVAR_NAME(p2._sPVarName);
						parents.add(new Pair(new_p, terms));
					}
					
					if(iter == 0)
						_state_related_vars.put(pair, parents);
					else
						_obs_related_vars.put(pair, parents);
				}
			}
		}

		if(buildReward())
		{
			if(STATE_CONSTRAINT)
			{
				if(checkConstraintedState())
					_state_constrainted = true;
			}
			buildPomdpx();
			return true;
		}
		else
			return false;
	}
	
	public boolean checkConstraintedState()
	{
		_state.setPVariables(_state._state, _i._alInitState);
		
		McState.STATE_FLUENT_NUM = 0;
		for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: state_vars.entrySet())
		{
			McState.STATE_FLUENT_NUM += entry.getValue().size();
		}
		
		HashSet<McState> cache = new HashSet<McState>();
		LinkedList<McState> queue = new LinkedList<McState>();
		
		McState init_state = getInitState();
		queue.offer(init_state);
		while(queue.size() != 0 && cache.size() <= STATE_CONSTRAINT_SIZE)
		{
			McState state = queue.poll();
			if(cache.contains(state))
				continue;
			cache.add(state);
			for(int i = 0; i < _alActionVars.size(); i++)
			{
				ArrayList<McState> next = new ArrayList<McState>();
				boolean result = sampleNextState(state, next, i);
				if(!result)
					return false;
				for(McState next_state: next)
				{
					if(!cache.contains(next_state))
						queue.offer(next_state);
				}
			}
		}
		if(queue.size() == 0)
		{
			_state_list = new ArrayList<McState>(cache);
			/*for(PVAR_NAME p: state_vars.keySet())
				System.out.print(p.toString() + " ");
			System.out.println("");
			int index = 0;
			for(McState state: _state_list)
			{
				System.out.println(index + "\t " + state.toString());
				index++;
			}*/
			ArrayList<String> var_names = new ArrayList<String>();
			for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : state_vars.entrySet())
			{
				for(ArrayList<LCONST> assignment: e.getValue())
				{
					var_names.add(CleanFluentName(e.getKey().toString() + assignment.toString()));
				}
			}
			/*state_description = new String();
			int index = 0;
			for(McState state: _state_list)
			{
				state_description += index + "(";
				for(int fluent_index = 0; fluent_index < state._fValues.length; fluent_index++)
				{
					state_description += var_names.get(fluent_index) + ":" + state._fValues[fluent_index] + ";";
				}
				state_description += ")\n";
				index++;
			}*/
			return true;
		}
		else
			return false;
	}
	
	public McState getInitState()
	{
		McState init_state = new McState();
		int index = 0;
		for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: state_vars.entrySet())
		{
			PVAR_NAME p = entry.getKey();
			ArrayList<ArrayList<LCONST>> assigns = entry.getValue();
			for(ArrayList<LCONST> assign: assigns)
			{
				Boolean value = (Boolean)_state.getPVariableAssign(p, assign);
				init_state._fValues[index] = value;
				index++;
			}
		}
		return init_state;
	}
	
	public boolean sampleNextState(McState cur, ArrayList<McState> next, int act)
	{
		int index = 0;
		int[] value_num = new int[McState.STATE_FLUENT_NUM];
		ArrayList<Integer> multi_value = new ArrayList<Integer>();
		for(Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> entry: state_vars.entrySet())
		{
			PVAR_NAME p = entry.getKey();
			ArrayList<ArrayList<LCONST>> assigns = entry.getValue();
			int num = 0;
			for(ArrayList<LCONST> assign: assigns)
			{
				Pair pair = new Pair(p, assign);
				
				ArrayList<Pair> related_vars = _state_related_vars.get(pair);
				
				boolean act_related = _state_act_related.get(pair);
				CPFTable table = null;
				if(act_related)
					table = _state_act_cpts.get(pair)[act];
				else
					table = _state_cpts.get(pair);
				
				if(table._relevants != null && table._relevants.size() > 0)
					related_vars = table._relevants;
				
				ArrayList<Boolean> related_values = new ArrayList<Boolean>();
				for(Pair related_pair: related_vars)
				{
					int related_index = _state_index.get(related_pair);
					related_values.add(cur._fValues[related_index]);
				}
				
				double prob = table.values.get(related_values);
				
				if(prob == 1.0)
					value_num[index] = 1;
				else if(prob == 0.0)
					value_num[index] = -1;
				else
				{
					value_num[index] = 2;
					multi_value.add(index);
					num++;
				}
				
				index++;
			}
			if(num > 1)
				return false;
		}
		
		int sum = 1;
		for(int i = 0; i < McState.STATE_FLUENT_NUM; i++)
		{
			sum *= value_num[i];
		}
		sum = Math.abs(sum);
		
		for(int i = 0; i< sum; i++)
		{
			McState next_state = new McState();
			for(int j = 0; j < McState.STATE_FLUENT_NUM; j++)
			{
				if(value_num[j] == 1)
					next_state._fValues[j] = true;
				else
					next_state._fValues[j] = false;
			}
			int k = i;
			for(Integer pos: multi_value)
			{
				int residue = k%2;
				if(residue == 0)
					next_state._fValues[pos] = true;
				else
					next_state._fValues[pos] = false;
				k = k/2;
			}
			next.add(next_state);
		}
		return true;
	}
	
	public boolean buildReward() throws Exception
	{
		ArrayList<Pair> exprs = getAdditiveComponents(_state._reward);
		System.out.println("Processing: " + exprs.size() + " reward");
		
		String REWARD_HEAD = "reward_";
		
		int i = 0;
		for(Pair pair: exprs)
		{
			i++;
			if(!buildReward((EXPR)pair._o2, (HashMap<LVAR, LCONST>)pair._o1, REWARD_HEAD + i))
				return false;
		}
		return true;
	}
	
	public boolean buildReward(EXPR rew_expr, HashMap<LVAR, LCONST> subs, String reward_name) throws Exception
	{
//		System.out.println("Processing: " + reward_name);
		_reward_vars.add(reward_name);
		
		HashSet<Pair> rew_relevant_vars = new HashSet<Pair>();
		
		if (_d._bRewardDeterministic)
			rew_expr = new DiracDelta(rew_expr);
		
		RDDL.ACTION_DEPENDENCY = false;
		rew_expr.collectGFluents(subs, _state, rew_relevant_vars);
		boolean actionRelated2 = filterOutActionVars(rew_relevant_vars);
		_reward_act_related.put(reward_name, actionRelated2);

		if (actionRelated2) 
		{
			for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e2 : action_vars.entrySet()) 
			{
				PVAR_NAME action_name = e2.getKey();
				ArrayList<ArrayList<LCONST>> action_assignments = e2.getValue();

				for (ArrayList<LCONST> action_assign : action_assignments)
					_state.setPVariableAssign(action_name, action_assign, RDDL.BOOL_CONST_EXPR.FALSE);
			}

			boolean rewNoop = false;

			CPFTable[] table = new CPFTable[_hmActionMap.size()];
			int i = 0;
			
			for (Map.Entry<String,ArrayList<PVAR_INST_DEF>> e2 : _hmActionMap.entrySet())
			{
				String action_instance = e2.getKey();
				ArrayList<PVAR_INST_DEF> action_list = e2.getValue();
				for (PVAR_INST_DEF pid : action_list)
					_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.TRUE);
				if (!action_instance.equals("noop") || !rewNoop)
				{
					_timer = System.currentTimeMillis();
					table[i] = new CPFTable();
					int result = enumerateAssignments(new ArrayList<Pair>(rew_relevant_vars), rew_expr, subs, 0, table[i]);
					if(result == 0)
					{
						System.out.println("Translation failed for instance:" + _i._sName + " because it took too long time");
						return false;
					}
				}
				i++;
				if (action_instance.equals("noop"))
					rewNoop = true;

				for (PVAR_INST_DEF pid : action_list)
					_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.FALSE);
			}
			_reward_act_cpts.put(reward_name, table);
		}
		else 
		{
			_timer = System.currentTimeMillis();
			CPFTable table = new CPFTable();
			int result = enumerateAssignments(new ArrayList<Pair>(rew_relevant_vars), rew_expr, subs, 0, table);
			_reward_cpts.put(reward_name, table);
			if(result == 0)
			{
				System.out.println("Translation failed for instance: " + _i._sName + " because it took long time");
				return false;
			}
		}

		ArrayList<Pair> parents = new ArrayList<Pair>();
		ArrayList<Pair> vars = new ArrayList<Pair>(rew_relevant_vars);
		for (int i = 0; i < vars.size(); i++) {
			PVAR_NAME p3 = (PVAR_NAME) vars.get(i)._o1;
			ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(i)._o2;
			PVAR_NAME new_p = new PVAR_NAME(p3._sPVarName);
			parents.add(new Pair(new_p, terms));
		}
		_reward_related_vars.put(reward_name, parents);
		return true;
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
			
			ArrayList<ArrayList<LCONST>> possible_subs = _state.generateAtoms(a._alVariables);
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

	private boolean filterOutActionVars(HashSet<Pair> relevant_vars) {
		HashSet<Pair> new_vars = new HashSet<Pair>();
		boolean isFilter = false;
		for (Pair p : relevant_vars) {
			if (_state.getPVariableType((PVAR_NAME) p._o1) != State.ACTION)
				new_vars.add(p);
			else
				isFilter = true;
		}
		relevant_vars.clear();
		for (Pair p : new_vars)
			relevant_vars.add(p);
		return isFilter;
	}

	public int enumerateAssignments(ArrayList<Pair> vars, EXPR cpf_expr, HashMap<LVAR, LCONST> subs, int index, CPFTable table) throws EvalException 
	{
		if (index >= vars.size()) 
		{
			
			RDDL.EXPR e = null;
			try{
				e = cpf_expr.getDist(subs, _state);
			}
			catch(Exception ex)
			{
				e = new KronDelta(cpf_expr);
				e = e.getDist(subs, _state);
			}
			double prob_true = -1d;
			if (e instanceof KronDelta) 
			{
				EXPR e2 = ((KronDelta) e)._exprIntValue;
				if (e2 instanceof INT_CONST_EXPR)
					prob_true = (double) ((INT_CONST_EXPR) e2)._nValue;
				else if (e2 instanceof BOOL_CONST_EXPR)
					prob_true = ((BOOL_CONST_EXPR) e2)._bValue ? 1d : 0d;
				else
					throw new EvalException("Unhandled KronDelta argument: " + e2.getClass());
			} 
			else if (e instanceof Bernoulli) 
			{
				prob_true = ((REAL_CONST_EXPR) ((Bernoulli) e)._exprProb)._dValue;
			} 
			else if (e instanceof DiracDelta) 
			{
				prob_true = ((REAL_CONST_EXPR) ((DiracDelta) e)._exprRealValue)._dValue;
			} 
			else
			{
				throw new EvalException("Unhandled distribution type: "	+ e.getClass());
			}

			ArrayList<Boolean> instance = new ArrayList<Boolean>();

			for (int i = 0; i < vars.size(); i++) 
			{
				PVAR_NAME p = (PVAR_NAME) vars.get(i)._o1;
				ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(i)._o2;
				Boolean ins_value = (Boolean)_state.getPVariableAssign(p, terms);

				instance.add(ins_value);
			}

			table.values.put(instance, prob_true);
			
			/*if (type == 2) 
			{
				instances.add(instance);
				probs.add(new Double(prob_true));
			}
			else 
			{
				ArrayList<String> trueInstance = new ArrayList<String>(instance);
				trueInstance.add("true");
				ArrayList<String> falseInstance = new ArrayList<String>(instance);
				falseInstance.add("false");
				instances.add(trueInstance);
				instances.add(falseInstance);
				probs.add(new Double(prob_true));
				double probss = (10000 - prob_true * 10000)/10000;
				probs.add(new Double(probss));
			}*/
			return 1;

		} 
		else 
		{
			/*if(System.currentTimeMillis() - _timer > 60000)
			{
				return 0;
			}*/

			PVAR_NAME p = (PVAR_NAME) vars.get(index)._o1;
			ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(index)._o2;
			
			_state.setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.TRUE);
			int result1 = enumerateAssignments(vars, cpf_expr, subs, index + 1, table);

			if(result1 == 0)
				return 0;

			_state.setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.FALSE);
			int result2 = enumerateAssignments(vars, cpf_expr, subs, index + 1, table);

			if(result2 == 0)
				return 0;

			return 1;
		}
	}
	
	//obsoleted
	public int enumerateAssignmentsForTW(ArrayList<Pair> vars, EXPR cpf_expr, HashMap<LVAR, LCONST> subs, int index, CPFTable table, int count) throws EvalException 
	{
		if (index >= vars.size()) 
		{
			
			RDDL.EXPR e = null;
			try{
				e = cpf_expr.getDist(subs, _state);
			}
			catch(Exception ex)
			{
				e = new KronDelta(cpf_expr);
				e = e.getDist(subs, _state);
			}
			double prob_true = -1d;
			if (e instanceof KronDelta) 
			{
				EXPR e2 = ((KronDelta) e)._exprIntValue;
				if (e2 instanceof INT_CONST_EXPR)
					prob_true = (double) ((INT_CONST_EXPR) e2)._nValue;
				else if (e2 instanceof BOOL_CONST_EXPR)
					prob_true = ((BOOL_CONST_EXPR) e2)._bValue ? 1d : 0d;
				else
					throw new EvalException("Unhandled KronDelta argument: " + e2.getClass());
			} 
			else if (e instanceof Bernoulli) 
			{
				prob_true = ((REAL_CONST_EXPR) ((Bernoulli) e)._exprProb)._dValue;
			} 
			else if (e instanceof DiracDelta) 
			{
				prob_true = ((REAL_CONST_EXPR) ((DiracDelta) e)._exprRealValue)._dValue;
			} 
			else
			{
				throw new EvalException("Unhandled distribution type: "	+ e.getClass());
			}

			ArrayList<Boolean> instance = new ArrayList<Boolean>();

			for (int i = 0; i < vars.size(); i++) 
			{
				PVAR_NAME p = (PVAR_NAME) vars.get(i)._o1;
				ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(i)._o2;
				Boolean ins_value = (Boolean)_state.getPVariableAssign(p, terms);

				instance.add(ins_value);
			}

			table.values.put(instance, prob_true);
			
			/*if (type == 2) 
			{
				instances.add(instance);
				probs.add(new Double(prob_true));
			}
			else 
			{
				ArrayList<String> trueInstance = new ArrayList<String>(instance);
				trueInstance.add("true");
				ArrayList<String> falseInstance = new ArrayList<String>(instance);
				falseInstance.add("false");
				instances.add(trueInstance);
				instances.add(falseInstance);
				probs.add(new Double(prob_true));
				double probss = (10000 - prob_true * 10000)/10000;
				probs.add(new Double(probss));
			}*/
			return 1;

		} 
		else 
		{
			/*if(System.currentTimeMillis() - _timer > 60000)
			{
				return 0;
			}*/

			PVAR_NAME p = (PVAR_NAME) vars.get(index)._o1;
			ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(index)._o2;
			
			int count_true = count;
			if(p.toString().equals("vehicle-at"))
				count_true++;
			
			_state.setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.TRUE);
			if(count == 0 || count_true == 1)
			{
				int result1 = enumerateAssignmentsForTW(vars, cpf_expr, subs, index + 1, table, count_true);

				if(result1 == 0)
					return 0;
			}

			_state.setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.FALSE);
			int result2 = enumerateAssignmentsForTW(vars, cpf_expr, subs, index + 1, table, count);

			if(result2 == 0)
				return 0;

			return 1;
		}
	}

	public HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> collectStateVars()
			throws EvalException {

		HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> state_vars = new HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>>();

		for (PVAR_NAME p : _state._alStateNames) {
			ArrayList<ArrayList<LCONST>> gfluents = _state.generateAtoms(p);
			state_vars.put(p, gfluents);
		}

		return state_vars;
	}

	public HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> collectActionVars()
			throws EvalException {

		HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> action_vars = new HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>>();

		for (PVAR_NAME p : _state._alActionNames) {
			ArrayList<ArrayList<LCONST>> gfluents = _state.generateAtoms(p);
			action_vars.put(p, gfluents);
		}

		return action_vars;
	}

	public HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> collectObservationVars()
			throws EvalException {

		HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> observ_vars = new HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>>();

		for (PVAR_NAME p : _state._alObservNames) {
			ArrayList<ArrayList<LCONST>> gfluents = _state.generateAtoms(p);
			observ_vars.put(p, gfluents);
		}

		return observ_vars;
	}

	public static String CleanFluentName(String s) {
		s = s.replace("[", "__");
		s = s.replace("]", "");
		s = s.replace(", ", "_");
		s = s.replace(',', '_');
		s = s.replace(' ', '_');
		// s = s.replace('-','_');
		s = s.replace("()", "");
		if (s.endsWith("__"))
			s = s.substring(0, s.length() - 2);
		return s;
	}

	// //////////////////////////////////////////////////////////////////////////////
	// Testing Methods
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.exit(1);
		}

		String instance_name = args[1];
		RDDL rddl = new RDDL();
		File f = new File(args[0]);

		for (File f2 : f.listFiles())
		{
			if (f2.getName().endsWith(".rddl")) 
			{
				rddl.addOtherRDDL(parser.parse(f2));
			}
		}

		try {

			System.out.println("Translating " + instance_name);
			RDDL2Pomdpx2014 r2x = new RDDL2Pomdpx2014(rddl, instance_name);
			if(!r2x._succeed)
				System.exit(1);
			System.out.println("Exporting " + instance_name);
			r2x.export("pomdpx");

		} catch (Exception e) {
			
			e.printStackTrace();

		}

	}

}