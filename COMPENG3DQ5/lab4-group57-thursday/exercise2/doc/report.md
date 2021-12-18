# SARM-BIST

## State Machine
There are three main state needed for this exercises. `S_EVEN_WRITE` that write data into even address in increasing order. `S_ODD_CHECK` that read data in odd address in increasing order.  In between these two state, I will write in odd and read from even at the same time in decreasing order. Since these state required both read and write, I will reuse and modify `S_WRITE_CYCLE` and `S_READ_CYCLE` from exp4. I will also reuse `S_IDLE` and `S_DELAY_4` for the initialization and finishing.

In `S_EVEN_WRITE`, the address increate 2 in every clock cycle until it reach 18'h3FFFF-18'd1. Then address will manually swtich to 18'h3FFFF and start decreasing 1 in every clock cycle. `BIST_we_n` swtich between 1 and 0 in every clock cycle to achive the function of write in odd and read in even. Then the BIST will increase address by 2 every clock cycle start from 0 to 3FFFF. Finally, mark finish at `BIST_finish` and end the check. 

## Shift register
In experiment 4, every read action appear every 2 clock cycle. Since read from SRAM also required 2 clock cycle, experiment 4 just put `BIST_expected_data` be address-1. However, in our exercises. Read may appear every 2 cycle or 1 cycle, and the step of read address are 2, using address-2 will experience problem.  We will use three register `BIST_expected_data[2:0]` to store the value 2 clock cycle before now. Comparing only `BIST_expected_data[2]` allows the expected value always be the address in the past 2 clock cycle. 
