function [qFn, policy] = solveQMDP(ddPOMDP)
% function [qFn, policy] = solveQMDP(ddPOMDP)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% ddPOMDP: factored POMDP problem
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% qFn: set of alpha vectors (one per action)
% policy: array of actions (corresponding to the array of alpha vectors)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% The function computes the optimal Q-function of the underlying MDP.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

stateVarsPrimeId = [ddPOMDP.stateVars.id]+ddPOMDP.nVars;
%for actId = 1:ddPOMDP.nActions
%  stochVars{actId} = setdiff(stateVarsPrimeId,ddPOMDP.actions(actId).backwardDetVarMapping(1,:));
%end

fprintf('\nComputing qMDP value function\n');
qFn = javaArray('DD',ddPOMDP.nActions);
%qFn2 = javaArray('DD',ddPOMDP.nActions);
valFn = DD.zero;
policy = 1:ddPOMDP.nActions;
ddDiscFact = DDleaf.myNew(ddPOMDP.discFact);
bellmanErr = ddPOMDP.tolerance;
for i = 1:50
  prevValFn = valFn;
  valFn = OP.primeVars(valFn,ddPOMDP.nVars);
  for actId = 1:ddPOMDP.nActions
    %valFn2 = OP.swapVarsNoReordering(valFn,ddPOMDP.actions(actId).backwardDetVarMapping);
    %valFn2 = OP.reorder(valFn2);
    %Global.newHashtables;
    %qFn2(actId) = OP.addMultVarElim([ddDiscFact,ddPOMDP.actions(actId).backwardStochTransFn,valFn2],stochVars{actId});
    %Global.newHashtables;
    qFn(actId) = OP.addMultVarElim([ddDiscFact,ddPOMDP.actions(actId).transFn,valFn],stateVarsPrimeId);
    %err = OP.maxAll(OP.abs(OP.sub(qFn(actId), qFn2(actId))));
    %if err > 1e-5
    %    err
    %    keyboard
    %end
    qFn(actId) = OP.add(ddPOMDP.actions(actId).rewFn,qFn(actId));
    qFn(actId) = OP.approximate(qFn(actId),bellmanErr*(1-ddPOMDP.discFact)/2,[0]);
  end
  valFn = OP.maxN(qFn);
  bellmanErr = OP.maxAll(OP.abs(OP.sub(valFn,prevValFn)));
  fprintf('Bellman Error: %f\n',bellmanErr);
  if bellmanErr <= ddPOMDP.tolerance
    break;
  end
  Global.newHashtables;
end

% remove dominated alphaVectors
notDominated = [];
for actId1 = 1:ddPOMDP.nActions
  dominated = 0;
  for actId2 = notDominated
    if OP.maxAll(OP.sub(qFn(actId1), qFn(actId2))) < ddPOMDP.tolerance
      dominated = 1;
      break;
    end
  end
  if ~dominated
    notDominated = [notDominated, actId1];
  end
end
qFn = qFn(notDominated);
policy = policy(notDominated);

[actId,actName,value] = queryPolicy(ddPOMDP.initialBelState, qFn, policy, ddPOMDP);
fprintf('qMDP init value: %f\n',value);
%fprintf('# of alpha vectors: %i\n',length(qFn));
