/*
 * Copyright (c) 2015, Freescale Semiconductor, Inc.
 * Copyright 2016-2017 NXP
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/* FreeRTOS kernel includes. */
#include "FreeRTOS.h"
#include "task.h"
#include "queue.h"
#include "timers.h"

/* Freescale includes. */
#include "fsl_device_registers.h"
#include "fsl_debug_console.h"
#include "pin_mux.h"
#include "clock_config.h"
#include "board.h"

/*******************************************************************************
 * Definitions
 ******************************************************************************/

/* Task priorities. */
#define hello_task_PRIORITY (configMAX_PRIORITIES - 1)
/*******************************************************************************
 * Prototypes
 ******************************************************************************/
static void hello_task(void *pvParameters);

/*******************************************************************************
 * Code
 ******************************************************************************/
/*!
 * @brief Application entry point.
 */
char* string;
int flag=0;
void hello_task(void *pvParameters)
{
	while(1)
	{
	PRINTF("Hello World\r\n");
	vTaskDelay(1000 / portTICK_PERIOD_MS);
	SCANF("%s",&string);
	flag=1;
	vTaskDelete(NULL);


	}
}

void hello_task2(void *pvParameters)
{

	while(1)
	{
		if(flag)PRINTF("Hello %s.\r\n", &string);
		vTaskDelay(1000 / portTICK_PERIOD_MS);
	}
}

char* str = "4DS";
int main(void)
{
	BaseType_t status;
//	TaskHandle_t* task2handler;

	/* Init board hardware. */
	BOARD_InitBootPins();
	BOARD_InitBootClocks();
	BOARD_InitDebugConsole();
	status = xTaskCreate(hello_task2, "Hello_task2", 200, (void*)str, 3, NULL);
	if (status != pdPASS)
	{
	PRINTF("Task creation failed!.\r\n");
	while (1);
	}
	status = xTaskCreate(hello_task, "Hello_task", 200, NULL, 2, NULL);

	if (status != pdPASS)
	{
	PRINTF("Task creation failed!.\r\n");
	while (1);
	}
	vTaskStartScheduler();
	for (;;);
}

/*!
 * @brief Task responsible for printing of "Hello world." message.
 */
//static void hello_task(void *pvParameters)
//{
//    for (;;)
//    {
//        PRINTF("Hello world.\r\n");
//        vTaskSuspend(NULL);
//    }
//}
