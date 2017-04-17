function [valueFunction,policy] = solvePOMDP(pomdp, nRounds, nIterations, maxAlphaSetSize, initPolicy, nBelStates, nSampledBelStates, episodeLength, threshold, lookAheadDepth, explorProb)
% function [valueFunction,policy] = solvePOMDP(pomdp, nRounds, nIterations, maxAlphaSetSize, initPolicy, nBelStates, nSampledBelStates, episodeLength, threshold, lookAheadDepth, explorProb)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% pomdp: pomdp problem
% nRounds: number of rounds (default value: 1)
% nIterations: number of iterations per round (default value: 50)
% maxAlphaSetSize: bound on the number of alpha vectors (default value: 200)
% initPolicy: policy initially executed to sample belief points (default value: 'QMDP', other possible values: 'MDP', 'random') 
% nBelStates: number of belief points to be retained (default value: 300)
% nSampledBelStates: maximum number of belief points to be sampled (default value: 1000)
% episodeLength: number of steps in an episode before re-starting when gathering belief states (default value: 50)
% threshold: maxnorm threshold used to reject similar or identical belief states (default value: 0.001)
% lookAheadDepth: depth of the lookahead search when executing the initial policy to gather belief states (default value: 1)
% explorProb: probability with which actions are chosen at random when gathering beliefs (default value: 0)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% valueFunction: set of alphaVectors
% policy: vector of actions for each alphaVector
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The function first parses a pomdp problem, then samples a set of belief points 
% and then executes a bounded, symbolic version of the Perseus algorithm.  
% "Symbolic" refers to the fact that ADDs are used and "bounded" refers to the 
% fact that the number of alphaVectors is bounded. 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if isstruct(pomdp)
  ddPOMDP = pomdp;
  name = [];
else
  ddPOMDP = parsePOMDP(pomdp);
  %nameEnd = strfind(pomdp,'.');
  %if isempty(nameEnd)
  %  nameEnd = length(pomdp);
  %else
  %  nameEnd = nameEnd(1)-1;
  %end
  %name = pomdp(1:nameEnd);
  [pathstr, filename, ext] = fileparts(pomdp); %'fileparts' reported buggy in earlier versions of matlab
  name = fullfile(pathstr, filename);
end

if ~exist('nRounds','var') | isempty(nRounds)
  nRounds = 1;
end

if ~exist('nIterations','var') | isempty(nIterations)
  nIterations = 50;
end

if ~exist('maxAlphaSetSize','var') | isempty(maxAlphaSetSize)
  maxAlphaSetSize = 200;
end

if ~exist('initPolicy','var') | isempty(initPolicy)
  initPolicy = 'QMDP';  
end

if ~exist('nBelStates','var') | isempty(nBelStates)
  nBelStates = 300;
end

if ~exist('nSampledBelStates','var') | isempty(nSampledBelStates)
  nSampledBelStates = 1000;
end

if ~exist('episodeLength','var') | isempty(episodeLength)
  episodeLength = 50;
end

if ~exist('threshold','var') | isempty(threshold)
  threshold = 0.001;
end

if ~exist('lookAheadDepth','var') | isempty(lookAheadDepth)
  lookAheadDepth = 1;  
end

if ~exist('explorProb','var') | isempty(explorProb)
  explorProb = 0;
end

startTime = cputime;

% sample reachable beliefs at random
if explorProb == 1 | strcmp(initPolicy,'random')
  belRegion = reachableBelRegion(ddPOMDP.initialBelState,ddPOMDP,nBelStates,nSampledBelStates,episodeLength,threshold);

% follow MDP policy to sample reachable beliefs
elseif strcmp(initPolicy,'MDP')
  [qMDPvalFn,qMDPpolicy] = solveQMDP(ddPOMDP);
  belRegion = reachableBelRegionMDPpolicy(ddPOMDP.initialBelState,ddPOMDP,qMDPvalFn,qMDPpolicy,nBelStates,nSampledBelStates,episodeLength,threshold,explorProb);
    
% follow QMDP policy
elseif strcmp(initPolicy,'QMDP')
  [qMDPvalFn,qMDPpolicy] = solveQMDP(ddPOMDP);
  belRegion = reachableBelRegionPOMDPpolicy(ddPOMDP.initialBelState,ddPOMDP,qMDPvalFn,qMDPpolicy,nBelStates,nSampledBelStates,episodeLength,threshold,lookAheadDepth,explorProb);

else
  error('"%s" is not a valid value of initPolicy. \n Possible values are: MDP, QMDP, random\n', initPolicy);
end

fprintf('\nrunning symbolic Perseus\n');
[valueFunction,policy,obsStrategy,witnessIds,iterId] = boundedPerseus(ddPOMDP, belRegion, [], [], 1:nIterations,maxAlphaSetSize,[],name);

for roundId = 2:nRounds
  belRegion = reachableBelRegionPOMDPpolicy(ddPOMDP.initialBelState,ddPOMDP,valueFunction,policy,nBelStates,nSampledBelStates,episodeLength,threshold,lookAheadDepth,explorProb);
  [valueFunction,policy,obsStrategy,witnessIds,iterId] = boundedPerseus(ddPOMDP, belRegion, valueFunction, policy, [1:nIterations]+iterId, maxAlphaSetSize, [], name);
end
runTime = cputime - startTime;

if ~isstruct(pomdp)
  fileName = sprintf('%s_%ibel_%iiter_%isize.mat',name,length(belRegion),iterId,maxAlphaSetSize);
  save(fileName,'valueFunction','policy','obsStrategy','ddPOMDP','runTime','belRegion','maxAlphaSetSize','witnessIds');
  fprintf('\nValue function and policy saved in %s\n',fileName);
end

