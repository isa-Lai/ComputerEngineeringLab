//*****************************************************************************
//
// Copyright (C) 2017 Texas Instruments Incorporated - http://www.ti.com/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
//  Redistributions of source code must retain the above copyright
//  notice, this list of conditions and the following disclaimer.
//
//  Redistributions in binary form must reproduce the above copyright
//  notice, this list of conditions and the following disclaimer in the
//  documentation and/or other materials provided with the
//  distribution.
//
//  Neither the name of Texas Instruments Incorporated nor the names of
//  its contributors may be used to endorse or promote products derived
//  from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//*****************************************************************************

//*****************************************************************************
//
// MSP432E4 main.c template
//
//*****************************************************************************

#include "msp.h"

int main(void)
{
    // Halt watchdog timer
		// Enable GPION peripheral
	SYSCTL->RCGCGPIO |= SYSCTL_RCGCGPIO_R12;
	SYSCTL->RCGCGPIO |= SYSCTL_RCGCGPIO_R11;

	// Check if the peripheral access is enabled.
	while(!(SYSCTL->PRGPIO & SYSCTL_PRGPIO_R12));
	while(!(SYSCTL->PRGPIO & SYSCTL_PRGPIO_R11));

	/* Enable the GPIO pin for the LED (PN0). */

	// Set drive strength
	GPION->DIR = 0x03;
	GPIOM->DIR = 0xFF;
	
	GPION->DEN = 0x03;
	GPIOM->DEN = 0xFF;

	uint32_t counter = 0;
	
	GPION->DATA = 0x01;
	while(1)
	{
	    // Toggle GPIO pin value
	    GPION->DATA ^= BIT0;
			GPION->DATA ^= BIT1;
	    for(counter = 0; counter < 1000000; counter++){}
			GPIOM->DATA = 0xFF;
	}
}
