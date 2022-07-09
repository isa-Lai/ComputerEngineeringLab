% Yuying Lai 400268588 
clc; clear;
format long

n = 1:10;
time = zeros(1,10);
for i =1:10
   temp = zeros(1,100);
   for k = 1:100
       x = rand(500*i,500*i);
       tic
       y = x;
       temp(k) = toc;
   end
   time(i) = mean(temp);
end
time
plot(1:10,time)

temp = time(2:10)./time(1:9)
base = n(2:10)./n(1:9)
power = log(temp)./log(base)

