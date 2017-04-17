function [nextBelStates,nzObsIds] = oneStepNZPrimeBelStatesFactored(POMDP, belState, normalize, smallestProb)
% function [nextBelStates,nzObsIds] = oneStepNZPrimeBelStatesFactored(POMDP, belState, normalize, smallestProb)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: pomdp problem
% belState: belief state
% normalize: boolean indicating whether the reachable belief states should be normalized
% smallestProb: smallest probability acceptable for an action-observation pair
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% nextBelStates: reachable belief states
% nzObsIds: id of observations with non-negligeable probability for each action
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The function computes the set of all reachable belief states in one step.  
% Only those belief states with probability greater than smallestProb are computed.  
% To speed up computation, a mean filed approximation is performed, whereby 
% the reachable belief states are approximated by the product of their marginals.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

stateVarsId = [POMDP.stateVars(:).id];
stateVarsPrimeId = stateVarsId + POMDP.nVars;
obsVarsId = [POMDP.obsVars(:).id];
obsVarsPrimeId = obsVarsId + POMDP.nVars;
obsVarsArity = [POMDP.obsVars(:).arity];
nObservations = prod(obsVarsArity);

nextBelStates = cell(POMDP.nActions,1);
marginals = javaArray('DD',POMDP.nStateVars+1);

for obsId = 1:nObservations
  obsConfig(obsId,:) = statedecode(obsId,POMDP.nObsVars,obsVarsArity);
end

Global.newHashtables;
for actId = 1:POMDP.nActions
      
  % find non-zero probability observations
  %[relevantCPTs, relevantVars] = findRelevant([belState,POMDP.actions(actId).obsFn,POMDP.actions(actId).transFn,POMDP.actions(actId).obsFn], obsVarsPrimeId);
  %elimVars = setdiff(relevantVars,obsVarsPrimeId);
  dd_obsProbs = OP.addMultVarElim([belState,POMDP.actions(actId).transFn,POMDP.actions(actId).obsFn],[stateVarsId,stateVarsPrimeId]);
  %dd_obsProbs2 = OP.addMultVarElim(relevantCPTs,elimVars);
  %if OP.maxAll(OP.abs(OP.sub(dd_obsProbs,dd_obsProbs2))) > 1e-8
  %    keyboard
  %end
  obsProbs = OP.convert2array(dd_obsProbs,obsVarsPrimeId);
  nzObsIds{actId} = find(obsProbs > smallestProb);
  
  % compute marginals
  if ~isempty(nzObsIds{actId})
    nextBelStates{actId} = javaArray('DD[]',length(nzObsIds{actId}));
    marginals = OP.marginals([belState,POMDP.actions(actId).transFn,POMDP.actions(actId).obsFn],stateVarsPrimeId,stateVarsId);
    for obsPtr = 1:length(nzObsIds{actId})
      obsId = nzObsIds{actId}(obsPtr);
      nextBelStates{actId}(obsPtr) = OP.restrictN(marginals,[obsVarsPrimeId;obsConfig(obsId,:)]);
    end
  end
end

