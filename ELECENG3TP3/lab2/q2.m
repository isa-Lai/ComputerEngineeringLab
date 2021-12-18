clc; clear;
% read signal
[signal, Fs] = audioread('my_speech_clip.wav');

L = length(signal); % number of samples in the signal
T = 1 / Fs; % sampling period in seconds
t = [0:L - 1] * T; % time vector in seconds
% process
Te = 200; % in ms
alpha = 1;

delay = round((Te / 1000) / T); 
signalplusecho = [signal; zeros(delay, 1)]; % init, add delay time to the end
signalplusecho(delay + 1:end) =alpha .* signal + signalplusecho(delay + 1:end) ;

% write into new file
signalplusecho = signalplusecho / max(abs(signalplusecho));
audiowrite('speechwithecho.wav', signalplusecho, Fs);