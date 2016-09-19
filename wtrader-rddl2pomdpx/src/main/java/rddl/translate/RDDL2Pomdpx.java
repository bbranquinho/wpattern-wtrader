/*
 * Some of the code is derived from RDDL2Format.java 
 * 		by Scott Sanner and Sungwook Yoon
 */

package rddl.translate;

import java.io.*;
import java.util.*;

import rddl.*;
import rddl.RDDL.*;
import rddl.parser.*;
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

public class RDDL2Pomdpx {

	public Map<String,ArrayList<PVAR_INST_DEF>> _hmActionMap;

	public final static int STATE_ITER = 0;
	public final static int OBSERV_ITER = 1;

	public State _state;
	public INSTANCE _i;
	public NONFLUENTS _n;
	public DOMAIN _d;

	public ArrayList<String> _alActionVars;

	public Document _dom;

	public ArrayList<ArrayList<String>> instances;
	public ArrayList<Double> probs;

	public boolean _succeed;
	public long _timer;
	
	HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> state_vars;
	HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> action_vars;
	HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> observ_vars;
	
	public boolean ACTION_RELATED = true;
	public boolean MOMDP = false;
	public boolean EXPR_LABEL = false;
	//public boolean STAR_NOTATION = true;
	//public boolean STATE_CONSTRAINT = true;

	public RDDL2Pomdpx(RDDL rddl, String instance_name) throws Exception {

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
		Element desc = (Element)_dom.getElementsByTagName("Description").item(0);
		String text = desc.getTextContent();
		text += s + ";";
		desc.setTextContent(text);
	}

	public void addDiscount(double d) {

		if(d == 1.0)
			d = 0.99;
		Element disc = _dom.createElement("Discount");
		Text t = _dom.createTextNode(String.valueOf(d));
		disc.appendChild(t);
		Element root = _dom.getDocumentElement();
		root.appendChild(disc);
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
			String type) {
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

	public boolean buildCPTs() throws Exception {

		if (_state._tmIntermNames.size() > 0)
			throw new Exception(
					"Cannot convert to POMDPX format: contains intermediate variables");

		for(PVAR_NAME pState: _state._alStateNames)
		{
			PVARIABLE_DEF varDef = _d._hmPVariables.get(pState);
			if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE))
				throw new Exception(
						"Only support bool type variables for now");
		}

