/*
Copyright by Henry Ko and Nicola Nicolici
Department of Electrical and Computer Engineering
McMaster University
Ontario, Canada
*/

`timescale 1ns/100ps
`ifndef DISABLE_DEFAULT_NET
`default_nettype none
`endif

module experiment4 (
		/////// board clocks                      ////////////
		input logic CLOCK_50_I,                   // 50 MHz clock

		/////// switches                          ////////////
		input logic[17:0] SWITCH_I,               // toggle switches

		/////// VGA interface                     ////////////
		output logic VGA_CLOCK_O,                 // VGA clock
		output logic VGA_HSYNC_O,                 // VGA H_SYNC
		output logic VGA_VSYNC_O,                 // VGA V_SYNC
		output logic VGA_BLANK_O,                 // VGA BLANK
		output logic VGA_SYNC_O,                  // VGA SYNC
		output logic[7:0] VGA_RED_O,              // VGA red
		output logic[7:0] VGA_GREEN_O,            // VGA green
		output logic[7:0] VGA_BLUE_O,              // VGA blue

		/////// PS2                               ////////////
		input logic PS2_DATA_I,                   // PS2 data
		input logic PS2_CLOCK_I                   // PS2 clock
);

`include "VGA_param.h"
parameter SCREEN_BORDER_OFFSET = 32;
parameter DEFAULT_MESSAGE_LINE = 280;
parameter DEFAULT_MESSAGE_START_COL = 360;
parameter KEYBOARD_MESSAGE_LINE = 320;
parameter KEYBOARD_MESSAGE_START_COL = 360;

logic resetn, enable;

logic [7:0] VGA_red, VGA_green, VGA_blue;
logic [9:0] pixel_X_pos;
logic [9:0] pixel_Y_pos;

logic [5:0] character_address;
logic [11:0]maxpressed_num;

logic rom_mux_output;

logic screen_border_on;

assign resetn = ~SWITCH_I[17];

logic [7:0] PS2_code;
logic [7:0] PS2_reg[30:0];
logic [4:0] keypressed [5:0];

logic [4:0]counter;
logic [4:0]maxpressed;
logic [5:0]maxpressed_address;


logic [5:0]PS2_address;

logic PS2_code_ready;

logic PS2_code_ready_buf;
logic PS2_make_code;

// PS/2 controller
PS2_controller ps2_unit (
	.Clock_50(CLOCK_50_I),
	.Resetn(resetn),
	.PS2_clock(PS2_CLOCK_I),
	.PS2_data(PS2_DATA_I),
	.PS2_code(PS2_code),
	.PS2_code_ready(PS2_code_ready),
	.PS2_make_code(PS2_make_code)
);


// Display text
always_ff @ (posedge CLOCK_50_I or negedge resetn) begin
	if (resetn == 1'b0) begin
		PS2_code_ready_buf <= 1'b0;
		
		PS2_reg[30] <= 8'd0;
		PS2_reg[29] <= 8'd0;
		PS2_reg[28] <= 8'd0;
		PS2_reg[27] <= 8'd0;
		PS2_reg[26] <= 8'd0;
		PS2_reg[25] <= 8'd0;
		PS2_reg[24] <= 8'd0;
		PS2_reg[23] <= 8'd0;
		PS2_reg[22] <= 8'd0;
		PS2_reg[21] <= 8'd0;
		PS2_reg[20] <= 8'd0;
		PS2_reg[19] <= 8'd0;
		PS2_reg[18] <= 8'd0;
		PS2_reg[17] <= 8'd0;
		PS2_reg[16] <= 8'd0;
		PS2_reg[15] <= 8'd0;
		PS2_reg[14] <= 8'd0;
		PS2_reg[13] <= 8'd0;
		PS2_reg[12] <= 8'd0;
		PS2_reg[11] <= 8'd0;
		PS2_reg[10] <= 8'd0;
		PS2_reg[9] <= 8'd0;
		PS2_reg[8] <= 8'd0;
		PS2_reg[7] <= 8'd0;
		PS2_reg[6] <= 8'd0;
		PS2_reg[5] <= 8'd0;
		PS2_reg[4] <= 8'd0;
		PS2_reg[3] <= 8'd0;
		PS2_reg[2] <= 8'd0;
		PS2_reg[1] <= 8'd0;
		PS2_reg[0] <= 8'd0;
		
		keypressed[5] <= 5'd0;
		keypressed[4] <= 5'd0;
		keypressed[3] <= 5'd0;
		keypressed[2] <= 5'd0;
		keypressed[1] <= 5'd0;
		keypressed[0] <= 5'd0;
		
		counter <= 5'd0;
		maxpressed <= 5'd0;
		maxpressed_address <=6'o0;
		maxpressed_num <=12'o0;
		PS2_address <= 6'o0;
		enable <= 1'b0;

		
		
	end else begin
		PS2_code_ready_buf <= PS2_code_ready;
		if (PS2_code_ready && ~PS2_code_ready_buf && PS2_make_code && counter < 5'd31) begin
			// scan code detected
			maxpressed<=5'd0;
			//counter part
			//detection
			case(PS2_code)
				8'h1C:  keypressed[0]<= keypressed[0]+5'd1; // A
				8'h32:  keypressed[1]<= keypressed[1]+5'd1; // B
				8'h21:  keypressed[2]<= keypressed[2]+5'd1; // C
				8'h23:  keypressed[3]<= keypressed[3]+5'd1; // D
				8'h24:  keypressed[4]<= keypressed[4]+5'd1; // E
				8'h2B:  keypressed[5]<= keypressed[5]+5'd1; // F
			endcase
			case(PS2_code)
				8'h1C: PS2_address = 6'o01; // A
				8'h32: PS2_address = 6'o02; // B
				8'h21: PS2_address = 6'o03; // C
				8'h23: PS2_address = 6'o04; // D
				8'h24: PS2_address = 6'o05; // E
				8'h2B: PS2_address = 6'o06; // F
				default: PS2_address = 6'o40; // space
				
			endcase
			
			//shift register
			PS2_reg[30] <= PS2_reg[29];
			PS2_reg[29] <= PS2_reg[28];
			PS2_reg[28] <= PS2_reg[27];
			PS2_reg[27] <= PS2_reg[26];
			PS2_reg[26] <= PS2_reg[25];
			PS2_reg[25] <= PS2_reg[24];
			PS2_reg[24] <= PS2_reg[23];
			PS2_reg[23] <= PS2_reg[22];
			PS2_reg[22] <= PS2_reg[21];
			PS2_reg[21] <= PS2_reg[20];
			PS2_reg[20] <= PS2_reg[19];
			PS2_reg[19] <= PS2_reg[18];
			PS2_reg[18] <= PS2_reg[17];
			PS2_reg[17] <= PS2_reg[16];
			PS2_reg[16] <= PS2_reg[15];
			PS2_reg[15] <= PS2_reg[14];
			PS2_reg[14] <= PS2_reg[13];
			PS2_reg[13] <= PS2_reg[12];
			PS2_reg[12] <= PS2_reg[11];
			PS2_reg[11] <= PS2_reg[10];
			PS2_reg[10] <= PS2_reg[9];
			PS2_reg[9] <= PS2_reg[8];
			PS2_reg[8] <= PS2_reg[7];
			PS2_reg[7] <= PS2_reg[6];
			PS2_reg[6] <= PS2_reg[5];
			PS2_reg[5] <= PS2_reg[4];
			PS2_reg[4] <= PS2_reg[3];
			PS2_reg[3] <= PS2_reg[2];
			PS2_reg[2] <= PS2_reg[1];
			PS2_reg[1] <= PS2_reg[0];
			PS2_reg[0] <= PS2_address;
			
			counter += 5'd1;
			
		end
		//finalize to check the max key
		else if(counter >= 5'd31)begin
			if(keypressed[0] > maxpressed) begin
				maxpressed = keypressed[0];
				maxpressed_address = 6'o01;
			end
			if (keypressed[1] > maxpressed) begin
				maxpressed = keypressed[1];
				maxpressed_address = 6'o02;
			end
			if (keypressed[2] > maxpressed) begin
				maxpressed = keypressed[2];
				maxpressed_address = 6'o03;
			end
			if (keypressed[3] > maxpressed) begin 
				maxpressed = keypressed[3];
				maxpressed_address = 6'o04;
			end
			if (keypressed[4] > maxpressed) begin 
				maxpressed = keypressed[4];
				maxpressed_address = 6'o05;
			end
			if (keypressed[5] > maxpressed) begin
				maxpressed = keypressed[5];
				maxpressed_address = 6'o06;
			end
			
			case(maxpressed)
				5'd00: maxpressed_num = 12'o6060; //0
				5'd01: maxpressed_num = 12'o6061; //1
				5'd02: maxpressed_num = 12'o6062; //2
				5'd03: maxpressed_num = 12'o6063; //3
				5'd04: maxpressed_num = 12'o6064; //4
				5'd05: maxpressed_num = 12'o6065; //5
				5'd06: maxpressed_num = 12'o6066; //6
				5'd07: maxpressed_num = 12'o6067; //7
				5'd08: maxpressed_num = 12'o6070; //8
				5'd09: maxpressed_num = 12'o6071; //9
				5'd10: maxpressed_num = 12'o6160; //10
				5'd11: maxpressed_num = 12'o6161; //11
				5'd12: maxpressed_num = 12'o6162; //12
				5'd13: maxpressed_num = 12'o6163; //13
				5'd14: maxpressed_num = 12'o6164; //14
				5'd15: maxpressed_num = 12'o6165; //15
				5'd16: maxpressed_num = 12'o6166; //16
				5'd17: maxpressed_num = 12'o6167; //17
				5'd18: maxpressed_num = 12'o6170; //18
				5'd19: maxpressed_num = 12'o6171; //19
				5'd20: maxpressed_num = 12'o6260; //20
				5'd21: maxpressed_num = 12'o6261; //21
				5'd22: maxpressed_num = 12'o6262; //22
				5'd23: maxpressed_num = 12'o6263; //23
				5'd24: maxpressed_num = 12'o6264; //24
				5'd25: maxpressed_num = 12'o6265; //25
				5'd26: maxpressed_num = 12'o6266; //26
				5'd27: maxpressed_num = 12'o6267; //27
				5'd28: maxpressed_num = 12'o6270; //28
				5'd29: maxpressed_num = 12'o6271; //29
				5'd30: maxpressed_num = 12'o6360; //30
				5'd31: maxpressed_num = 12'o6361; //31
				default: maxpressed_num = 12'o4040; //space
			endcase
			enable<=1'b1;
		end
	end
end


VGA_controller VGA_unit(
	.clock(CLOCK_50_I),
	.resetn(resetn),
	.enable(enable),

	.iRed(VGA_red),
	.iGreen(VGA_green),
	.iBlue(VGA_blue),
	.oCoord_X(pixel_X_pos),
	.oCoord_Y(pixel_Y_pos),
	
	// VGA Side
	.oVGA_R(VGA_RED_O),
	.oVGA_G(VGA_GREEN_O),
	.oVGA_B(VGA_BLUE_O),
	.oVGA_H_SYNC(VGA_HSYNC_O),
	.oVGA_V_SYNC(VGA_VSYNC_O),
	.oVGA_SYNC(VGA_SYNC_O),
	.oVGA_BLANK(VGA_BLANK_O)
);

logic [2:0] delay_X_pos;

always_ff @(posedge CLOCK_50_I or negedge resetn) begin
	if(!resetn) begin
		delay_X_pos[2:0] <= 3'd0;
	end else begin
		delay_X_pos[2:0] <= pixel_X_pos[2:0];
	end
end

// Character ROM
char_rom char_rom_unit (
	.Clock(CLOCK_50_I),
	.Character_address(character_address),
	.Font_row(pixel_Y_pos[2:0]),
	.Font_col(delay_X_pos[2:0]),
	.Rom_mux_output(rom_mux_output)
);

// this experiment is in the 800x600 @ 72 fps mode
//assign enable = 1'b1;
assign VGA_CLOCK_O = ~CLOCK_50_I;

always_comb begin
	screen_border_on = 0;
	if (pixel_X_pos == SCREEN_BORDER_OFFSET || pixel_X_pos == H_SYNC_ACT-SCREEN_BORDER_OFFSET)
		if (pixel_Y_pos >= SCREEN_BORDER_OFFSET && pixel_Y_pos < V_SYNC_ACT-SCREEN_BORDER_OFFSET)
			screen_border_on = 1'b1;
	if (pixel_Y_pos == SCREEN_BORDER_OFFSET || pixel_Y_pos == V_SYNC_ACT-SCREEN_BORDER_OFFSET)
		if (pixel_X_pos >= SCREEN_BORDER_OFFSET && pixel_X_pos < H_SYNC_ACT-SCREEN_BORDER_OFFSET)
			screen_border_on = 1'b1;
end

//print module
always_comb begin

	character_address = 6'o40; // Show space by default
	
	// 8 x 8 characters
	if (pixel_Y_pos[9:3] == ((DEFAULT_MESSAGE_LINE) >> 3)) begin
		// Reach the section where the text is displayed
		//when there is key 
		if(maxpressed[4:0]>5'd0 && maxpressed[4:0] <= 5'd9) begin
			case (pixel_X_pos[9:3])
				(DEFAULT_MESSAGE_START_COL >> 3) +  0: character_address = 6'o13; // K
				(DEFAULT_MESSAGE_START_COL >> 3) +  1: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) +  2: character_address = 6'o31; // y
				(DEFAULT_MESSAGE_START_COL >> 3) +  3: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) +  4: character_address = maxpressed_address; // X			
				(DEFAULT_MESSAGE_START_COL >> 3) +  5: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) +  6: character_address = 6'o20; // P
				(DEFAULT_MESSAGE_START_COL >> 3) +  7: character_address = 6'o22; // R
				(DEFAULT_MESSAGE_START_COL >> 3) +  8: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) +  9: character_address = 6'o23; // S
				(DEFAULT_MESSAGE_START_COL >> 3) + 10: character_address = 6'o23; // S			
				(DEFAULT_MESSAGE_START_COL >> 3) + 11: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) + 12: character_address = 6'o04; // D
				(DEFAULT_MESSAGE_START_COL >> 3) + 13: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) + 14: character_address = maxpressed_num[5:0]; // X
				(DEFAULT_MESSAGE_START_COL >> 3) + 15: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) + 16: character_address = 6'o24; // T
				(DEFAULT_MESSAGE_START_COL >> 3) + 17: character_address = 6'o11; // I
				(DEFAULT_MESSAGE_START_COL >> 3) + 18: character_address = 6'o15; // M	
				(DEFAULT_MESSAGE_START_COL >> 3) + 19: character_address = 6'o05; // E	
				(DEFAULT_MESSAGE_START_COL >> 3) + 20: character_address = 6'o23; // S	
				default: character_address = 6'o40; // space
			endcase
		end 
		else if(maxpressed[4:0]>5'd0 && maxpressed[4:0]>5'd9) begin
			case(pixel_X_pos[9:3])
				(DEFAULT_MESSAGE_START_COL >> 3) +  0: character_address = 6'o13; // K
				(DEFAULT_MESSAGE_START_COL >> 3) +  1: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) +  2: character_address = 6'o31; // y
				(DEFAULT_MESSAGE_START_COL >> 3) +  3: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) +  4: character_address = maxpressed_address; // X			
				(DEFAULT_MESSAGE_START_COL >> 3) +  5: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) +  6: character_address = 6'o20; // P
				(DEFAULT_MESSAGE_START_COL >> 3) +  7: character_address = 6'o22; // R
				(DEFAULT_MESSAGE_START_COL >> 3) +  8: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) +  9: character_address = 6'o23; // S
				(DEFAULT_MESSAGE_START_COL >> 3) + 10: character_address = 6'o23; // S			
				(DEFAULT_MESSAGE_START_COL >> 3) + 11: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) + 12: character_address = 6'o04; // D
				(DEFAULT_MESSAGE_START_COL >> 3) + 13: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) + 14: character_address = maxpressed_num[11:6]; // X
				(DEFAULT_MESSAGE_START_COL >> 3) + 15: character_address = maxpressed_num[5:0];  // X
				(DEFAULT_MESSAGE_START_COL >> 3) + 16: character_address = 6'o40; // space
				(DEFAULT_MESSAGE_START_COL >> 3) + 17: character_address = 6'o24; // T
				(DEFAULT_MESSAGE_START_COL >> 3) + 18: character_address = 6'o11; // I	
				(DEFAULT_MESSAGE_START_COL >> 3) + 19: character_address = 6'o15; // M	
				(DEFAULT_MESSAGE_START_COL >> 3) + 20: character_address = 6'o05; // E	
				(DEFAULT_MESSAGE_START_COL >> 3) + 21: character_address = 6'o23; // S
				default: character_address = 6'o40; // space
			endcase
		end
		//when no key
		else begin
			case(pixel_X_pos[9:3])
				(DEFAULT_MESSAGE_START_COL >> 3) +  0: character_address = 6'o16; // N
				(DEFAULT_MESSAGE_START_COL >> 3) +  1: character_address = 6'o17; // O
				(DEFAULT_MESSAGE_START_COL >> 3) +  2: character_address = 6'o40; //  
				(DEFAULT_MESSAGE_START_COL >> 3) +  3: character_address = 6'o15; // M
				(DEFAULT_MESSAGE_START_COL >> 3) +  4: character_address = 6'o17; // O   
				(DEFAULT_MESSAGE_START_COL >> 3) +  5: character_address = 6'o16; // N
				(DEFAULT_MESSAGE_START_COL >> 3) +  6: character_address = 6'o11; // I
				(DEFAULT_MESSAGE_START_COL >> 3) +  7: character_address = 6'o24; // T
				(DEFAULT_MESSAGE_START_COL >> 3) +  8: character_address = 6'o17; // O
				(DEFAULT_MESSAGE_START_COL >> 3) +  9: character_address = 6'o22; // R
				(DEFAULT_MESSAGE_START_COL >> 3) + 10: character_address = 6'o05; // E   
				(DEFAULT_MESSAGE_START_COL >> 3) + 11: character_address = 6'o04; // D
				(DEFAULT_MESSAGE_START_COL >> 3) + 12: character_address = 6'o40; // 
				(DEFAULT_MESSAGE_START_COL >> 3) + 13: character_address = 6'o13; // K
				(DEFAULT_MESSAGE_START_COL >> 3) + 14: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) + 15: character_address = 6'o31; // Y
				(DEFAULT_MESSAGE_START_COL >> 3) + 16: character_address = 6'o23; // S
				(DEFAULT_MESSAGE_START_COL >> 3) + 17: character_address = 6'o40; //  
				(DEFAULT_MESSAGE_START_COL >> 3) + 18: character_address = 6'o20; // P 
				(DEFAULT_MESSAGE_START_COL >> 3) + 19: character_address = 6'o22; // R
				(DEFAULT_MESSAGE_START_COL >> 3) + 20: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) + 21: character_address = 6'o23; // S
				(DEFAULT_MESSAGE_START_COL >> 3) + 22: character_address = 6'o23; // S
				(DEFAULT_MESSAGE_START_COL >> 3) + 23: character_address = 6'o05; // E
				(DEFAULT_MESSAGE_START_COL >> 3) + 24: character_address = 6'o04;// D
				default: character_address = 6'o40; // space
			endcase
		end
	end

	
	
	// 8 x 8 characters
	if (pixel_Y_pos[9:3] == ((KEYBOARD_MESSAGE_LINE) >> 3)) begin
			case(pixel_X_pos[9:3])
				(KEYBOARD_MESSAGE_START_COL >> 3) +  0: character_address = PS2_reg[30]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  1: character_address = PS2_reg[29]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  2: character_address = PS2_reg[28];   
				(KEYBOARD_MESSAGE_START_COL >> 3) +  3: character_address = PS2_reg[27]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  4: character_address = PS2_reg[26];    
				(KEYBOARD_MESSAGE_START_COL >> 3) +  5: character_address = PS2_reg[25]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  6: character_address = PS2_reg[24]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  7: character_address = PS2_reg[23]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  8: character_address = PS2_reg[22]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) +  9: character_address = PS2_reg[21]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 10: character_address = PS2_reg[20];    
				(KEYBOARD_MESSAGE_START_COL >> 3) + 11: character_address = PS2_reg[19]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 12: character_address = PS2_reg[18];  
				(KEYBOARD_MESSAGE_START_COL >> 3) + 13: character_address = PS2_reg[17]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 14: character_address = PS2_reg[16]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 15: character_address = PS2_reg[15]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 16: character_address = PS2_reg[14]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 17: character_address = PS2_reg[13];  
				(KEYBOARD_MESSAGE_START_COL >> 3) + 18: character_address = PS2_reg[12];  
				(KEYBOARD_MESSAGE_START_COL >> 3) + 19: character_address = PS2_reg[11]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 20: character_address = PS2_reg[10]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 21: character_address = PS2_reg[9]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 22: character_address = PS2_reg[8]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 23: character_address = PS2_reg[7]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 24: character_address = PS2_reg[6]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 25: character_address = PS2_reg[5]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 26: character_address = PS2_reg[4]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 27: character_address = PS2_reg[3]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 28: character_address = PS2_reg[2]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 29: character_address = PS2_reg[1]; 
				(KEYBOARD_MESSAGE_START_COL >> 3) + 30: character_address = PS2_reg[0];
				default: character_address = 6'o40; // space
			endcase
	end
	end
	
// RGB signals
always_comb begin
		VGA_red = 8'h00;
		VGA_green = 8'h00;
		VGA_blue = 8'h00;

		if (screen_border_on) begin
			// blue border
			VGA_blue = 8'hFF;
		end
		
		if (rom_mux_output) begin
			// yellow text
			VGA_red = 8'hFF;
			VGA_green = 8'hFF;
		end
end

endmodule
