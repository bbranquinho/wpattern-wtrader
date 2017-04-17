function avRew = evalPOMDPpolicyStationary(pomdpFileName, policyFileName, nRuns, nSteps, initStateConfig)
% function avRew = evalPOMDPpolicyStationary(pomdpFileName, policyFileName, nRuns, nSteps, initStateConfig)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% pomdpFileName: file containing the POMDP description
% policyFileName: file containing the policy
% nRuns: number of policy runs (default value: 500)
% nSteps: number of time steps in each run (default value: 50)
% initStateConfig: initial state (default value: state sampled from initial belief state)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% avRew: expected total discounted reward averaged over all runs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Computes the expected total discounted reward averaged over all runs for
% the *stationary* greedy policy induced by a value function.  At each step,
% the action of the best alphaVector for the current belief state is
% executed.
%
% The average value computed may be arbitrarily lower than that of the 
% policy graph induced by boundedPerseus.  The value of the initial belief 
% state printed on the screen by boundedPerseus after each iteration is an 
% estimate of the value of the induced policy graph (not the stationary 
% policy).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% load POMDP problem
fprintf('Loading policy...\n');
POMDP = ParseSPUDD(pomdpFileName);
POMDP.parsePOMDP(false);
ddPOMDP = pomdpFlatAct(POMDP);

stateVars = 1:ddPOMDP.nStateVars;
observedVars = stateVars;
stateVarsPrime = stateVars+ddPOMDP.nVars;
obsVars = ddPOMDP.nStateVars+1:ddPOMDP.nVars;
obsVarsPrime = obsVars+ddPOMDP.nVars;

% load POMDP policy
load(policyFileName);
ddPOMDP = pomdpFlatAct(POMDP);

%[valueFunction, policy] = solveQMDP(ddPOMDP);

if ~exist('nRuns','var') | isempty(nRuns)
  nRuns = 500;
end

if ~exist('nSteps','var') | isempty(nSteps)
  nSteps = 50;
end

avRew = 0;
for runId = 1:nRuns

  runId = runId
  rand('state',runId);
  Global.random.setSeed(runId);
  belState = ddPOMDP.initialBelState;
  if exist('initStateConfig','var')
    stateConfig = initStateConfig;
  else
    stateConfig = OP.sampleMultinomial(belState,stateVars);
  end
  
  totRew = 0;
  for stepId = 1:nSteps
    %fprintf('stepId = %i\n',stepId);
    
    % pick action
    [actId,alphaRatings] =  policyQuery(stateVars,belState,valueFunction,policy);
    %fprintf('\naction = %s\n',ddPOMDP.actions(actId).name);

    % sum rewards
    %immRew =  OP.dotProduct(belState,ddPOMDP.actions(actId).rewFn, stateVars)
    totRew = totRew + ddPOMDP.discFact^(stepId-1) * OP.eval(ddPOMDP.actions(actId).rewFn, stateConfig);

    % sample observation
    restrictedTransFn = OP.restrictN(ddPOMDP.actions(actId).transFn, stateConfig); 
    nextStateConfig = OP.sampleMultinomial(restrictedTransFn,stateVarsPrime);
    restrictedObsFn = OP.restrictN(ddPOMDP.actions(actId).obsFn, [stateConfig, nextStateConfig]);
    obsConfig = OP.sampleMultinomial(restrictedObsFn, obsVarsPrime);
    %fprintf('\n%s = %s\n',char(Global.varNames(obsConfig(1,1))), char(Global.valNames(obsConfig(1,1),obsConfig(2,1))));
    %fprintf('%s = %s\n',char(Global.varNames(obsConfig(1,2))), char(Global.valNames(obsConfig(1,2),obsConfig(2,2))));
    
    % update belief state
    belState = beliefUpdate(ddPOMDP, belState, actId, obsConfig);
    %for stateVarId = 1:ddPOMDP.nStateVars
    %  OP.addMultVarElim(belState,[1:stateVarId-1,stateVarId+1:ddPOMDP.nStateVars])
    %end
    stateConfig = Config.primeVars(nextStateConfig,-ddPOMDP.nVars);
    %for i = 1:size(stateConfig,2)
    %  fprintf('%s = %s\n',char(Global.varNames(stateConfig(1,i))), char(Global.valNames(stateConfig(1,i),stateConfig(2,i))));
    %end

    Global.newHashtables;
    %keyboard
  end

  avRew = ((runId-1)*avRew + totRew) / runId;
  fprintf('totRew = %f\t avRew = %f\n',totRew,avRew);
  %keyboard
end

