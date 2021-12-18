% Read in the signal from the audio filee
[signal,Fs] = audioread("tones2021.wav");
L = length(signal);
T = 1/Fs;
t = [0:L-1]*T;

% plot
t_plot = 5;
msec_per_sec = 1000;
numSample = t_plot*Fs/msec_per_sec;

plot(msec_per_sec*t(1:numSample), signal(1:numSample));
title("Yuying Lai 400268588");
xlabel("time (ms)");
ylabel("Input signal");
grid('minor');

exportgraphics(gcf, 'Part1Q1.png');