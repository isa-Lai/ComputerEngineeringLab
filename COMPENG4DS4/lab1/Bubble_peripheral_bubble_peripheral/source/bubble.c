/*
 * Copyright 2018-2019 NXP
 * All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

#include "fsl_debug_console.h"
#include "math.h"
#include "fsl_fxos.h"
#include "pin_mux.h"
#include "peripherals.h"
#include "board.h"
#include "fsl_dspi.h"

/*******************************************************************************
 * Definitions
 ******************************************************************************/
#define I2C_RELEASE_BUS_COUNT 100U

/*******************************************************************************
 * Prototypes
 ******************************************************************************/
void BOARD_I2C_ReleaseBus(void);
static void Board_UpdatePwm(uint16_t x, uint16_t y);
/*******************************************************************************
 * Variables
 ******************************************************************************/
volatile int16_t xAngle = 0;
volatile int16_t yAngle = 0;
/* FXOS device address */
const uint8_t g_accel_address[] = {0x1CU, 0x1DU, 0x1EU, 0x1FU};

/*******************************************************************************
 * Code
 ******************************************************************************/

//static void i2c_release_bus_delay(void)
//{
//    uint32_t i = 0;
//    for (i = 0; i < I2C_RELEASE_BUS_COUNT; i++)
//    {
//        __NOP();
//    }
//}
//
//void BOARD_I2C_ReleaseBus(void)
//{
//    uint8_t i = 0;
//
//    BOARD_Init_I2C_GPIO_pins();
//
//    /* Drive SDA low first to simulate a start */
//    GPIO_PinWrite(BOARD_ACCEL_I2C_SDA_GPIO, BOARD_ACCEL_I2C_SDA_PIN, 0U);
//    i2c_release_bus_delay();
//
//    /* Send 9 pulses on SCL and keep SDA high */
//    for (i = 0; i < 9; i++)
//    {
//        GPIO_PinWrite(BOARD_ACCEL_I2C_SCL_GPIO, BOARD_ACCEL_I2C_SCL_PIN, 0U);
//        i2c_release_bus_delay();
//
//        GPIO_PinWrite(BOARD_ACCEL_I2C_SDA_GPIO, BOARD_ACCEL_I2C_SDA_PIN, 1U);
//        i2c_release_bus_delay();
//
//        GPIO_PinWrite(BOARD_ACCEL_I2C_SCL_GPIO, BOARD_ACCEL_I2C_SCL_PIN, 1U);
//        i2c_release_bus_delay();
//        i2c_release_bus_delay();
//    }
//
//    /* Send stop */
//    GPIO_PinWrite(BOARD_ACCEL_I2C_SCL_GPIO, BOARD_ACCEL_I2C_SCL_PIN, 0U);
//    i2c_release_bus_delay();
//
//    GPIO_PinWrite(BOARD_ACCEL_I2C_SDA_GPIO, BOARD_ACCEL_I2C_SDA_PIN, 0U);
//    i2c_release_bus_delay();
//
//    GPIO_PinWrite(BOARD_ACCEL_I2C_SCL_GPIO, BOARD_ACCEL_I2C_SCL_PIN, 1U);
//    i2c_release_bus_delay();
//
//    GPIO_PinWrite(BOARD_ACCEL_I2C_SDA_GPIO, BOARD_ACCEL_I2C_SDA_PIN, 1U);
//    i2c_release_bus_delay();
//}
///* Update the duty cycle of an active pwm signal */
//static void Board_UpdatePwm(uint16_t x, uint16_t y)
//{
//    /* Start PWM mode with updated duty cycle */
//    FTM_UpdatePwmDutycycle(BOARD_TIMER_PERIPHERAL, kFTM_Chnl_5, kFTM_EdgeAlignedPwm, x);
//    FTM_UpdatePwmDutycycle(BOARD_TIMER_PERIPHERAL, kFTM_Chnl_1, kFTM_EdgeAlignedPwm, y);
//    /* Software trigger to update registers */
//    FTM_SetSoftwareTrigger(BOARD_TIMER_PERIPHERAL, true);
//}


void setupSPI()
{
dspi_master_config_t masterConfig;
/* Master config */
masterConfig.whichCtar = kDSPI_Ctar0;
masterConfig.ctarConfig.baudRate = 500000;
masterConfig.ctarConfig.bitsPerFrame = 8U;
masterConfig.ctarConfig.cpol =
kDSPI_ClockPolarityActiveHigh;
masterConfig.ctarConfig.cpha = kDSPI_ClockPhaseFirstEdge;
masterConfig.ctarConfig.direction = kDSPI_MsbFirst;
masterConfig.ctarConfig.pcsToSckDelayInNanoSec = 1000000000U / 500000;
masterConfig.ctarConfig.lastSckToPcsDelayInNanoSec = 1000000000U / 500000;
masterConfig.ctarConfig.betweenTransferDelayInNanoSec = 1000000000U / 500000;
masterConfig.whichPcs = kDSPI_Pcs0;
masterConfig.pcsActiveHighOrLow = kDSPI_PcsActiveLow;
masterConfig.enableContinuousSCK = false;
masterConfig.enableRxFifoOverWrite = false;
masterConfig.enableModifiedTimingFormat = false;
masterConfig.samplePoint = kDSPI_SckToSin0Clock;
DSPI_MasterInit(SPI1, &masterConfig, BUS_CLK);
}

