/*
Comp Eng 3DY4 (Computer Systems Integration Project)

Copyright by Nicola Nicolici
Department of Electrical and Computer Engineering
McMaster University
Ontario, Canada
*/

#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <complex>
#include <cmath>

#define PI 3.14159265358979323846

// used for generate plot
void plotaddedSinesSpectrum(const std::vector<float> &t, const std::vector<float> &x, const std::vector<float> &y, const std::vector<std::complex<float>> &Xf, const std::vector<std::complex<float>> &Yf)
{
	// write data in text format to be parsed by GNU plot
	const std::string filename = "../data/block.dat";
	std::fstream fd;  // file descriptor
	fd.open(filename, std::ios::out);
	fd << "#\tindex\tdata in\tdata out\tspectrum in\tspectrum out\n";
	for (unsigned int i = 0; i < t.size(); i++) {
		fd << "\t " << i << "\t";
		fd << x[i] << "\t "<< y[i] << "\t\t ";
		fd << std::abs(Xf[i])/Xf.size() << "\t\t " << std::abs(Yf[i])/Yf.size() <<"\n";
	}
	std::cout << "Generated " << filename << " to be used by GNU plot\n";
	fd.close();
}

// only first N sample will be used for DFT
void DFT(unsigned int N, const std::vector<float> &x, std::vector<std::complex<float>> &Xf)
{
	Xf.clear();
	Xf.resize(N, static_cast<std::complex<float>>(0));
	for (unsigned int m = 0; m < N; m++) {
		for (unsigned int k = 0; k < N; k++) {
				std::complex<float> expval(0, -2*PI*(k*m) / x.size());
				Xf[m] += x[k] * std::exp(expval);
		}
	}
}


// function for computing the impulse response (reuse from previous experiment)
void impulseResponseLPF(float Fs, float Fc, unsigned short int num_taps, std::vector<float> &h)
{
	// allocate memory for the impulse response
	h.clear(); h.resize(num_taps, 0.0);

	// the rest of the code in this function is to be completed by you
	// based on your understanding and the Python code from the first lab
	const float normCut = Fc/(Fs/2.0);
	float x;
	for (unsigned int i = 0; i<num_taps;i++){
		if(i == (num_taps-1)/2){
			h[i] = normCut;
		}
		else{
			h[i] = normCut*(std::sin(PI*normCut*(i-(num_taps-1)/2)))/(PI*normCut*(i-(num_taps-1)/2.0));
		}
		x = std::sin(i*PI/static_cast<float>(num_taps));
		h[i] = h[i] * std::pow(x,2.0);
	}
}

// function for computing the impulse response (reuse from previous experiment)
void convolveFIR(std::vector<float> &y, const std::vector<float> &x, const std::vector<float> &h)
{
	// allocate memory for the output (filtered) data
	y.clear(); y.resize(x.size()+h.size()-1, 0.0);

	// the rest of the code in this function is to be completed by you
	// based on your understanding and the Python code from the first lab
	for (unsigned int n = 0;n<y.size();n++){
		for (unsigned int k=0;k<h.size();k++){
			if(n-k>=0 && n-k<x.size()){
				y[n] += h[k]*x[n-k];
			}
		}
	}
}

void blockFilter(unsigned int size,std::vector<float>::iterator y, std::vector<float> &state, std::vector<float>::iterator x, const std::vector<float> &h){
	//filter process
	for (unsigned int n = 0 ; n< size;n++){
		for (unsigned int k = 0; k<h.size();k++){
			if(n-k>=0){
				*(y+n) += h[k] * *(x+n-k);
			}
			else{
				*(y+n) += h[k] * state[n-k];
			}
		}
	}

	//store initial value in state
	//find iteration first
	std::vector<float>::const_iterator start;
	std::vector<float>::const_iterator end;

	if (size<state.size()){
		//state[0:state.size()-x.size()] = state[x.size():];
		//state[state.size()-x.size():] = x;
		start = state.begin() + size;
		end = state.end();
		std::vector<float> temp(start,end);
		temp.insert(temp.end(),x, x+size);
		state = temp;

	}
	else{
		start = x + size - state.size();
		end = x + size;
		state.assign(start, end);
	}
}


//size: block size
void blockProcessing(const unsigned int size, std::vector<float> &y, std::vector<float> &x, const std::vector<float> &h){
	y.clear(); y.resize(x.size()+h.size()-1, 0.0);

	unsigned int position = 0;

	std::vector<float> state;
	state.resize(h.size()-1,0.0);

	while (true){
		blockFilter(size, y.begin()+position,state,x.begin()+position,h);

		position += size;
		//ignore the leas
		if (position > x.size()-size){
			break;
		}
	}

}


