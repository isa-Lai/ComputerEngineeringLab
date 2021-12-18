clc; clear; close all;
% frequency in  Hz
f = [7200, 7600, 7800, 7900];

fs = 8000; % sampling f
Ts = 1/fs;  % interval

% Set time duration of plot, i.e., 10 msec.
tfinalplot = 10e-3;

% Make the time vector for the plot
nplot=0:Ts:tfinalplot;

% replaed
tfinal = 2;
nsound = 0:Ts:tfinal;
xnT = zeros(size(nsound, 2), 4); % inital

% sample and plot
fig = figure('Name', 'Question 3');
t = tiledlayout(2,2); 
title(t, 'Yuying Lai 400268588');

for i = 1:length(f)
    xnT_part = sin(2*pi*f(i)*nsound);
    xnT(:, i) = xnT_part;
    nexttile;
    plot(nplot, xnT_part(1:length(nplot)));
    titlename = "frequency at " +f(i)+"Hz";
    title(titlename);
    xlabel("nT"); ylabel("x[nT]");
end    

% combine 4 sound
s = xnT(:);
audiowrite('soundfileq3.wav', s, fs); 
exportgraphics(fig,'teleq3.png'); 