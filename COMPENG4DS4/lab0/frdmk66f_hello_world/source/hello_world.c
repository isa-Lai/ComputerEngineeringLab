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
/*
void testFunction()
{
	int x = 0;
	int *ptr = &x;
	int *ptr_location = 0x20001000;

	*ptr = 10;
	*ptr_location = 11;
}
*/

#define MEM_LOC(x) *((int*)x)
#define LOC1 *((uint8_t*)0x20001000)
#define LOC2 MEM_LOC(0x20001001)
#define LOC3 *((uint16_t*)0x20001005)
#define LOC4 MEM_LOC(0x20001007)

void testFunction()
{
	int *ptr_location = 0x20001000;
	*ptr_location = 11;

	LOC1 = 0xac;
	LOC2 = 0xaabbccdd;
	LOC3 = 0xabcd;
	LOC4 = 0xaabbccdd;
}

/*
struct ARBITRARY_MODULE
{
	int location_1;
	char location_2;
	int location_3;
};
*/
typedef struct __attribute__((__packed__))
{
	int location_1;
	char location_2;
	int location_3;
}ARBITRARY_MODULE;

#define MODULE ((ARBITRARY_MODULE*)0x20001000)

void testFunction2()
{
	/*
	struct ARBITRARY_MODULE* module = (struct ARBITRARY_MODULE*) 0x20001000;
	module->location_1 = 0xaaaaaaaa;
	module->location_2 = 0xbb;
	module->location_3 = 0xcccccccc;
	*/
	MODULE->location_1 = 0xaaaaaaaa;
	MODULE->location_2 = 0xbb;
	MODULE->location_3 = 0xcccccccc;
}

int main(void)
{
    char ch;

    /* Init board hardware. */
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    BOARD_InitDebugConsole();

    PRINTF("hello world.\r\n");

    testFunction();
    testFunction2();

    while (1)
    {
        ch = GETCHAR();
        PUTCHAR(ch);
    }
}
