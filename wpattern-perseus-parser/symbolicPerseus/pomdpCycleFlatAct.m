function ddPOMDP = pomdpCycleFlatAct(n)
% function ddPOMDP = pomdpCycleFlatAct(n)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2008)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% n: number of machines in the cycle.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% ddPOMDP: pomdp struct (factored POMDP) encoded with algebraic decision diagrams
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Generates a POMDP struct (factored POMDP) of the cycle network problem 
% described in:
%
% VDCBPI: an Approximate Scalable Algorithm for Large Scale POMDPs
% Pascal Poupart and Craig Boutilier
% In Advances in Neural Information Processing Systems 17 (NIPS), pages 1081-1088, Vancouver, BC, 2004
% http://www.cs.uwaterloo.ca/~ppoupart/publications/publications.html
%
% and
% 
% Exploiting Structure to Efficiently Solve Large Scale Partially Observable Markov Decision Processes
% Pascal Poupart
% Ph.D. thesis, Department of Computer Science, University of Toronto, Toronto, 2005
% http://www.cs.uwaterloo.ca/~ppoupart/publications/publications.html
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

parentMachine = cell(n,1);
for i = 1:n
  parentMachine{i} = [i-1];
end
parentMachine{1} = [n];
server = 1;

ddPOMDP = pomdpNetworkFlatAct(server, parentMachine);
