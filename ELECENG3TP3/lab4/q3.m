% Read in the signal from the audio filee
[signal,Fs] = audioread("tones2021.wav");
L = length(signal);
T = 1/Fs;
t = [0:L-1]*T;

% plot
t_plot = 5;
msec_per_sec = 1000;
numSample = t_plot*Fs/msec_per_sec;

sin1 = 0.4*sin(2*pi*1000*t);
sin2 = 0.2*sin(2*pi*4000*t);
sin3 = 0.2*sin(2*pi*6000*t);

result = sin1+sin2+sin3;

title(t, 'Yuying Lai 400268588');
tiledlayout('flow');
nexttile;
plot(msec_per_sec*t(1:numSample), signal(1:numSample));
title("Input ");xlabel("time (ms)");ylabel("Input signal");
grid('minor');

nexttile;
plot(msec_per_sec*t(1:numSample), result(1:numSample));
title("Generated");xlabel("time (ms)");ylabel("Generated signal");
grid('minor');

exportgraphics(gcf, 'Part1Q3.png');