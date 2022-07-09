/*
Comp Eng 3DY4 (Computer Systems Integration Project)

Department of Electrical and Computer Engineering
McMaster University
Ontario, Canada
*/

// source code for Fourier-family of functions
#include "dy4.h"
#include "fourier.h"
#include <cmath>
#include <vector>

// just DFT function (no FFT yet)
void DFT(const std::vector<float> &x, std::vector<std::complex<float>> &Xf) {
	Xf.resize(x.size(), static_cast<std::complex<float>>(0));
	for (unsigned int m = 0; m < Xf.size(); m++) {
		for (unsigned int k = 0; k < x.size(); k++) {
				std::complex<float> expval(0, -2*PI*(k*m) / x.size());
				Xf[m] += x[k] * std::exp(expval);
		}
	}
}

// function to compute the magnitude values in a complex vector
void computeVectorMagnitude(const std::vector<std::complex<float>> &Xf, std::vector<float> &Xmag)
{
	// only the positive frequencies
	Xmag.resize(Xf.size(), static_cast<float>(0));
  for (unsigned int i = 0; i < Xf.size(); i++) {
    Xmag[i] = std::abs(Xf[i])/Xf.size();
  }
}

void printRealVector1(const std::vector<float> &x)
{
	std::cout << "Printing float vector of size " << x.size() << "\n";
	for (unsigned int i = 0; i < x.size(); i++)
		std::cout << x[i] << " ";
	std::cout << "\n";
}

void printComplexVector1(const std::vector<std::complex<float>> &X)
{
	std::cout << "Printing complex vector of size " << X.size() << "\n";
	for (unsigned int i = 0; i < X.size(); i++)
		std::cout << X[i] << " ";
	std::cout << "\n";
}

// add your own code to estimate the PSD
// NFFT is already definded in dy4.h
void estimatePSD(const std::vector<float> &samples, const float Fs, std::vector<float> &freq, std::vector<float> &psd_est) {

	float df = Fs/NFFT;

	//initialize frequency
	freq.clear(); freq.resize(Fs/2/df,  static_cast<float>(0));
	for(unsigned int i = 0; i<freq.size();i++)
		freq[i] = i*df;

	// design the Hann window used to smoothen the discrete data in order
	// to reduce the spectral leakage after the Fourier transform
	std::vector<float> hann;
	hann.resize(NFFT, static_cast<float>(0));
	for (unsigned int i = 0; i<NFFT; i++)
		hann[i] = std::pow(std::sin(i*PI/NFFT),2);
	// create an empty list where the PSD for each segment is computed
	std::vector<float> psd_list;

	// samples should be a multiple of frequency bins, so
	// the number of segments used for estimation is an integer
	// note: for this to work you must provide an argument for the
	// number of frequency bins not greater than the number of samples!
	unsigned int no_segments = (int)std::floor(samples.size()/(float)NFFT);
	std::vector<std::complex<float>>  Xf;
	std::vector<float> psd_seg, windowed_samples;

	// iterate through all the segments
	for (unsigned int k = 0; k<no_segments ; k++){
		windowed_samples.clear();windowed_samples.resize((int)NFFT, static_cast<float>(0));

		// apply the hann window (using pointwise multiplication)
		// before computing the Fourier transform on a segment
		for (unsigned int i = 0; i<NFFT;i++){
			windowed_samples[i] = samples[(int)k*NFFT+i] * hann[i];
		}

		// compute the Fourier transform using the built-in FFT from numpy
		Xf.clear();
		DFT(windowed_samples, Xf);

		// since input is real, we keep only the positive half of the spectrum
		// however, we will also add the signal energy of negative frequencies
		// to have a better a more accurate PSD estimate when plotting
		std::vector<std::complex<float>>::iterator end = Xf.begin()+(int)NFFT/2;
		Xf.assign(Xf.begin(), end); // keep only positive freq bins

		//psd_seg
		psd_seg.clear(); psd_seg.resize(Xf.size(),  static_cast<float>(0));
		for (unsigned int i = 0; i<Xf.size();i++){
			psd_seg[i] = 2*(1/(Fs*NFFT/2)) * std::pow(std::abs(Xf[i]),2) ;// compute signal power
			 // add the energy from the negative freq bins
			 // translate to the decibel (dB) scale
			 psd_seg[i] = 10*std::log10(psd_seg[i]);
		}

		// append to the list where PSD for each segment is stored
		// in sequential order (first segment, followed by the second one, ...)
		psd_list.insert(psd_list.end(), psd_seg.begin(), psd_seg.end());

	}
	//printRealVector1(psd_list);

	// compute the estimate to be returned by the function through averaging
	psd_est.clear();
	psd_est.resize((int)NFFT/2,  static_cast<float>(0));

	// iterate through all the frequency bins (positive freq only)
	// from all segments and average them (one bin at a time ...)
	for (unsigned int k = 0; k< (int)(NFFT/2); k++){
		// iterate through all the segments
		for( unsigned int l = 0; l< no_segments; l++){
			psd_est[k] += psd_list[k + l*(int)(NFFT/2)];
		}
		// compute the estimate for each bin
		psd_est[k] = psd_est[k] / (float)no_segments;
	}
	//printRealVector1(psd_est);

}
