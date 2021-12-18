# Group 57 Lab 1 report
## Counter
### Counting Up
The counter is controlled by three buttons. When `PUSH_BUTTON_N_I[1]` is pushed, it is in the adding stage with `up_count` equals to 1. Adding 1 to the first 3 bits on `counter[3:0]` when the binary number is less than 100, otherwise adding 1 to `counter[7:4]` and leaving 000 at `counter[3:0]`. If `counter[7:4]` currently is 100 and it has to add 1, it will switch back to 000

### Counting Down
When `PUSH_BUTTON_N_I[2]` is pushed, it is in the subtracting stage with `up_count` equals to 0. Subtracting 1 to the first 3 bits on `counter[3:0]` when the binary number is greater than 000, otherwise subtracting 1 to `counter[7:4]` and leaving 100 at `counter[3:0]`. If `counter[7:4]` currently is 000 and it has to minus 1, it will switch back to 100

### Stop Counting
When `PUSH_BUTTON_N_I[0]` is pushed, `stop_count` will toggle. If `stop_count` is 1, the counter will not execute counting. If `stop_count` is 0, the counter will execute in either counting up or down. 

Since the module `convert_hex_to_seven_segment` requires input of 4 bits.  The counter will also use 4 bits in counting. Since the decimal value will not exceed 4, the largest significant bit in that 4-bit variable will remain 0 all the time. 

## Switch & LED

- LED 8 is clear which is always high(logic 1); 
- LED 7 is using not exclusive or(**NXOR**); When there is even number of high, the result will be 1; 
- LED 6 is using an **AND** gate yo unite `SWITCH_I[15:8]` and **NOR** gate unite `SWITCH_I[7:0]` then using an **AND** gate unite two parts; **AND** gate requires all bits be 1 to result in 1. **NOR** gate requires all bits be 0 to result in 1. 
- LED 5 is controlled by a **NAND** gate to unite `SWITCH_I[15:8]`; **NAND** gate requires all bits be 1 to result in 0.
- LED 4 is controlled by an **OR** gate to unite `SWITCH_I[7:0]`; **OR** gate requires all bits be 0 to result in 0.
- In terms of LED 3 to 0, the best way is using a while loop to check the least significant bit which is equal to `SWITCH_I[15]`. However, the loop is not hardware descriptive, so we manually write if statements to check from switch 0 to switch 14.  If there is any bit equal to switch 15, the result will be the index of that switch, and it will not check the following higher switch.  If none of the switches is equal to switch 15, the result will be 15 in LED 3 to 0.

Since all bits store in the same variable `LED_GREEN_O`, unary operators may be the easiest way to do some operation.
