function [belId,distance] = findSimilarFactBelief(belief,belSet,threshold)
% function [belId,distance] = findSimilarFactBelief(belief,belSet,threshold)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% belief: belief state
% belSet: set of belief states
% threshold: maxnorm threshold used to find similar or identical belief states (default value: 0.001)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% belId: id of some belief state within a distance smaller than the threshold or otherwise id of closest belief state
% distance: distance to the belief state found
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function finds a belief state that is within a distance smaller than
% the threshold specified. The distance is in terms of the max norm.  If
% none of the beliefs have a distance smaller than the threshold, then the
% closest belief is returned with its distance.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if ~exist('threshold','var') | isempty(threshold)
  threshold = 0.001;
end


% compare belState
smallestDist = inf;
closestBelId = 1;
for i = 1:length(belSet)
  maxnorm = -inf;
  for varId = 1:length(belief)
    dist = OP.maxAll(OP.abs(OP.sub(belSet{i}(varId),belief(varId))));
    if dist > maxnorm
      maxnorm = dist;
      if maxnorm >= smallestDist
        break;
      end
    end
  end
  if maxnorm < smallestDist
    smallestDist = maxnorm;
    closestBelId = i;
    if smallestDist <= threshold
      break;
    end
  end
end

belId = closestBelId;
distance = smallestDist;

