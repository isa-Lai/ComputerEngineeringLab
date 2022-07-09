%% Yuying Lai 400268588
clc;clear;
format long
%% main
%train
num = zeros(1,14);
pass = zeros(1,14);
falsepositive= zeros(1,14);
falsenegative= zeros(1,14);
for j = 1:14
numpic = 2^j;
[imgs labels] = readMNIST("train-images.idx3-ubyte", "train-labels.idx1-ubyte", numpic, 0);
flattern_imgs = reshape(imgs,[],numpic); %flateern image to process
newlables = zeros(numpic,10)-1;% make 10 lables for each image
for i = 1:numpic
    index = labels(i);
    newlables(i,index+1) = 1;
end
w = trainingModel(flattern_imgs,newlables)
% test
testnum = 10000;
[imgs labels] = readMNIST("t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte", testnum, 0);
flattern_imgs = reshape(imgs,[],testnum); %flateern image to process
newlables = zeros(testnum,10)-1;% make 10 lables for each image
for i = 1:testnum
    index = labels(i);
    newlables(i,index+1) = 1;
end
[result, correct]= testModel(flattern_imgs,newlables,w)
num(j) = numpic;
pass(j) = correct/testnum;
temppos = 0;
tempneg =0;
for i = 1:10
   tempneg = tempneg + result(1,i)/(result(1,i)+result(4,i));
   temppos = temppos + result(3,i)/(result(3,i)+result(2,i));
end
falsepositive(j) = temppos/10;
falsenegative(j) = tempneg/10;
end

figure()
plot(num,pass,num,falsepositive,num,falsenegative );
legend("pass rate","false positive rate","false negative rate")

%% training process
function [w] = trainingModel(x,t)
    [m,n] = size(x);
    rng(400268588);
    w = rand(m,10)*100;
    for i = 1:n
        yi = t(i,:)'+1;
        while(~(isequal(yi, t(i,:)')))
            %update w
            for k = 1:10
                if(yi(k)~=t(i,k))
                    w(:,k) = w(:,k) + t(i,k)*x(:,i);
                end
            end
            z = w'*x(:,i);
            yi = sign(z);
            yi(yi==0) = 1;
        end
    end
end

%% testing
function [result, correct] = testModel(x,t,w)
    %result(1,:) number of false negative
    %result(2,:) number of ture negative
    %result(3,:) number of false positive
    %result(4,:) number of true positive
    result = zeros(4,10);
    correct = 0;
    [m,n] = size(x);
    for i = 1:n
        z = w'*x(:,i);
        yi = sign(z);
        yi(yi==0) = 1;
        tempresult = sign(yi-t(i,:)');
        for k = 1:10
            if(tempresult(k) == 0)
                if(yi(k) == 1)
                    result(4,k)  = result(4,k)+ 1;
                else
                    result(2,k)  = result(2,k)+ 1;
                end
            else
                if(yi(k) == 1)
                    result(3,k)  = result(3,k)+ 1;
                else
                    result(1,k)  = result(1,k)+ 1;
                end
            end
        end
        if(isequal(yi, t(i,:)'))
            correct = correct+1;
        end
    end
    
end

%% read file
% readMNIST by Siddharth Hegde
%
% Description:
% Read digits and labels from raw MNIST data files
% File format as specified on http://yann.lecun.com/exdb/mnist/
% Note: The 4 pixel padding around the digits will be remove
%       Pixel values will be normalised to the [0...1] range
%
% Usage:
% [imgs labels] = readMNIST(imgFile, labelFile, readDigits, offset)
%
% Parameters:
% imgFile = name of the image file
% labelFile = name of the label file
% readDigits = number of digits to be read
% offset = skips the first offset number of digits before reading starts
%
% Returns:
% imgs = 20 x 20 x readDigits sized matrix of digits
% labels = readDigits x 1 matrix containing labels for each digit
%
function [imgs labels] = readMNIST(imgFile, labelFile, readDigits, offset)
    
    % Read digits
    fid = fopen(imgFile, 'r', 'b');
    header = fread(fid, 1, 'int32');
    if header ~= 2051
        error('Invalid image file header');
    end
    count = fread(fid, 1, 'int32');
    if count < readDigits+offset
        error('Trying to read too many digits');
    end
    
    h = fread(fid, 1, 'int32');
    w = fread(fid, 1, 'int32');
    
    if offset > 0
        fseek(fid, w*h*offset, 'cof');
    end
    
    imgs = zeros([h w readDigits]);
    
    for i=1:readDigits
        for y=1:h
            imgs(y,:,i) = fread(fid, w, 'uint8');
        end
    end
    
    fclose(fid);

    % Read digit labels
    fid = fopen(labelFile, 'r', 'b');
    header = fread(fid, 1, 'int32');
    if header ~= 2049
        error('Invalid label file header');
    end
    count = fread(fid, 1, 'int32');
    if count < readDigits+offset
        error('Trying to read too many digits');
    end
    
    if offset > 0
        fseek(fid, offset, 'cof');
    end
    
    labels = fread(fid, readDigits, 'uint8');
    fclose(fid);
    
    % Calc avg digit and count
    imgs = trimDigits(imgs, 4);
    imgs = normalizePixValue(imgs);
    %[avg num stddev] = getDigitStats(imgs, labels);
    
end

function digits = trimDigits(digitsIn, border)
    dSize = size(digitsIn);
    digits = zeros([dSize(1)-(border*2) dSize(2)-(border*2) dSize(3)]);
    for i=1:dSize(3)
        digits(:,:,i) = digitsIn(border+1:dSize(1)-border, border+1:dSize(2)-border, i);
    end
end

function digits = normalizePixValue(digits)
    digits = double(digits);
    for i=1:size(digits, 3)
        digits(:,:,i) = digits(:,:,i)./255.0;
    end
end



