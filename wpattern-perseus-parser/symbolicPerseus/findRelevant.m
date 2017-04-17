function [relevantCPTs,relevantVarIds] = findRelevant(cpts,varIds)
% function [relevantCPTs,relevantVarIds] = findRelevant(cpts,varIds)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% cpts: array of conditional probability tables
% varIds: initial set of relevant variables
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% relevantCPTs: all CPTs of the relevant variables
% relevantVarIds: varIds and their ancestors
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function finds the variables and CPTs that are relevant to an
% inference query on varIds (assuming no evidence)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

relevantVarIds = [];
while ~isempty(varIds)
  if isempty(find(relevantVarIds == varIds(1)))
    relevantVarIds = [relevantVarIds, varIds(1)];
    varIds = [varIds; cpts(varIds(1)).getVarSet];
  end
  varIds = varIds(2:end);
end
relevantCPTs = cpts(relevantVarIds);

