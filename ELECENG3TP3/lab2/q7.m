clc; clear;
% read signal
[signal, Fs] = audioread('my_speech_clip.wav');

L = length(signal); % number of samples in the signal
T = 1 / Fs; % sampling period in seconds
t = [0:L - 1] * T; % time vector in seconds
% process
Te = 70; % in ms
alpha = 1;
Ne = 5;

delay = round((Te / 1000) / T); 
IR = [1;zeros(Ne*delay,1)];
for i = 1:Ne
    IR(i * delay + 1) = alpha ^ i;%put echo into response function
end
signalplusecho = conv(signal,IR);

% write into new file
signalplusecho = signalplusecho / max(abs(signalplusecho));
audiowrite('speechwithecho_rev.wav', signalplusecho, Fs);