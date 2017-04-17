function [alphaVectors,policy] = pureStrategies(POMDP)
% function [alphaVectors,policy] = pureStrategies(POMDP)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: a POMDP problem
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% alphaVectors: set of alpha vectors corresponding to the value of each pure strategy
% policy: array associating each alphaVector to its corresponding action
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function computes the value function of each pure strategy (i.e.,
% strategy that always executes the same action).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

alphaVectors = [];
policy = [];
dd_discount = DDleaf.myNew(POMDP.discFact);
for actId = 1:length(POMDP.actions)
  fprintf('Computing pure strategy for action %s\n',POMDP.actions(actId).name);
  newAlpha = DD.zero;
  bellmanErr = POMDP.tolerance;
  for i = 1:50
    prevAlpha = newAlpha;
    newAlpha = OP.primeVars(newAlpha,POMDP.nVars);
    newAlpha = OP.addMultVarElim([dd_discount,POMDP.actions(actId).transFn,newAlpha],[POMDP.stateVars.id]+POMDP.nVars);
    newAlpha = OP.addN([POMDP.actions(actId).rewFn, newAlpha]);
    newAlpha = OP.approximate(newAlpha,bellmanErr*(1-POMDP.discFact)/2,[0]);
    bellmanErr = OP.maxAll(OP.abs(OP.sub(newAlpha,prevAlpha)));
    if bellmanErr <= POMDP.tolerance
      break;  
    end
    Global.newHashtables;
  end
     
  % add vector only if not dominated
  dominated = 0;
  for alphaId = 1:length(alphaVectors)
    if OP.maxAll(OP.sub(newAlpha,alphaVectors(alphaId))) < POMDP.tolerance
      dominated = 1;
      break;
    end
  end
  if ~dominated
    alphaVectors = [alphaVectors,newAlpha];
    policy = [policy,actId];
  end
end