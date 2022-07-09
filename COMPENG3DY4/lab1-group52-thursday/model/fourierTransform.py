#
# Comp Eng 3DY4 (Computer Systems Integration Project)
#
# Copyright by Nicola Nicolici
# Department of Electrical and Computer Engineering
# McMaster University
# Ontario, Canada
#

import matplotlib.pyplot as plt
import numpy as np
import cmath, math
import sys
from scipy import signal

def plotSpectrum(x, Fs, type = 'FFT'):

    n = len(x)             # length of the signal
    df = Fs/n              # frequency increment (width of freq bin)

    # compute Fourier transform, its magnitude and normalize it before plotting
    if type == 'FFT':
        Xfreq = np.fft.fft(x)
    elif type == "DFT":
        Xfreq = myDFT(x)
    XMag = abs(Xfreq)/n

    # Note: because x is real, we keep only the positive half of the spectrum
    # Note also: half of the energy is in the negative half (not plotted)
    XMag = XMag[0:int(n/2)]

    # freq vector up to Nyquist freq (half of the sample rate)
    freq = np.arange(0, Fs/2, df)

    fig, ax = plt.subplots()
    ax.plot(freq, XMag)
    ax.set(xlabel='Frequency (Hz)', ylabel='Magnitude',
        title='Frequency domain plot')
    # fig.savefig("freq.png")
    plt.show()

def plotTime(x, time):

    fig, ax = plt.subplots()
    ax.plot(time, x)
    ax.set(xlabel='Time (sec)', ylabel='Amplitude',
            title='Time domain plot')
    # fig.savefig("time.png")
    plt.show()

def generateSin(Fs, interval, frequency = 7.0, amplitude = 5.0, phase = 0.0):

    dt = 1.0/Fs                          # sampling period (increment in time)
    time = np.arange(0, interval, dt)    # time vector over interval

    # generate the sin signal
    x = amplitude*np.sin(2*math.pi*frequency*time+phase)

    return time, x

# dc: duty cycle
def generateSquare(dc,Fs, interval, frequency = 7.0, amplitude = 1.0, phase = 0.0):

    dt = 1.0/Fs                          # sampling period (increment in time)
    time = np.arange(0, interval, dt)    # time vector over interval

    x = amplitude* signal.square(2*np.pi*frequency*time + phase, dc)

    return time, x

def cli_error_msg():

    # error message to provide the correct command line interface (CLI) arguments
    print('Valid arguments:')
    print('\trc:  reference code')
    print('\til1: in-lab 1')
    print('\til2: in-lab 2')
    print('\til3: in-lab 3')
    print('\tth:  take-home')
    sys.exit()

def myDFT(x):
    N = len(x)
    Xfreq = np.zeros(N,dtype = complex)

    for i in range(0,N):
        xm =0
        for k in range(0,N):
            xm = xm + x[k]*cmath.exp(-2j*np.pi*k*i/N)
        Xfreq[i] = xm
    return Xfreq

def myIDFT(x):
    N = len(x)
    Xfreq = np.zeros(N,dtype = complex)
    for i in range(0,N):
        xm =0
        for k in range(0,N):
            xm = xm + x[k]*cmath.exp(2j*np.pi*k*i/N)
        Xfreq[i] = xm/N
    return Xfreq

if __name__ == "__main__":

    if len(sys.argv[0:]) != 2:
        cli_error_msg()

    Fs = 100.0          # sampling rate
    interval = 1.0      # set up to one full second

    if (sys.argv[1] == 'rc'): # runs the reference code (rc)

        print('Reference code for the Fourier transform')

        # generate the user-defined sin function
        time, x = generateSin(Fs, interval)
        # plot the signal in time domain
        plotTime(x, time)
        # plot the signal in frequency domain
        plotSpectrum(x, Fs, type = 'FFT')

    elif (sys.argv[1] == 'il1'):

        print('In-lab experiment 1 for the Fourier transform')

        # compute the spectrum with your own DFT
        # you can use cmath.exp() for complex exponentials
        time, x = generateSin(Fs, interval)
        plotTime(x, time)

        plotSpectrum(x, Fs, type = 'FFT') # for validate

        plotSpectrum(x, Fs, type = 'DFT')

        # inverse DFT
        tmpDFT = myDFT(x)
        inv = myIDFT(tmpDFT)
        plotTime(inv,time)

        # confirm DFT/IDFT correctness by checking if x == IDFT(DFT(x))
        checkresult = True
        for i in range(len(x)):
            if(abs(x[i]-inv[i])>0.0001):
                checkresult = False
                break
        print(checkresult)
        # for further details, if any, check the lab document

    elif (sys.argv[1] == 'il2'):

        print('In-lab experiment 2 for the Fourier transform')

        # use np.random.randn() for randomization
        # we can owverwrie the default values
        frequency =  np.random.randn()                       # frequency of the signal
        amplitude =  np.random.randn()                       # amplitude of the signal
        phase = np.random.randn()                          # phase of the signal
        time, x = generateSin(Fs, 10, frequency, 10, phase)

        plotTime(x,time)

        dft = myDFT(x)
        N = len(x)
        energy_x = sum(abs(x)**2)
        energy_X = sum(abs(dft)**2)/N

        checkresult = abs(energy_x-energy_X)<0.0001
        print("energy in time domain is "+str(energy_x)+", enegry in frequency domain is "+str(energy_X)+", the check result is "+str(checkresult))

        # You should also numerically check if the signal energy
        # in time and frequency domains is identical

        # for further details, if any, check the lab document

    elif (sys.argv[1] == 'il3'):

        print('In-lab experiment 3 for the Fourier transform')

        # generate randomized multi-tone signals
        # plot them in both time and frequency domain
        time, x = generateSin(Fs, interval)
        time, x1 = generateSin(Fs, interval, 20, 2, 4) # frequency of 20, amp 2, phase 4
        tone = x + x1
        # plot the signal in time domain
        plotTime(tone, time)
        plotSpectrum(tone, Fs, type = 'DFT')
        # for further details, if any, check the lab document

    elif (sys.argv[1] == 'th'):

        print('Take-home exercise for the Fourier transform')

        # for specific details check the lab document

        # generate the user-defined sin function
        time, x = generateSquare(0.2, Fs, interval, frequency = 5)
        # plot the signal in time domain
        plotTime(x, time)
        # plot the signal in frequency domain
        plotSpectrum(x, Fs, type = 'DFT')

    else:

        cli_error_msg()

    plt.show()
