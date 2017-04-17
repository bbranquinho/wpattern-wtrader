function avRew = evalPOMDPpolicyNonStationary(pomdpFileName, policyFileName, nRuns, nSteps, initStateConfig)
% function avRew = evalPOMDPpolicyNonStationary(pomdpFileName, policyFileName, nRuns, nSteps, initStateConfig)
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
% the *nonstationary* greedy policy induced by the sequence of value 
% functions computed by boundedPerseus at each iteration.  The value 
% functions of all iterations are loaded into memory (this may require a 
% lot of memory). At each step, the best alphaVector is found in the value 
% function for the matching step and the corresponding action is executed.  
% When the number of steps is greater than the number of value functions, 
% the algorithm cycles by re-starting at the last value function.
%
% The average value is guaranteed to be equal to or greather than the value
% of the policy graph induced by boundedPerseus.  
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

% find initAlphaId
initIterId = nIter;
[actId, alphaRatings] = policyQuery(stateVars,ddPOMDP.initialBelState,valueFunctions{initIterId},policies{initIterId});
[maxVal,initAlphaId] = max([alphaRatings.value]);
expectedValue = maxVal

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
  alphaId = initAlphaId;
  iterId = initIterId;
  if exist('initStateConfig','var')
    stateConfig = initStateConfig;
  else
    stateConfig = OP.sampleMultinomial(belState,stateVars);
  end
  
  totRew = 0;
  for stepId = 1:nSteps
    %stepId = stepId
    
    % pick action
    [actId,alphaRatings] =  policyQuery(stateVars,belState,valueFunctions{iterId},policies{iterId});
    %ddPOMDP.actions(actId).name
    
    % sum rewards
    %immRew =  OP.dotProduct(belState,ddPOMDP.actions(actId).rewFn, stateVars)
    totRew = totRew + ddPOMDP.discFact^(stepId-1) * OP.eval(ddPOMDP.actions(actId).rewFn, stateConfig);

    % sample observation
    restrictedTransFn = OP.restrictN(ddPOMDP.actions(actId).transFn, stateConfig); 
    nextStateConfig = OP.sampleMultinomial(restrictedTransFn,stateVarsPrime);
    restrictedObsFn = OP.restrictN(ddPOMDP.actions(actId).obsFn, [stateConfig, nextStateConfig]);
    obsConfig = OP.sampleMultinomial(restrictedObsFn, obsVarsPrime);
    %Global.valNames(obsConfig(1,1),obsConfig(2,1))
    %Global.valNames(obsConfig(1,2),obsConfig(2,2))
    
    % update belief state
    belState = beliefUpdate(ddPOMDP, belState, actId, obsConfig);
    stateConfig = Config.primeVars(nextStateConfig,-ddPOMDP.nVars);
    
    % update alphaId and iterId
    if (iterId > 1)
      iterId = iterId - 1;
    else
      iterId = nIter;
    end
    
    Global.newHashtables;
  end

  %OP.addMultVarElim(belState,[1:4,6,7])
  %OP.addMultVarElim(belState,[1:5,7])
  %OP.addMultVarElim(belState,[1:6])
  avRew = ((runId-1)*avRew + totRew) / runId;
  fprintf('totRew = %f\t avRew = %f\n',totRew,avRew);
  %keyboard
end

