function obsConfig = queryObservation(ddPOMDP,actId,belState)
% function obsConfig = queryObservation(ddPOMDP,actId,belState)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2008)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% ddPOMDP: POMDP model
% actId: action id
% belState: current belief state
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% obsConfig: observation variable assignment
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function queries the user for specific observations.  For each 
% observation variable, the user must type the string name of some 
% observation value.  If an invalid string is entered or the observation
% chosen has zero probability, the user will be prompted to enter a new
% observation value.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

obsConfig = [ddPOMDP.obsVars.id]+ddPOMDP.nVars;
for i = 1:ddPOMDP.nObsVars
  obsString{i} = ['\n',ddPOMDP.obsVars(i).name,' = (possible choices are: ',ddPOMDP.obsVars(i).valNames{1}];
  for j = 2:length(ddPOMDP.obsVars(i).valNames)
    obsString{i} = [obsString{i}, ', ', ddPOMDP.obsVars(i).valNames{j}];  
  end
  obsString{i} = [obsString{i},'): '];
end
obsProbs = OP.addMultVarElim([belState,ddPOMDP.actions(actId).transFn,ddPOMDP.actions(actId).obsFn],[ddPOMDP.stateVars.id, [ddPOMDP.stateVars.id]+ddPOMDP.nVars]);

fprintf('\nEnter a value string for each observation variable.\n');
i = 1;
while i <= ddPOMDP.nObsVars
  obsValName = input(obsString{i},'s');
  valId = Global.findValId(obsConfig(1,i), obsValName);
  if valId > 0
    obsConfig(2,i) = valId;
    restObsProbs = OP.restrictOrdered(obsProbs,obsConfig(:,i));
    if OP.maxAll(restObsProbs) < 1e-8
      fprintf('ERROR: this observation has zero probability according to the POMDP model.  Choose another observation.\n');
    else
      obsProbs = restObsProbs;
      i = i + 1;
    end
  end 
end