		for(PVAR_NAME pObs: _state._alObservNames)
		{
			PVARIABLE_DEF varDef = _d._hmPVariables.get(pObs);
			if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE))
				throw new Exception(
						"Only support bool type variables for now");
		}

		for(PVAR_NAME pAction: _state._alActionNames)
		{
			PVARIABLE_DEF varDef = _d._hmPVariables.get(pAction);
			if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE))
				throw new Exception(
						"Only support bool type variables for now");
		}
		
		//boolean single_state_constraint = false;
		//ArrayList<RDDL.PVAR_NAME> constrainted_states = new ArrayList<RDDL.PVAR_NAME>();

		addRoot();
		addDescription();
		addIntoDescription("horizon:" + String.valueOf(_i._nHorizon));
		addDiscount(_i._dDiscount);
		addSections();

		state_vars = collectStateVars();
		action_vars = collectActionVars();
		observ_vars = collectObservationVars();

		for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : state_vars
				.entrySet()) {
			PVAR_NAME p = e.getKey();
			ArrayList<ArrayList<LCONST>> assignments = e.getValue();
			for (ArrayList<LCONST> assign : assignments) {
				String name = CleanFluentName(p.toString() + assign);

				boolean isBool = true;
				PVARIABLE_DEF varDef = _d._hmPVariables.get(p);
				ArrayList<String> values = new ArrayList<String>();

				instances.clear();
				probs.clear();
				if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE)) {
					String initValue = ((ENUM_VAL) _state.getPVariableAssign(p,
							assign)).toString();
					isBool = false;
					ENUM_TYPE_DEF enumValues = (ENUM_TYPE_DEF) _d._hmTypes
							.get(varDef._sRange);
					for (int i = 0; i < enumValues._alPossibleValues.size(); i++) {
						String enumValue = enumValues._alPossibleValues.get(i)
								.toString();
						values.add(enumValue);
						ArrayList<String> enumInstance = new ArrayList<String>();
						enumInstance.add(enumValue);
						if (enumValue.equals(initValue)) {
							instances.add(enumInstance);
							probs.add(new Double(1.0));
						} else {
							instances.add(enumInstance);
							probs.add(new Double(0.0));
						}
					}
				} else {
					Boolean initValue = (Boolean) _state.getPVariableAssign(p,
							assign);
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
				addFun(name + "_0", null, instances, probs,
						"InitialStateBelief");
				addStateVar(name, values, isBool);
			}
		}

		_hmActionMap = ActionGenerator.getLegalBoolActionMap(_state);
		_alActionVars = new ArrayList<String>(_hmActionMap.keySet());
		//_alActionVars = new ArrayList<String>();
		//_alActionVars.add("noop");
		/*for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : action_vars
				.entrySet()) {
			PVAR_NAME p = e.getKey();
			ArrayList<ArrayList<LCONST>> assignments = e.getValue();
			for (ArrayList<LCONST> assign : assignments) {
				String name = CleanFluentName(p.toString() + assign);
				_alActionVars.add(name);
			}
		}*/
		addActVar(null, _alActionVars);

		for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : observ_vars
				.entrySet()) {
			PVAR_NAME p = e.getKey();
			ArrayList<ArrayList<LCONST>> assignments = e.getValue();
			for (ArrayList<LCONST> assign : assignments) {
				String name = CleanFluentName(p.toString() + assign);

				boolean isBool = true;
				PVARIABLE_DEF varDef = _d._hmPVariables.get(p);
				ArrayList<String> values = new ArrayList<String>();
				if (!varDef._sRange.equals(RDDL.TYPE_NAME.BOOL_TYPE)) {
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

		//addRewardVar();

		for (int iter = STATE_ITER; iter <= OBSERV_ITER; iter++) {

			HashMap<PVAR_NAME, ArrayList<ArrayList<LCONST>>> src = iter == STATE_ITER ? state_vars
					: observ_vars;

			for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e : src
					.entrySet()) {

				PVAR_NAME p = e.getKey();
				ArrayList<ArrayList<LCONST>> assignments = e.getValue();

				CPF_DEF cpf = _state._hmCPFs.get(new PVAR_NAME(p.toString()
						+ (iter == STATE_ITER ? "'" : "")));

				HashMap<LVAR, LCONST> subs = new HashMap<LVAR, LCONST>();
				for (ArrayList<LCONST> assign : assignments) {

					String cpt_var = CleanFluentName(p.toString() + assign);
					System.out.println("Processing: " + cpt_var);

					subs.clear();
					for (int i = 0; i < cpf._exprVarName._alTerms.size(); i++) {
						LVAR v = (LVAR) cpf._exprVarName._alTerms.get(i);
						LCONST c = (LCONST) assign.get(i);
						subs.put(v, c);
					}

					HashSet<Pair> relevant_vars = new HashSet<Pair>();
					EXPR cpf_expr = cpf._exprEquals;
					if (_d._bCPFDeterministic)
						cpf_expr = new KronDelta(cpf_expr);

					cpf_expr.collectGFluents(subs, _state, relevant_vars);

					boolean actionRelated = filterOutActionVars(relevant_vars);

					instances.clear();
					probs.clear();
					if (actionRelated) {

						for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e2 : action_vars
								.entrySet()) {

							PVAR_NAME action_name = e2.getKey();
							ArrayList<ArrayList<LCONST>> action_assignments = e2
									.getValue();

							for (ArrayList<LCONST> action_assign : action_assignments)
								_state.setPVariableAssign(action_name,
										action_assign,
										RDDL.BOOL_CONST_EXPR.FALSE);

						}

						boolean stateNoop = false;

						for (Map.Entry<String,ArrayList<PVAR_INST_DEF>> e2 : _hmActionMap.entrySet())
						{
							//for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e2 : action_vars
							//.entrySet()) {
							String action_instance = e2.getKey();
							ArrayList<PVAR_INST_DEF> action_list = e2.getValue();

							// Set all pvariables in action
							//for (PVAR_INST_DEF pid : action_list)
							//_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.TRUE);

							//PVAR_NAME action_name = e2.getKey();
							//ArrayList<ArrayList<LCONST>> action_assignments = (ArrayList<ArrayList<LCONST>>) e2
							//.getValue().clone();

							//action_assignments.add(new ArrayList<LCONST>());

							//for (int k = 0; k < action_assignments.size(); k++) {
							//ArrayList<LCONST> action_assign = action_assignments
							//.get(k);
							//String action_instance;
							//if (action_assignments.size() == 2 && k == 0
							//|| action_assign.size() > 0) {
							//action_instance = CleanFluentName(action_name
							//.toString() + action_assign);
							//_state.setPVariableAssign(action_name,
							//action_assign,
							//RDDL.BOOL_CONST_EXPR.TRUE);
							//} else
							//action_instance = "noop";

							// Set all pvariables in action
							for (PVAR_INST_DEF pid : action_list)
								_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.TRUE);
							if (!action_instance.equals("noop")
									|| !stateNoop)
							{
								_timer = System.currentTimeMillis();
								int result = enumerateAssignments(action_instance,
										new ArrayList<Pair>(relevant_vars),
										cpf_expr, subs, 0, iter);
								if(result == 0)
								{
									System.out.println("Translation failed for instance:" + _i._sName + " because it took too long time");
									return false;
								}
							}
							if (action_instance.equals("noop"))
								stateNoop = true;

							//_state.setPVariableAssign(action_name,
							//action_assign,
							//RDDL.BOOL_CONST_EXPR.FALSE);
							for (PVAR_INST_DEF pid : action_list)
								_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.FALSE);

						}
					} 
					else {
						_timer = System.currentTimeMillis();
						int result = enumerateAssignments(null, new ArrayList<Pair>(
								relevant_vars), cpf_expr, subs, 0, iter);
						if(result == 0)
						{
							System.out.println("Translation failed for instance:" + _i._sName + " because it took too long time");
							return false;
						}
					}

					ArrayList<String> parents = new ArrayList<String>();
					ArrayList<Pair> vars = new ArrayList<Pair>(relevant_vars);
					for (int i = 0; i < vars.size(); i++) {
						PVAR_NAME p2 = (PVAR_NAME) vars.get(i)._o1;
						ArrayList<LCONST> terms = (ArrayList<LCONST>) vars
								.get(i)._o2;
						String var_name = CleanFluentName(p2._sPVarName + terms);
						if (iter == 1)
							var_name += "_1";
						else
							var_name += "_0";

						parents.add(var_name);
					}


					boolean observable = false;
					if(iter == 1 && parents.size() == 1)
					{
						ArrayList<ArrayList<String>> newInstances = instances;
						ArrayList<Double> newProbs = probs;
						observable = true;
						String sFilter = new String();
						if(actionRelated)
						{
							sFilter = cpt_var + ":" + parents.get(0);
							for(int k = 0; k < _alActionVars.size(); k++)
							{
								if(!(probs.get(4 * k).doubleValue() == 1.0 && probs.get(4 * k + 3).doubleValue() == 1.0 ||
										probs.get(4 * k + 1).doubleValue() == 1.0 && probs.get(4 * k + 2).doubleValue() == 1.0 ))
								{
									observable = false;
									break;
								}
								else
								{
									if(probs.get(4 * k).doubleValue() == 1.0)
									{
										sFilter += "," + newInstances.get(4 * k).get(1) + "=true";
									}
									else
									{
										sFilter += "," + newInstances.get(4 * k).get(1) + "=false";
									}
								}
							}
						}
						else
						{
							sFilter = cpt_var + ":" + parents.get(0);
							if(!(probs.get(0).doubleValue() == 1.0 && probs.get(3).doubleValue() == 1.0 ||
									probs.get(1).doubleValue() == 1.0 && probs.get(2).doubleValue() == 1.0 ))
							{
								observable = false;
							}
							else
							{
								if(probs.get(0).doubleValue() == 1.0)
								{
									sFilter += ",true";
								}
								else
								{
									sFilter += ",false";
								}
							}
						}
						if(observable)
						{
							if(MOMDP)
							{
								Element nVar = (Element)_dom.getElementsByTagName("Variable").item(0);
								NodeList nl = nVar.getElementsByTagName("ObsVar");
								for(int k = 0; k < nl.getLength(); k++)
								{
									if(((Element)nl.item(k)).getAttribute("vname").compareTo(cpt_var) == 0)
									{
										nVar.removeChild(nl.item(k));
									}
								}
							}
							NodeList nl2 = _dom.getElementsByTagName("StateVar");
							for(int k = 0; k < nl2.getLength(); k++)
							{
								if(((Element)nl2.item(k)).getAttribute("vnameCurr").compareTo(parents.get(0)) == 0)
								{
									((Element)nl2.item(k)).setAttribute("fullyObs", "true");
								}
							}
							addIntoDescription(sFilter);
						}
					}
					if (actionRelated || ACTION_RELATED)
						parents.add("action");
					if(!actionRelated && ACTION_RELATED)
					{
						for(ArrayList<String> instance: instances)
						{
							instance.add(instance.size() - 1, "*");
						}
					}
					if(!observable || !MOMDP)
					{
						if (iter == 0) {
							cpt_var += "_1";
							addFun(cpt_var, parents, instances, probs, "StateTransitionFunction");
						} else {
							addFun(cpt_var, parents, instances, probs, "ObsFunction");
						}
					}

				}
			}
		}

		return buildReward();
	}
	
	public boolean buildReward() throws Exception
	{
		ArrayList<Pair> exprs = getAdditiveComponents(_state._reward);
		
		String REWARD_HEAD = "reward_";
		
		int i = 0;
		for(Pair pair: exprs)
		{
			i++;
			if(!buildReward((EXPR)pair._o2, (HashMap<LVAR, LCONST>)pair._o1, REWARD_HEAD + i))
				return false;
		}
		if(EXPR_LABEL)
			addRewardExpr();
		return true;
	}
	
	public boolean buildReward(EXPR rew_expr, HashMap<LVAR, LCONST> subs, String reward_name) throws Exception
	{
		addRewardVar(reward_name);
		
		HashSet<Pair> rew_relevant_vars = new HashSet<Pair>();
		
		if (_d._bRewardDeterministic)
			rew_expr = new DiracDelta(rew_expr);
		
		rew_expr.collectGFluents(subs, _state, rew_relevant_vars);
		boolean actionRelated2 = filterOutActionVars(rew_relevant_vars);

		instances.clear();
		probs.clear();
		if (actionRelated2) {

			for (Map.Entry<PVAR_NAME, ArrayList<ArrayList<LCONST>>> e2 : action_vars
					.entrySet()) {
				PVAR_NAME action_name = e2.getKey();
				ArrayList<ArrayList<LCONST>> action_assignments = e2.getValue();

				for (ArrayList<LCONST> action_assign : action_assignments)
					_state.setPVariableAssign(action_name, action_assign,
							RDDL.BOOL_CONST_EXPR.FALSE);

			}

			boolean rewNoop = false;

			for (Map.Entry<String,ArrayList<PVAR_INST_DEF>> e2 : _hmActionMap.entrySet())
			{
				//PVAR_NAME action_name = e2.getKey();
				//ArrayList<ArrayList<LCONST>> action_assignments = (ArrayList<ArrayList<LCONST>>) e2
				//.getValue().clone();

				//action_assignments.add(new ArrayList<LCONST>());

				//for (int k = 0; k < action_assignments.size(); k++) {
				//ArrayList<LCONST> action_assign = action_assignments.get(k);
				//String action_instance;
				//if (action_assignments.size() == 2 && k == 0
				//|| action_assign.size() > 0) {
				//action_instance = CleanFluentName(action_name
				//.toString() + action_assign);
				//_state.setPVariableAssign(action_name, action_assign,
				//RDDL.BOOL_CONST_EXPR.TRUE);
				//} else
				//action_instance = "noop";

				String action_instance = e2.getKey();
				ArrayList<PVAR_INST_DEF> action_list = e2.getValue();
				for (PVAR_INST_DEF pid : action_list)
					_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.TRUE);
				if (!action_instance.equals("noop") || !rewNoop)
				{
					_timer = System.currentTimeMillis();
					int result = enumerateAssignments(action_instance,
							new ArrayList<Pair>(rew_relevant_vars),
							rew_expr, subs, 0, 2);
					if(result == 0)
					{
						System.out.println("Translation failed for instance:" + _i._sName + " because it took too long time");
						return false;
					}
				}
				if (action_instance.equals("noop"))
					rewNoop = true;

				for (PVAR_INST_DEF pid : action_list)
					_state.setPVariableAssign(pid._sPredName, pid._alTerms, RDDL.BOOL_CONST_EXPR.FALSE);
			}
			//}
		} else {
			_timer = System.currentTimeMillis();
			int result = enumerateAssignments(null, new ArrayList<Pair>(rew_relevant_vars),
					rew_expr, subs, 0, 2);
			if(result == 0)
			{
				System.out.println("Translation failed for instance: " + _i._sName + " because it took long time");
				return false;
			}
		}

		ArrayList<String> parents = new ArrayList<String>();
		ArrayList<Pair> vars = new ArrayList<Pair>(rew_relevant_vars);
		for (int i = 0; i < vars.size(); i++) {
			PVAR_NAME p3 = (PVAR_NAME) vars.get(i)._o1;
			ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(i)._o2;
			String var_name = CleanFluentName(p3.toString() + terms);
			var_name += "_0";
			parents.add(var_name);
		}
		if (actionRelated2 || ACTION_RELATED)
			parents.add("action");
		if(!actionRelated2 && ACTION_RELATED)
		{
			for(ArrayList<String> instance: instances)
			{
				instance.add("*");
			}
		}
		addFun(reward_name, parents, instances, probs, "RewardFunction");
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

	public int enumerateAssignments(String action_instance,
			ArrayList<Pair> vars, EXPR cpf_expr, HashMap<LVAR, LCONST> subs,
			int index, int type) throws EvalException {

		if (index >= vars.size()) {

			RDDL.EXPR e = cpf_expr.getDist(subs, _state);
			double prob_true = -1d;
			if (e instanceof KronDelta) {
				EXPR e2 = ((KronDelta) e)._exprIntValue;
				if (e2 instanceof INT_CONST_EXPR)
					prob_true = (double) ((INT_CONST_EXPR) e2)._nValue;
				else if (e2 instanceof BOOL_CONST_EXPR)
					prob_true = ((BOOL_CONST_EXPR) e2)._bValue ? 1d : 0d;
				else
					throw new EvalException("Unhandled KronDelta argument: "
							+ e2.getClass());
			} else if (e instanceof Bernoulli) {
				prob_true = ((REAL_CONST_EXPR) ((Bernoulli) e)._exprProb)._dValue;
			} else if (e instanceof DiracDelta) {
				prob_true = ((REAL_CONST_EXPR) ((DiracDelta) e)._exprRealValue)._dValue;
			} else
				throw new EvalException("Unhandled distribution type: "
						+ e.getClass());

			ArrayList<String> instance = new ArrayList<String>();

			for (int i = 0; i < vars.size(); i++) {
				PVAR_NAME p = (PVAR_NAME) vars.get(i)._o1;
				ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(i)._o2;
				String ins_value = _state.getPVariableAssign(p, terms)
						.toString();

				instance.add(ins_value);
			}
			if (action_instance != null)
				instance.add(action_instance);

			if (type == 2) {
				instances.add(instance);
				probs.add(new Double(prob_true));
			} else {
				ArrayList<String> trueInstance = new ArrayList<String>(instance);
				trueInstance.add("true");
				ArrayList<String> falseInstance = new ArrayList<String>(
						instance);
				falseInstance.add("false");
				instances.add(trueInstance);
				instances.add(falseInstance);
				probs.add(new Double(prob_true));
				double probss = (10000 - prob_true * 10000)/10000;
				probs.add(new Double(probss));
			}
			return 1;

		} else {

			/*if(System.currentTimeMillis() - _timer > 60000)
			{
				return 0;
			}*/

			PVAR_NAME p = (PVAR_NAME) vars.get(index)._o1;
			ArrayList<LCONST> terms = (ArrayList<LCONST>) vars.get(index)._o2;
			
			_state.setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.TRUE);
			int result1 = enumerateAssignments(action_instance, vars, cpf_expr, subs,
					index + 1, type);

			if(result1 == 0)
				return 0;

			_state.setPVariableAssign(p, terms, RDDL.BOOL_CONST_EXPR.FALSE);
			int result2 = enumerateAssignments(action_instance, vars, cpf_expr, subs,
					index + 1, type);

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
		//if (output_dir.endsWith(File.separator))
		//output_dir = output_dir.substring(output_dir.length() - 1);
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


			RDDL2Pomdpx r2x = new RDDL2Pomdpx(rddl, instance_name);
			if(!r2x._succeed)
				System.exit(1);
			r2x.export("pomdpx");

		} catch (Exception e) {
			
			e.printStackTrace();

		}

	}

}
