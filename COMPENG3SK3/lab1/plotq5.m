%Yuying Lai 400268588
clc;
format long;
x = 3;
result = 0;
sign = 1;
realresult = ln(3);

resultarray1 = zeros(1,100);
error = zeros(1,100);
relativeerror = zeros(1,100);

error(1) = abs(-ln(3));
relativeerror(1) = error(1)/ln(3);

for i = 1:1:99
    result = result + ((x-1)^i/i)*sign;
    sign = sign *-1;
    if i == 4
       result 
    end
    resultarray1(i+1) = result;
    error(i+1) = abs(result-ln(3));
    relativeerror(i+1) = 
end

result

x = 1:1:100;
plot(x,resultarray1,"r")
xlabel("number of term")
ylabel("value")
