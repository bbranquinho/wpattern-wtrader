function [newAlphaVectors,newActions,newObsStrat,witnessIds,stepId] = boundedPerseus(POMDP, belRegion, alphaVectors, policy, stepRange, maxAlphaSetSize, bellmanErr, name)
% function [newAlphaVectors,newActions,newObsStrat,witnessIds,stepId] = boundedPerseus(POMDP, belRegion, alphaVectors, policy, stepRange, maxAlphaSetSize, bellmanErr, name)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: a POMDP problem
% belRegion: a set of reachable belief states
% alphaVectors: set of alpha vectors corresponding to the initial value function (default value: value of the pure policies i.e., policies where the same action is always executed)
% policy: action fpr each alpha vector
% stepRange: number of iterations (default value: 1:50)
% maxAlphaSetSize: bound on the number of alpha vectors (default value: 200)
% bellmanErr: bellmanError (default value: 20 * POMDP.tolerance)
% name: problem name (default value: 'boundedPerseus')
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% newAlphaVectors: set of alpha vectors corresponding to the final value function
% newActions: best action for each alpha vector
% newObsStrat: best choice of conditional plan for each observation in the induced policy graph
% witnessIds: ids of the witness beliefs
% stepId: id of the last step performed
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function implements the Perseus algorithm [Vlassis & Spaan]
% with algebraic decision diagrams (ADDs).  It is further optimized by 
% approximating alphaVectors at each iteration (using DD.approximate) in 
% a similar way to APRICODD [Hoey, St-Aubin, & al.].  Note however that 
% this approximation does not impact convergence since the largest error 
% introduced is bounded to a fraction of Bellman error. An additional
% speed up is obtained by bounding the number of alpha vectors (default 
% value: 200).  In other words, the Bellman backup is stopped when 200 
% alpha vectors are generated.
% Note also that the dynamic programming backup (in dpBackup.m) is also 
% fairly optimized (see comments in dpBackup.m for more details) and the
% ADD operations are optimized as much as possible to take advantage of
% sparsity and factored structure.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% initial value function
if ~exist('alphaVectors','var') | isempty(alphaVectors)
  fprintf('\nInitializing value function to pure strategies\n');
  [alphaVectors, policy] = pureStrategies(POMDP);
end

% estimate of the bellman error
if ~exist('bellmanErr','var') | isempty(bellmanErr)
  bellmanErr = 20*POMDP.tolerance;
end

% number of iterations
if ~exist('stepRange','var') | isempty(stepRange)
  stepRange = 1:50;
end

% maximum number of alpha vectors in a value function
if ~exist('maxAlphaSetSize','var') | isempty(maxAlphaSetSize)
  maxAlphaSetSize = 200;
end

% problem name
if ~exist('name','var') | isempty(name)
  name = 'boundedPerseus'; 
end

global nextBelStates
nextBelStates = cell(1,length(belRegion));

% compute current point-based values
belRegionArray = javaArray('DD[]',length(belRegion));
for i = 1:length(belRegion)
  belRegionArray(i) = belRegion{i}; 
end
currentPointBasedValues = OP.factoredExpectationSparseNoMem(belRegionArray,alphaVectors);

startTime = cputime;

