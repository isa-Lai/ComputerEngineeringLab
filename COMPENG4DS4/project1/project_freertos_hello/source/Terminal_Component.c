#include "Terminal_Component.h"

EventGroupHandle_t event_group;
QueueHandle_t uart_queue;
TimerHandle_t timer_handle;
static int semphr_not_took = 1;

void timerCallbackFunction(TimerHandle_t timer_handle)
{
	xSemaphoreGive(rc_hold_semaphore); // give indicate not hold
	semphr_not_took = 1;
	PRINTF("Time out, use RC control\r\n");
}

void setupTerminalComponent()
{
	BaseType_t status;

	setupTerminalPins();

	setupUART();

    /*************** UART Task ***************/
	uart_queue = xQueueCreate(10, sizeof(char*));
	if (uart_queue == NULL)
	{
		PRINTF("Queue creation failed!.\r\n");
		while (1);
	}
    status = xTaskCreate(uartTask, "UART task", 200, NULL, 2, NULL);
    if (status != pdPASS)
    {
        PRINTF("Task creation failed!.\r\n");
        while (1);
    }

	timer_handle = xTimerCreate("one shot timer",
	200 / portTICK_PERIOD_MS,
	pdFALSE,
	NULL,
	timerCallbackFunction);

	sendMessage("123");
	PRINTF("123");

    /*************** Terminal Control Task ***************/
    //Create Event groups
    event_group = xEventGroupCreate();
	//Create Terminal Control Task
	status = xTaskCreate(terminalControlTask, "terminalControlTask", 200, (void*)event_group, 2, NULL);
	if (status != pdPASS)
	{
		PRINTF("Task creation failed!.\r\n");
		while (1);
	}
}

void setupTerminalPins()
{
    //Configure UART pins
    /* Port A Clock Gate Control: Clock enabled */
    /* Port B Clock Gate Control: Clock enabled */
    CLOCK_EnableClock(kCLOCK_PortE);
    CLOCK_EnableClock(kCLOCK_PortC);
    PORT_SetPinMux(PORTC, 13U, kPORT_MuxAlt3);
    PORT_SetPinMux(PORTC, 14U, kPORT_MuxAlt3);
    PORT_SetPinMux(PORTC, 15U, kPORT_MuxAlt3);
    PORT_SetPinMux(PORTE, 27U, kPORT_MuxAlt3);

}

void sendMessage(const char *format, ...)
{
	va_list args;

	char* str = (char*)pvPortMalloc(250 * sizeof(char));

	va_start(args, format);
	vsprintf(str, format, args);

	if(xQueueSendToBack(uart_queue, (void *) &str, portMAX_DELAY) != pdPASS )
	{
		PRINTF("Send message to uart_queue failed!.\r\n");
		while (1);
	}

	va_end(args);
}

void setupUART()
{
	//Setup UART for sending and receiving
	uart_config_t config;
	UART_GetDefaultConfig(&config);
	config.baudRate_Bps = 57600;
	config.enableTx = true;
	config.enableRx = true;
	config.enableRxRTS = true;
	config.enableTxCTS = true;
	UART_Init(TARGET_UART, &config, CLOCK_GetFreq(kCLOCK_BusClk));
	/******** Enable Interrupts *********/
	UART_EnableInterrupts(TARGET_UART, kUART_RxDataRegFullInterruptEnable);
	NVIC_SetPriority(UART4_RX_TX_IRQn, 2);
	EnableIRQ(UART4_RX_TX_IRQn);
}

void uartTask(void* pvParameters)
{
	char* welcome_message = "UART task started\n\r";
	char* received_str;
	BaseType_t status;

	UART_WriteBlocking(TARGET_UART, welcome_message, strlen(welcome_message));

	while(1)
	{
		status = xQueueReceive(uart_queue, (void *) &received_str, portMAX_DELAY);
		if (status != pdPASS)
		{
			PRINTF("Queue Send failed!.\r\n");
			while (1);
		}
		UART_WriteBlocking(TARGET_UART, received_str, strlen(received_str));
		vPortFree(received_str);
	}
}

void UART4_RX_TX_IRQHandler()
{
	//UART ISR
	BaseType_t xHigherPriorityTaskWoken = pdFALSE;
	UART_GetStatusFlags(TARGET_UART);
	char ch = UART_ReadByte(TARGET_UART);


	switch(ch)
	{
		case 'a':
		xEventGroupSetBitsFromISR(event_group, LEFT_BIT, &
		xHigherPriorityTaskWoken);
		break;
		case 's':
		xEventGroupSetBitsFromISR(event_group, DOWN_BIT, &
		xHigherPriorityTaskWoken);
		break;
		case 'd':
		xEventGroupSetBitsFromISR(event_group, RIGHT_BIT, &
		xHigherPriorityTaskWoken);
		break;
		case 'w':
		xEventGroupSetBitsFromISR(event_group, UP_BIT, &
		xHigherPriorityTaskWoken);
		break;
	}

	portYIELD_FROM_ISR(xHigherPriorityTaskWoken);
}

void terminalControlTask(void* pvParameters)
{
	//Terminal Control Task implementation
	EventGroupHandle_t event_group = (EventGroupHandle_t)pvParameters;
	EventBits_t bits;
	BaseType_t status;
	
	while(1)
	{
		bits = xEventGroupWaitBits(event_group,
		LEFT_BIT | RIGHT_BIT | UP_BIT | DOWN_BIT,
		pdTRUE,
		pdFALSE,
		portMAX_DELAY);

		// hold rc
		if(semphr_not_took)
		{
			status = xSemaphoreTake(rc_hold_semaphore,portMAX_DELAY);
			if (status != pdPASS)
			{
				PRINTF("Couldn't take hold semaphore!.\r\n");
				while (1);
			}
			semphr_not_took = 0;
		}

		motor_msg motor_value;
		int angle_val = 1500;
		int dir_val = 2000;
		motor_value.type= 0; // type angle
		motor_value.val = 1500;

		if((bits & LEFT_BIT) == LEFT_BIT)
		{
			PRINTF("Left\r\n");
			angle_val = 1000;
			status = xQueueSendToBack(angle_queue, (void *)&angle_val, portMAX_DELAY);

		}
		if((bits & RIGHT_BIT) == RIGHT_BIT)
		{
			PRINTF("Right\r\n");
			angle_val = 2000;
			status = xQueueSendToBack(angle_queue, (void *)&angle_val, portMAX_DELAY);
		}
		if((bits & UP_BIT) == UP_BIT)
		{
			PRINTF("Up\r\n");
			dir_val = 2000;
			status = xQueueSendToBack(motor_queue, (void *)&motor_value, portMAX_DELAY);
			status = xQueueSendToBack(direction_queue, (void *)&dir_val, portMAX_DELAY);
		}
		if((bits & DOWN_BIT) == DOWN_BIT)
		{
			PRINTF("Down\r\n");
			dir_val = 1000;
			status = xQueueSendToBack(motor_queue, (void *)&motor_value, portMAX_DELAY);
			status = xQueueSendToBack(direction_queue, (void *)&dir_val, portMAX_DELAY);
		}

		//status = xQueueSendToBack(motor_queue, (void *)&motor_value, portMAX_DELAY);
		//status = xQueueSendToBack(angle_queue, (void *)&angle_val, portMAX_DELAY);

		// start timer
		status = xTimerReset(timer_handle,portMAX_DELAY);
		if (status != pdPASS)
		{
			PRINTF("timer failed to start!.\r\n");
			while (1);
		}

	}
}
