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
#include "semphr.h"

/*******************************************************************************
 * Definitions
 ******************************************************************************/
typedef struct {
	SemaphoreHandle_t* semphr;
	uint8_t val;
} sem_wrapper;

#define LEFT_BIT (1 << 0)
#define RIGHT_BIT (1 << 1)
#define UP_BIT (1 << 2)
#define DOWN_BIT (1 << 3)
void producer_event(void* pvParameters)
{
	sem_wrapper* value = (sem_wrapper*)pvParameters;
	SemaphoreHandle_t producer_semaphore = value->semphr[0];
	SemaphoreHandle_t consumer_semaphore = value->semphr[1];
	BaseType_t status;
	char c;
	while(1)
	{
		status = xSemaphoreTake(consumer_semaphore, portMAX_DELAY);
		if (status != pdPASS)
		{
		PRINTF("Failed to acquire consumer_semaphore\r\n");
		while (1);
		}

		scanf(" %c", &c); //in case '\n' will pass to the next scanf, add a space before %c

		switch(c)
		{
		case 'a':
			value->val = LEFT_BIT;
		break;
		case 's':
			value->val = DOWN_BIT;
		break;
		case 'd':
			value->val = RIGHT_BIT;
		break;
		case 'w':
			value->val = UP_BIT;
		break;
		}

		xSemaphoreGive(producer_semaphore);
	}
}

void consumer_event(void* pvParameters)
{
	sem_wrapper* value = (sem_wrapper*)pvParameters;
	SemaphoreHandle_t producer_semaphore = value->semphr[0];
	SemaphoreHandle_t consumer_semaphore = value->semphr[1];
	uint8_t bits;
	BaseType_t status;
	while(1)
	{
		xSemaphoreGive(consumer_semaphore);
		status = xSemaphoreTake(producer_semaphore, portMAX_DELAY);
		if (status != pdPASS)
		{
		PRINTF("Failed to acquire consumer_semaphore\r\n");
		while (1);
		}
		bits = value->val;
		if((bits & LEFT_BIT) == LEFT_BIT)
		{
		PRINTF("Left\r\n");
		}
		if((bits & RIGHT_BIT) == RIGHT_BIT)
		{
		PRINTF("Right\r\n");
		}
		if((bits & UP_BIT) == UP_BIT)
		{
		PRINTF("Up\r\n");
		}
		if((bits & DOWN_BIT) == DOWN_BIT)
		{
		PRINTF("Down\r\n");
		}

	}
}

int main(void)
{
	BaseType_t status;
	/* Init board hardware. */
	BOARD_InitBootPins();
	BOARD_InitBootClocks();
	BOARD_InitDebugConsole();

	sem_wrapper* value = malloc(sizeof(sem_wrapper));

	value->semphr = malloc(2*sizeof(SemaphoreHandle_t));
	value->semphr[0] = xSemaphoreCreateBinary();
	value->semphr[1] = xSemaphoreCreateBinary();

	status = xTaskCreate(producer_event, "producer", 200, (void*)value, 2, NULL);
	if (status != pdPASS)
	{
	PRINTF("Task creation failed!.\r\n");
	while (1);
	}
	status = xTaskCreate(consumer_event, "consumer", 200, (void*)value, 2, NULL);
	if (status != pdPASS)
	{
	PRINTF("Task creation failed!.\r\n");
	while (1);
	}
	vTaskStartScheduler();
	while (1)
	{}
}
