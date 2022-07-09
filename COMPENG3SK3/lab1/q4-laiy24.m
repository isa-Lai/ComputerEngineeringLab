%Yuying Lai 400268588
clc;
format long;
% machine precision
machpre = 1;

while (1.0+machpre/2) >1.0
    machpre = machpre/2;
end

machpre
1+machpre 
1+machpre/2 

% fime smallest possible number
smallest = 1;
compare = 0.0;

while (compare+smallest/2) >compare
    smallest = smallest/2;
end
smallest