#include "RC_Receiver_Component.h"

SemaphoreHandle_t rc_hold_semaphore;
TaskHandle_t rc_task_handle;
BaseType_t status;

void setupRCReceiverComponent()
{
	setupRCPins();

	setupUART_RC();

    /*************** RC Task ***************/
	//Create RC Semaphore
	SemaphoreHandle_t* sem_ptr = &rc_hold_semaphore;
	sem_ptr = (SemaphoreHandle_t*) malloc(1 * sizeof(
		SemaphoreHandle_t));
	rc_hold_semaphore = xSemaphoreCreateBinary();
	xSemaphoreGive(rc_hold_semaphore); // init semphr to 1

	//Create RC Task
	status = xTaskCreate(rcTask, "rcTask", 200, NULL, 3, NULL);
	if (status != pdPASS)
	{
	PRINTF("Task creation failed!.\r\n");
	while (1);
	}

}

void setupRCPins()
{
	//Configure RC pins
    /* Port A Clock Gate Control: Clock enabled */
    CLOCK_EnableClock(kCLOCK_PortC);

    /* PORTA2 (pin K6) is configured as TRACE_SWO */
    PORT_SetPinMux(PORTC,3U , kPORT_MuxAlt3);
}

void setupUART_RC()
{
	//setup UART for RC receiver
	uart_config_t config;
	UART_GetDefaultConfig(&config);
	config.baudRate_Bps = 115200;
	config.enableTx = false;
	config.enableRx = true;
	UART_Init(UART1, &config, CLOCK_GetFreq(kCLOCK_CoreSysClk));
}

void rcTask(void* pvParameters)
{
	//RC task implementation
	uart_config_t config;
	RC_Values rc_values;
	uint8_t* ptr = (uint8_t*) &rc_values;
	motor_msg motor_value;
	motor_value.type= 0; // type speed
	int angle = 0;
	uint32_t mode = 0;
	uint32_t prevMode = 0;
	int prev_dire = 0;
	int direction = 0;

	while (1)
	{
		UART_ReadBlocking(UART1, ptr, 1);
		if(*ptr == 0x20)
		{
			UART_ReadBlocking(UART1, &ptr[1], sizeof(rc_values) - 1);
			xSemaphoreTake(rc_hold_semaphore, portMAX_DELAY);
			/*--------------implement task here------------------------*/
			if(rc_values.header == 0x4020)
				{
				/*
				printf("Channel 1 = %d\t", rc_values.ch1);
				printf("Channel 2 = %d\t", rc_values.ch2);
				printf("Channel 3 = %d\t", rc_values.ch3);
				printf("Channel 4 = %d\t", rc_values.ch4);
				printf("Channel 5 = %d\t", rc_values.ch5);
				printf("Channel 6 = %d\t", rc_values.ch6);
				printf("Channel 7 = %d\t", rc_values.ch7);
				printf("Channel 8 = %d\t", rc_values.ch8);
				printf("Channel 8 = %d\t", rc_values.ch9);
				printf("Channel 8 = %d\r\n", rc_values.ch10);
				*/

				prevMode = mode;
				mode = (uint32_t)rc_values.ch9;
				if (mode != prevMode)
					{
					status = xQueueSendToBack(led_queue,(void*)&mode,portMAX_DELAY);
					if (status != pdPASS)
						{
						PRINTF("Queue Send failed!.\r\n");
						while (1);
						}
					}

				if(mode == 2000){
					motor_value.val = rc_values.ch3;
				}else if(mode == 1500){
					motor_value.val = (int)(rc_values.ch3-1000)*2/3+1000;
				}else if(mode == 1000){
					motor_value.val = (int)(rc_values.ch3-1000)*1/3+1000;
				}

				status = xQueueSendToBack(motor_queue,(void*)&motor_value,portMAX_DELAY);
				if (status != pdPASS)
					{
					PRINTF("Queue Send failed!.\r\n");
					while (1);
					}
				angle = rc_values.ch1;
				status = xQueueSendToBack(angle_queue,(void*)&angle,portMAX_DELAY);
				if (status != pdPASS)
					{
					PRINTF("Queue Send failed!.\r\n");
					while (1);
					}

				prev_dire = direction;
				direction = rc_values.ch8;//either 1000 or 2000
				if(direction != prev_dire){
					status = xQueueSendToBack(direction_queue,(void*)&direction,portMAX_DELAY);
					if (status != pdPASS)
						{
						PRINTF("Queue Send failed!.\r\n");
						while (1);
						}
					}
				}

			/*--------------implement task end------------------------*/
			xSemaphoreGive(rc_hold_semaphore); // after complete give
			vTaskDelay(5);
		}
	}

}


