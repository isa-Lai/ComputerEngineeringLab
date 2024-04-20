#include "Accelerometer_Component.h"

static fxos_handle_t fxosHandle;
static uint8_t data_scale;

extern QueueHandle_t motor_queue;

void setupAccelerometerComponent()
{
	setupAccelerometerPins();
	voltageRegulator_enable();
	accelerometer_enable();

	spi_init();


    /*************** Accelerometer Task ***************/
	//Create Accelerometer Task
	int status = xTaskCreate(accelerometerTask, "accelerometerTask", 200, (void*)NULL, 2, NULL);
	if (status != pdPASS)
	{
		PRINTF("Task creation failed!.\r\n");
		while (1);
	}
}

void setupAccelerometerPins()
{
	//Initialize Accelerometer Pins
    /* Port A Clock Gate Control: Clock enabled */
    /* Port B Clock Gate Control: Clock enabled */
	CLOCK_EnableClock(kCLOCK_PortA);
    CLOCK_EnableClock(kCLOCK_PortB);

    /* PORTA2 (pin K6) is configured as TRACE_SWO */
    PORT_SetPinMux(PORTA, 25u, kPORT_MuxAsGpio);
    PORT_SetPinMux(PORTB, 8u, kPORT_MuxAsGpio);
    PORT_SetPinMux(PORTB, 10u, kPORT_MuxAlt2);
    PORT_SetPinMux(PORTB, 11u, kPORT_MuxAlt2);
    PORT_SetPinMux(PORTB, 16u, kPORT_MuxAlt2);
    PORT_SetPinMux(PORTB, 17u, kPORT_MuxAlt2);
}

void voltageRegulator_enable()
{
	gpio_pin_config_t pin_config = {
	.pinDirection = kGPIO_DigitalOutput,
	.outputLogic = 0U};
	GPIO_PinInit(GPIOB, 8, &pin_config);
	GPIO_PinWrite(GPIOB, 8, 1U);
}

void accelerometer_enable()
{
	gpio_pin_config_t pin_config = {
	.pinDirection = kGPIO_DigitalOutput,
	.outputLogic = 0U};
	GPIO_PinInit(GPIOA, 25, &pin_config);
	GPIO_PinWrite(GPIOA, 25, 0U);
}

void spi_init()
{
    //Initialize SPI
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

status_t SPI_Send(uint8_t regAddress, uint8_t value)
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

status_t SPI_receive(uint8_t regAddress, uint8_t *rxBuff, uint8_t rxBuffSize)
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

void accelerometerTask(void* pvParameters)
{
	BaseType_t status;

	//Accelerometer task implementation
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

	int xAngle = 0;
	int yAngle = 0;

	/***** Delay *****/
	//vTaskDelay(100/portTICK_PERIOD_MS);
	/* Configure the SPI function */

	config.SPI_writeFunc = SPI_Send;
	config.SPI_readFunc = SPI_receive;

	result = FXOS_Init(&fxosHandle, &config);
	sendMessage("Accelerometer Initial Success");
	if (result != kStatus_Success)
		{
		PRINTF("\r\nSensor device initialize failed!\r\n");
		return;
		}
	/* Get sensor range */
	if (FXOS_ReadReg(&fxosHandle, XYZ_DATA_CFG_REG, &sensorRange, 1) != kStatus_Success)
		{
		return;
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
	//PRINTF("\r\nWelcome to the BUBBLE example\r\n");
	//PRINTF("\r\nYou will see the change of angle data\r\n");
	/* Main loop. Get sensor data and update duty cycle */
	while (1)
		{
		/* Get new accelerometer data. */
		if (FXOS_ReadSensorData(&fxosHandle, &sensorData) != kStatus_Success)
			{
			PRINTF("ERROR Received");
			return;
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
		//PRINTF("x= %2d y = %2d\r\n", xAngle, yAngle);
		motor_msg motor_value;
		motor_value.type= 1; // type angle
		motor_value.val = (-1) * xAngle;
		/***** Send and Delay *****/
		status = xQueueSendToBack(motor_queue, (void *)&motor_value, portMAX_DELAY);
		if (status != pdPASS)
			{
				PRINTF("Queue Send failed!.\r\n");
				while (1);
			}
		vTaskDelay(50/portTICK_PERIOD_MS);
		}
}
