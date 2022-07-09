%% Yuying Lai 400268588
clear
hold off
format long e

% time samples
N = 2^16; %No. of FFT samples
sampling_rate = 40e4; %unit Hz
tstep = 1/sampling_rate;
tmax = N*tstep/2;
tmin = -tmax;
tt = tmin:tstep:tmax-tstep;

%freq samples
fmax = sampling_rate/2; 
fmin = -fmax;
fstep = (fmax-fmin)/N;
freq = fmin:fstep:fmax-fstep;

%% Modulation

%carrier
fc=20e3;
Ac = 1;
ct=Ac*cos(2*pi*fc*tt);

%message signal
Am=-2;
Tm = 0.0005;
mt = Am*sinc(tt/Tm);


%max of absolute of m(t)
maxmt = abs(Am);
%For 50% modulation
ka=0.5/maxmt;

%AM signal
st = (1+ka*mt).*ct;

%% Demodulation
testvalue = 0.0001:0.00001:0.001;
bestvalue = 0;
besterror = 100;
for RC = testvalue
    %Envelope detector
    yt = st;
    n=1;
    for t=tt
        if(n > 1)
            if(yt(n-1) > st(n))
                yt0 = yt(n-1);
                %time when C starts discharging
                tc = tt(n-1);
                yt(n) = yt0*exp(-(t-tc)/RC);
            end
        end
        n=n+1;
    end
    yt(1)=yt(2);
    
    % DC removeval
    yt1 = (yt - 1)/ka;
    
    % LPF
    f0 = 1.1*1/Tm;
    mt1 = lowpass(yt1,f0,sampling_rate);
    
    % check error
    error = mean(abs(mt1-mt));
    if error <besterror
        besterror = error;
        bestvalue = RC;
    end
end
bestvalue
besterror
