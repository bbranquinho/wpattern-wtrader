function belRegion = reachableBelRegionMDPpolicy(initBelState,POMDP,MDPvalFn,MDPpolicy,desiredSize,maxSize,episodeLength,threshold,explorProb)
% function belRegion = reachableBelRegionMDPpolicy(initBelState,POMDP,MDPvalFn,MDPpolicy,desiredSize,maxSize,episodeLength,threshold,explorProb)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% initBelState: initial belief state
% POMDP: pomdp problem
% MDPvalFn: set of alpha vectors corresponding to the q-functions
% MDPpolicy: action coresponding to each q-function
% desiredSize: desired number of distinct reachable belief states that will be retained (default value: 300)
% maxSize: maximum number of reachable belief states that will be sampled (default value: 1000)
% episodeLength: number of belief states sampled in an episode before re-starting (default value: 50)
% threshold: maxnorm threshold used to reject similar or identical belief states (default value: 0.001)
% explorProb: probability with which actions are selected at random (defaut value: 0)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% belRegion: set of reachable belief states
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The algorithm samples trajectories (of 50 time steps by executing 
% actions selected at random with probability explorProb and otherwise 
% according to an MDP policy) to collect reachable belief states.  The MDP 
% policy is executed by selecting actions based on the underlying state.  
% Since this is performed in simulation, we have access to the underlying 
% state. At most 'maxSize' beliefs are sampled and the procedure is stopped 
% when 'desiredSize' distinct beliefs are sampled. Beliefs with a maxnorm 
% difference less than "threshold" compared to the previous beliefs are 
% rejected.
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

if ~exist('explorProb','var') | isempty(explorProb)
  explorProb = 0;
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


nextBelState = javaArray('DD',POMDP.nStateVars);
count = 1;
sampleCount = 1;
belRegion = cell(1,desiredSize);
belRegion{count} = initBelState;
belRegionArray = zeros(sum([POMDP.stateVars.arity]),desiredSize);
belRegionArray(:,count) = initBelStateArray;
while sampleCount < maxSize & count < desiredSize

  belState = initBelState;
  stateConfig = OP.sampleMultinomial(belState,stateVarsId);
    
  for stepId = 1:episodeLength
    %stepId = stepId
      
    % sample action
    choice = sampleMultinomial([1-explorProb,explorProb]);
    if choice == 1
      [actId, alphaRatings] = policyQuery(stateVarsId,Config.convert2dd(stateConfig),MDPvalFn,MDPpolicy);
    else
      actId = sampleMultinomial(ones(1,POMDP.nActions));
    end
    %POMDP.actions(actId).name
    
    % sample observation
    restrictedTransFn = OP.restrictN(POMDP.actions(actId).transFn, stateConfig); 
    nextStateConfig = OP.sampleMultinomial(restrictedTransFn,stateVarsPrimeId);
    obsDistn = OP.restrictN(POMDP.actions(actId).obsFn, [stateConfig, nextStateConfig]);
    obsConfig = OP.sampleMultinomial(obsDistn, obsVarsPrimeId);
    restrictedObsFn = OP.restrictN(POMDP.actions(actId).obsFn,obsConfig);
    %Global.valNames(obsConfig(1,1),obsConfig(2,1))
    
    % compute next belState
    nextBelStateArray = [];
    for varId = 1:POMDP.nStateVars
      nextBelState(varId) = OP.addMultVarElim([belState,POMDP.actions(actId).transFn,restrictedObsFn],[stateVarsPrimeId([1:varId-1,varId+1:end]),stateVarsId]);
      nextBelState(varId) = OP.approximate(nextBelState(varId), 1e-6, [0]);
      nextBelState(varId) = OP.div(nextBelState(varId),OP.addMultVarElim(nextBelState(varId),stateVarsPrimeId(varId)));
      nextBelStateArray = [nextBelStateArray; OP.convert2array(nextBelState(varId),stateVarsPrimeId(varId))];
    end
    
    % make sure that belief state has changed
    if stepId > 1 & max(abs(nextBelStateArray - belStateArray)) < threshold
      break;
    end
    
    % update belState
    belStateArray = nextBelStateArray;
    belState = OP.primeVarsN(nextBelState,-POMDP.nVars);
    stateConfig = Config.primeVars(nextStateConfig,-POMDP.nVars);

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

    % add state to belRegion
    factState = javaArray('DD',POMDP.nStateVars);
    factStateArray = [];
    for varId = 1:POMDP.nStateVars
      factState(varId) = Config.convert2dd(stateConfig(:,varId));
      factStateArray = [factStateArray; OP.convert2array(factState(varId),stateVarsId(varId))];
    end
    %[simBelId,distance] = findSimilarFactBelief(factState,{belRegion{1:count}},threshold);
    distance = min(max(abs(belRegionArray(:,1:count) - repmat(factStateArray,1,count))));
    if distance > threshold
      count = count + 1;
      belRegion{count} = factState;
      belRegionArray(:,count) = factStateArray;
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

    % stop when handsWashed=yes (for hierarchicalHandwashing4.txt)
    %children = belState(end).getChildren;
    %if (children(1).getVal > 0.99)
    %  break;
    %end
    Global.newHashtables;
  end
end

belRegion = {belRegion{1:count}};
