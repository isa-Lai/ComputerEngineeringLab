%Yuying Lai 400268588
clc;
format long;
x = 8.3;
result = 1;
demoninator = 1;
sign = -1;


resultarray1 = zeros(1,25);
resultarray2 = zeros(1,25);
resultarray1(1) = 1;
resultarray2(1) = 1;

for i = 1:1:24
    temp = factorial(i);
    result = result + (x^i/temp)*sign;
    demoninator = demoninator + x^i/temp;
    sign = sign *-1;
    
    resultarray1(i+1) = result;
    resultarray2(i+1) = 1/demoninator;
end

x = 20:1:25;
plot(x,resultarray1(20:25),"r")
hold on
plot(x,resultarray2(20:25),"b")
xlabel("number of term")
ylabel("result")
legend("formula(1)","formula(2)")
title("Yuying Lai 400268588")
hold off
