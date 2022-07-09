% Yuying Lai 400268588
clc;clear;
format long
%% Check singular value drop speed
allFiles = dir("d:\lab\sk\lab3\pictures");
names = {allFiles.name};

figure(1)
coe = [];
for i = names
    name = i{1}
    if(~strcmp(name,'.') & ~strcmp(name,'..'))
        image = imread(strcat('pictures/',name));
        I = double(image);
        s = svd(I(:,:,1));
        s = s';
        d = diff(s);
        % calculate coeficient
        last = 0;
        temp = [];
        count = 0;
        for k = s
            lnresult = log10(k/last);
            if (lnresult   ~= Inf)
                temp = [temp, log10(k/last)];
            end
            last = k;
            if(count >= 10)
                break;
            end
            count = count +1;
        end
        coe = [coe, mean(temp)];
        plot(1:100,s(1:100));
        hold on
    end
    
end
coe
names(3:end)
hold off
axis([0 100 0 4e4])
legend(names{3:end})