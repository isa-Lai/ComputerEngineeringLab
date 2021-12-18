% frequency in  Hz
f = 100;

fs = 16000; % sampling f
Ts = 1/fs;  % interval
u = 2000

% Set time duration of plot, i.e., 10 msec.
tfinalplot = 8;

% Make the time vector for the plot
nsound=0:Ts:tfinalplot;
nplot = 0:Ts: Ts*2000;

% Sample the sinusoid.
cnT = cos(pi*u*nsound.^2 + 2*pi*f*nsound);

plot(nplot, cnT(1:length(nplot)));
title('Yuying Lai 400268588');
xlabel("nT"); ylabel("c[nT]");

audiowrite('chripq116k.wav', cnT, fs);
exportgraphics(gcf,'chripq116k.png');