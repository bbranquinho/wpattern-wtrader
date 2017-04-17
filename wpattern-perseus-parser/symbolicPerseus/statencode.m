
function statenum = statencode(statevec,bases)

if ~exist('bases','var');
  bases = 2*ones(1,length(statevec));
end

cumProd = ones(length(bases),1);
for i = 2:length(cumProd)
  cumProd(i) = bases(i-1) * cumProd(i-1);
end

%statenum = 1 + (statevec-1) * cumProd;
statenum = 1 + (statevec-1.0) * [1,cumprod(bases(1:length(bases)-1))]';

% statenum = 1 + (statevec-1) * 2.^(0:length(statevec)-1)';