// function to read audio data from a binary file that contains raw samples
// represented as 32-bit floats; we also assume two audio channels
// note: check the Python script that can prepare this type of files
// directly from .wav files
void read_audio_data(const std::string in_fname, std::vector<float> &audio_data)
{
	// file descriptor for the input to be read
	std::ifstream fdin(in_fname, std::ios::binary);
	if(!fdin) {
		std::cout << "File " << in_fname << " not found ... exiting\n";
		exit(1);
	} else {
		std::cout << "Reading raw audio from \"" << in_fname << "\"\n";
	}
	// search for end of file to count the number of samples to be read
	fdin.seekg(0, std::ios::end);
	// we assume the Python script has written data in 32-bit floats
	const unsigned int num_samples = fdin.tellg() / sizeof(float);

	// allocate memory space to store all the samples
	audio_data.clear(); audio_data.resize(num_samples);
	// back to the beginning of the file to read all samples at once
	fdin.seekg(0, std::ios::beg);
	// do a single read for audio data from the input file stream
	fdin.read(reinterpret_cast<char*>(&audio_data[0]), \
						num_samples*sizeof(float));
	// close the input file
	fdin.close();
}

// function to split an audio data where the left channel is in even samples
// and the right channel is in odd samples
void split_audio_into_channels(const std::vector<float> &audio_data, std::vector<float> &audio_left, std::vector<float> &audio_right)
{
	for (unsigned int i=0; i<audio_data.size(); i++) {
		if (i%2 == 0)
			audio_left.push_back(audio_data[i]);
		else
			audio_right.push_back(audio_data[i]);
	}
}

// function to write audio data to a binary file that contains raw samples
// represented as 32-bit floats; we also assume two audio channels
// note: check the python script that can read this type of files
// and then reformat them to .wav files to be run on third-party players
void write_audio_data(const std::string out_fname, const std::vector<float> &audio_left, const std::vector<float> &audio_right)
{
	// file descriptor for the output to be written
	if (audio_left.size() != audio_right.size()) {
		std::cout << "Something got messed up with audio channels\n";
		std::cout << "They must have the same size ... exiting\n";
		exit(1);
	} else {
		std::cout << "Writing raw audio to \"" << out_fname << "\"\n";
	}
	std::ofstream fdout(out_fname, std::ios::binary);
	for (unsigned int i=0; i<audio_left.size(); i++) {
		// we assume we have handled a stereo audio file
		// hence, we must interleave the two channels
		// (change as needed if testing with mono files)
		fdout.write(reinterpret_cast<const char*>(&audio_left[i]),\
								sizeof(audio_left[i]));
		fdout.write(reinterpret_cast<const char*>(&audio_right[i]),\
								sizeof(audio_right[i]));
	}
	fdout.close();
}

int main()
{
	// assume the wavio.py script was run beforehand to produce a binary file
	const std::string in_fname = "../data/float32samples.bin";
	// declare vector where the audio data will be stored
	std::vector<float> audio_data;
	// note: we allocate memory for audio_data from within this read function
	read_audio_data(in_fname, audio_data);

	// set up the filtering flow
	float Fs = 44100.0;	// sample rate for our "assumed" audio (change as needed for 48 ksamples/sec audio files)
	float Fc = 5000.0;	// cutoff frequency (explore ... but up-to Nyquist only!)
	// number of FIR filter taps (feel free to explore ...)
	unsigned short int num_taps = 101;

	// impulse response (reuse code from the previous experiment)
	std::vector<float> h;
	impulseResponseLPF(Fs, Fc, num_taps, h);
	// note: memory for the impulse response vector and output data vectors
	// should be allocated from within the corresponding functions
	// (as for the previous experiment, from where you should reuse your code)

	// there is one more point before filtering is done:
	// recall we assume there are two channels in the audio data
	// the channels must be handled separately by your DSP functions, hence
	// split the audio_data into two channels (audio_left and audio_right)

	// declare vectors where the audio left/right channels will be stored
	std::vector<float> audio_left, audio_right;
	// note: we allocate the memory for the left/right channels
	// from within the split function that is called in the code below
	split_audio_into_channels(audio_data, audio_left, audio_right);

	// convolution code for filtering (reuse from the previous experiment)
	std::vector<float> single_pass_left, single_pass_right;
	convolveFIR(single_pass_left, audio_left, h);
	convolveFIR(single_pass_right, audio_right, h);
	// note: by default the above convolution produces zero on the output stream
	// YOU will need to update the convolveFIR and impulseResponseLPF functions

	// create a binary file to be read by wavio.py script to produce a .wav file
	// note: small adjustments will need to be made to wavio.py, i.e., you should
	// match the filenames, no need for self-checks as default Python code, ...
	const std::string out_fname = "../data/float32filtered.bin";
	write_audio_data(out_fname, single_pass_left,	single_pass_right);

	//take home block processing
	unsigned int blockSize = 100;

	std::vector<float> block_left, block_right;
	blockProcessing(blockSize,block_left, audio_left, h);
	blockProcessing(blockSize,block_right, audio_right, h);

	const std::string block_out_fname = "../data/float32blockfiltered.bin";
	write_audio_data(block_out_fname, block_left,	block_right);

	// plot comment out to see plot
/*
	std::vector<std::complex<float>> audio_DFT, filtered_DFT;
	std::vector<float> t;
	t.resize(30000,0.0); //p;ot only the first 0.2 s
	DFT(30000,audio_left, audio_DFT);
	DFT(30000, block_left, filtered_DFT);
	plotaddedSinesSpectrum(t,audio_left,block_left,audio_DFT,filtered_DFT);
*/

	return 0;
}
