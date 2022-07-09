% Yuying Lai 400268588
clc;clear;
format long
F = @(z) z^3-2*z-5;
Fprime = @(z) 3*z^2 -2;
fractals(F,Fprime,0,30,512)
