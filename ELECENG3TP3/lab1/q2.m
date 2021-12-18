%read excel
opts = detectImportOptions('course_grades_2021.xlsx');
opts = setvartype(opts,{'ID_Number','Name'},'string');
t = readtable('course_grades_2021.xlsx',opts);

%create new colume
d = t(t.Name~="Maximum Mark",:);
d.Lab_sum = sum(d{:,3:6},2);
d.Exam_sum = sum(d{:,8:11},2);
d.Final = sum(d{:,3:11},2);


% Q2a
[maxlab,maxlabidx] = max(d.Lab_sum);
best_stu_lab = d(maxlabidx,:);
disp(best_stu_lab.Name+' get highest lab mark:'+maxlab);

% Q2b
[maxexam,maxexamidx] = max(d.Exam_sum);
best_stu_exam = d(maxexamidx,:);
disp(best_stu_exam.Name+' get highest exam mark:'+maxexam);

% Q2c
[maxfinal,maxfinalidx] = max(d.Final);
best_stu_final = d(maxfinalidx,:);
disp(best_stu_final.Name+' get highest final mark:'+maxfinal);

% Q2d
newrow = {'Yuying Lai','400268588',6,7,8,9,17,6,7,8,9};
t = [t;newrow];
