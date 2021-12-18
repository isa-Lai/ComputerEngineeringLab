// 2DX4StudioW30E1_Decoding a Button Press
// This program illustrates detecting a single button press.
// This program uses code directly from your course textbook.
//
// This example will be extended for in W21E0 and W21E1.
//
// Written by Ama Simons
// January 30, 2020
// Last Update: January 21, 2020



#include <stdint.h>
#include "tm4c1294ncpdt.h"
#include "Systick.h"
#include "PLL.h"



void PortE0E1E2E3_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R4; // activate the clock for Port E
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R4) == 0){}; // allow time for clock to stabilize
	GPIO_PORTE_DEN_R = 0b00001111; // Enable PE0:PE3 
return;
}


void PortM0M1M2M3_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R11; //activate the clock for Port M
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R11) == 0){}; //allow time for clock to stabilize
	GPIO_PORTM_DIR_R = 0b00000000; // Make PM0:PM3 inputs, reading if the button is pressed or not
	GPIO_PORTM_DEN_R = 0b00001111; // Enable PM0:PM3
return;
}




//Turns on D2, D1
void PortN0N1_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R12; //activate the clock for Port N
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R12) == 0){};//allow time for clock to stabilize
GPIO_PORTN_DIR_R=0b00000011; //Make PN0 and PN1 outputs, to turn on LED's
GPIO_PORTN_DEN_R=0b00000011; //Enable PN0 and PN1
return;
}



//Turns on D3, D4
void PortF0F4_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R5; //activate the clock for Port F
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R5) == 0){};//allow time for clock to stabilize
GPIO_PORTF_DIR_R=0b00010001; //Make PF0 and PF4 outputs, to turn on LED's
GPIO_PORTF_DEN_R=0b00010001;
return;
}

void PortK_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R9; //activate the clock for Port F
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R9) == 0){};//allow time for clock to stabilize
GPIO_PORTK_DIR_R=0xFF; //Make Pk0to7, to turn on LED's
GPIO_PORTK_DEN_R=0xFF;
return;
}



uint32_t check(void){
	//Row 4

	GPIO_PORTE_DIR_R = 0b00001111; //Drive Row 2
	GPIO_PORTE_DATA_R = 0b00000111; 
	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b0111);
	}	
	//Row 3
	GPIO_PORTE_DATA_R = 0b00001011; 
	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b1011);
	}
	
	
	//Row 2

	GPIO_PORTE_DATA_R = 0b00001101; 
	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b1101);
	}
	//Row 1

	GPIO_PORTE_DATA_R = 0b00001110;

	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b1110); //return M0-3 E0-3
	}


	
	
	return 0b00000000;
}

//keyboard layout
uint32_t keyboard_bi[4][4] = {{1,2,3,10,},{4,5,6,11},{7,8,9,12},{14,0,15,13}};
char keyboard[4][4] = {{'1','2','3','A'},{'4','5','6','B'},{'7','8','9','C'},{'*','0','#','D'}};

	
uint32_t toBin(uint32_t value){
	int col = (int)((value>>4)^0b1111);
	//for 1 and 2 just dont need to change. 
	//find index of col
	switch(col){
		case 1:
			col=0;
			break;
		case 2:
			col=1;
			break;
		case 4:
			col=2;
			break;
		case 8:
			col=3;
			break;
		default:
			col = 1;}//output 0 as default
	//find index of row
	int row = (int)(((value&0b1111)^0b1111));
	switch(row){
		case 1:
			row=0;
			break;
		case 2:
			row=1;
			break;
		case 4:
			row=2;
			break;
		case 8:
			row=3;
			break;
		default:
			row = 3;}//output 0 as default
	return keyboard_bi[row][col];
}
char toASCII(uint32_t value){
	int col = (int)((value>>4)^0b1111);
	//for 1 and 2 just dont need to change. 
	//find index of col
	switch(col){
		case 1:
			col=0;
			break;
		case 2:
			col=1;
			break;
		case 4:
			col=2;
			break;
		case 8:
			col=3;
			break;
		default:
			col = 1;}//output 0 as default
	//find index of row
	int row = (int)(((value&0b1111)^0b1111));
	switch(row){
		case 1:
			row=0;
			break;
		case 2:
			row=1;
			break;
		case 4:
			row=2;
			break;
		case 8:
			row=3;
			break;
		default:
			row = 3;}//output 0 as default
	return keyboard[row][col];
}

uint32_t value = 0,
					binary = 0;

void setLED(uint32_t value){
	GPIO_PORTK_DATA_R = toBin(value)&0b1111; //set led at pk0-3
}

void setASCII(uint32_t value){
	if(value == 0b00000000){
		GPIO_PORTK_DATA_R= value;
		return;}
	char output = toASCII (value); // get char
	uint32_t output_bi = (uint32_t)output; //change to ascii
	GPIO_PORTK_DATA_R = output_bi; //set led at pk4-7
}


int main(void){
	
	PLL_Init();
	//SysTick_Init();
	PortE0E1E2E3_Init();
	PortM0M1M2M3_Init();
	PortN0N1_Init();
	PortF0F4_Init();
	PortK_Init();
	

	while(1){
		//value = value;
		value = check(); //ms1
		//binary = binary;
		binary = toBin(value); //ms2
		//setLED(value); //for ms3
		setASCII(value); //ms4

	}
}
