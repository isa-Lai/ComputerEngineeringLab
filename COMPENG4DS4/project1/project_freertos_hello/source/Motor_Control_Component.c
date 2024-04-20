#include "Motor_Control_Component.h"

volatile int forward = 1;

QueueHandle_t motor_queue;
QueueHandle_t angle_queue;
QueueHandle_t direction_queue;


void setupMotorComponent()
{
	BaseType_t status;
	setupMotorPins();

	setupDCMotor();
	setupServo();

    /*************** Motor Task ***************/
	//Create Motor Queue
	motor_queue = xQueueCreate(5, sizeof(motor_msg));
	if (motor_queue == NULL)
	{
	PRINTF("Queue creation failed!.\r\n");
	}

	direction_queue = xQueueCreate(5, sizeof(int));
	if (direction_queue == NULL)
	{
		PRINTF("Queue creation failed!.\r\n");
	}


	status = xTaskCreate(directionTask, "Direction Task", 200, (void*)direction_queue, 2, NULL);
	if (status != pdPASS){
			PRINTF("Task creation failed!.\r\n");
			while (1);
	}


	//Create Motor Task
	status = xTaskCreate(motorTask, "motorTask", 200, (void*)motor_queue, 2, NULL);
	if (status != pdPASS){
		PRINTF("Task creation failed!.\r\n");
		while (1);
	}

    /*************** Position Task ***************/
	//Create Angle Queue
	angle_queue = xQueueCreate(5, sizeof(int));
	if (angle_queue == NULL)
	{
	PRINTF("Queue creation failed!.\r\n");
	}
	//Create Position Task
	status = xTaskCreate(positionTask, "positionTask", 200, (void*)angle_queue, 2, NULL);
	if (status != pdPASS){
		PRINTF("Task creation failed!.\r\n");
		while (1);
	}

}

void setupMotorPins()
{
    //Configure PWM pins for DC and Servo motors
    CLOCK_EnableClock(kCLOCK_PortC);
    CLOCK_EnableClock(kCLOCK_PortA);

    PORT_SetPinMux(PORTC, 1U, kPORT_MuxAlt4);
    PORT_SetPinMux(PORTA, 6U, kPORT_MuxAlt3);
}

void setupDCMotor()
{
	//Initialize PWM for DC motor
	ftm_config_t ftmInfo;
	ftm_chnl_pwm_signal_param_t ftmParam;
	ftm_pwm_level_select_t pwmLevel = kFTM_HighTrue;
	ftmParam.chnlNumber = FTM_CHANNEL_DC_MOTOR;
	ftmParam.level = pwmLevel;
	ftmParam.dutyCyclePercent = 7;
	ftmParam.firstEdgeDelayPercent = 0U;
	ftmParam.enableComplementary = false;
	ftmParam.enableDeadtime = false;
	FTM_GetDefaultConfig(&ftmInfo);
	ftmInfo.prescale = kFTM_Prescale_Divide_128;
	FTM_Init(FTM_MOTORS, &ftmInfo);
	FTM_SetupPwm(FTM_MOTORS, &ftmParam, 1U, kFTM_EdgeAlignedPwm, 50U, CLOCK_GetFreq(
	kCLOCK_BusClk));
	FTM_StartTimer(FTM_MOTORS, kFTM_SystemClock);
}

void setupServo()
{
	//Initialize PWM for Servo motor
	ftm_config_t ftmInfo;
	ftm_chnl_pwm_signal_param_t ftmParam;
	ftm_pwm_level_select_t pwmLevel = kFTM_HighTrue;
	ftmParam.chnlNumber = FTM_CHANNEL_SERVO;
	ftmParam.level = pwmLevel;
	ftmParam.dutyCyclePercent = 7;
	ftmParam.firstEdgeDelayPercent = 0U;
	ftmParam.enableComplementary = false;
	ftmParam.enableDeadtime = false;
	FTM_GetDefaultConfig(&ftmInfo);
	ftmInfo.prescale = kFTM_Prescale_Divide_128;
	FTM_Init(FTM_MOTORS, &ftmInfo);
	FTM_SetupPwm(FTM_MOTORS, &ftmParam, 1U, kFTM_EdgeAlignedPwm, 50U, CLOCK_GetFreq(
	kCLOCK_BusClk));
	FTM_StartTimer(FTM_MOTORS, kFTM_SystemClock);
}

