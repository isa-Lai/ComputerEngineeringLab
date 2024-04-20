#include "LED_Component.h"

QueueHandle_t led_queue;

//#define RED_LED_GPIO GPIOD
#define RED_LED_GPIO   GPIOD
#define RED_LED_GPIO_PIN 1
//#define GB_LED_GPIO GPIOC
#define GB_LED_GPIO   GPIOC
#define GREEN_LED_GPIO_PIN 9
#define BLUE_LED_GPIO_PIN 8


void setupLEDComponent()
{
	setupLEDPins();

	setupLEDs();

    /*************** LED Task ***************/
	//Create LED Queue
	led_queue = xQueueCreate(5, sizeof(uint32_t));
	 if (led_queue == NULL) {
	        PRINTF("Queue creation failed!\r\n");
	        while (1);
	    }
	//Create LED Task
	 BaseType_t status;
	 status = xTaskCreate(ledTask, "led", 200, (void*)led_queue, 3, NULL);
}

void setupLEDPins()
{
	//Configure LED pins
    /* Port B Clock Gate Control: Clock enabled */
    //CLOCK_EnableClock(kCLOCK_PortB);
    /* Port C Clock Gate Control: Clock enabled */
    CLOCK_EnableClock(kCLOCK_PortD);
    CLOCK_EnableClock(kCLOCK_PortC);

    PORT_SetPinMux(PORTD, 1U, kPORT_MuxAlt4);

    PORT_SetPinMux(PORTC, 8U, kPORT_MuxAlt3);
    PORT_SetPinMux(PORTC, 9U, kPORT_MuxAlt3);

    /* PORTC9 (pin D7) is configured as PTC9 */
    //PORT_SetPinMux(PORTC, 9U, kPORT_MuxAsGpio);
}

void setupLEDs(){
	ftm_config_t ftmInfo;
	//ftm_chnl_pwm_signal_param_t ftmParam;
	ftm_chnl_pwm_signal_param_t ftmParam_RD;
	ftm_chnl_pwm_signal_param_t ftmParam_GR;
	ftm_chnl_pwm_signal_param_t ftmParam_BL;

	ftmParam_RD.chnlNumber = kFTM_Chnl_1;
	ftmParam_RD.level = kFTM_HighTrue;
	ftmParam_RD.dutyCyclePercent = 0;
	ftmParam_RD.firstEdgeDelayPercent = 0U;
	ftmParam_RD.enableComplementary = false;
	ftmParam_RD.enableDeadtime = false;

	ftmParam_GR.chnlNumber = kFTM_Chnl_5;
	ftmParam_GR.level = kFTM_HighTrue;
	ftmParam_GR.dutyCyclePercent = 0;
	ftmParam_GR.firstEdgeDelayPercent = 0U;
	ftmParam_GR.enableComplementary = false;
	ftmParam_GR.enableDeadtime = false;

	ftmParam_BL.chnlNumber = kFTM_Chnl_4;
	ftmParam_BL.level = kFTM_HighTrue;
	ftmParam_BL.dutyCyclePercent = 0;
	ftmParam_BL.firstEdgeDelayPercent = 0U;
	ftmParam_BL.enableComplementary = false;
	ftmParam_BL.enableDeadtime = false;

	FTM_GetDefaultConfig(&ftmInfo);

	FTM_Init(FTM3, &ftmInfo);
	FTM_SetupPwm(FTM3, &ftmParam_RD, 1U, kFTM_EdgeAlignedPwm, 5000U, CLOCK_GetFreq(
			kCLOCK_BusClk));
	FTM_SetupPwm(FTM3, &ftmParam_GR, 5U, kFTM_EdgeAlignedPwm, 5000U, CLOCK_GetFreq(
			kCLOCK_BusClk));
	FTM_SetupPwm(FTM3, &ftmParam_BL, 4U, kFTM_EdgeAlignedPwm, 5000U, CLOCK_GetFreq(
			kCLOCK_BusClk));
	FTM_StartTimer(FTM3, kFTM_SystemClock);
}

void setLEDcolor(uint32_t speedmode)
{
	switch(speedmode){
		case 2000: //fast mode LED red
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_1, kFTM_EdgeAlignedPwm, 100); //r
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_5, kFTM_EdgeAlignedPwm, 0); //g
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_4, kFTM_EdgeAlignedPwm, 0);//b
			break;
		case 1500: //moderate mode LED purple (100% RED + 100% blue)
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_1, kFTM_EdgeAlignedPwm, 100);//r
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_5, kFTM_EdgeAlignedPwm, 0);//g
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_4, kFTM_EdgeAlignedPwm, 100);//b
			break;
		case 1000: //slow mode LED yellow (RED + GREEN)
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_1, kFTM_EdgeAlignedPwm, 100);//r
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_5, kFTM_EdgeAlignedPwm, 100);//g
			FTM_UpdatePwmDutycycle(FTM3, kFTM_Chnl_4, kFTM_EdgeAlignedPwm, 0);//b
			break;
		default:
			return;
	}
	FTM_SetSoftwareTrigger(FTM3, true);
}

void ledTask(void* pvParameters)
{
	//LED task implementation
	QueueHandle_t queue = (QueueHandle_t)pvParameters;
	BaseType_t status;
	uint32_t mode;

	while(1)
	{
		status = xQueueReceive(queue, (void *) &mode, portMAX_DELAY);
		if (status != pdPASS)
		{
			PRINTF("Queue Receive failed!.\r\n");
			while (1);
		}
		PRINTF("mode %d\r\n",mode);
		setLEDcolor(mode);
		vTaskDelay(1);
	}
}




