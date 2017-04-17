%function tracePOMDPpolicyNonStationary(pomdpFileName, policyFileName, nSteps, belState)
% function tracePOMDPpolicyNonStationary(pomdpFileName, policyFileName, nSteps, belState)
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
% The policy traced corresponds to the *nonstationary* greedy policy 
% induced by the sequence of value functions computed by boundedPerseus at 
% each iteration.  The value functions of all iterations are loaded into 
% memory (this may require a lot of memory). At each step, the best 
% alphaVector is found in the value function for the matching step and the 
% corresponding action is executed.  When the number of steps is greater 
% than the number of value functions, the algorithm cycles by re-starting 
% at the last value function.
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
  load(iterPolicyFileName,'policy','valueFunction');
  policies{iter} = policy;
  valueFunctions{iter} = valueFunction;
end

if ~exist('nSteps','var') | isempty(nSteps)
  nSteps = 50;
end

if ~exist('belState','var') | isempty(belState)
  belState = ddPOMDP.initialBelState;
end

iterId = nIter;
for stepId = 1:nSteps
  fprintf('\nstepId = %i\n',stepId);
    
  [actId,actName] = queryPolicy(belState, valueFunctions{iterId}, policies{iterId}, ddPOMDP);
  fprintf('\naction = %s\n',actName);
  
  obsConfig = queryObservation(ddPOMDP, actId, belState);
  belState = beliefUpdate(ddPOMDP, belState, actId, obsConfig);
  
  % update alphaId and iterId
  if (iterId > 1)
    iterId = iterId - 1;
  else
    iterId = nIter;
  end
    
  Global.newHashtables;
end
  
  
