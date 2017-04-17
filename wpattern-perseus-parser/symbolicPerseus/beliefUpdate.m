function nextBelState = beliefUpdate(ddPOMDP, belState, actId, obsConfig)
% function nextBelState = beliefUpdate(ddPOMDP, belState, actId, obsConfig)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% ddPOMDP: pomdp problem
% belState: belief state
% actId: action
% obsConfig: assignment of observation variables with observation values
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% nextBelState: next belief state
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The function computes the belief state reached after executing an action 
% and making an observation.  This computation is exact.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

stateVars = [ddPOMDP.stateVars.id];
transFn = ddPOMDP.actions(actId).transFn;
obsFn = OP.restrictN(ddPOMDP.actions(actId).obsFn, obsConfig);
nextBelState = OP.addMultVarElim([belState, transFn, obsFn], stateVars);
nextBelState = OP.primeVars(nextBelState, -ddPOMDP.nVars);
obsProb = OP.addMultVarElim(nextBelState, stateVars);
if obsProb.getVal < 1e-8
  fprintf('WARNING: Zero-probability observation, resetting belief state to a uniform distribution\n');
  nextBelState = DD.one;
end
nextBelState = OP.div(nextBelState, OP.addMultVarElim(nextBelState, stateVars));
