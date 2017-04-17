function [newAlpha, bestValue, bestActId, bestObsStrat] = dpBackup(POMDP, belState, alphaVectors, primedV, maxAbsVal)
% function [newAlpha, bestValue, bestActId, bestObsStrat] = dpBackup(POMDP, belState, alphaVectors, primedV, maxAbsVal)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: pomdp problem
% belState: belief state
% alphaVectors: alphaVectors of the current value function
% primedV: alphaVectors pre-primed
% maxAbsVal: largest (absolute) value achievable by any policy
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% newAlpha: new alpha vector for belState
% bestValue: highest value at belState
% bestActId: best action for belState
% bestObsStrat: best choice of conditional plan for each observation in the induced policy graph
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The function is decomposed in two parts: i) finding the best action
% and observation strategy at belState and ii) constructing the
% corresponding alpha vector.  To find the best action and observation
% strategy, we first find the "next belief states" reachable in one step for the
% action-observation pairs that have non-negligeable probability. To
% accelerate the computation of the reachable belief states, a mean field
% approximation is used.  In other words, the reachable belief states are
% approximated by the product of the marginals of each variable (see
% oneStepNZPrimeBelStatesFactored.m for more details).
% For each of those next belief states, we find the best alpha vector
% (which determines the observation strategy).  All the observations that
% select the same best alpha vector are then aggregated together.  Once
% the action and observation strategy are computed, we construct the
% corresponding alpha vector using bellman's equation (exploiting the
% fact that some observations may be aggregated).  Note that the computation 
% of the new alphaVector for the action and observation strategy chosen is exact.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% i) find best action and observation strategy at belState
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

stateVarsId = [POMDP.stateVars(:).id];
stateVarsPrimeId = stateVarsId + POMDP.nVars;
obsVarsId = [POMDP.obsVars(:).id];
obsVarsPrimeId = obsVarsId + POMDP.nVars;
obsVarsArity = [POMDP.obsVars(:).arity];
nObservations = prod(obsVarsArity);

if ~exist('primedV','var')
  primedV = javaArray('DD',length(alphaVectors));
  for i = 1:length(alphaVectors)
    primedV(i) = OP.primeVars(alphaVectors(i),POMDP.nVars);
  end
end

% get next unnormalized belStates
[nextBelStates, nzObsIds] = oneStepNZPrimeBelStatesFactored(POMDP, belState, true, POMDP.tolerance/maxAbsVal);
%[nextBelStates2, nzObsIds2] = oneStepNZPrimeBelStates(POMDP, belState, true, POMDP.tolerance/maxAbsVal);

% precompute obsVals
for actId = 1:POMDP.nActions
  if ~isempty(nzObsIds{actId})
    obsVals{actId} = OP.factoredExpectationSparse(nextBelStates{actId},primedV);
    %obsVals2{actId} = OP.dotProductLeafPrune(nextBelStates2{actId},primedV,stateVarsPrimeId); 
    %obsVals3{actId} = OP.dotProduct(nextBelStates2{actId},primedV,stateVarsPrimeId); 
    
    %diff = max(max(abs(obsVals3{actId} - obsVals2{actId})));
    %if diff > 1e-5
    %    diff
    %    keyboard
    %end
  end
end

bestValue = -inf;
for actId = 1:POMDP.nActions
  %actId = actId

  % compute immediate reward
  actValue = 0;
  for rewId = 1:length(POMDP.actions(actId).rewFn)
    actValue = actValue + OP.factoredExpectationSparse(belState,POMDP.actions(actId).rewFn(rewId));
  end

  % compute obsStrat
  obsStrat = ones(1,nObservations);
  obsValues = zeros(1,nObservations);
  for obsPtr = 1:length(nzObsIds{actId})
    obsId = nzObsIds{actId}(obsPtr);
    obsProb = nextBelStates{actId}(obsPtr,POMDP.nStateVars+1).getVal;
    [alphaValue, obsStrat(obsId)] = max(obsVals{actId}(obsPtr,:));
    obsValues(obsId) = obsProb * alphaValue;
  end
  
  actValue = actValue + sum(obsValues) * POMDP.discFact;

  % adjust bestValue
  if actValue > bestValue
    bestValue = actValue;
    bestActId = actId;
    bestObsStrat = obsStrat;
  end
end

% ii) construct corresponding alpha vector
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

newAlpha = DD.zero;
nextValFn = DD.zero;
for alphaId = unique(bestObsStrat)
  obsDd = DD.zero;
  for obsId = find(bestObsStrat == alphaId)
    obsConfig = statedecode(obsId,POMDP.nObsVars,obsVarsArity);
    obsDd = OP.add(obsDd,Config.convert2dd([obsVarsPrimeId;obsConfig]));
  end
  nextValFn = OP.add(nextValFn,OP.multN([DDleaf.myNew(POMDP.discFact),obsDd,primedV(alphaId)]));
end
newAlpha = OP.addMultVarElim([POMDP.actions(bestActId).transFn, POMDP.actions(bestActId).obsFn,nextValFn], [stateVarsPrimeId,obsVarsPrimeId]);

newAlpha = OP.add(POMDP.actions(bestActId).rewFn,newAlpha);
bestValue = OP.factoredExpectationSparse(belState,newAlpha);

