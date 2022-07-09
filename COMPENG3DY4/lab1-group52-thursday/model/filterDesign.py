#
# Comp Eng 3DY4 (Computer Systems Integration Project)
#
# Copyright by Nicola Nicolici
# Department of Electrical and Computer Engineering
# McMaster University
# Ontario, Canada
#

import matplotlib.pyplot as plt
from scipy import signal
import numpy as np
import sys

# use generateSin/plotTime from the fourierTransform module
from fourierTransform import generateSin, plotTime

def freqzPlot(coeff, Fs, msg):

	# find the frequency response using freqz from SciPy:
	# https://docs.scipy.org/doc/scipy/reference/generated/scipy.signal.freqz.html
	w, h = signal.freqz(coeff)

	# Reminder: np.pi rad/sample is actually the Nyquist frequency
	w = w * Fs/(2*np.pi) # needed to draw the frequency on the X axis

	# plots the magnitude response where the x axis is normalized in rad/sample
	fig, ax1 = plt.subplots()
	ax1.set_title('Digital filter frequency response (' + msg + ')')
	ax1.plot(w, 20 * np.log10(abs(h)), 'b')
	ax1.set_ylabel('Amplitude [dB]', color='b')
	ax1.set_xlabel('Frequency [Hz]')

	# uncomment the lines below if you wish to inspect the phase response
	# Note: as important as the phase response is for some applications,
	# it is not critical at this stage because we expect a linear phase in the passband

	ax2 = ax1.twinx()
	angles = np.unwrap(np.angle(h))
	ax2.plot(w, angles, 'g')
	ax2.set_ylabel('Angle (radians)', color='g')

def filterSin(Fs, Fc, coeff):

	# we can control the frequency relative to the filter cutoff
	time, x = generateSin(Fs, interval = 1.0, frequency = Fc * 2)
	plotTime(x, time)

	# use lfilter from SciPy for FIR filtering:
	# https://docs.scipy.org/doc/scipy/reference/generated/scipy.signal.lfilter.html
	fx = signal.lfilter(coeff, 1.0, x)

	# you should clearly observe the effects (attenuation, delay) introduced by the filter
	plotTime(fx, time)

def myLowPassFilter(Fc, Fs, Ntaps):
	h = np.zeros(Ntaps)
	norm_f = Fc/(Fs/2)
	for i in range(Ntaps):
		if (i == (Ntaps -1)/2):
			h[i] = norm_f
		else:
			h[i] = norm_f*np.sin(np.pi*norm_f*(i-(Ntaps-1)/2))/(np.pi*norm_f*(i-(Ntaps-1)/2))
		h[i] = h[i] *(np.sin(i*np.pi/Ntaps))**2
	return h

#---input: cutoff freq at low freq, cutoof at high, sampling freq, Ntaps
def myBandPassFilter(Fc_L,Fc_H, Fs, Ntaps):

	nyq = Fs/2.0 # Nyquist freq
	filter = signal.firwin(Ntaps,[Fc_L,Fc_H],nyq = nyq, pass_zero = 'bandpass', window = 'hann')

	return filter

def cli_error_msg():

	# error message to provide the correct command line interface (CLI) arguments
	print('Valid arguments:')
	print('\trc:  reference code')
	print('\til1: in-lab 1')
	print('\til2: in-lab 2')
	print('\tth:  take-home')
	sys.exit()

if __name__ == "__main__":

	if len(sys.argv[0:]) != 2:
		cli_error_msg()

	Fs = 100.0           # sampling rate
	Fc = 15.0            # cutoff frequency
	N_taps = 41          # number of taps for the FIR


	if (sys.argv[1] == 'rc'): # runs the reference code (rc)

		print('Reference code for the digital filter design')

		# derive filter coefficients using firwin from Scipy:
		# https://docs.scipy.org/doc/scipy/reference/generated/scipy.signal.firwin.html
		# second argument is the normalized cutoff frequency, i.e., the
		# cutoff frequency divided by Nyquist frequency (half of sampling rate)
		firwin_coeff = signal.firwin(N_taps, Fc/(Fs/2), window=('hann'))

		# plot the frequency response obtained through freqz
		freqzPlot(firwin_coeff, Fs, 'firwin for ' + str(int(Fc)) + ' Hz cutoff with ' + str(N_taps) + ' taps')

	elif (sys.argv[1] == 'il1'):

		print('In-lab experiment 1 for the digital filter design')

		# implement your own method for finding the coefficients for a low pass filter
		# my_own_coeff = ... provide the following arguments: Fc, Fs and N_taps
		my_own_coeff = myLowPassFilter(Fc,Fs,N_taps)
		# compare through visual inspection the frequency response against firwin
		freqzPlot(my_own_coeff, Fs, 'my own FIR design with ' + str(N_taps) + ' taps')

		#reference code
		firwin_coeff = signal.firwin(N_taps, Fc/(Fs/2), window=('hann'))

		# plot the frequency response obtained through freqz
		freqzPlot(firwin_coeff, Fs, 'firwin for ' + str(int(Fc)) + ' Hz cutoff with ' + str(N_taps) + ' taps')


	elif (sys.argv[1] == 'il2'):

		print('In-lab experiment 2 for the digital filter design')

		# you can confirm that a single tone has been filtered
		my_own_coeff = myLowPassFilter(Fc,Fs,N_taps)
		filterSin(Fs, Fc, my_own_coeff)
		freqzPlot(my_own_coeff, Fs, str(int(Fc)) + ' Hz cutoff with ' + str(N_taps) + ' taps')

	elif (sys.argv[1] == 'th'):

		print('Take-home exercise for the digital filter design')

		# for specific details check the lab document
		Fc_L = 12.0
		Fc_H = 30.0

		bandpass = myBandPassFilter(Fc_L,Fc_H, Fs, N_taps)
		freqzPlot(bandpass, Fs, 'my own bandpass filter with ' + str(N_taps) + ' taps')

		#filter a single sin function
		filterSin(Fs, Fc, bandpass)

		#filter a 3 tones wave
		time, x1 = generateSin(Fs, interval = 1, frequency = 20, amplitude = 10)
		time, x2 = generateSin(Fs, interval = 1, frequency = 6, amplitude = 0.7)
		time, x3 = generateSin(Fs, interval = 1, frequency = 40, amplitude = 2)
		wave = x1+x2+x3

		# uncomment to see three tone repectively
		#plotTime(x1, time)
		#plotTime(x2, time)
		#plotTime(x3, time)
		plotTime(wave, time)

		fx = signal.lfilter(bandpass, 1.0, wave)
		plotTime(fx, time)



	else:

		cli_error_msg()

	plt.show()
