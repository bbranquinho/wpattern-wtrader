function no_return = matlabSymbolicPerseus(problemFileLocation)
%One link between rddl client code and symbolicPerseus.
%Expected usage: this function should be called by java.lang.Runtime to execute the matlab code found below.
%Example java code:
%   String cmd = "/home/kyle/matlab/bin/matlab -nodesktop -nosplash -r matlabSymbolicPerseus('problems/coffee/coffee3po.txt');";
%   Runtime run = Runtime.getRuntime();
%   Process pr = run.exec(cmd);
%

cd symbolicPerseus;
javaaddpath ./javaClasses
[alphaValues, policy] = solvePOMDP(problemFileLocation)
quit force;
end
