;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


; Name: Yuying Lai;  Zepeng Wang Put your name here for labs
; Student Number: 400268588; 400248490
; Lab Section: 02
; Description of Code: This code allows User LED D1 to light when the button is pressed
; Last Modified: FEB 1 2021
 
; Original: Copyright 2014 by Jonathan W. Valvano, valvano@mail.utexas.edu



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;ADDRESS DEFINTIONS

;The EQU directive gives a symbolic name to a numeric constant, a register-relative value or a program-relative value


SYSCTL_RCGCGPIO_R            EQU 0x400FE608  ;General-Purpose Input/Output Run Mode Clock Gating Control Register (RCGCGPIO Register)
GPIO_PORTN_DIR_R             EQU 0x40064400  ;GPIO Port N Direction Register address 
GPIO_PORTN_DEN_R             EQU 0x4006451C  ;GPIO Port N Digital Enable Register address
GPIO_PORTN_DATA_R            EQU 0x400643FC  ;GPIO Port N Data Register address

GPIO_PORTM_DIR_R             EQU 0x40063400  ;GPIO Port M Direction Register Address (Fill in these addresses)
GPIO_PORTM_DEN_R             EQU 0x4006351C  ;GPIO Port M Direction Register Address (Fill in these addresses)
GPIO_PORTM_DATA_R            EQU 0x400633FC  ;GPIO Port M Data Register Address      (Fill in these addresses) 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;Do not alter this section

        AREA    |.text|, CODE, READONLY, ALIGN=2 ;code in flash ROM
        THUMB                                    ;specifies using Thumb instructions
        EXPORT Start

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;Define Functions in your code here 
;The function Port F_Init to configures the GPIO Port F Pin 4 for Digital Output 

PortN_Init
    ; STEP 1
    LDR R1, =SYSCTL_RCGCGPIO_R          ;Loads the memory address of RCGCGPIO into register 1(R1); R1 = 0x400FE608
    LDR R0, [R1]                        ;Put the contents of the memory address of RCGCGPIO into register 0 (R0), R0 = 0x00000000
    ORR R0,R0, #0x00001000              ;Performs a bitwise OR operation with the contents of R0 and #0x00001000  
    STR R0, [R1]                        ;Stores R0 contents into contents of the address located in R1,RCGCGPIO now has Ox00001000 stored in it 
    NOP                                 ;Waiting for GPIO Port N to be enabled
	NOP                                 ;Waiting for GPIO Port N to be enabled
  
   ; STEP 5
    LDR R1, =GPIO_PORTN_DIR_R           ;Load the memory address of the GPIO Port N DIR Register into register 1 (R1), R1 = 0x40064400
    LDR R0, [R1]                        ;Put the contents of the memory address of GPIO Port N DIR Register in R0, R0 = 0x00000000
    ORR R0,R0, #0x2                 	;Perform a bitwise OR operation with the contents of R0 with 0x2 and put the contents into R0 , R0 = 0x02
    STR R0, [R1]                        ;Stores R0 contents into contents of the address located in R1; GPIO Port N Direction Register now has 0x02 stored in it 
    
	; STEP 7
    LDR R1, =GPIO_PORTN_DEN_R           ;Load the memory addess of the GPIO Port N DEN Register into register 1 (R1), R1 = 0x4006451C
    LDR R0, [R1]                        ;Put the contents of the memory address of GPIO Port N DEN Register in register 0 (R0,), R0 = 0x00000000
    ORR R0, R0, #0x2                  ;Perform a bitwise OR operation with the contents of R0 with 0x2 and put the contents into R0, R0 = 0x02
    STR R0, [R1]	                    ;Stores R0 contents into contents of the address located in R1; GPIO Port N DEN Register now has 0x02 stored in it 
    BX  LR                              ;return

PortM_Init 
	; step 1
    LDR R1, =SYSCTL_RCGCGPIO_R          ;Loads the memory address of RCGCGPIO into register 1(R1); R1 = 0x400FE608
    LDR R0, [R1]                        ;Put the contents of the memory address of RCGCGPIO into register 0 (R0), R0 = 0x00000000
    ORR R0,R0, #0x00000800              ;Performs a bitwise OR operation with the contents of R0 and #0x00000800  
    STR R0, [R1]                        ;Stores R0 contents into contents of the address located in R1,RCGCGPIO now has Ox00000800 stored in it 
    NOP                                 ;Waiting for GPIO Port M to be enabled
	NOP                                 ;Waiting for GPIO Port M to be enabled
  
   ; STEP 5
    LDR R1, =GPIO_PORTM_DIR_R           ;Load the memory address of the GPIO Port M DIR Register into register 1 (R1), R1 = 0x40063400
    LDR R0, [R1]                        ;Put the contents of the memory address of GPIO Port F DIR Register in R0, R0 = 0x00000000
    AND R0,R0, #0x0                 	;Perform a bitwise AND operation with the contents of R0 with 0x0 and put the contents into R0 , R0 = 0x0
    STR R0, [R1]                        ;Stores R0 contents into contents of the address located in R1; GPIO Port M Direction Register now has 0x0 stored in it 
    
	; STEP 7
    LDR R1, =GPIO_PORTM_DEN_R           ;Load the memory addess of the GPIO Port F DEN Register into register 1 (R1), R1 = 0x4006351C
    LDR R0, [R1]                        ;Put the contents of the memory address of GPIO Port F DEN Register in register 0 (R0,), R0 = 0x00000000
    ORR R0, R0, #0x1                  ;Perform a bitwise OR operation with the contents of R0 with 0x1 and put the contents into R0, R0 = 0x01
    STR R0, [R1]	                    ;Stores R0 contents into contents of the address located in R1; GPIO Port M DEN Register now has 0x01 stored in it 
    BX  LR                              ;return
   
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Start                             ; Your assembly code starts executing here
	BL  PortM_Init                ;The BL instruction is like a function call to initalize Port M
	BL  PortN_Init                ;The BL instruction is like a function call to initalize Port F  
 
Loop LDR R0, =GPIO_PORTM_DATA_R  ;Save the address of Input Port M to R0   
     LDR R1, [R0]                ;Save the contents at Input Port M to R1
	 
	 EOR R1, R1, #1                         ;Flips the input bit value at R1 bit 0: 0-> 1 or 1 -> 0 (remember push button is active low, so when we push we get 0 and we need 1 to turn on LED)
	 LSL R1, R1, #1                         ;Left shift R1 1 bits to the left depends on student number, just use shift of zero here if not
	 
	 LDR R0, =GPIO_PORTN_DATA_R
     STR R1, [R0]
     
	 B Loop 
 
     ALIGN   
     END   