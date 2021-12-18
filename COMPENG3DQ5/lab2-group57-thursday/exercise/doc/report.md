

## 16 Shift Register
Every input will right shift in the 16 shift registers `data_reg`. Only `data_reg[15]` will be transferred to the LCD controller.

## Upper case & Lower Case
Checking the variable `PS2_code`, if it is equal to 12, then it is left shift and the current display code should be uppercase. If it is equal to 59, it is right shift and the current display code should be lowercase. We use the variable `upper_case` to determine current case state, and use `case_reg` to store the case state of each code in `data_reg`, so `case_reg` will shift the same way the `data_reg` shifts. When data is transferred to the LCD, it will input value `{case_reg[15], data_reg[15]}`.
Uppercase PS2-to-LCE information will be stored in the ROM `PS2_to_LCD_ROM.mif` where the address starts with 1. Since punctuations, space, and number are not affected. Those keys remain the same in the upper part of the ROM. For example, addresses 069 and 169 both contain value 31, which represent number 1.

## LED
Comparison only starts when `data_counter == 4'd0`, it indicates that a line is completed or a new line starts. When the current line `LCD_line` is 0, only control the red LEDs. When it is 1, only control the green LEDs.
Turn on all 18 red LEDs, when the first 8 characters and the last 8 characters from the top line are the same, pass 18'b111111111111111111 into the output variable `LED_RED_O`; and turn off when the input overwrites the line.
Turn on all 9 green LEDs, when the first 8 characters and the last 8 characters from the bottom line are the same, pass 9’b111111111 into the output variable `LED_GREEN_O`; and turn off when the input overwrites the line.
