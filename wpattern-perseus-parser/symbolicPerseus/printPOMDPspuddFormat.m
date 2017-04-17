function printPOMDPspuddFormat(fileName, ddPOMDP)
% function printPOMDPspuddFormat(fileName, ddPOMDP)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2008)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% fileName: name of the file where the POMDP encoding will be written
% ddPOMDP: pomdp struct encoded with algebraic decision diagrams
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% none
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% Prints to "fileName" the factored POMDP "ddPOMDP" in the SPUDD format.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

fid = fopen(fileName,'w');
indent = '    ';

% print state variables
fprintf(fid,'(variables\n');
for i = 1:ddPOMDP.nStateVars
  fprintf(fid,'%s(%s',indent,ddPOMDP.stateVars(i).name);
  for j = 1:length(ddPOMDP.stateVars(i).valNames)
    fprintf(fid,' %s',ddPOMDP.stateVars(i).valNames{j});
  end
  fprintf(fid,')\n');
end
fprintf(fid,')\n\n');

% print observation variables
fprintf(fid,'(observations\n');
for i = 1:ddPOMDP.nObsVars
  fprintf(fid,'%s(%s',indent,ddPOMDP.obsVars(i).name);
  for j = 1:length(ddPOMDP.obsVars(i).valNames)
    fprintf(fid,' %s',ddPOMDP.obsVars(i).valNames{j});
  end
  fprintf(fid,')\n');
end
fprintf(fid,')\n\n');

% print initial belief state
fprintf(fid,'init (%s)\n\n',char(OP.displaySpuddFormat(ddPOMDP.initialBelState,6)));

% print unnormalised in case of rounding errors
fprintf(fid,'unnormalised\n\n');

% print each action declaration
for actId = 1:ddPOMDP.nActions
  fprintf(fid,'action %s\n',ddPOMDP.actions(actId).name);
  
  % print transition function
  for cptId = 1:length(ddPOMDP.actions(actId).transFn)
    fprintf(fid,'%s%s ',indent,ddPOMDP.stateVars(cptId).name);
    displacement = length(indent) + length(ddPOMDP.stateVars(cptId).name) + 2;
    fprintf(fid,'(%s)\n',char(OP.displaySpuddFormat(ddPOMDP.actions(actId).transFn(cptId),displacement)));
  end
  
  % print observation function
  fprintf(fid,'%sobserve\n',indent);
  for cptId = 1:length(ddPOMDP.actions(actId).obsFn)
    fprintf(fid,'%s%s%s ',indent,indent,ddPOMDP.obsVars(cptId).name);
    displacement = 2*length(indent) + length(ddPOMDP.obsVars(cptId).name) + 2;
    fprintf(fid,'(%s)\n',char(OP.displaySpuddFormat(ddPOMDP.actions(actId).obsFn(cptId),displacement)));
  end  
  fprintf(fid,'%sendobserve\n',indent);
  
  % print cost function
  if length(ddPOMDP.actions(actId).rewFn) == 1
    cost = OP.neg(ddPOMDP.actions(actId).rewFn);
    displacement = length(indent) + 6;
    fprintf(fid,'%scost (%s)\n',indent,char(OP.displaySpuddFormat(cost,displacement)));
  else
    displacement = length(indent) + 9;
    cost = OP.neg(ddPOMDP.actions(actId).rewFn(1));
    fprintf(fid,'%scost [+ (%s)\n',indent,char(OP.displaySpuddFormat(cost,displacement)));
    for rewId = 2:length(ddPOMDP.actions(actId).rewFn)-1
      cost = OP.neg(ddPOMDP.actions(actId).rewFn(rewId));
      fprintf(fid,'%s        (%s)\n',indent,char(OP.displaySpuddFormat(cost,displacement)));
    end
    cost = OP.neg(ddPOMDP.actions(actId).rewFn(end));
    fprintf(fid,'%s        (%s)]\n',indent,char(OP.displaySpuddFormat(cost,displacement)));
  end

  fprintf(fid,'endaction\n\n');
end

% print discount
fprintf(fid,'discount %f\n\n', ddPOMDP.discFact);

% print tolerance
fprintf(fid,'tolerance %f\n\n', ddPOMDP.tolerance);

fclose(fid);