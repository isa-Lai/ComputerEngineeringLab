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


//keyboard output
void PortL0L1L2L3_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R10; // activate the clock for Port E
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R10) == 0){}; // allow time for clock to stabilize
	GPIO_PORTL_DEN_R = 0b00001111; // Enable PE0:PE3 
return;
}

//keyboard input
void PortM0M1M2M3_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R11; //activate the clock for Port M
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R11) == 0){}; //allow time for clock to stabilize
	GPIO_PORTM_DIR_R = 0b00000000; // Make PM0:PM3 inputs, reading if the button is pressed or not
	GPIO_PORTM_DEN_R = 0b00001111; // Enable PM0:PM3
return;
}

//motor
void PortH_Init(void){
	//Use PortM pins for output
	SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R7;				// activate clock for Port N
	while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R7) == 0){};	// allow time for clock to stabilize
	GPIO_PORTH_DIR_R |= 0xFF;        								// make PN0 out (PN0 built-in LED1)
  GPIO_PORTH_AFSEL_R &= ~0xFF;     								// disable alt funct on PN0
  GPIO_PORTH_DEN_R |= 0xFF;        								// enable digital I/O on PN0
																									// configure PN1 as GPIO
  //GPIO_PORTM_PCTL_R = (GPIO_PORTM_PCTL_R&0xFFFFFF0F)+0x00000000;
  GPIO_PORTH_AMSEL_R &= ~0xFF;     								// disable analog functionality on PN0		
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
//leds
void PortK_Init(void){
SYSCTL_RCGCGPIO_R |= SYSCTL_RCGCGPIO_R9; //activate the clock for Port F
while((SYSCTL_PRGPIO_R&SYSCTL_PRGPIO_R9) == 0){};//allow time for clock to stabilize
GPIO_PORTK_DIR_R=0xFF; //Make Pk0to7, to turn on LED's
GPIO_PORTK_DEN_R=0xFF;
return;
}

//Flash D1
void FlashLED1() {
			GPIO_PORTN_DATA_R ^= 0b00000010; 								//hello world!
			SysTick_Wait10ms(1);														//.1s delay
			GPIO_PORTN_DATA_R ^= 0b00000010;			
		
}


uint32_t check(void){
	//Row 4

	GPIO_PORTL_DIR_R = 0b00001111; //Drive Row 2
	GPIO_PORTL_DATA_R = 0b00000111; 
	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b0111);
	}	
	//Row 3
	GPIO_PORTL_DATA_R = 0b00001011; 
	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b1011);
	}
	
	
	//Row 2

	GPIO_PORTL_DATA_R = 0b00001101; 
	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b1101);
	}
	//Row 1

	GPIO_PORTL_DATA_R = 0b00001110;

	
	if((GPIO_PORTM_DATA_R&0b1111)^0b00001111){
			return (((GPIO_PORTM_DATA_R&0b1111)<<4)|0b1110); //return M0-3 E0-3
	}


	
	
	return 0b00000000;
}

//keyboard layout
uint32_t keyboard_bi[4][4] = {{1,2,3,10,},{4,5,6,11},{7,8,9,12},{14,0,15,13}};


	
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
			col = 4;}//output 0 as default
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
			row = 4;}//output 0 as default
	if(col>=4 && row>=4)
		return 0;
	return keyboard_bi[row][col];
}


//motor spin
void spin(int dir,int speed,float angle){
	int step = (int)(angle/360.0*512.0);
	int count = 1;
	if(dir == 1){
		//clockwise spin
		while(1){
			GPIO_PORTH_DATA_R = 0b00001100;
			SysTick_Wait10ms(speed);
			GPIO_PORTH_DATA_R = 0b0000110;
			SysTick_Wait10ms(speed);
			GPIO_PORTH_DATA_R = 0b00000011;
			SysTick_Wait10ms(speed);
			GPIO_PORTH_DATA_R = 0b00001001;
			SysTick_Wait10ms(speed);
			if(count == step){
				FlashLED1();
				count = 1;
			}
			else
				count++;
			uint32_t stop = check();
			if(stop!=0 && toBin(stop)==0) //if 0 pushed break
				break;
		}
	}
	else if(dir == -1){
		//conter clockwise spin
		while(1){
			GPIO_PORTH_DATA_R = 0b00000011;
			SysTick_Wait10ms(speed);
			GPIO_PORTH_DATA_R = 0b0000110;
			SysTick_Wait10ms(speed);
			GPIO_PORTH_DATA_R = 0b00001100;
			SysTick_Wait10ms(speed);
			GPIO_PORTH_DATA_R = 0b00001001;
			SysTick_Wait10ms(speed);
			if(count == step){
				FlashLED1();
				count = 1;
			}
			else
				count++;
			uint32_t stop = check();
			if(stop!=0 && toBin(stop)==0) //if 0 pushed break
				break;
		}
	}
}

//this is for ms3
void setLED(int angle, int direction, int speed){
	uint32_t angle_bi = angle-10;
	uint32_t dir_bi = (direction == 1)?0:1;
	uint32_t speed_bi = (uint32_t)speed;
	GPIO_PORTK_DATA_R = (angle_bi<<5)|dir_bi<<4|speed_bi;
}
	
//to recieve input
void getInput(){
	uint32_t input = 0;
	int angle_input, speed,direction;
	float angle;
	//state 0 , to get angle
	GPIO_PORTK_DATA_R = 0;
	GPIO_PORTN_DATA_R = 0;
	GPIO_PORTF_DATA_R = 0;
	while(1){
		input = check(); //get angle
		if(input ==0)
			continue;
		angle_input = (int)toBin(input);
		if(angle_input == 10){
			angle = 11.25;
			break;
		}
		if(angle_input == 11){
			angle = 45;
			break;
		}
		if(angle_input == 12){
			angle = 90;
			break;
		}
		if(angle_input == 13){
			angle = 360;
			break;
		}
		else
			continue;
	}
	//state 1, to get direction
	GPIO_PORTN_DATA_R = 0x1;
	while(1){
		input = check(); 
		if(input ==0)
			continue;
		direction = (int)toBin(input);
		if(direction == 14){
			direction = 1;
			break;
		}
		if(direction == 15){
			direction = -1;
			break;
		}
		if(direction == 0){
			return;
		}
		else
			continue;
	}
	//state 2, to get speed
	GPIO_PORTN_DATA_R = 0x0;
	GPIO_PORTF_DATA_R = 0x10;
	while(1){
		input = check(); 
		if(input ==0)
			continue;
		speed = (int)toBin(input);
		if(speed<10&&speed>0){
			break;
		}
		if(speed == 0)
			return;
		else
			continue;
	}
	//state 3
	GPIO_PORTF_DATA_R = 0x01;
	setLED(angle_input,direction, speed);
	spin(direction,speed, angle);
}

uint32_t value = 0,
				binary = 0;
int main(void){
	
	PLL_Init();
	SysTick_Init();
	PortL0L1L2L3_Init();
	PortM0M1M2M3_Init();
	PortN0N1_Init();
	PortF0F4_Init();
	PortK_Init();
	PortH_Init();
	

	while(1){
		getInput();
		//value = value;
		value = check(); //ms1
		//binary = binary;
		binary = toBin(value); //ms2
		//setLED(value); //for ms3

	}
}
