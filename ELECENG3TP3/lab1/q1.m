%question 1
n = -10:10;
f=SimpleFunctions();
% function
q1a = f.unitstep(n) - 2 .* f.unitstep(n - 1) + f.unitstep(n - 4);
q1b = (n + 2) .* f.unitstep(n + 2) - 2 .* f.unitstep(n) - n .* f.unitstep(n - 4);
q1c = f.delta(n + 1) - f.delta(n) + f.unitstep(n + 1) - f.unitstep(n - 2);
q1d = exp(0.8 .* n) .* f.unitstep(n + 1) + f.unitstep(n);

% create figure
fig = figure('units', 'normalized', 'outerposition', [0, 0.08, 1, 0.9], 'Name',...
    'Question 1');
t = tiledlayout(2, 2);

nexttile; 
stem(n, q1a); grid on;
title("Question1a"); 
xlabel("n"); 
ylabel("x[n]");

nexttile;
stem(n, q1b); grid on;
title( "Question1b"); 
xlabel("n"); 
ylabel("x[n]");

nexttile;
stem(n, q1c); grid on;
title( "Question1c"); 
xlabel("n"); 
ylabel("x[n]");

nexttile;
stem(n, q1d); grid on;
title("Question1d"); 
xlabel("n"); 
ylabel("x[n]");

title(t, "Yuying Lai 400268588");

exportgraphics(fig, "lab1_question1.png");
