% Yuying Lai 400268588 laiy24
%% square wave generator
clc
clear all
hold off

f0=10000;     %fundamental freq of input triangular wave
T0 = 1/f0;  %period 
tstep = 0.001*T0;
no_sample = 5*T0/tstep + 1; %no. of samples  within  5*T0
tt = -2.5*T0:tstep:2.5*T0;

gp_in = square(tt*2*pi*f0 , 50); %50% duty cycle square wave
figure(1)
Hp1 = plot(tt,gp_in);
set(Hp1,'LineWidth',2)
Ha = gca;
set(Ha,'Fontsize',16)
title('input - time domain laiy24')

%% Fourier series representation of signal (Amplitude Spectrum)
      
N=100; %no. of harmonics
nvec = -N:N;
f = nvec*f0; %frequency vector
c_in = zeros(size(nvec));
for n = nvec
    m = n + N+1;
    % since for even, sin(pi*n/2) = 0, we directly assign 0 to 
    % prevent error in floating point calculation
    if (mod(n,2)==0)
      c_in(m) = 0.0;
    else
       c_in(m) = sin(pi*n/2)/(pi*n/2);
    end
end

figure(2)
Hp1=stem(f,abs(c_in));
axis([-8*f0 8*f0 0 max(abs(c_in))])
set(Hp1,'LineWidth',2)
Ha = gca;
set(Ha,'Fontsize',16)
title('magnitude spectrum of input laiy24')

%% Fourier series representation of signal (Phase Spectrum)

figure(3)
Hp1=stem(f,angle(c_in));
set(Hp1,'LineWidth',2)
Ha = gca;
set(Ha,'Fontsize',16)
axis([-8*f0 8*f0 -pi pi])
title('phase spectrum of inputn laiy24')

%% Designing the 2st order Butterworth filter

fc = 12000;

Hf = 1 ./((1i*f/fc).^2 + 1.414*(1i*f/fc) + 1);%filter transfer function
c_out = c_in .* Hf; %Fourier coefficients of the filter output

figure(4)
plot(f, abs(Hf))
hold on
plot(f,angle(Hf))
title('filter spectrum laiy24')
legend('mag','angle')
hold off

figure(5)
stem(f,abs(c_in),'r','LineWidth',2);
hold on
stem(f,abs(c_out),'b','LineWidth',2);
hold off
axis([-8*f0 8*f0 0 max(abs(c_in))])
Ha = gca;
set(Ha,'Fontsize',16)
title('magnitude spectrum of filter output and input laiy24')
Ha = gca;
set(Ha,'Fontsize',16)
legend('input','output')
hold off

figure(6)
Hp1=stem(f,angle(c_out))
set(Hp1,'LineWidth',2)
Ha = gca;
set(Ha,'Fontsize',16)
title('phase spectrum of output laiy24')
axis([-8*f0 8*f0 -pi pi])
hold on
Hp1=stem(f,angle(c_in),'r')
set(Hp1,'LineWidth',2)
Ha = gca;
set(Ha,'Fontsize',16)
hold off

%% Construct the output signal from the Cout Fourier coefficients

A = zeros(2*N+1,ceil(no_sample));
for n = nvec
    m=n+N+1;
    A(m,:) = c_out(m) .* exp(1i*2*pi*n*f0*tt);
end
gp_out = sum(A);
figure(7)
Hp1 = plot(tt,real(gp_out),'b',tt,gp_in,'r');
set(Hp1,'LineWidth',2)
Ha = gca;
set(Ha,'Fontsize',16)
title('filter input and output-time domain laiy24')
set(Ha,'Fontsize',16)
legend('output','input')
