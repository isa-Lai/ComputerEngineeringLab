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
from scipy.io import wavfile
from scipy import signal
import sys

from filterDesign import myLowPassFilter, freqzPlot
from fourierTransform import plotTime,plotSpectrum



def filter_block_processing(audio_data, \
							block_size, \
							audio_Fc, \
							audio_Fs, \
							N_taps):

	# derive filter coefficients
	firwin_coeff = signal.firwin(N_taps,
								audio_Fc/(audio_Fs/2),
								window=('hann'))

	# we assume the data is stereo as in the audio test file
	filtered_data = np.empty(shape = audio_data.shape)

	# start at the first block (with relative position zero)
	position = 0

	# intiial filter state - state is the size of the impulse response minus 1
	# we need to channels for the state (one for each audio channel)
	filter_state = np.zeros(shape = (len(firwin_coeff)-1, 2))

	while True:

		# filter both left and right channels
		filtered_data[position:position+block_size, 0], filter_state[:,0] = \
			signal.lfilter(firwin_coeff, 1.0, \
			audio_data[position:position+block_size, 0], zi = filter_state[:,0])
		# the filter state has been saved only for the first channel above
		# you will need to adjust the code for the second channel below
		filtered_data[position:position+block_size, 1], filter_state[:,1] = \
			signal.lfilter(firwin_coeff, 1.0, \
			audio_data[position:position+block_size, 1], zi = filter_state[:,1])

		position += block_size

		# the last incomplete block is ignored
		if position > len(audio_data):
			break

	return filtered_data

def my_filter_block_processing_with_state(audio_data, \
							block_size, \
							audio_Fc, \
							audio_Fs, \
							N_taps):

	# derive filter coefficients
	firwin_coeff = myLowPassFilter(audio_Fc, audio_Fs, Ntaps)

	# we assume the data is stereo as in the audio test file
	filtered_data = np.empty(shape = audio_data.shape)

	# start at the first block (with relative position zero)
	position = 0

	# intiial filter state - state is the size of the impulse response minus 1
	# we need to channels for the state (one for each audio channel)
	filter_state = np.zeros(shape = (len(firwin_coeff)-1, 2))

	while True:

		# filter both left and right channels
		filtered_data[position:position+block_size, 0], filter_state[:,0] = \
			block_processing_with_state(firwin_coeff,
			audio_data[position:position+block_size, 0], filter_state[:,0])
		# the filter state has been saved only for the first channel above
		# you will need to adjust the code for the second channel below
		filtered_data[position:position+block_size, 1], filter_state[:,1] = \
			block_processing_with_state(firwin_coeff,
			audio_data[position:position+block_size, 1], filter_state[:,1])

		position += block_size

		# the last incomplete block is ignored
		if position > len(audio_data)-block_size:
			break

	return filtered_data

# Please note: this function is replaced by my_filter_block_processing_with_state() and block_processing_with_state()
# it direcly use convolution for the whole signal instead of using blocks
# Currently not in use, only for reference
def myfilter_processing(audio_data, \
							block_size, \
							audio_Fc, \
							audio_Fs, \
							N_taps):

	# derive filter coefficients
	firwin_coeff = myLowPassFilter(audio_Fc, audio_Fs, Ntaps)

	# we assume the data is stereo as in the audio test file
	filtered_data = np.empty(shape = audio_data.shape)

	# start at the first block (with relative position zero)
	position = 0


	while True:

		# filter both left and right channels
		for i in range(len(firwin_coeff)):
			# prevent negative index
			if (position-i<0):
				continue
			# filter both channel
			filtered_data[position,0] = filtered_data[position,0] + firwin_coeff[i]*audio_data[position-i,0]
			filtered_data[position,1] = filtered_data[position,1] + firwin_coeff[i]*audio_data[position-i,1]


		# the last incomplete block is ignored
		if position > len(audio_data):
			break

	return filtered_data

def block_processing_with_state(h,xb,state):
	yb = np.zeros(len(xb))
	for n in range(len(yb)):
		for k in range(len(h)):
			if n-k>=0:
				yb[n] += h[k] *xb[n-k]
			#to compute with the initial value
			else:
				yb[n] += h[k] * state[n-k]

	if(len(xb)<len(state)):
		# original values are push to the front
		# values in xb is inserted at the back
		state[0:len(state)-len(xb)] = state[len(xb):]
		state[len(state)-len(xb):] = xb
	else:
		# since xb have enough value stored in state
		# direcly assign
		state = xb[len(xb)-len(state):]
	return yb,state


