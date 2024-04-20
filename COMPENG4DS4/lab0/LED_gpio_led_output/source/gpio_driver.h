#include "pin_mux.h"
#include "fsl_debug_console.h"
#include "fsl_gpio.h"

typedef struct {
  int PDOR;
  int PSOR;
  int PCOR;
  int PTOR;
  int PDIR;
  int PDDR;
} GPIO_Struct;


void gpio_init(GPIO_Struct *base, uint32_t pin, const gpio_pin_config_t *config);

void gpio_toggle(GPIO_Struct *base, uint32_t pin);

void gpio_write(GPIO_Struct *base, uint32_t pin, uint8_t output);
