% Yuying Lai 400268588 Laiy24
clc;clear;
format long

%% Part 1 Validation Code
A = [2 1 -1
     5  2 2
      3 1 1]
B =[-8
    -6
    -4]
%A = [15 -5 0 -5; -5 12 -2 0;0 -2 6 -2;-5 0 -2 9];
%x = [12.6;0.02;9;3];
%B = [-5;5;0;0];
%B = A*x
solution_vec = Gauss_Elimination_Pivoting(A, B)
realsolution = A\B


% %% Part 2
% 
% % Question 2
% for i = 2:1:4
%    [A_mat, b_vec] =  random_test_case(i);
%    solution = Gauss_Elimination_Pivoting(A_mat, b_vec);
%    X = A_mat\b_vec;
%    if(size(solution) ~= 0)
%        % accept error with 0.0000001
%        maxError = max(abs(X-solution));
%        if(maxError < 0.0000001)
%            disp("Gauss_Elimination() is correct")
%        else
%            disp("Gauss_Elimination() failed")
%            A_mat
%            b_vec
%            solution
%            X
%        end
%    end
% end
% 
% % Question 3
% n = [10 20 30 50 75 100 125 150 175 200 225 250 275 300];
% time = zeros(size(n));
% time_pivot = zeros(size(n));
% index = 1;
% for i = n
%    time_array = zeros(1,10);
%    time_array_pivot = zeros(1,10);
%    for k = 1:10
%        [A_mat, b_vec] =  random_test_case(i);
%        tic
%        solution = Gauss_Elimination(A_mat, b_vec);
%        time_array(k) = toc;
%        tic
%        solution = Gauss_Elimination_Pivoting(A_mat, b_vec);
%        time_array_pivot(k) = toc;
%    end
%    time(index) = mean(time_array);
%    time_pivot(index) = mean(time_array_pivot);
%    
%    X = A_mat\b_vec;
%    if(size(solution) ~= 0)
%        % accept error with 0.0000001
%        maxError = max(abs(X-solution));
%        if(maxError < 0.0000001)
%            disp("Gauss_Elimination() is correct")
%        else
%            disp("Gauss_Elimination() failed")
%            A_mat
%            b_vec
%            solution
%            X
%        end
%    end
%    index = index +1;
% end
% %calculate the power
% 
% temp = time(2:length(n))./time(1:length(n)-1)
% base = n(2:length(n))./n(1:length(n)-1)
% power = log(temp)./log(base)
% 
% figure
% plot(n,time,n,time_pivot)
% xlabel("n for nxn matrix")
% ylabel("time")
% legend("Without Pivot", "With Pivot")
% title("Yuying Lai 400268588 Time vs Matrix size")
% 
% figure
% plot(n,abs(time-time_pivot)./time)
% xlabel("n for nxn matrix")
% ylabel("Percentage")
% title("Yuying Lai 400268588 Percentage for Adding Povoting")
% 
% % Question 4
% error = zeros(1,100);
% k = 0:5;
% mean_array = zeros(1,6);
% std_array = zeros(1,6);
% for j=k
%    for i = 1:size(error)
%        [A_mat, b_vec] =  random_test_case(10);
%        solution = Gauss_Elimination(A_mat, b_vec);
% 
%        r = 2*rand()*10^(-j)-10^(-j);
%        A_mat_new = A_mat + r;
%        b_vec_new = b_vec + r;
%        new_solution = Gauss_Elimination(A_mat_new, b_vec_new);
%        if(size(solution) ~= 0 & size(new_solution) ~= 0)
%             E = sqrt(sum((new_solution-solution).^2, "all"));
%             error(i) = E;
%        end
%     end
%     mean_array(j+1) = mean(error);
%     std_array(j+1) = std(error) ;
% end
% figure
% yyaxis left
% plot(k,mean_array)
% ylabel("Mean")
% yyaxis right
% plot(k,std_array)
% xlabel("k for perturbation 10^(-k)")
% ylabel("Std")
% legend("Mean", "Standard Deviation")
% title("Yuying Lai 400268588 Perturbation")
% 
% 
% % Question 1
% function [A_mat, b_vec] = random_test_case(n)
%     A_mat = 200.*rand(n,n)-100;
%     b_vec = 200.*rand(n,1)-100;
% end




%% Part 1


function [new_mat, new_b_vec] = reduce_column(A_mat, b_vec, column)
    [r,r] = size(A_mat);
    new_mat = A_mat;
    new_b_vec = b_vec;
    for i = column+1:r
       [new_mat, new_b_vec] = reduce_row_at_col(new_mat, new_b_vec,column, column, i);
    end
end

