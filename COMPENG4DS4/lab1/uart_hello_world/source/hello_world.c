/*
 * Copyright (c) 2013 - 2015, Freescale Semiconductor, Inc.
 * Copyright 2016-2017 NXP
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

#include "fsl_device_registers.h"
#include "fsl_debug_console.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "board.h"
#include "fsl_uart.h"
#include "fsl_ftm.h"
#include "stdlib.h"
#include "string.h"

/*******************************************************************************
 * Definitions
 ******************************************************************************/
#define TARGET_UART UART4

char ch[4];
volatile int new_char = 0;
int input = 0;
int inputServo = 0;
int flag = 0;
int isNeg = 0;
int i = 0;

void setupUART()
{
uart_config_t config;
UART_GetDefaultConfig(&config);
config.baudRate_Bps = 57600;
config.enableTx = true;
config.enableRx = true;
config.enableRxRTS = true;
config.enableTxCTS = true;
UART_Init(TARGET_UART, &config, CLOCK_GetFreq(kCLOCK_BusClk));

UART_EnableInterrupts(TARGET_UART, kUART_RxDataRegFullInterruptEnable);
EnableIRQ(UART4_RX_TX_IRQn);
}


#define FTM_MOTOR FTM0
#define FTM_CHANNEL_DC_MOTOR kFTM_Chnl_0
#define FTM_CHANNEL_SERVO_MOTOR kFTM_Chnl_3

/*******************************************************************************
 * Prototypes
 ******************************************************************************/

void setupPWM(ftm_chnl_t chl)
{
ftm_config_t ftmInfo;
ftm_chnl_pwm_signal_param_t ftmParam;
ftm_pwm_level_select_t pwmLevel = kFTM_HighTrue;
ftmParam.chnlNumber = chl;
ftmParam.level = pwmLevel;
ftmParam.dutyCyclePercent = 7;
ftmParam.firstEdgeDelayPercent = 0U;
ftmParam.enableComplementary = false;
ftmParam.enableDeadtime = false;
FTM_GetDefaultConfig(&ftmInfo);
ftmInfo.prescale = kFTM_Prescale_Divide_128;
FTM_Init(FTM_MOTOR, &ftmInfo);
FTM_SetupPwm(FTM_MOTOR, &ftmParam, 1U, kFTM_EdgeAlignedPwm, 50U, CLOCK_GetFreq(
kCLOCK_BusClk));
FTM_StartTimer(FTM_MOTOR, kFTM_SystemClock);
}

void updatePWM_dutyCycle(ftm_chnl_t channel, float dutyCycle)
{
uint32_t cnv, cnvFirstEdge = 0, mod;
/* The CHANNEL_COUNT macro returns -1 if it cannot match the FTM instance */
assert(-1 != FSL_FEATURE_FTM_CHANNEL_COUNTn(FTM_MOTOR));
mod = FTM_MOTOR->MOD;
if (dutyCycle == 0U)
{
/* Signal stays low */
cnv = 0;
}
else
{
cnv = mod * dutyCycle;
/* For 100% duty cycle */
if (cnv >= mod)
{
cnv = mod + 1U;
}
}
FTM_MOTOR->CONTROLS[channel].CnV = cnv;
}
/*******************************************************************************
 * Prototypes
 ******************************************************************************/

/*******************************************************************************
 * Code
 ******************************************************************************/
/*!
 * @brief Main function
 */

int convertChar2Int(char* ch){
	int result = 0;
	int tmp = 0;
	for (int index = 1; index < 4; index ++)
	{
		switch (ch[index])
		{
		case '1': tmp = 1; break;
		case '2': tmp = 2; break;
		case '3': tmp = 3; break;
		case '4': tmp = 4; break;
		case '5': tmp = 5; break;
		case '6': tmp = 6; break;
		case '7': tmp = 7; break;
		case '8': tmp = 8; break;
		case '9': tmp = 9; break;
		default: tmp = 0;
		}
		result = result*10 + tmp;
	}
	if (ch[0] == '-') result *= -1;
	return result;
}

// please send message 2 by 2
int count = 0;
void UART4_RX_TX_IRQHandler()
{
	//UART_GetStatusFlags(TARGET_UART);
	UART_ReadBlocking(TARGET_UART, &ch[count], 2);
	count += 2;
	//ch = UART_ReadByte(TARGET_UART);
	PRINTF("UART %s\r\n", ch);
	if (count == 4){
		count = 0;
		if (i == 0) {input = convertChar2Int(ch);}
		//exit
		if (i == 1){ flag = 1; i =0; inputServo = convertChar2Int(ch);}
		else i++;
	}
}

int main(void)
{
    //char ch;
    char txbuff[] = "12345\r\n";
    //int input;
    //int inputServo;
    float dutyCycle;
    float dutyCycleServo;
    char substring[5];
    //int flag = 0;
    //int isNeg = 0;




    /* Init board hardware. */
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    BOARD_InitDebugConsole();

    setupPWM(FTM_CHANNEL_DC_MOTOR);
    setupPWM(FTM_CHANNEL_SERVO_MOTOR);
    setupUART();
    /******* Delay *******/
    for(volatile int i = 0U; i < 10000000; i++)
    	__asm("NOP");
    //PRINTF("%s", txbuff);

    updatePWM_dutyCycle(FTM_CHANNEL_DC_MOTOR, 0.07025);
    updatePWM_dutyCycle(FTM_CHANNEL_SERVO_MOTOR, 0.075);
    FTM_SetSoftwareTrigger(FTM_MOTOR, true);

    UART_WriteBlocking(TARGET_UART, txbuff, sizeof(txbuff) - 1);
    while (1)
    {
    	/*
    	// You can send message 4 by 4, like 0020(for dc) -080(for servo)
    	UART_ReadBlocking(TARGET_UART, ch, 4);
    	//ch = UART_ReadByte(TARGET_UART);
    	PRINTF("UART %s\r\n", ch);
    	if (i == 0) {input = convertChar2Int(ch);}
    	//exit
    	if (i == 1){ flag = 1; i =0; inputServo = convertChar2Int(ch);}
    	else i++;
    	*/

    	//scanf("%d",&inputServo );

    	if (flag == 1)
    	{
			dutyCycle = input * 0.025f/100.0f + 0.07025;
			updatePWM_dutyCycle(FTM_CHANNEL_DC_MOTOR, dutyCycle);

			dutyCycleServo = inputServo * 0.025f/100.0f + 0.075;
			updatePWM_dutyCycle(FTM_CHANNEL_SERVO_MOTOR, dutyCycleServo);

			FTM_SetSoftwareTrigger(FTM_MOTOR, true);

			isNeg = 0;
			flag = 0;
			input = 0;
			inputServo = 0;
    	}

    }
}
