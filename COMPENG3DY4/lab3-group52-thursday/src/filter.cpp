/*
Comp Eng 3DY4 (Computer Systems Integration Project)

Department of Electrical and Computer Engineering
McMaster University
Ontario, Canada
*/

#include <cmath>
#include "dy4.h"
#include "filter.h"

// function to compute the impulse response "h" based on the sinc function
void impulseResponseLPF(float Fs, float Fc, unsigned short int num_taps, std::vector<float> &h)
{
	// bring your own functionality
  // allocate memory for the impulse response
	h.clear(); h.resize(num_taps, 0.0);

	// the rest of the code in this function is to be completed by you
	// based on your understanding and the Python code from the first lab
	const float normCut = Fc/(Fs/2.0);
	float x;
	for (unsigned short int i = 0; i<num_taps;i++){
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

// function to compute the filtered output "y" by doing the convolution
// of the input data "x" with the impulse response "h"
void convolveFIR(std::vector<float> &y, const std::vector<float> &x, const std::vector<float> &h)
{
	// bring your own functionality
  // allocate memory for the output (filtered) data
	y.clear(); y.resize(x.size()+h.size()-1, 0.0);

	// the rest of the code in this function is to be completed by you
	// based on your understanding and the Python code from the first lab
	for (unsigned int n = 0;n<y.size();n++){
		for (unsigned int k=0;k<h.size();k++){
			if(n>=k && n-k<x.size()){
				y[n] += h[k]*x[n-k];
			}
		}
	}
}
