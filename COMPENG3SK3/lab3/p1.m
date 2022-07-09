% Yuying Lai 400268588
clc;clear;
format long;
image = imread('pictures/lily.jpg');
r = 1;

%usign gray and extract u s v
I = double(rgb2gray(image));
%I = double(image);
[n,m] = size(I);
[u s v] = svd(I);
stemp = svd(I);

figure(1)
tiledlayout('flow')
last = false;
E = [];
rate = [];
%rank approximation
while(1)
%% SVD
    % now print the last rank
    if(r==min(n,m))
        last = true;
    end
    %decom
    c=s;  
    c(r+1:end,:)=0;  
    c(:,r+1:end)=0;  
    D=u*c*v';
    
    %plot
    nexttile
    imshow(uint8(D));
    title("rank:" + r);
%% percentage error
    dif = I-D;
    sum1 = 0;
    sum2 = 0;
    for index1 = 1:n
        for index2 = 1:m
            sum1 = sum1 + dif(index1,index2)^2;
            sum2 = sum2 + I(index1, index2)^2;
        end
    end
    E = [E,sqrt(sum1/sum2)];
%% Comparession rate
    rate = [rate, (r+r*n+r*m)/(n*m)];
    
    %next rank
    r = r*2;
    if(r>min(n,m))
        if(last)
            break;
        else
            r = min(n,m);
        end
    end

 
end
E
rate





