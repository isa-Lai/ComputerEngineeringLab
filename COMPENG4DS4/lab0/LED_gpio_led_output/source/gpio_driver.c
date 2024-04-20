#include "gpio_driver.h"

void gpio_init(GPIO_Struct *base, uint32_t pin, const gpio_pin_config_t *config)
{
    assert(NULL != config);

    if (config->pinDirection == kGPIO_DigitalInput)
    {
        base->PDDR &= (~(1UL << pin));
    }
    else
    {
    	gpio_write(base, pin, config->outputLogic);
        base->PDDR |= ((1UL << pin));
    }
}

void gpio_toggle(GPIO_Struct *base, uint32_t pin)
{
    base->PTOR = (1u << pin);

}

void gpio_write(GPIO_Struct *base, uint32_t pin, uint8_t output)
{
    if (output == 0U)
    {
        base->PCOR = (1UL << pin);
    }
    else
    {
        base->PSOR = (1UL << pin);
    }
}
