function [bestActId,alphaRating] = policyQuery(stateVars, belState, alphaVectors, policy)
% function [bestActId,alphaRating] = policyQuery(stateVars, belState, alphaVectors, policy)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% stateVars: state variables
% belState: current belief state
% alphaVectors: value function
% policy: list of actions for each alphaVector
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% bestActId: best action
% alphaRating: value and action for each alpha vector
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Computes the best action for a given belief state according to a set of alphaVectors
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% single DD belief state
if isa(belState,'DD')
  bestVal = -inf;
  for alphaId = 1:length(alphaVectors)
    val = OP.dotProduct(belState, alphaVectors(alphaId), stateVars);
    alphaRating(alphaId).value = val;
    alphaRating(alphaId).action = policy(alphaId);
    if val > bestVal
      bestVal = val; 
      bestAlphaId = alphaId;
    end
  end
  bestActId = policy(bestAlphaId);
  
% must be a factored belief state  
else
  values = OP.factoredExpectationSparseNoMem(belState,alphaVectors);
  bestVal = -inf;
  for alphaId = 1:length(alphaVectors)
    alphaRating(alphaId).value = values(alphaId);
    alphaRating(alphaId).action = policy(alphaId);
    if values(alphaId) > bestVal
      bestVal = values(alphaId); 
      bestAlphaId = alphaId;
    end
  end
  bestActId = policy(bestAlphaId);
end
