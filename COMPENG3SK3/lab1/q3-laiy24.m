%Yuying Lai 400268588
clc;
format long;
x1 = pi/3;
x2 = pi/4;
result1 = 1;
result2 = 1;
sign = -1;

for i = 2:2:300
    result1 = result1 + sign*(x1^i/factorial(i));
    result2 = result2 + sign*(x2^i/factorial(i));
    sign = sign *-1;
end

result1
realresult1 = cos(x1)
result2 
realresult = cos(x2)
