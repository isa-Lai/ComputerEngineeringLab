% Read in the signal from the audio filee
[signal,Fs] = audioread("tones2021.wav");
L = length(signal);
T = 1/Fs;
t = [0:L-1]*T;

% plot
t_plot = 5;
msec_per_sec = 1000;
numSample = t_plot*Fs/msec_per_sec;

result = fftshift(signal)/L;
f = linspace(0,1,L/2+1)*Fs/2;

stem(f,2*abs(result(1:L/2+1)));
title("Yuying Lai 400268588");
xlabel("Hz");
ylabel("|Y(F)|");
axis([0 Fs/2 0 .5]);

grid('minor');

exportgraphics(gcf, 'Part1Q2.png');