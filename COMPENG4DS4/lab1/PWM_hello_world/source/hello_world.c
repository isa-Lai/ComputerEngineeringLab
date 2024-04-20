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
#include "fsl_ftm.h"
/*******************************************************************************
 * Definitions
 ******************************************************************************/
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
 * Code
 ******************************************************************************/
/*!
 * @brief Main function
 */
int main(void)
 {
 uint8_t ch;
 int input;
 int inputServo;
 float dutyCycle;
 float dutyCycleServo;

 BOARD_InitBootPins();
 BOARD_InitBootClocks();

 setupPWM(FTM_CHANNEL_DC_MOTOR);
 setupPWM(FTM_CHANNEL_SERVO_MOTOR);
 /******* Delay *******/
 for(volatile int i = 0U; i < 1000000; i++)
 __asm("NOP");

 updatePWM_dutyCycle(FTM_CHANNEL_DC_MOTOR, 0.07025); // 0.07025 has speed 0
 updatePWM_dutyCycle(FTM_CHANNEL_SERVO_MOTOR, 0.075);
 FTM_SetSoftwareTrigger(FTM_MOTOR, true);

 scanf("s = %d %d", &input,&inputServo );
 //scanf("%d",&inputServo );
 dutyCycle = input * 0.025f/100.0f + 0.07025; // 0.07025 has speed 0
 updatePWM_dutyCycle(FTM_CHANNEL_DC_MOTOR, dutyCycle);

 dutyCycleServo = inputServo * 0.025f/100.0f + 0.075;
 updatePWM_dutyCycle(FTM_CHANNEL_SERVO_MOTOR, dutyCycleServo);

 FTM_SetSoftwareTrigger(FTM_MOTOR, true);

 while (1)
 {
 }
 }

