function tracePOMDPpolicyGraph(pomdpFileName, policyFileName, nSteps, belState)
% function tracePOMDPpolicyGraph(pomdpFileName, policyFileName, nSteps, belState)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2008)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% pomdpFileName: file containing the POMDP description
% policyFileName: file containing the policy
% nSteps: number of time steps in each run (default value: 50)
% belState: initial belief state (default value: ddPOMDP.initialBelState)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% none
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function allows a user to trace a policy by manually entering the
% observations at each step.  The action selected is displayed.  To end the
% trace, simply type CTRL-C.  To evaluate a policy, use the function
% evalPOMDPpolicyStationary, evalPOMDPpolicyNonStationary or
% evalPOMDPpolicyGraph.
%
% The policy traced corresponds to the induced policy graph implicitely 
% computed by boundedPerseus.m.  The algorithm loads into memory the value 
% function of the last iteration as well as the action mapping and 
% observation strategy of all iterations.  More precisely, the following 
% variables encode the induced policy graph:
%
% policies: alphaId --> actionId
% obsStrategies: alphaId,obsId --> nextAlphaId
%
% At the first step, the best alphaVector is found for the current belief
% state and its corresponding action is executed.  For all subsequent
% steps, actions are chosen by following the implicit graph encoded in the
% "policies" and "obsStrategies" variables.  When the end of the graph is
% reached, the policy is reset to the beginning of the graph by finding the
% best alphaVector for the current belief state.  The execution of the
% policy graph is quite fast since finding the best alphaVector only needs 
% to be performed at the first step and when the end of the graph is 
% reached.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



% load POMDP problem
fprintf('Loading policy...\n');
POMDP = ParseSPUDD(pomdpFileName);
POMDP.parsePOMDP(false);
ddPOMDP = pomdpFlatAct(POMDP);

% load POMDP policy
load(policyFileName);
ddPOMDP = pomdpFlatAct(POMDP);
startId = regexp(policyFileName, '_\d+iter') + 1;
endId = regexp(policyFileName, 'iter') - 1;
nIter = sscanf(policyFileName(startId:endId),'%f');
policies = cell(1,nIter);
for iter = 1:nIter
  iterPolicyFileName = sprintf('%s%i%s',policyFileName(1:startId-1),iter,policyFileName(endId+1:end));
  load(iterPolicyFileName,'policy','obsStrategy');
  policies{iter} = policy;
  obsStrategies{iter} = obsStrategy;
end

if ~exist('nSteps','var') | isempty(nSteps)
  nSteps = 50;
end

if ~exist('belState','var') | isempty(belState)
  belState = ddPOMDP.initialBelState;
end

stateVars = [ddPOMDP.stateVars.id];

% find initAlphaId
iterId = nIter;
[actId, alphaRatings] = policyQuery(stateVars,belState,valueFunction,policies{iterId});
[maxVal,alphaId] = max([alphaRatings.value]);

for stepId = 1:nSteps
  fprintf('\nstepId = %i\n',stepId);
    
  actId = policies{iterId}(alphaId);
  fprintf('\naction = %s\n',ddPOMDP.actions(actId).name);
  
  obsConfig = queryObservation(ddPOMDP, actId, belState);
  obsId = statencode(obsConfig(2,:),double(Global.varDomSize(obsConfig(1,:))));
  belState = beliefUpdate(ddPOMDP, belState, actId, obsConfig);
  
  % update alphaId and iterId
  if (iterId > 1)
    alphaId = obsStrategies{iterId}(alphaId,obsId);
    iterId = iterId - 1;
  else
    iterId = nIter;
    [actId, alphaRatings] = policyQuery(stateVars,belState,valueFunction,policies{iterId});
    [maxVal,alphaId] = max([alphaRatings.value]);
  end
    
  Global.newHashtables;
end
  
  
