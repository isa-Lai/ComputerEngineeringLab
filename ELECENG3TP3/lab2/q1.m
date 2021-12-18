n = 0:10;
f=SimpleFunctions();
% add function
q1ax = f.unitstep(n)-f.unitstep(n-3);
q1av = f.unitstep(n)-f.unitstep(n-4);
q1bx = (-n+2).*f.unitstep(n)-(-n+2).*f.unitstep(n-2);
q1bv = f.unitstep(n)-f.unitstep(n-4);
q1cx = (-n+2).*f.unitstep(n)-(-n+2).*f.unitstep(n-2);
q1cv = n.*f.unitstep(n)-n.*f.unitstep(n-3);

%manually do a
tempsum = 0;
q1a_con = zeros(1,10);
for a = 1:10
    disp(a)
    for b = 1:a
        disp(b)
        tempsum = tempsum+ (q1ax(1,b)*q1av(1,a-b+1));
    end
    q1a_con(1,a) = tempsum;
    tempsum = 0;
end

%manually do b
tempsum = 0;
q1b_con = zeros(1,10);
for a = 1:10
    disp(a)
    for b = 1:a
        disp(b)
        tempsum = tempsum+ (q1bx(1,b)*q1bv(1,a-b+1));
    end
    q1b_con(1,a) = tempsum;
    tempsum = 0;
end

%manually do c
tempsum = 0;
q1c_con = zeros(1,10);
for a = 1:10
    disp(a)
    for b = 1:a
        disp(b)
        tempsum = tempsum+ (q1cx(1,b)*q1cv(1,a-b+1));
    end
    q1c_con(1,a) = tempsum;
    tempsum = 0;
end

%conv
q1a_con = conv(q1ax, q1av);
q1b_con = conv(q1bx, q1bv);
q1c_con = conv(q1cx, q1cv);

% create figure
x = 0:5 % only need the first 6 element
fig = figure('units', 'normalized',  'Name',...
    'Question 1');
t = tiledlayout(3, 3);

nexttile; 
stem(x, q1ax(:,1:6)); grid on;
xlabel("n"); 
ylabel("x[n]");

nexttile; 
stem(x, q1bx(:,1:6)); grid on;
xlabel("n"); 
ylabel("x[n]");

nexttile; 
stem(x, q1cx(:,1:6)); grid on;
xlabel("n"); 
ylabel("x[n]");

nexttile; 
stem(x, q1av(:,1:6)); grid on;
xlabel("n"); 
ylabel("v[n]");

nexttile; 
stem(x, q1bv(:,1:6)); grid on;
xlabel("n"); 
ylabel("v[n]");

nexttile; 
stem(x, q1cv(:,1:6)); grid on;
xlabel("n"); 
ylabel("v[n]");

nexttile; 
stem(x, q1a_con(:,1:6)); grid on;
xlabel("n"); 
ylabel("x[n]*v[n]");

nexttile;
stem(x, q1b_con(:,1:6)); grid on;
xlabel("n"); 
ylabel("x[n]*v[n]");

nexttile;
stem(x, q1c_con(:,1:6)); grid on;
xlabel("n"); 
ylabel("x[n]*v[n]");


title(t, "Yuying Lai 400268588 Part b");

exportgraphics(fig, "lab2_question1.png");