function convertPOMDP2CassandraFormat(ddPOMDP,fileName)
%function convertPOMDP2CassandraFormat(ddPOMDP,fileName)
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Author: Pascal Poupart (Copyright 2007)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Inputs:
%
% ddPOMDP: a factored POMDP encoded with decision diagrams
% fileName: name of the file in which the converted POMDP problem will be written
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Outputs:
%
% none
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Comments:
%
% This function converts a factored POMDP encoded with decision diagrams into the
% Cassandra format (flat POMDP).  To keep the flat encoding small, only the
% non-zero probabilities and rewards are written (i.e., sparse encoding).
% However, for large problems the resulting encoding may exceed the amount
% of RAM of current computers (i.e., more than a few Gigabytes).  For these
% problems, only a factored encoding is viable.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

nStates = prod([ddPOMDP.stateVars.arity]);
nObs = prod([ddPOMDP.obsVars.arity]);
nAct = length(ddPOMDP.actions);

stateVarsId = [ddPOMDP.stateVars.id];
stateVarsPrimeId = stateVarsId + ddPOMDP.nVars;
obsVarsId = [ddPOMDP.obsVars.id];
obsVarsPrimeId = obsVarsId + ddPOMDP.nVars;

discFact = ddPOMDP.discFact;
initBel = OP.convert2array(ddPOMDP.initialBelState,[stateVarsId]);

fid = fopen(fileName,'w');
fprintf(fid,'discount: %f\n', discFact);
fprintf(fid,'values: reward\n');
fprintf(fid,'states: %i\n', nStates);
fprintf(fid,'actions: %i\n', nAct);
fprintf(fid,'observations: %i\n', nObs);

fprintf(fid,'\nstart:');
fprintf(fid,' %i',initBel);
fprintf(fid,'\n');

transMat = zeros(nStates,nStates);
obsMat = zeros(nStates,nObs);
for actId = 1:length(ddPOMDP.actions)
actId =actId

  fprintf(fid,'\n');
  transFn = OP.convert2array(OP.multN(ddPOMDP.actions(actId).transFn),[stateVarsId,stateVarsPrimeId]);
  transMat(:) = transFn;
  for stateId = 1:nStates
    for nextStateId = 1:nStates
      if transMat(stateId,nextStateId) ~= 0
        fprintf(fid,'T : %i : %i : %i %f\n',actId-1,stateId-1,nextStateId-1,transMat(stateId,nextStateId));
      end
    end
  end

  fprintf(fid,'\n');
  obsFn = OP.convert2array(OP.multN(ddPOMDP.actions(actId).obsFn),[stateVarsPrimeId,obsVarsPrimeId]);
  obsMat(:) = obsFn;
  for stateId = 1:nStates
    for obsId = 1:nObs
      if obsMat(stateId,obsId) ~= 0
        fprintf(fid,'O : %i : %i : %i %f\n',actId-1,stateId-1,obsId-1,obsMat(stateId,obsId));
      end
    end
  end

  fprintf(fid,'\n');
  rewFn(actId,:) = OP.convert2array(OP.addN(ddPOMDP.actions(actId).rewFn),[stateVarsId]);
  for stateId = 1:nStates
    if rewFn(actId,stateId) ~= 0 
      fprintf(fid,'R: %i : %i : * : * %f\n',actId-1,stateId-1,rewFn(actId,stateId));
    end
  end
end


fclose(fid);

