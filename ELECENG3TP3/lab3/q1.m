% frequency in  Hz
f = 100;

fs = 8000; % sampling f
Ts = 1/fs;  % interval

% Set time duration of plot, i.e., 10 msec.
tfinalplot = 10e-3;

% Make the time vector for the plot
nplot=0:Ts:tfinalplot;

% Sample the sinusoid.
xnT = sin(2*pi*f*nplot);

stem(nplot, xnT);
title('Yuying Lai 400268588');
xlabel("nT"); ylabel("x[nT]");

exportgraphics(gcf,'teleq1.png');