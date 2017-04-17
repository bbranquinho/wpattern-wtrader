function tracePOMDPpolicyStationary(pomdpFileName, policyFileName, nSteps, belState)
% function tracePOMDPpolicyStationary(pomdpFileName, policyFileName, nSteps, belState)
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
% The policy traced corresponds to the *stationary* greedy policy induced 
% by a value function.  At each step, the action of the best alphaVector 
% for the current belief state is executed.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% load POMDP problem
fprintf('Loading policy...\n');
POMDP = ParseSPUDD(pomdpFileName);
POMDP.parsePOMDP(false);
ddPOMDP = pomdpFlatAct(POMDP);

% load POMDP policy
load(policyFileName);
ddPOMDP = pomdpFlatAct(POMDP);

if ~exist('nSteps','var') | isempty(nSteps)
  nSteps = 50;
end

if ~exist('belState','var') | isempty(belState)
  belState = ddPOMDP.initialBelState;
end

for stepId = 1:nSteps
  fprintf('\nstepId = %i\n',stepId);
    
  [actId,actName] = queryPolicy(belState, valueFunction, policy, ddPOMDP);
  fprintf('\naction = %s\n',actName);
  
  obsConfig = queryObservation(ddPOMDP, actId, belState);
  belState = beliefUpdate(ddPOMDP, belState, actId, obsConfig);
  
  Global.newHashtables;
end