void updatePWM_dutyCycle(ftm_chnl_t channel, float dutyCycle)
{
	uint32_t cnv, cnvFirstEdge = 0, mod;

	/* The CHANNEL_COUNT macro returns -1 if it cannot match the FTM instance */
	assert(-1 != FSL_FEATURE_FTM_CHANNEL_COUNTn(FTM_MOTORS));

	mod = FTM_MOTORS->MOD;
	if(dutyCycle == 0U)
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

	FTM_MOTORS->CONTROLS[channel].CnV = cnv;
}



void directionTask(void *pvParameters) {

    int receivedDirection = 0;

    while(1) {
        if(xQueueReceive(direction_queue, &receivedDirection, portMAX_DELAY) == pdPASS) {

            if(receivedDirection == 1000) {
                forward = 1;
            }

            else if(receivedDirection == 2000) {
                forward = 0;
            }
        }
    }
}



void motorTask(void* pvParameters)
{
	//Motor task implementation
	QueueHandle_t queue = (QueueHandle_t)pvParameters;
	BaseType_t status;
	motor_msg motor_struct;
	float duty_motor;
	int angle = 0;
	double max_extra_speed = 200;

	while(1)
	{
		status = xQueueReceive(queue, (void *) &motor_struct, portMAX_DELAY);
		if (status != pdPASS)
		{
			PRINTF("Queue Receive failed!.\r\n");
			while (1);
		}
		//PRINTF("type %d val %d\r\n",motor_struct.type, motor_struct.val);
		//vTaskDelay(1);

		duty_motor = 0.0f;

		if (motor_struct.type == 0){

			int motor_val = motor_struct.val;

			double extra_speed = max_extra_speed * sin((double)angle/180*3.1415926);

			if (forward == 1){
				if (motor_val >= 1000 && motor_val <= 2000){
					motor_val = motor_val + (int) extra_speed;
					if (motor_val > 2000) motor_val = 2000;
					if (motor_val < 1000) motor_val = 1000;

					duty_motor = ((motor_val - 1000) * (0.1f - 0.075f) / (2000 - 1000)) + 0.075f;
				}

			}
			else{
				if (motor_val >= 1000 && motor_val <= 2000){
					motor_val = motor_val - (int) extra_speed;
					if (motor_val > 2000) motor_val = 2000;
					if (motor_val < 1000) motor_val = 1000;

					//duty_motor = ((motor_val - 1000) * (0.075f - 0.05f) / (2000 - 1000)) + 0.05f;
					duty_motor = ((motor_val - 1000) * (0.05f - 0.075f) / (2000 - 1000)) + 0.075f;
				}
			}

			updatePWM_dutyCycle(FTM_CHANNEL_DC_MOTOR, duty_motor);
			FTM_SetSoftwareTrigger(FTM_MOTORS, true);
		}
		if (motor_struct.type == 1){

			angle = motor_struct.val;
			//PRINTF("type %d val %d\r\n",motor_struct.type, motor_struct.val);
		}
		vTaskDelay(1);
		PRINTF("DC Motor Duty Cycle: %f\n", duty_motor);
	}
}

void positionTask(void* pvParameters)
{
	//Position task implementation
	QueueHandle_t queue = (QueueHandle_t)pvParameters;
	BaseType_t status;
	int angle;
	float duty_servo;

	while(1)
	{
		status = xQueueReceive(queue, (void *) &angle, portMAX_DELAY);
		if (status != pdPASS)
		{
			PRINTF("Queue Receive failed!.\r\n");
			while (1);
		}
		//PRINTF("angle %d\r\n",angle);
		//vTaskDelay(1);

		//map 1000 to 5%, 2000 to 10%
		//duty_servo = ((angle - 1000) * (0.10f - 0.05f) / (2000 - 1000)) + 0.05f;

		//map 1000 to 10%, 2000 to 5%
		duty_servo = ((angle - 1000) * (0.05f - 0.10f) / (2000 - 1000)) + 0.10f;

		// Update the PWM duty cycle for the servo motor
		updatePWM_dutyCycle(FTM_CHANNEL_SERVO, duty_servo);
		FTM_SetSoftwareTrigger(FTM_MOTORS, true);

		PRINTF("Servo Motor Duty Cycle: %f\n", duty_servo);
		vTaskDelay(1);
	}
}
