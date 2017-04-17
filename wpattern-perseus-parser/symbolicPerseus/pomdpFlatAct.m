function ddPOMDP = pomdpFlatAct(POMDP)
% function ddPOMDP = pomdpFlatAct(POMDP)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: pomdp struct read from a file
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% ddPOMDP: pomdp struct that will be used by Symbolic Perseus.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Generates a POMDP struct (factored POMDP) to be used by Symbolic Perseus.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.nStateVars = POMDP.nStateVars;
ddPOMDP.nObsVars = POMDP.nObsVars;
ddPOMDP.nVars = ddPOMDP.nStateVars + ddPOMDP.nObsVars;

%%%%%%%%%%%%%%%%% State variables %%%%%%%%%%%%%%%%%%%%%%%

for varId = 1:ddPOMDP.nStateVars
  stateVarStruct.arity = POMDP.valNames.get(varId-1).size;
  stateVarStruct.name = POMDP.varNames.get(varId-1);
  stateVarStruct.id = varId;
  stateVarStruct.valNames = cell(1,stateVarStruct.arity);
  for valId = 1:stateVarStruct.arity
    stateVarStruct.valNames{valId} = POMDP.valNames.get(varId-1).get(valId-1);
  end
  ddPOMDP.stateVars(varId) = stateVarStruct;
end

%%%%%%%%%%%%%%%%% Observation variables %%%%%%%%%%%%%%%%%%%%%

for varId = 1:ddPOMDP.nObsVars
  obsVarStruct.arity = POMDP.valNames.get(POMDP.nStateVars+varId-1).size;
  obsVarStruct.name = POMDP.varNames.get(POMDP.nStateVars+varId-1);
  obsVarStruct.id = POMDP.nStateVars+varId;
  obsVarStruct.valNames = cell(1,obsVarStruct.arity);
  for valId = 1:obsVarStruct.arity
    obsVarStruct.valNames{valId} = POMDP.valNames.get(POMDP.nStateVars+varId-1).get(valId-1);
  end
  ddPOMDP.obsVars(varId) = obsVarStruct;
end

%%%%%%%%%%%%%%%%%% Globals %%%%%%%%%%%%%%%%%%%%%%%

Global.setVarDomSize([ddPOMDP.stateVars(:).arity, ddPOMDP.obsVars(:).arity, ... 
        	      ddPOMDP.stateVars(:).arity, ddPOMDP.obsVars(:).arity]);

varNames = {ddPOMDP.stateVars(:).name, ddPOMDP.obsVars(:).name};
for i = 1:ddPOMDP.nVars
  varNames = {varNames{:},[varNames{i},char(39)]};
end
Global.setVarNames(varNames);

for i = 1:ddPOMDP.nStateVars
	Global.setValNames(i,ddPOMDP.stateVars(i).valNames);
end
for i = 1:ddPOMDP.nObsVars
	Global.setValNames(ddPOMDP.nStateVars+i,ddPOMDP.obsVars(i).valNames);
end
for i = 1:ddPOMDP.nStateVars
	Global.setValNames(ddPOMDP.nVars+i,ddPOMDP.stateVars(i).valNames);
end
for i = 1:ddPOMDP.nObsVars
	Global.setValNames(ddPOMDP.nVars+ddPOMDP.nStateVars+i,ddPOMDP.obsVars(i).valNames);
end

%%%%%%%%%%%%%%%%% Dynamics %%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.nActions = POMDP.actTransitions.size;

for actId = 1:ddPOMDP.nActions

  actStruct = []; 
  actStruct.name = POMDP.actNames.get(actId-1);
  actStruct.transFn = POMDP.actTransitions.get(actId-1);
  actStruct.obsFn = POMDP.actObserve.get(actId-1);
  actStruct.rewFn = OP.sub(POMDP.reward, POMDP.actCosts.get(actId-1));
  actStruct.rewFn = OP.addMultVarElim([actStruct.rewFn,actStruct.transFn], ddPOMDP.nVars+1:ddPOMDP.nVars+ddPOMDP.nStateVars);
  
  % find stochastic transitions (and deterministic varMappings)
  detVarMapping = [];
  stochTransFn = [];
  for cptId = 1:length(actStruct.transFn)
    varSet = actStruct.transFn(cptId).getVarSet;
    deterministicMapping = true;
    if length(varSet) == 2 & Global.varDomSize(varSet(1)) == Global.varDomSize(varSet(2))
      for i = 1:Global.varDomSize(varSet(1))
        for j = 1:Global.varDomSize(varSet(2))
          val = OP.eval(actStruct.transFn(cptId),[varSet'; [i, j]]);
          if (i == j & val ~= 1) | (i ~= j & val ~= 0)
            deterministicMapping = false;
            break;
          end
        end
      end
    else
      deterministicMapping = false;
    end
    if deterministicMapping
      detVarMapping = [detVarMapping, varSet];
    else
      stochTransFn = [stochTransFn, actStruct.transFn(cptId)];
    end
  end
  
  if isempty(detVarMapping)
    actStruct.forwardDetVarMapping = [];
    actStruct.backwardDetVarMapping = [];
  else
    actStruct.forwardDetVarMapping = detVarMapping;
    actStruct.backwardDetVarMapping = [detVarMapping(2,:); detVarMapping(1,:)];
  end
  
  if isempty(stochTransFn) 
    actStruct.forwardStochTransFn = [];
    actStruct.backwardStochTransFn = [];
  else
    actStruct.forwardStochTransFn = OP.swapVars(stochTransFn,actStruct.forwardDetVarMapping);
    actStruct.backwardStochTransFn = OP.swapVars(stochTransFn,actStruct.backwardDetVarMapping);
  end
  
  ddPOMDP.actions(actId) = actStruct;
end

%%%%%%%%%%%%%%%%%%%%%% Discount Factor %%%%%%%%%%%%%%%%%%%%%

ddPOMDP.discFact = POMDP.discount.getVal;

%%%%%%%%%%%%%%%%%%%%%% Tolerance %%%%%%%%%%%%%%%%%%%%%%%%%%%

if isempty(POMDP.tolerance) 
  maxVal = -inf;
  minVal = inf;
  for actId = 1:ddPOMDP.nActions
    maxVal = max(maxVal,OP.maxAll(OP.addN(ddPOMDP.actions(actId).rewFn)));
    minVal = min(minVal,OP.minAll(OP.addN(ddPOMDP.actions(actId).rewFn)));
  end
  maxDiffRew = maxVal - minVal;
  maxDiffVal = maxDiffRew/(1-min(0.95,ddPOMDP.discFact));
  ddPOMDP.tolerance = 1e-5 * maxDiffVal;
else
  ddPOMDP.tolerance = POMDP.tolerance.getVal;
end

for actId = 1:ddPOMDP.nActions
  ddPOMDP.actions(actId).rewFn = OP.approximate(ddPOMDP.actions(actId).rewFn, ddPOMDP.tolerance, [0]);
end

%%%%%%%%%%%%%%%%%%%%%% Init %%%%%%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.initialBelState = POMDP.init(1);






