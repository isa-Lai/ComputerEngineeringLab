/*
 * Copyright (c) 2013 - 2015, Freescale Semiconductor, Inc.
 * Copyright 2016-2017 NXP
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
#include "fsl_ftm.h"
#include "fsl_device_registers.h"
#include "fsl_debug_console.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "board.h"

/*******************************************************************************
 * Definitions
 ******************************************************************************/


/*******************************************************************************
 * Prototypes
 ******************************************************************************/

/*******************************************************************************
 * Code
 ******************************************************************************/
/*!
 * @brief Main function
 */

void pwm_setup(ftm_chnl_t chl){
	ftm_config_t ftmInfo;
	ftm_chnl_pwm_signal_param_t ftmParam;

	ftmParam.chnlNumber = chl;// kFTM_Chnl_1
	ftmParam.level = kFTM_HighTrue;
	ftmParam.dutyCyclePercent = 0;
	ftmParam.firstEdgeDelayPercent = 0U;
	ftmParam.enableComplementary = false;
	ftmParam.enableDeadtime = false;


	FTM_GetDefaultConfig(&ftmInfo);

	FTM_Init(FTM3, &ftmInfo);
	FTM_SetupPwm(FTM3, &ftmParam, 1U, kFTM_EdgeAlignedPwm, 5000U, CLOCK_GetFreq(kCLOCK_BusClk));
	FTM_StartTimer(FTM3, kFTM_SystemClock);

}

int main(void)
{
    char ch;
    int duty_cycle = 50;

    /* Init board hardware. */
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    BOARD_InitDebugConsole();

    pwm_setup(kFTM_Chnl_5);
    pwm_setup(kFTM_Chnl_4);
    pwm_setup(kFTM_Chnl_1);



    scanf("%x", &duty_cycle);

    FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_1, kFTM_EdgeAlignedPwm, (float)((duty_cycle&0xff0000)>>16)/255.0*100.0); //r
    FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_4, kFTM_EdgeAlignedPwm, (float)((duty_cycle&0xff))/255.0*100.0); //b
    FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_5, kFTM_EdgeAlignedPwm, (float)((duty_cycle&0xff00)>>8)/255.0*100.0); //g
    FTM_SetSoftwareTrigger(FTM3, true);


    while (1)
    {
/*        ch = GETCHAR();
        PUTCHAR(ch);*/
    }
}
