
function statevec = statedecode(statenum, n, bases)
% interface:
%
%   statevec = statedecode(statenum, n)        ---> assumes binary variables
%                                                   n is the number of bits
%   statevec = statedecode(statenum, n, bases) ---> allows non-binary variables
%                                                   bases is the vector of domain sizes of the variables

if ~exist('bases')
  bases = 2*ones(1,n);
end

   statevec = zeros(1,n);
   if statenum == 1, statevec = statevec + 1; return; end
   statenum = statenum - 1;
   res = statenum;
   for i = 1:n
      if res == 1, statevec(i) = 1; break; end
      remd = rem(res,bases(i));
      res = floor(res/bases(i));
      statevec(i) = remd;
   end	
   statevec = statevec + 1;