void voltageRegulatorEnable()
{
gpio_pin_config_t pin_config = {
.pinDirection = kGPIO_DigitalOutput,
.outputLogic = 0U};
GPIO_PinInit(GPIOB, 8, &pin_config);
GPIO_PinWrite(GPIOB, 8, 1U);
}
void accelerometerEnable()
{
gpio_pin_config_t pin_config = {
.pinDirection = kGPIO_DigitalOutput,
.outputLogic = 0U};
GPIO_PinInit(GPIOA, 25, &pin_config);
GPIO_PinWrite(GPIOA, 25, 0U);
}

status_t SPI_read(uint8_t regAddress, uint8_t *rxBuff, uint8_t rxBuffSize)
{
dspi_transfer_t masterXfer;
uint8_t *masterTxData = (uint8_t*)malloc(rxBuffSize + 2);
int8_t *masterRxData = (uint8_t*)malloc(rxBuffSize + 2);
masterTxData[0] = regAddress & 0x7F; //Clear the most significant bit
masterTxData[1] = regAddress & 0x80; //Clear the least significant 7 bits
masterXfer.txData = masterTxData;
masterXfer.rxData = masterRxData;
masterXfer.dataSize = rxBuffSize + 2;
masterXfer.configFlags = kDSPI_MasterCtar0 | kDSPI_MasterPcs0 |
kDSPI_MasterPcsContinuous;
status_t ret = DSPI_MasterTransferBlocking(SPI1, &masterXfer);
memcpy(rxBuff, &masterRxData[2], rxBuffSize);
free(masterTxData);
free(masterRxData);
return ret;
}

status_t SPI_write(uint8_t regAddress, uint8_t value)
{
dspi_transfer_t masterXfer;
uint8_t *masterTxData = (uint8_t*)malloc(3);
int8_t *masterRxData = (uint8_t*)malloc(3);
masterTxData[0] = regAddress | 0x80/*write mode*/; //set most signicant bit to 1 for write
masterTxData[1] = regAddress & 0x80; //Clear the least significant 7 bits
masterTxData[2] = value;
masterXfer.txData = masterTxData;
masterXfer.rxData = masterRxData;
masterXfer.dataSize = 3;
masterXfer.configFlags = kDSPI_MasterCtar0 | kDSPI_MasterPcs0 |
kDSPI_MasterPcsContinuous;
status_t ret = DSPI_MasterTransferBlocking(SPI1, &masterXfer);
free(masterTxData);
free(masterRxData);
return ret;
}

int main(void)
{
    fxos_handle_t fxosHandle = {0};
    fxos_data_t sensorData   = {0};
    fxos_config_t config     = {0};
    uint8_t sensorRange      = 0;
    uint8_t dataScale        = 0;
    int16_t xData            = 0;
    int16_t yData            = 0;
    uint8_t i                = 0;
    uint8_t array_addr_size  = 0;
    status_t result          = kStatus_Fail;

    volatile int16_t xAngle = 0;
    volatile int16_t yAngle = 0;

    /* Board pin, clock, debug console init */
    BOARD_InitBootPins();
    BOARD_InitBootClocks();
    voltageRegulatorEnable();
    accelerometerEnable();
    setupSPI();
    /***** Delay *****/
    for (volatile uint32_t i = 0; i < 4000000; i++)
    __asm("NOP");
    /* Configure the SPI function */
    config.SPI_writeFunc = SPI_write;
    config.SPI_readFunc = SPI_read;
    result = FXOS_Init(&fxosHandle, &config);
    if (result != kStatus_Success)
    {
    PRINTF("\r\nSensor device initialize failed!\r\n");
    return -1;
    }
    /* Get sensor range */
    if (FXOS_ReadReg(&fxosHandle, XYZ_DATA_CFG_REG, &sensorRange, 1) != kStatus_Success)
    {
    return -1;
    }
    if (sensorRange == 0x00)
    {
    dataScale = 2U;
    }
    else if (sensorRange == 0x01)
    {
    dataScale = 4U;
    }
    else if (sensorRange == 0x10)
    {
    dataScale = 8U;
    }
    else
    {
    }
    /* Print a note to terminal */
    PRINTF("\r\nWelcome to the BUBBLE example\r\n");
    PRINTF("\r\nYou will see the change of angle data\r\n");
    /* Main loop. Get sensor data and update duty cycle */
    while (1)
    {
    /* Get new accelerometer data. */
    if (FXOS_ReadSensorData(&fxosHandle, &sensorData) != kStatus_Success)
    {
    return -1;
    }
    /* Get the X and Y data from the sensor data structure in 14 bit left format
    data*/
    xData = ((int16_t)((uint16_t)((uint16_t)sensorData.accelXMSB << 8) | (uint16_t)
    sensorData.accelXLSB)) / 4U;
    yData = ((int16_t)((uint16_t)((uint16_t)sensorData.accelYMSB << 8) | (uint16_t)
    sensorData.accelYLSB)) / 4U;
    /* Convert raw data to angle (normalize to 0-90 degrees). No negative angles. */
    xAngle = (int16_t)floor((double)xData * (double)dataScale * 90 / 8192);
    yAngle = (int16_t)floor((double)yData * (double)dataScale * 90 / 8192);
    /* Print out the angle data. */
    PRINTF("x= %2d y = %2d\r\n", xAngle, yAngle);
    /***** Delay *****/
    for (volatile uint32_t i = 0; i < 500000; i++)
    __asm("NOP");
    }
}
