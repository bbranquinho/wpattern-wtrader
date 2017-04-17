function [bestActId, bestValue] = nStepLookAhead(POMDP, belState, valFn, policy, smallestProb, depth)
% function [bestActId, bestValue] = nStepLookAhead(POMDP, belState, valFn, policy, smallestProb, depth)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: pomdp problem
% belState: initial belief state
% valFn: set of alpha vectors corresponding to some value function
% policy: mapping of alphaVectors to actions
% smallestProb: smallest probability that should not be rounded to 0.
% depth: depth of the lookahead search when selecting an action based on valFn
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% bestActId: best Action
% bestValue: best value amongst all actions
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The algorithm searches forward to depth d.  Values are propagated
% backwards up the tree.  The base approximate value function is valFn.
% The best action for the starting belief is returned with its
% corresponding value.  This version performs exact computation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if depth == 0
  [bestActId, bestActName, bestValue] = queryPolicy(belState, valFn, policy, POMDP);
  %bestActId = 1;
  %bestValue = OP.dotProduct(belState, OP.maxN(valFn), [POMDP.stateVars.id]);
  
else
  obsVarsArity = [POMDP.obsVars(:).arity];
  nObservations = prod(obsVarsArity);
  stateVarsId = [POMDP.stateVars.id];
  stateVarsPrimeId = stateVarsId + POMDP.nVars;
  obsVarsPrimeId = [POMDP.obsVars.id] + POMDP.nVars;
  obsConfig(1,:) = obsVarsPrimeId;
  
  bestValue = -inf;
  for actId = 1:POMDP.nActions
      
    % compute immediate reward
    actValue = 0;
    for rewId = 1:length(POMDP.actions(actId).rewFn)
      actValue = actValue + OP.dotProduct(belState,POMDP.actions(actId).rewFn(rewId),stateVarsId);
    end

    % find non-zero observations
    dd_obsProbs = OP.addMultVarElim([belState,POMDP.actions(actId).transFn,POMDP.actions(actId).obsFn],[stateVarsId,stateVarsPrimeId]);
    obsProbs = OP.convert2array(dd_obsProbs,obsVarsPrimeId);
    nzObsIds = find(obsProbs > smallestProb);

    % compute future rewards
    for obsPtr = 1:length(nzObsIds)
      obsConfig(2,:) = statedecode(nzObsIds(obsPtr),length(obsVarsArity),obsVarsArity);
      nextBelState = beliefUpdate(POMDP, belState, actId, obsConfig);
      [childActId, childValue] = nStepLookAhead(POMDP, nextBelState, valFn, policy, smallestProb, depth-1);
      actValue = actValue + POMDP.discFact * obsProbs(obsPtr) * childValue;
    end
    
    % check if better value found
    if actValue > bestValue
      bestValue = actValue;
      bestActId = actId;
    end
  end
end
