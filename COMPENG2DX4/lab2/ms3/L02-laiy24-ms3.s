;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;Yuying Lai 400268588
;ADDRESS DEFINTIONS

;The EQU directive gives a symbolic name to a numeric constant, a register-relative value or a program-relative value


SYSCTL_RCGCGPIO_R            EQU 0x400FE608  ;General-Purpose Input/Output Run Mode Clock Gating Control Register (RCGCGPIO Register)
GPIO_PORTN_DIR_R             EQU 0x40064400  ;GPIO Port N Direction Register address 
GPIO_PORTN_DEN_R             EQU 0x4006451C  ;GPIO Port N Digital Enable Register address
GPIO_PORTN_DATA_R            EQU 0x400643FC  ;GPIO Port N Data Register address
	
GPIO_PORTM_DIR_R             EQU 0x40063400  ;GPIO Port M Direction Register Address (Fill in these addresses)
GPIO_PORTM_DEN_R             EQU 0x4006351C  ;GPIO Port M Direction Register Address (Fill in these addresses)
GPIO_PORTM_DATA_R            EQU 0x400633FC  ;GPIO Port M Data Register Address      (Fill in these addresses) 

GPIO_PORTF_DIR_R             EQU 0x4005D400  ;GPIO Port F Direction Register address 
GPIO_PORTF_DEN_R             EQU 0x4005D51C  ;GPIO Port F Digital Enable Register address
GPIO_PORTF_DATA_R            EQU 0x4005D3FC  ;GPIO Port F Data Register address
	
COUNTER EQU 0x88888
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;Do not alter this section

        AREA    |.text|, CODE, READONLY, ALIGN=2 ;code in flash ROM
        THUMB                                    ;specifies using Thumb instructions
        EXPORT Start

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;Function PortN_Init 
PortN_Init 
    ;STEP 1
    LDR R1, =SYSCTL_RCGCGPIO_R 
    LDR R0, [R1]   
    ORR R0,R0, #0x1000           				          
    STR R0, [R1]               
    NOP 
    NOP   
   
    ;STEP 5
    LDR R1, =GPIO_PORTN_DIR_R   
    LDR R0, [R1] 
    ORR R0,R0, #0x3         
	STR R0, [R1]   
    
    ;STEP 7
    LDR R1, =GPIO_PORTN_DEN_R   
    LDR R0, [R1] 
    ORR R0, R0, #0x3                                    
    STR R0, [R1]  
    BX  LR                            
 
PortM_Init 
    ;STEP 1 
	LDR R1, =SYSCTL_RCGCGPIO_R       
	LDR R0, [R1]   
    ORR R0,R0, #0x800 
	STR R0, [R1]   
    NOP 
    NOP   
 
    ;STEP 5
    LDR R1, =GPIO_PORTM_DIR_R   
    LDR R0, [R1] 
    ORR R0,R0, #0x00          
	STR R0, [R1]   
    
	;STEP 7
    LDR R1, =GPIO_PORTM_DEN_R   
    LDR R0, [R1] 
    ORR R0, R0, #0x11         
	                          
    STR R0, [R1]    
	BX  LR                     

PortF_Init
    ; STEP 1
    LDR R1, =SYSCTL_RCGCGPIO_R          
    LDR R0, [R1]                        
    ORR R0,R0, #0x20                    
    STR R0, [R1]                         
    NOP                                 
	NOP                                 
  
   ; STEP 5
    LDR R1, =GPIO_PORTF_DIR_R           
    LDR R0, [R1]                       
    ORR R0,R0, #0x10                  	
    STR R0, [R1]                         
    
	; STEP 7
    LDR R1, =GPIO_PORTF_DEN_R           
    LDR R0, [R1]                        
    ORR R0, R0, #0x10                    
    STR R0, [R1]	                     
    BX  LR                              


State_Init LDR R5,=GPIO_PORTN_DATA_R  ;Locked is the Initial State
	       MOV R4,#2_00000000
	       STR R4,[R5]
		   LDR R5,=GPIO_PORTF_DATA_R  
	       MOV R4,#2_00000000
	       STR R4,[R5]
	       BX LR 

Start                             
	BL  PortN_Init 
	BL  PortF_Init  
	BL  PortM_Init
	BL  State_Init
	LDR R0, = GPIO_PORTM_DATA_R  ; Inputs set pointer to the input 
	LDR R3, = COUNTER        ;R3 stores our counter
	
