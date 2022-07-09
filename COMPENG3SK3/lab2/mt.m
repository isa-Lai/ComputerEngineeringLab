clc;clear;
A = [2 1 -1
     5  2 2
      3 1 1]
B =[-8
    -6
    -4]
Aprime = inv(A)
conA = cond(A,1) % 1-norm
[U,S,V] = svd(A)
[L,u,P] = lu(A)
solution = A\B

u = [2 1 -1
    0 -1/2 9/2
    0 0 -2]
L = A*inv(u)
L*u
d = det(A)

