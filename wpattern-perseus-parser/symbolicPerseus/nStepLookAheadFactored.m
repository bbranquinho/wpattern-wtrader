function [bestActId, bestValue] = nStepLookAheadFactored(POMDP, belState, primedV, maxAbsVal, depth)
% function [bestActId, bestValue] = nStepLookAheadFactored(POMDP, belState, primedV, maxAbsVal, depth)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: pomdp problem
% belState: initial belief state
% primedV: set of primed alpha vectors corresponding to some value function
% maxAbsVal: largest possible (absolute) value of any policy 
% depth: depth of the lookahead search when selecting an action based on primedV
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% bestActId: best Action
% bestValue: best value amongst all actions
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The algorithm searches forward to depth d.  Values are propagated
% backwards up the tree.  The base approximate value function is primedV.
% The best action for the starting belief is returned with its
% corresponding value.  This version assumes that beliefs are approximated
% at each step by the product of the marginals.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

obsVarsArity = [POMDP.obsVars(:).arity];
nObservations = prod(obsVarsArity);

% get next unnormalized belStates
[nextBelStates, nzObsIds] = oneStepNZPrimeBelStatesFactored(POMDP, belState, true, POMDP.tolerance/maxAbsVal);

% precompute obsVals

for actId = 1:POMDP.nActions
  if depth <= 1
    if ~isempty(nzObsIds{actId})
      alphaValues = OP.factoredExpectationSparseNoMem(nextBelStates{actId},primedV); 
      obsVals{actId} = max(alphaValues,[],2);
    end
  else
    for obsPtr = 1:length(nzObsIds{actId})
      unprimedNextBelState = javaArray('DD',POMDP.nStateVars);
      for varId = 1:length(unprimedNextBelState)
        unprimedNextBelState(varId) = OP.primeVars(nextBelStates{actId}(obsPtr,varId), -POMDP.nVars);
      end
      [childActId, obsVals{actId}(obsPtr)] = nStepLookAheadFactored(POMDP, unprimedNextBelState, primedV, maxAbsVal, depth-1);
    end
  end
end

bestValue = -inf;
for actId = 1:POMDP.nActions
  %actId = actId
  %POMDP.actions(actId).name
  
  % compute immediate reward
  actValue = OP.factoredExpectationSparseNoMem(belState,POMDP.actions(actId).rewFn);

  % compute obsStrat
  for obsPtr = 1:length(nzObsIds{actId})
    obsProb = nextBelStates{actId}(obsPtr,POMDP.nStateVars+1).getVal;
    actValue = actValue + POMDP.discFact * obsProb * obsVals{actId}(obsPtr);
  end
  %keyboard

  % adjust bestValue
  if actValue > bestValue
    bestValue = actValue;
    bestActId = actId;
  end
end