stepRange
for stepId = stepRange
  stepId = stepId
  newAlphaVectors = [];
  newPointBasedValues = [];
  newActions = [];
  newObsStrat = [];
  witnessIds = [];
  
  tolerance = POMDP.tolerance;
  
  % precompute primedV
  primedV = javaArray('DD',length(alphaVectors));
  for i = 1:length(alphaVectors)
    primedV(i) = OP.primeVars(alphaVectors(i),POMDP.nVars);
  end
  maxAbsVal = max(abs([OP.maxAllN(alphaVectors);OP.minAllN(alphaVectors);1e-10]));

  count = 0;
  nDpBackups = 0;
  permutedIds = randperm(length(belRegion));
  while ~isempty(permutedIds)
    if nDpBackups >= 2*length(alphaVectors)
      bestImprovement = max(max(newPointBasedValues,[],2) - max(currentPointBasedValues,[],2));
      worstDecline = min(max(newPointBasedValues,[],2) - max(currentPointBasedValues,[],2));
      if bestImprovement > tolerance & bestImprovement > -2*worstDecline
        break;
      end
    end
    Global.newHashtables;
    count = count + 1;
    if mod(count,100) == 0 
      count = count
    end    

    if isempty(newPointBasedValues)
      choice = 1;
    else
      diff = max(currentPointBasedValues(permutedIds,:),[],2) - max(newPointBasedValues(permutedIds,:),[],2) + tolerance;
      nnz = find(diff > 0);
      permutedIds = permutedIds(nnz);
      diff = diff(nnz);
      if isempty(permutedIds)
        break;
      end
      choice = sampleMultinomial(max(diff,0));
    end
    i = permutedIds(choice);
    permutedIds = permutedIds([1:choice-1,choice+1:end]);
    
    if length(newAlphaVectors) < 1 | max(newPointBasedValues(i,:)) - max(currentPointBasedValues(i,:)) < tolerance

      % dpBackup
      [newVector, newVal, actId, obsStrat] = dpBackup(POMDP, belRegion{i}, alphaVectors, primedV, maxAbsVal);
      newVector = OP.approximate(newVector, bellmanErr*(1-POMDP.discFact)/2, [0]);
      fprintf('%i edges, %i nodes, %i leaves\n',OP.nEdges(newVector),OP.nNodes(newVector),OP.nLeaves(newVector));
      nDpBackups = nDpBackups + 1;

      % merge and trim
      newValues = OP.factoredExpectationSparseNoMem(belRegionArray,newVector);
      if length(newAlphaVectors) < 1
        improvement = inf;
      else
        improvement = max(newValues - max(newPointBasedValues,[],2));
      end
      if improvement > tolerance
        newAlphaVectors = [newAlphaVectors, newVector];
        newPointBasedValues = [newPointBasedValues, newValues];
        newActions = [newActions, actId];
        witnessIds = [witnessIds, i];
        newObsStrat = [newObsStrat; obsStrat];

        %improvement = max(newPointBasedValues(i,:)) - max(currentPointBasedValues(i,:));
        %tolerance = max(1e-10,min(tolerance, improvement));
        %[newAlphaVectors, newPointBasedValues, newActions, newObsStrat, witnessIds] = trim(newAlphaVectors, newPointBasedValues, newActions, newObsStrat, witnessIds, maxAlphaSetSize, tolerance); 
        if length(newAlphaVectors) >= maxAlphaSetSize
          break;
        end
      end
    end
  end

  %% merge current alphaVectors that would induce a decline if removed
  %for currentAlphaId = 1:length(alphaVectors)
  %  possibleImprovement = max(currentPointBasedValues(:,currentAlphaId) - max(newPointBasedValues,[],2));
  %  if possibleImprovement > tolerance 
  %    newPointBasedValues = [newPointBasedValues, currentPointBasedValues(:,currentAlphaId)];
  %    newAlphaVectors = [newAlphaVectors, alphaVectors(currentAlphaId)];
  %    newActions = [newActions, policy(currentAlphaId)];
  %  end
  %end
  
  % statistics
  bestImprovement = max(max(newPointBasedValues,[],2) - max(currentPointBasedValues,[],2));
  worstDecline = min(max(newPointBasedValues,[],2) - max(currentPointBasedValues,[],2));
  initValue = max(newPointBasedValues(1,:));
  initImprovement = max(newPointBasedValues(1,:)) - max(currentPointBasedValues(1,:));
  fprintf('\nbestImprovement = %f  worstDecline = %f  initImprovement = %f  initValue = %f\n',bestImprovement, worstDecline, initImprovement, initValue);
  fprintf('# of vectors = %i, # of DP backups = %i\n',length(newAlphaVectors), nDpBackups);
  alphaVectors = newAlphaVectors;
  currentPointBasedValues = newPointBasedValues;
  bellmanErr = min(10,max(bestImprovement,-worstDecline));

  % save data 
  policy = newActions;
  obsStrategy = newObsStrat;
  valueFunction = javaArray('DD',length(alphaVectors));
  for i = 1:length(alphaVectors)
    valueFunction(i) = alphaVectors(i);
  end
  ddPOMDP = POMDP;
  runTime = cputime - startTime;
  fileName = sprintf('%s_%ibel_%iiter_%isize.mat',name,length(belRegion),stepId,maxAlphaSetSize);
  save(fileName,'valueFunction','policy','obsStrategy','ddPOMDP','runTime','belRegion','bellmanErr','maxAlphaSetSize','witnessIds');
  %OP.printPolicySpuddFormat(fileName,valueFunction,policy)
  
  if stepId >= stepRange(1)+5 & (bestImprovement <= -worstDecline | bestImprovement <= tolerance)
    break;
  end
end

newAlphaVectors = valueFunction;