def filter_single_pass(audio_data, audio_Fc, audio_Fs, N_taps):

	# derive filter coefficients
	firwin_coeff = signal.firwin(N_taps,
								audio_Fc/(audio_Fs/2),
								window=('hann'))

	# we assume the data is stereo as in the audio test file
	filtered_data = np.empty(shape = audio_data.shape)
	# filter left channel
	filtered_data[:,0] = signal.lfilter(firwin_coeff, 1.0, audio_data[:,0])
	# filter stereo channel
	filtered_data[:,1] = signal.lfilter(firwin_coeff, 1.0, audio_data[:,1])

	return filtered_data

def cli_error_msg():

	# error message to provide the correct command line interface (CLI) arguments
	print('Valid arguments:')
	print('\trc:  reference code')
	print('\til1: in-lab 1')
	print('\tth:  take-home')
	sys.exit()

# audio test file from: https://www.videvo.net/royalty-free-music/
if __name__ == "__main__":

	if len(sys.argv[0:]) != 2:
		cli_error_msg()

	# use use wavfile from scipy.io for handling .wav files
	print('Opening audio file (.wav format)')
	audio_Fs, audio_data = wavfile.read("../data/audio_test.wav")
	print(' Audio sample rate = {0:d} \
		\n Number of channels = {1:d} \
		\n Numbef of samples = {2:d}' \
		.format(int(audio_Fs), audio_data.ndim, len(audio_data)))

	if (sys.argv[1] == 'rc'): # runs the reference code (rc)

		print('Reference code for processing streams divided in blocks')

		# you can control the cutoff frequency and number of taps
		single_pass_data = filter_single_pass(audio_data, \
							audio_Fc = 10e3, \
							audio_Fs = audio_Fs, \
							N_taps = 51)

		# write filtered data back to a .wav file
		wavfile.write("../data/single_pass_filtered.wav", \
					audio_Fs, \
					single_pass_data.astype(np.int16))

	elif (sys.argv[1] == 'il1'):

		print('In-lab experiment 1 for processing streams divided in blocks')

		# you can control also the block size
		block_processing_data = filter_block_processing(audio_data, \
							block_size = 50, \
							audio_Fc = 10e3, \
							audio_Fs = audio_Fs, \
							N_taps = 51)

		wavfile.write("../data/block_processing_filtered.wav", \
					audio_Fs, \
					block_processing_data.astype(np.int16))
		# plot half s to check
		dt = 1/audio_Fs
		time = np.arange(0, 0.5, dt)
		plotTime(audio_data[:round(audio_Fs/2),0],time)
		plotTime(block_processing_data[:round(audio_Fs/2),0],time)
		plotSpectrum(audio_data[:round(audio_Fs/2)], audio_Fs)
		plotSpectrum(block_processing_data[:round(audio_Fs/2)], audio_Fs)

		plt.show()

	elif (sys.argv[1] == 'th'):

		print('Take-home exercise for processing streams divided in blocks')

		# for specific details check the lab document

		# it is suggested that you add plotting while troubleshooting
		# if you plot in the time domain, select a subset of samples,
		# from a particular channel (or both channels) e.g.,
		# audio_data[start:start+number_of_samples, 0]

		Fc = 5e3
		Ntaps = 51
		firwin_coeff = myLowPassFilter(Fc, audio_Fs, Ntaps) # direcly using from frilterDesign.py


		block_processing_data = my_filter_block_processing_with_state(audio_data, \
							block_size = 100, \
							audio_Fc = Fc, \
							audio_Fs = audio_Fs, \
							N_taps = 51)

		wavfile.write("../data/myblock_processing_filtered.wav", \
					audio_Fs, \
					block_processing_data.astype(np.int16))

		# PLOT TO check
		freqzPlot(firwin_coeff, audio_Fs, 'my own FIR design with ' + str(Ntaps) + ' taps')

		# plot 1 s
		dt = 1/audio_Fs
		time = np.arange(0, 0.5, dt)
		plotTime(audio_data[:round(audio_Fs/2),1],time)
		plotTime(block_processing_data[:round(audio_Fs/2),1],time)
		plotSpectrum(audio_data[:round(audio_Fs/2),1], audio_Fs)
		plotSpectrum(block_processing_data[:round(audio_Fs/2),1], audio_Fs)
		plt.show()

	else:

		cli_error_msg()

	# plt.show()
