function avRew = evalPOMDPpolicyGraph(pomdpFileName, policyFileName, nRuns, nSteps, initStateConfig)
% function avRew = evalPOMDPpolicyGraph(pomdpFileName, policyFileName, nRuns, nSteps, initStateConfig)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% pomdpFileName: file containing the POMDP description
% policyFileName: file containing the policy
% nRuns: number of policy runs (default value: 500)
% nSteps: number of time steps in each run (default value: 50)
% initStateConfig: initial state (default value: initial belief state)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% avRew: expected total discounted reward averaged over all runs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Computes the expected total discounted reward averaged over all runs for
% the induced policy graph implicitely computed by boundedPerseus.m.  
% The algorithm loads into memory the value function of the last iteration 
% as well as the action mapping and observation strategy of all iterations.  
% More precisely, the following variables encode the induced policy graph:
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
%
% The value of the initial belief state printed on the screen by
% boundedPerseus after each iteration is an estimate of the value of the
% induced policy graph.
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
  load(iterPolicyFileName,'policy','obsStrategy');
  policies{iter} = policy;
  obsStrategies{iter} = obsStrategy;
end

% find initAlphaId
initIterId = nIter;
[actId, alphaRatings] = policyQuery(stateVars,ddPOMDP.initialBelState,valueFunction,policies{initIterId});
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
    actId = policies{iterId}(alphaId);
    %ddPOMDP.actions(actId).name

    % sum rewards
    %immRew =  OP.dotProduct(belState,ddPOMDP.actions(actId).rewFn, stateVars)
    %for rewId = 1:length(ddPOMDP.actions(actId).rewFn)
    totRew = totRew + ddPOMDP.discFact^(stepId-1) * OP.eval(ddPOMDP.actions(actId).rewFn, stateConfig);

    % sample observation
    restrictedTransFn = OP.restrictN(ddPOMDP.actions(actId).transFn, stateConfig); 
    nextStateConfig = OP.sampleMultinomial(restrictedTransFn,stateVarsPrime);
    restrictedObsFn = OP.restrictN(ddPOMDP.actions(actId).obsFn, [stateConfig, nextStateConfig]);
    obsConfig = OP.sampleMultinomial(restrictedObsFn, obsVarsPrime);
    obsId = statencode(double(obsConfig(2,:)),double(Global.varDomSize(obsConfig(1,:))));
    %Global.valNames(obsConfig(1,1),obsConfig(2,1))
    %Global.valNames(obsConfig(1,2),obsConfig(2,2))
    
    % update belief state
    belState = beliefUpdate(ddPOMDP, belState, actId, obsConfig);
    stateConfig = Config.primeVars(nextStateConfig,-ddPOMDP.nVars);
    
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

  %OP.addMultVarElim(belState,[1:4,6,7])
  %OP.addMultVarElim(belState,[1:5,7])
  %OP.addMultVarElim(belState,[1:6])
  avRew = ((runId-1)*avRew + totRew) / runId;
  fprintf('totRew = %f\t avRew = %f\n',totRew,avRew);
  %keyboard
end

