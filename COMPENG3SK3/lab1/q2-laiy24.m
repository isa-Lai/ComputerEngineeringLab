%Yuying Lai 400268588
clc;
format long;
x = 8.3;
result = 1;
demoninator = 1;
realresult = exp(-x);
sign = -1;	
 
for i = 1:1:24
    temp = factorial(i);
    result = result + (x^i/temp)*sign;
    demoninator = demoninator + x^i/temp;
    sign = sign *-1;
end
 
result1 = result
result2 = 1/demoninator
realresult
