function ddPOMDP = pomdp3legsFlatAct(n)
% function ddPOMDP = pomdp3legsFlatAct(n)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2008)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% n: number of machines (since there are 3 legs and 1 server, n must satisfy n % 3 = 1).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% ddPOMDP: pomdp struct (factored POMDP) encoded with algebraic decision diagrams
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Generates a POMDP struct (factored POMDP) of the 3legs network problem 
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

if mod(n,3) ~= 1 
  error('The number n of machines for 3leg networks must satisfy n % 3 = 1');
end

server = 1;  % machine id for the server

m = (n-1)/3;   % m is # of machines in each leg
k = 2+m;       % k is beginning of second leg
l = 2+2*m;     % l is beginning of 3rd leg

parentMachine = cell(n,1);
for i = 1:n
  parentMachine{i} = [i-1]; %by default each machine depends on the previous one
end

% empty indicates no parent
parentMachine{1} = [];

parentMachine{k} = [1];     % machine k depends on server
parentMachine{l} = [1];     % machine l depends on server

ddPOMDP = pomdpNetworkFlatAct(server, parentMachine);
