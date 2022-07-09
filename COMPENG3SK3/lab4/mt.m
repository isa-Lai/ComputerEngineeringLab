clc;clear;
A = [10 -7 0
     -3  2 6
      5 -1 5]
B =[6
    27
    4]
Aprime = inv(A)
conA = cond(A,1) % 1-norm
[U,S,V] = svd(A)
[L,u] = lu(A)
solution = A\B