function [new_mat, new_b_vec] = reduce_row_at_col(A_mat, b_vec,col, row_added, row_reduced)
    new_mat = A_mat;
    new_b_vec = b_vec;
    if(A_mat(row_added,col)==0)
        coe = 0;
    else
        coe = A_mat(row_reduced, col)/A_mat(row_added,col);
    end
    new_mat(row_reduced, :) = A_mat(row_reduced,:) - A_mat(row_added,:).*coe;
    new_b_vec(row_reduced, :) = b_vec(row_reduced,:) - b_vec(row_added,:).*coe;
end

function [ut_mat, new_b_vec] = Forward_Elimination(A_mat, b_vec)
    [c,c] = size(A_mat);
    cur_mat = A_mat;
    cur_vec = b_vec;
    for cur_col = 1:c
        if(cur_mat(cur_col,cur_col)==0)
            cur_mat = [];
            cur_vec = [];
            break;
        end
        [cur_mat, cur_vec] = reduce_column(cur_mat, cur_vec, cur_col);
    end
    ut_mat = cur_mat;
    new_b_vec = cur_vec;
end

function new_part_solution_vec = back_subst_for_col(ut_mat, new_b_vec, column, part_solution_vec)
    [r,r] = size(ut_mat);
    new_part_solution_vec = part_solution_vec;
    if column<r
        for i = column+1:r
            new_part_solution_vec(column,:) = new_part_solution_vec(column,:)- new_part_solution_vec(i,:)*ut_mat(column,i);
        end
    end
    new_part_solution_vec(column,:) = new_part_solution_vec(column,:)/ut_mat(column,column);
end

function solution_vec = Back_Substitution(ut_mat, new_b_vec)
    [c,c] = size(ut_mat);
    solution_vec = new_b_vec;
    for cur_col = c:-1:1
        solution_vec = back_subst_for_col(ut_mat, new_b_vec,cur_col, solution_vec);
    end
end


function solution_vec = Gauss_Elimination(A_mat, b_vec)
 % check for consistent size of A_mat and b_vec
 [r,c] = size(A_mat);
 if (r ~= c)
     solution_vec = [];
     disp("A is not a square matrix")
 elseif (c ~= size(b_vec))
     solution_vec = [];
     disp("size of b did not match size of A")
 else
     % reduce coefficient matrix to upper triangular form, modifying the
     % right-hand side vector appropriatly
     [ut_mat, new_b_vec] = Forward_Elimination(A_mat, b_vec);
     if(size(ut_mat) == 0)
        disp("there is zero pivod")
        solution_vec = [];
     else
         % Compute the solution vector using back substitution
         solution_vec = Back_Substitution(ut_mat, new_b_vec);
         % we are done
     end
 end
end

% pivot version ---------------------------------------------------------
%-----------------------------------------------------------------------
function [new_mat, new_b_vec] = pivot(A_mat, b_vec, col)
    [c,c] = size(A_mat);
    for i = col+1:c
        if A_mat(i,col)~=0
            [new_mat, new_b_vec] = swap_rows(A_mat, b_vec, col, i);
        end
    end
end

function [new_mat, new_vec] = swap_rows(mat, vec, row1, row2)
    new_mat = mat;
    new_mat(row1,:) = mat(row2,:);
    new_mat(row2,:) = mat(row1,:);
    
    new_vec = vec;
    new_vec(row1,:) = vec(row2,:);
    new_vec(row2,:) = vec(row1,:);
end

function [ut_mat, new_b_vec] = Forward_Elimination_Pivot(A_mat, b_vec)
    [c,c] = size(A_mat);
    cur_mat = A_mat;
    cur_vec = b_vec;
    for cur_col = 1:c
        if(cur_mat(cur_col,cur_col)==0)
            [cur_mat, cur_vec] = pivot(cur_mat, cur_vec, cur_col);
        end
        [cur_mat, cur_vec] = reduce_column(cur_mat, cur_vec, cur_col)
    end
    ut_mat = cur_mat;
    new_b_vec = cur_vec;
end


function solution_vec =  Gauss_Elimination_Pivoting(A_mat, b_vec)
 % check for consistent size of A_mat and b_vec
 [r,c] = size(A_mat);
 if (r ~= c)
     solution_vec = [];
     disp("A is not a square matrix")
 elseif (c ~= size(b_vec))
     solution_vec = [];
     disp("size of b did not match size of A")
 else
     % reduce coefficient matrix to upper triangular form, modifying the
     % right-hand side vector appropriatly
     [ut_mat, new_b_vec] = Forward_Elimination_Pivot(A_mat, b_vec);
     % Compute the solution vector using back substitution
     solution_vec = Back_Substitution(ut_mat, new_b_vec);
     % we are done
 end
end

