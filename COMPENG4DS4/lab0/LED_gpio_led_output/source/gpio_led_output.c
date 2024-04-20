/*
 * Copyright (c) 2015, Freescale Semiconductor, Inc.
 * Copyright 2016-2017 NXP
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

#include "pin_mux.h"
#include "clock_config.h"
#include "board.h"
#include "fsl_debug_console.h"
#include "fsl_gpio.h"
#include "gpio_driver.h"

/*******************************************************************************
 * Definitions
 ******************************************************************************/
#define BOARD_LED_GPIO     GPIOD
#define BOARD_LED_GPIO_PIN 13

//#define RED_LED_GPIO GPIOD
#define RED_LED_GPIO     ((GPIO_Struct*)0x400FF0C0u)
#define RED_LED_GPIO_PIN 1
//#define GB_LED_GPIO GPIOC
#define GB_LED_GPIO     ((GPIO_Struct*)0x400FF080u)
#define GREEN_LED_GPIO_PIN 9
#define BLUE_LED_GPIO_PIN 8

/*******************************************************************************
 * Prototypes
 ******************************************************************************/
/*!
 * @brief delay a while.
 */
void delay(void);

/*******************************************************************************
 * Variables
 ******************************************************************************/

/*******************************************************************************
 * Code
 ******************************************************************************/
void delay(void)
{
    volatile uint32_t i = 0;
    for (i = 0; i < 8000000; ++i)
    {
        __asm("NOP"); /* delay */
    }
}

/*!
 * @brief Main function
 */
int main(void)
{
    /* Define the init structure for the output LED pin*/

	gpio_pin_config_t led_config = {
        kGPIO_DigitalOutput,
        0,
    };

    /* Board pin, clock, debug console init */
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    BOARD_InitDebugConsole();

    /* Print a note to terminal. */
    PRINTF("\r\n GPIO Driver example\r\n");
    PRINTF("\r\n The LED is blinking.\r\n");

    /* Init output LED GPIO. */

    //question 3
    /*
	GPIO_PinInit(RED_LED_GPIO, RED_LED_GPIO_PIN, &led_config);
    GPIO_PinInit(GB_LED_GPIO, BLUE_LED_GPIO_PIN, &led_config);
    GPIO_PinInit(GB_LED_GPIO, GREEN_LED_GPIO_PIN, &led_config);
	*/

    // question 4
    gpio_init(RED_LED_GPIO, RED_LED_GPIO_PIN, &led_config);
    gpio_init(GB_LED_GPIO, BLUE_LED_GPIO_PIN, &led_config);
    gpio_init(GB_LED_GPIO, GREEN_LED_GPIO_PIN, &led_config);


    while (1)
    {

        //question 3
        /*
		GPIO_PortToggle(GB_LED_GPIO, 1u <<  BLUE_LED_GPIO_PIN);
        delay();

        GPIO_PortToggle(GB_LED_GPIO,1u <<  BLUE_LED_GPIO_PIN);
        delay();
        GPIO_PortToggle(GB_LED_GPIO,1u << GREEN_LED_GPIO_PIN);
        delay();

        GPIO_PortToggle(GB_LED_GPIO,1u << GREEN_LED_GPIO_PIN);
        delay();
        GPIO_PortToggle(RED_LED_GPIO,1u <<   RED_LED_GPIO_PIN);
        delay();
        GPIO_PortToggle(RED_LED_GPIO,1u <<  RED_LED_GPIO_PIN);
        delay();
         */

        //question 4
    	gpio_toggle(GB_LED_GPIO,  BLUE_LED_GPIO_PIN);
        delay();

        gpio_toggle(GB_LED_GPIO, BLUE_LED_GPIO_PIN);
        delay();
        gpio_toggle(GB_LED_GPIO,GREEN_LED_GPIO_PIN);
        delay();

        gpio_toggle(GB_LED_GPIO,GREEN_LED_GPIO_PIN);
        delay();
        gpio_toggle(RED_LED_GPIO,  RED_LED_GPIO_PIN);
        delay();
        gpio_toggle(RED_LED_GPIO, RED_LED_GPIO_PIN);
        delay();
    }
}
