function belRegion = reachableBelRegion(initBelState,POMDP,desiredSize,maxSize,episodeLength,threshold)
% function belRegion = reachableBelRegion(initBelState,POMDP,desiredSize,maxSize,episodeLength,threshold)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% initBelState: initial belief state
% POMDP: pomdp problem
% desiredSize: desired number of distinct reachable belief states that will be retained (default value: 300)
% maxSize: maximum number of reachable belief states that will be sampled (default value: 1000)
% episodeLength: number of belief states sampled in an episode before re-starting (default value: 50)
% threshold: maxnorm threshold used to reject similar or identical belief states (default value: 0.001)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% belRegion: set of reachable belief states
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The algorithm samples trajectories (of 50 time steps by executing 
% random actions) to collect reachable belief states.  At most 'maxSize' 
% beliefs are sampled and the procedure is stopped when 'desiredSize' 
% distinct beliefs are sampled. Belief states with a maxnorm difference 
% less than "threshold" compared to the previous belief states are rejected.
%
% Since belief state monitoring is generally intractable, belief states
% are approximated my making every variable independent.  In other
% words, the product of the marginals of the variables is used as an
% approximation.  This product representation can also be exploited in
% ADD operations such as dot products which become extremely fast.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fprintf('\nsampling reachable belief states\n');

if ~exist('desiredSize','var') | isempty(desiredSize)
  desiredSize = 300;
end

if ~exist('maxSize','var') | isempty(maxSize)
  maxSize = 1000;
end

if ~exist('episodeLength','var') | isempty(episodeLength)
  episodeLength = 50;
end

if ~exist('threshold','var') | isempty(threshold)
  threshold = 0.001;
end

stateVarsId = [POMDP.stateVars(:).id];
stateVarsPrimeId = stateVarsId + POMDP.nVars;
obsVarsId = [POMDP.obsVars(:).id];
obsVarsPrimeId = obsVarsId + POMDP.nVars;

% factorize initBelState
temp = javaArray('DD',POMDP.nStateVars);
initBelStateArray = [];
for varId = 1:POMDP.nStateVars
  temp(varId) = OP.addMultVarElim(initBelState,stateVarsId([1:varId-1,varId+1:end]));
  initBelStateArray = [initBelStateArray; OP.convert2array(temp(varId),stateVarsId(varId))];
end
initBelState = temp;

% preset policy
%actions = [2,2,8,12,1,7,12,4,9,12,1,10,12,4,6,12,1,2,2,5,12,2,11,12];

nextBelState = javaArray('DD',POMDP.nStateVars);
belRegion = cell(1,desiredSize);
count = 1;
sampleCount = 1;
belRegion{count} = initBelState;
belRegionArray = zeros(sum([POMDP.stateVars.arity]),desiredSize);
belRegionArray(:,count) = initBelStateArray;
while sampleCount < maxSize & count < desiredSize
    
  belState = initBelState;

  for stepId = 1:episodeLength

    % sample action
    actId = sampleMultinomial(ones(1,POMDP.nActions));
    %actId = actions(stepId);
    %POMDP.actions(actId).name

    % sample observation
    obsDist = OP.addMultVarElim([belState,POMDP.actions(actId).transFn,POMDP.actions(actId).obsFn],[stateVarsId,stateVarsPrimeId]);
    %if actId >= 5 & actId <= 11
    %  obsConfig = [obsVarsPrimeId;2];
    %else
      obsConfig = OP.sampleMultinomial(obsDist,obsVarsPrimeId);
    %end
    restrictedObsFn = OP.restrictN(POMDP.actions(actId).obsFn,obsConfig);

    % update belState
    belStateArray = [];
    for varId = 1:POMDP.nStateVars
      nextBelState(varId) = OP.addMultVarElim([belState,POMDP.actions(actId).transFn,restrictedObsFn],[stateVarsPrimeId([1:varId-1,varId+1:end]),stateVarsId]);
      nextBelState(varId) = OP.approximate(nextBelState(varId), 1e-6, [0]);
      nextBelState(varId) = OP.div(nextBelState(varId),OP.addMultVarElim(nextBelState(varId),stateVarsPrimeId(varId)));
      belStateArray = [belStateArray; OP.convert2array(nextBelState(varId),stateVarsPrimeId(varId))];
    end
    belState = OP.primeVarsN(nextBelState,-POMDP.nVars);

    % add belState to belRegion
    %[simBelId,distance] = findSimilarFactBelief(belState,{belRegion{1:count}},threshold);
    distance = min(max(abs(belRegionArray(:,1:count) - repmat(belStateArray,1,count))));
    if distance > threshold
      count = count + 1;
      belRegion{count} = belState;
      belRegionArray(:,count) = belStateArray;
      if mod(count,10) == 0  
        fprintf('%i belief states sampled\n', count); 
      end
      if count >= desiredSize
        return
      end
    end
    
    sampleCount = sampleCount + 1;
    if sampleCount >= maxSize
      belRegion = {belRegion{1:count}};
      return;
    end

    Global.newHashtables;
  end
end

belRegion = {belRegion{1:count}};
