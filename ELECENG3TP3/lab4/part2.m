% Read in the signal from the audio filee
[signal,Fs] = audioread("SecretMessage2021.wav");
L = length(signal);
T = 1/Fs;
t = [0:L-1]*T;

% plot
t_plot = 1;
msec_per_sec = 1000;
numSample = t_plot*Fs/msec_per_sec;

result = fft(signal(1+1*Fs:(1+1) * Fs))/L;
f = linspace(0,1,L/40+1)*Fs/2;

stem(f(1:16000),2*abs(result));
title("Yuying Lai 400268588");
xlabel("Hz");
ylabel("|Y(F)|");
axis([0 Fs/2 0 .5]);

grid('minor');

exportgraphics(gcf, 'Part2.png');