function [alphaVectors, pointBasedValues, actions, obsStrats, witnessIds] = trim(alphaVectors, pointBasedValues, actions, obsStrats, witnessIds, maxSize, minImprovement);
% function [alphaVectors, pointBasedValues, actions, obsStrats, witnessIds] = trim(alphaVectors, pointBasedValues, actions, obsStrats, witnessIds, maxSize, minImprovement);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% alphaVectors: matrix of alphaVectors
% pointBasedValues: set of belief points
% actions: best actions for each alphaVector
% obsStrats: ids of the children conditional plan in the induced policy graph
% witnessIds: ids of the witness beliefs
% maxSize: maximum number of alphaVectors after trimming
% minImprovement: minimum improvement that each alphaVector must produce at some belief point
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% alphaVectors: trimmed set of alphaVectors
% pointBasedValues: best value at each belief point
% actions: best actions at each alphaVector
% obsStrats: ids of the children conditional plan in the induced policy graph
% witnessIds: ids of the witness beliefs
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The function trims the set of alphaVectors by removing all those that do not 
% improve the value function by at least minImprovement (when compared to the 
% remaining alphaVectors).  If there are more alphaVectors left than maxSize, then 
% only the top maxSize alphaVectors are kept.  Those top alphaVectors are the ones 
% that improve the value function the most at some belief point.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

nVectors = length(alphaVectors);
if nVectors <= 1
  return;
end

for i = 1:nVectors
  improvement(i) = max(pointBasedValues(:,i) - max(pointBasedValues(:,[1:i-1,i+1:nVectors]),[],2));
  if improvement(i) < minImprovement
    alphaVectors = alphaVectors([1:i-1,i+1:nVectors]);
    pointBasedValues = pointBasedValues(:,[1:i-1,i+1:nVectors]);
    actions = actions([1:i-1,i+1:nVectors]);
    obsStrats = obsStrats([1:i-1,i+1:nVectors],:);
    witnessIds = witnessIds([1:i-1,i+1:nVectors]);
    [alphaVectors, pointBasedValues, actions, obsStrats, witnessIds] = trim(alphaVectors, pointBasedValues, actions, obsStrats, witnessIds, maxSize, minImprovement);
    return;
  end
end

if nVectors > maxSize
  [minImprovement, i] = min(improvement);
  alphaVectors = alphaVectors([1:i-1,i+1:nVectors]);
  pointBasedValues = pointBasedValues(:,[1:i-1,i+1:nVectors]);
  actions = actions([1:i-1,i+1:nVectors]);
  obsStrats = obsStrats([1:i-1,i+1:nVectors],:);
  witnessIds = witnessIds([1:i-1,i+1:nVectors]);
  %[alphaVectors, pointBasedValues, actions, obsStrats, witnessIds] = trim(alphaVectors, pointBasedValues, actions, obsStrats, witnessIds, maxSize, minImprovement);
end
