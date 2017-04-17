function ddPOMDP = pomdpNetworkFlatAct(server, parentMachine)
% function ddPOMDP = pomdpNetworkFlatAct(server, parentMachine)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2008)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% server: id of the machine, which is the server in the network (keeping 
%         the server running yields more rewards than regular machines).  
% parentMachine: cellarray of parent ids (parentMachine{i} is the id that
%                machine i depends on in the network)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% ddPOMDP: pomdp struct (factored POMDP) encoded with algebraic decision diagrams
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Generates a POMDP struct (factored POMDP) of a network problem (such as 
% the cycle and 3leg networks) described in:
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

%%%%%%%%%%%%%%%%% State variables %%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.nStateVars = length(parentMachine);

for i = 1:ddPOMDP.nStateVars
  ddPOMDP.stateVars(i).arity = 2;
  ddPOMDP.stateVars(i).name = sprintf('m%i',i);
  ddPOMDP.stateVars(i).id = i;
  ddPOMDP.stateVars(i).valNames = {'down','up'};
end

%%%%%%%%%%%%%%%%% Observation variables %%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.nObsVars = 1;

ddPOMDP.obsVars(1).arity = 2;
ddPOMDP.obsVars(1).name = 'status';
ddPOMDP.obsVars(1).id = ddPOMDP.nStateVars+1;
ddPOMDP.obsVars(1).valNames = {'oDown','oUp'};

%%%%%%%%%%%%%%%%%% Globals %%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.nVars = ddPOMDP.nStateVars + ddPOMDP.nObsVars;

Global.setVarDomSize([ddPOMDP.stateVars(:).arity, ddPOMDP.obsVars(:).arity, ... 
        	      ddPOMDP.stateVars(:).arity, ddPOMDP.obsVars(:).arity]);

varNames = {ddPOMDP.stateVars(:).name, ddPOMDP.obsVars(:).name};
for i = 1:ddPOMDP.nVars
  varNames = {varNames{:},[varNames{i},char(39)]};
end
Global.setVarNames(varNames);

for i = 1:ddPOMDP.nStateVars
	Global.setValNames(i,ddPOMDP.stateVars(i).valNames);
end
for i = 1:ddPOMDP.nObsVars
	Global.setValNames(ddPOMDP.nStateVars+i,ddPOMDP.obsVars(i).valNames);
end
for i = 1:ddPOMDP.nStateVars
	Global.setValNames(ddPOMDP.nVars+i,ddPOMDP.stateVars(i).valNames);
end
for i = 1:ddPOMDP.nObsVars
	Global.setValNames(ddPOMDP.nVars+ddPOMDP.nStateVars+i,ddPOMDP.obsVars(i).valNames);
end


%%%%%%%%%%%%%%%%% Transition function %%%%%%%%%%%%%%%%%%%%%

% reboot machine i
% ping machine i
% do nothing

ddPOMDP.nActions = 2*ddPOMDP.nStateVars + 1;

% reboot machine i
for actId = 1:ddPOMDP.nActions
  if actId <= ddPOMDP.nStateVars
    ddPOMDP.actions(actId).name = sprintf('reboot_m%i',actId);
  elseif actId <= 2*ddPOMDP.nStateVars
    ddPOMDP.actions(actId).name = sprintf('ping_m%i',actId - ddPOMDP.nStateVars);
  else
    ddPOMDP.actions(actId).name = 'doNothing';
  end
  
  for machineId = 1:ddPOMDP.nStateVars

    % machine is rebooted
    if actId == machineId
      ddPOMDP.actions(actId).transFn(machineId) = ...
        DDnode.myNew(machineId+ddPOMDP.nVars,[DDleaf.myNew(0.05),DDleaf.myNew(0.95)]);

    % machine has no parent
    elseif isempty(parentMachine{machineId})
      ddPOMDP.actions(actId).transFn(machineId) = ...
        DDnode.myNew(machineId+ddPOMDP.nVars, ...
          [DDnode.myNew(machineId,[DDleaf.myNew(0.99), DDleaf.myNew(0.1)]), ... %0.1
           DDnode.myNew(machineId,[DDleaf.myNew(0.01), DDleaf.myNew(0.9)])]);  %0.9
    
    % machine has a parent
    else
      ddPOMDP.actions(actId).transFn(machineId) = ...
        DDnode.myNew(machineId+ddPOMDP.nVars, ...
          [OP.reorder(DDnode.myNew(machineId, ...
             [DDleaf.myNew(0.99), ...
							DDnode.myNew(parentMachine{machineId}(1), ...
                [DDleaf.myNew(0.333), ...  %0.333
                 DDleaf.myNew(0.1)])])), ... %0.1
           OP.reorder(DDnode.myNew(machineId, ...
             [DDleaf.myNew(0.01), ...
							DDnode.myNew(parentMachine{machineId}(1), ...
                [DDleaf.myNew(0.667), ...  %0.667
                 DDleaf.myNew(0.9)])]))]); %0.9

    end
  end