;at initial state 000
Locked_000 
	LDR R5,=GPIO_PORTN_DATA_R
	MOV R4,#2_00000000		;set led D2
	STR R4,[R5]
	LDR R5,=GPIO_PORTF_DATA_R
	MOV R4,#2_00000000		;set led D3
	STR R4,[R5]
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00000000		;if PM4 unpushed, can go check is PM4 is pushed again
	BEQ Locked_000_Loop
	BNE Locked_000	;otherwise stay in this state
	
Locked_000_Loop
	LDR R3, = COUNTER		;delay for about 0.4s
	BL Delay
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00010000		;if PM4 pushed, go check PM0
	BEQ Locked_000_Measure
	BNE Locked_000_Loop		;otherwise measure PM4
 
Locked_000_Measure
	LDR R1,[R0]            
	AND R2,R1,#2_00000001	;if PM0 is 1 go state001
	CMP R2,#2_00000001
	BEQ Locked_001
	BNE Locked_000			;otherwise return to initial state

;state 001
Locked_001 
	LDR R5,=GPIO_PORTN_DATA_R
	MOV R4,#2_00000000		;set led D2
	STR R4,[R5]
	LDR R5,=GPIO_PORTF_DATA_R
	MOV R4,#2_00010000		;set led D3
	STR R4,[R5]
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00000000		;if PM4 unpushed, can go check is PM4 is pushed again
	BEQ Locked_001_Loop
	BNE Locked_001	;otherwise stay in this state
	
Locked_001_Loop
	LDR R3, = COUNTER		;delay for about 0.4s
	BL Delay
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00010000		;if PM4 pushed, go check PM0
	BEQ Locked_001_Measure
	BNE Locked_001_Loop		;otherwise measure PM4
 
Locked_001_Measure
	LDR R1,[R0]            
	AND R2,R1,#2_00000001
	CMP R2,#2_00000000		;if PM0 is 0 go state010
	BEQ Locked_010
	BNE Locked_000			;otherwise return to initial state
 
;state 010
Locked_010 
	LDR R5,=GPIO_PORTN_DATA_R
	MOV R4,#2_00000001		;set led D2
	STR R4,[R5]
	LDR R5,=GPIO_PORTF_DATA_R
	MOV R4,#2_00000000		;set led D3
	STR R4,[R5]
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00000000		;if PM4 unpushed, can go check is PM4 is pushed again
	BEQ Locked_010_Loop
	BNE Locked_010	;otherwise stay in this state
	
Locked_010_Loop
	LDR R3, = COUNTER		;delay for about 0.4s
	BL Delay
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00010000		;if PM4 pushed, go check PM0
	BEQ Locked_010_Measure
	BNE Locked_010_Loop		;otherwise measure PM4
 
Locked_010_Measure
	LDR R1,[R0]            
	AND R2,R1,#2_00000001
	CMP R2,#2_00000001		;if PM0 is 1 go state011
	BEQ Locked_011
	BNE Locked_000			;otherwise return to initial state
	
;state 011
Locked_011
	LDR R5,=GPIO_PORTN_DATA_R
	MOV R4,#2_00000001		;set led D2
	STR R4,[R5]
	LDR R5,=GPIO_PORTF_DATA_R
	MOV R4,#2_00010000		;set led D3
	STR R4,[R5]
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00000000		;if PM4 unpushed, can go check is PM4 is pushed again
	BEQ Locked_011_Loop
	BNE Locked_011	;otherwise stay in this state
	
Locked_011_Loop
	LDR R3, = COUNTER		;delay for about 0.4s
	BL Delay
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00010000		;if PM4 pushed, go check PM0
	BEQ Locked_011_Measure
	BNE Locked_011_Loop		;otherwise measure PM4
 
Locked_011_Measure
	LDR R1,[R0]            
	AND R2,R1,#2_00000001
	CMP R2,#2_00000001		;if PM0 is 1 go unlock
	BEQ Unlocked_State
	BNE Locked_000			;otherwise return to initial state

;state 100 which unlock
Unlocked_State
	LDR R5,=GPIO_PORTN_DATA_R
	MOV R4,#2_00000010		;set led D1
	STR R4,[R5]
	LDR R5,=GPIO_PORTF_DATA_R
	MOV R4,#2_00000000		;set led D3
	STR R4,[R5]
	LDR R1,[R0]            
	AND R2,R1,#2_00010000
	CMP R2,#2_00000000		;if PM4 unpushed, can go back to initial
	BEQ Locked_000
	BNE Unlocked_State	;otherwise stay in this state
	
Delay
	SUB R3, R3, #0x1	;delay until 88888 becomes 00000
	CMP R3,#0x00
	BNE Delay
	BX LR
	
	ALIGN   
    END  