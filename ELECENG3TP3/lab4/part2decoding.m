% Read in the signal from the audio filee
[signal,Fs] = audioread("SecretMessage2021.wav");
L = length(signal);
T = 1/Fs;
t = [0:L-1]*T;

resultF = zeros(40,4);
result = "";
sample_second = L/40;
f = linspace(0,1,sample_second/2)*Fs/2;


title(t, 'Yuying Lai 400268588');
tiledlayout('flow');
second = 0;
% plot
for i = sample_second:sample_second:L
    Y = fft(signal(i-sample_second+1:i))/L ;
    % extract the highest 4
    [A,B] = maxk(abs(Y(1:sample_second/2)),4);
    resultF(second+1,:) = B-1;
    second = second+1;
    
    %plot
    nexttile;
    stem(f,1000*abs(Y(1:sample_second/2)));
    title("Plot");
    xlabel("Hz");
    ylabel("|Y(F)|");
    axis([0 Fs/2 0 .5]);
    grid('minor');
end

exportgraphics(gcf, 'Part2Spe.png');

% sort
resultF = sort(resultF,2);

for i = 1:40
    if resultF(i, :) == [1000, 2000, 3000, 4000]
        result = strcat(result, "A");
    elseif resultF(i, :) == [1000, 2000, 3000, 5000]
        result = strcat(result, "B");
    elseif resultF(i, :) == [1000, 2000, 3000, 6000]
        result = strcat(result, "C");
    elseif resultF(i, :) == [1000, 2000, 3000, 7000]
        result = strcat(result, "D");
    elseif resultF(i, :) == [1000, 2000, 4000, 5000]
        result = strcat(result, "E");
    elseif resultF(i, :) == [1000, 2000, 4000, 6000]
        result = strcat(result, "F");
    elseif resultF(i, :) == [1000, 2000, 4000, 7000]
        result = strcat(result, "G");
    elseif resultF(i, :) == [1000, 2000, 5000, 6000]
        result = strcat(result, "H");
    elseif resultF(i, :) == [1000, 2000, 5000, 7000]
        result = strcat(result, "I");
    elseif resultF(i, :) == [1000, 2000, 6000, 7000]
        result = strcat(result, "J");
    elseif resultF(i, :) == [1000, 3000, 4000, 5000]
        result = strcat(result, "K");
    elseif resultF(i, :) == [1000, 3000, 4000, 6000]
        result = strcat(result, "L");
    elseif resultF(i, :) == [1000, 3000, 4000, 7000]
        result = strcat(result, "M");
    elseif resultF(i, :) == [1000, 3000, 5000, 6000]
        result = strcat(result, "N");
    elseif resultF(i, :) == [1000, 3000, 5000, 7000]
        result = strcat(result, "O");
    elseif resultF(i, :) == [1000, 3000, 6000, 7000]
        result = strcat(result, "P");
    elseif resultF(i, :) == [1000, 4000, 5000, 6000]
        result = strcat(result, "Q");
    elseif resultF(i, :) == [1000, 4000, 5000, 7000]
        result = strcat(result, "R");
    elseif resultF(i, :) == [1000, 4000, 6000, 7000]
        result = strcat(result, "S");
    elseif resultF(i, :) == [1000, 5000, 6000, 7000]
        result = strcat(result, "T");
    elseif resultF(i, :) == [2000, 3000, 4000, 5000]
        result = strcat(result, "U");
    elseif resultF(i, :) == [2000, 3000, 4000, 6000]
        result = strcat(result, "V");
    elseif resultF(i, :) == [2000, 3000, 4000, 7000]
        result = strcat(result, "W");
    elseif resultF(i, :) == [2000, 3000, 5000, 6000]
        result = strcat(result, "X");
    elseif resultF(i, :) == [2000, 3000, 5000, 7000]
        result = strcat(result, "Y");
    elseif resultF(i, :) == [2000, 3000, 6000, 7000]
        result = strcat(result, "Z");
    elseif resultF(i, :) == [2000, 4000, 5000, 6000]
        result = strcat(result, " ");
    elseif resultF(i, :) == [2000, 4000, 5000, 7000]
        result = strcat(result, ".");
    end
end

% Display message
result