end

%%%%%%%%%%%%%%%%% Observation function %%%%%%%%%%%%%%%%%%%%%

for actId = 1:ddPOMDP.nActions

  % observe ping'ed machine
  if actId > ddPOMDP.nStateVars & actId <= 2*ddPOMDP.nStateVars
    ddPOMDP.actions(actId).obsFn(1) = ...
      DDnode.myNew(ddPOMDP.obsVars(1).id + ddPOMDP.nVars, ...
        [DDnode.myNew(actId+1, [DDleaf.myNew(0.95),DDleaf.myNew(0.05)]),...
         DDnode.myNew(actId+1, [DDleaf.myNew(0.05),DDleaf.myNew(0.95)])]);

  % no observation
  else 
    ddPOMDP.actions(actId).obsFn(1) = ...
      DDnode.myNew(ddPOMDP.obsVars(1).id + ddPOMDP.nVars, [DD.one,DD.zero]);
  end
end

%%%%%%%%%%%%%%%%%%% Reward function %%%%%%%%%%%%%%%%%%%%%%

for actId = 1:ddPOMDP.nActions

  % reward for each machine that's up
  %OP.actions(actId).rewFn = DD.zero;
  for machineId = 1:ddPOMDP.nStateVars

    if machineId == server
      ddPOMDP.actions(actId).rewFn(machineId) = DDnode.myNew(machineId,[DD.zero,DDleaf.myNew(2)]);  

    else
      ddPOMDP.actions(actId).rewFn(machineId) = DDnode.myNew(machineId,[DD.zero,DD.one]);  
    end
  end
  
  % cost for rebooting
  if actId <= ddPOMDP.nStateVars
    ddPOMDP.actions(actId).rewFn = [ddPOMDP.actions(actId).rewFn, DDleaf.myNew(-2.5)];

  % cost for ping'ing
  elseif actId <= 2*ddPOMDP.nStateVars
    ddPOMDP.actions(actId).rewFn = [ddPOMDP.actions(actId).rewFn, DDleaf.myNew(-0.1)];
  end
end

%%%%%%%%%%%%%%%%%% Discount Factor %%%%%%%%%%%%%%%%%%%%%%%%

ddPOMDP.discFact = 0.95;

%%%%%%%%%%%%%%%%%% Initial Belief State %%%%%%%%%%%%%%%%%%%%%%%%

ddInit = DD.one;
for machineId = 1:ddPOMDP.nStateVars
  ddInit = OP.mult(ddInit, DDnode.myNew(machineId,[DD.zero,DD.one]));
end
ddPOMDP.initialBelState = ddInit;

%%%%%%%%%%%%%%%%%% Tolerance %%%%%%%%%%%%%%%%%%%%%%%%

maxVal = -inf;
minVal = inf;
for actId = 1:ddPOMDP.nActions
  maxVal = max(maxVal,OP.maxAll(OP.addN(ddPOMDP.actions(actId).rewFn)));
  minVal = min(minVal,OP.minAll(OP.addN(ddPOMDP.actions(actId).rewFn)));
end
maxDiffRew = maxVal - minVal;
maxDiffVal = maxDiffRew/(1-min(0.95,ddPOMDP.discFact));
ddPOMDP.tolerance = 1e-5 * maxDiffVal;

%for actId = 1:ddPOMDP.nActions
%  ddPOMDP.actions(actId).rewFn = OP.approximate(ddPOMDP.actions(actId).rewFn, ddPOMDP.tolerance, [0]);
%end
