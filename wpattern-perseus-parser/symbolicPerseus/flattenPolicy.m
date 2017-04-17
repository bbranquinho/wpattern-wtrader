function [matrixValFn,vectPolicy] = flattenPolicy(POMDP,valFn,policy,fileName)
% [matrixValFn,vectPolicy] = flattenPolicy(POMDP,valFn,policy,fileName)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% POMDP: pomdp problem
% valFn: value function
% policy: policy
% fileName: name of the file in which the flat policy and value function will be written
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% matrixValFn: flat value function stored in a matrix
% vectPolicy: actions for each column vector (alphaVector) in matrixValFn
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Converts an ADD-based value function and policy into a flat matrix-based value 
% function.  The flat value function and policy are written to an ascii file.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

vectPolicy = policy;
matrixValFn = zeros(length(valFn),prod([POMDP.stateVars.arity]));
for alphaId = 1:length(valFn)
  matrixValFn(alphaId,:) = OP.convert2array(valFn(alphaId),[POMDP.stateVars.id]);
end

if exist('fileName','var')
  save(fileName,'vectPolicy','matrixValFn','-ASCII');
end
