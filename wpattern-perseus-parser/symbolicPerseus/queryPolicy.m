function [actId,actName,value] = queryPolicy(belState, valueFunction, policy, ddPOMDP)
% function [actId,actName,value] = queryPolicy(belState, valueFunction, policy, ddPOMDP)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% belState: current belief state
% valueFunction: value function
% policy: list of actions for each alphaVector
% ddPOMDP: pomdp problem
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% actId: best action
% actName: name of the best action
% value: value of the best action
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Computes the best action for a given belief state according to a set of alphaVectors
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

stateVars = [ddPOMDP.stateVars.id];

bestVal = -inf;
for alphaId = 1:length(valueFunction)
  if isa(belState,'DD')
    val = OP.dotProduct(belState, valueFunction(alphaId), stateVars);
  else
    val = OP.factoredExpectationSparseNoMem(belState, valueFunction(alphaId));
  end
  if val > bestVal
    bestVal = val; 
    bestAlphaId = alphaId;
  end
end
actId = policy(bestAlphaId);
actName = ddPOMDP.actions(actId).name;
value = bestVal;
