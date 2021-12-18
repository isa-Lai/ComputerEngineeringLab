;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


; Name: Yuying Lai;  Zepeng Wang Put your name here for labs
; Student Number: 400268588; 400248490
; Lab Section: 02
; Description of Code: This code allows User LED D2 to blink
; Last Modified: Feb 1 2021
 
; Original: Copyright 2014 by Jonathan W. Valvano, valvano@mail.utexas.edu



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;ADDRESS DEFINTIONS

;The EQU directive gives a symbolic name to a numeric constant, a register-relative value or a program-relative value


SYSCTL_RCGCGPIO_R            EQU 0x400FE608  ;General-Purpose Input/Output Run Mode Clock Gating Control Register (RCGCGPIO Register)
GPIO_PORTN_DIR_R             EQU 0x40064400  ;GPIO Port N Direction Register address 
GPIO_PORTN_DEN_R             EQU 0x4006451C  ;GPIO Port N Digital Enable Register address
GPIO_PORTN_DATA_R            EQU 0x400643FC  ;GPIO Port N Data Register address
COUNTER                      EQU 0xFFFFF	;COUNTER to set the delay period
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
    ORR R0,R0, #0x00001000              ;Performs a bitwise OR operation with the contents of R0 and #0x00001000  and stores it back into R0, R0 = 0x00001000
    STR R0, [R1]                        ;Stores R0 contents into contents of the address located in R1,RCGCGPIO now has Ox00001000 stored in it 
    NOP                                 ;Waiting for GPIO Port N to be enabled
	NOP                                 ;Waiting for GPIO Port N to be enabled
  
   ; STEP 5
    LDR R1, =GPIO_PORTN_DIR_R           ;Load the memory address of the GPIO Port N DIR Register into register 1 (R1), R1 = 0x40064400
    LDR R0, [R1]                        ;Put the contents of the memory address of GPIO Port N DIR Register in R0, R0 = 0x00000000
    ORR R0,R0, #0x1                 	;Perform a bitwise OR operation with the contents of R0 with 0x10 and put the contents into R0 , R0 = 0x01
    STR R0, [R1]                        ;Stores R0 contents into contents of the address located in R1; GPIO Port N Direction Register now has 0x01 stored in it 
    
	; STEP 7
    LDR R1, =GPIO_PORTN_DEN_R           ;Load the memory addess of the GPIO Port N DEN Register into register 1 (R1), R1 = 0x4006451C
    LDR R0, [R1]                        ;Put the contents of the memory address of GPIO Port N DEN Register in register 0 (R0,), R0 = 0x00000000
    ORR R0, R0, #0x1                  ;Perform a bitwise OR operation with the contents of R0 with 0x10 and put the contents into R0, R0 = 0x01
    STR R0, [R1]	                    ;Stores R0 contents into contents of the address located in R1; GPIO Port N DEN Register now has 0x01 stored in it 
    BX  LR                              ;return

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Start
      BL  PortN_Init                ; The BL instruction is like a function call 
	  
      ;STEP 8                              		
      LDR R1, =GPIO_PORTN_DATA_R
      LDR R0,[R1]
      ORR R0,R0, #0x1
loop  STR R0, [R1]
      EOR R0,#0x1             ;XOR is used to toggle value
      
	  LDR R4, =COUNTER

loop1 NOP
      NOP                      ;Go into a loop
      SUB R4,R4,#0x1
      CMP R4,#0x00
	  BNE loop1
	  
	  B loop 
      ALIGN                         ;Make sure the end of this section is aligned
      END  