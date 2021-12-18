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

module SRAM_BIST (
	input logic Clock,
	input logic Resetn,
	input logic BIST_start,
	
	output logic [17:0] BIST_address,
	output logic [15:0] BIST_write_data,
	output logic BIST_we_n,
	input logic [15:0] BIST_read_data,
	
	output logic BIST_finish,
	output logic BIST_mismatch
);

enum logic [2:0] {
	S_IDLE,
	S_ODD_CHECK,
	S_EVEN_WRITE,
	S_WRITE_CYCLE,
	S_READ_CYCLE,
	S_DELAY_4
} BIST_state;

enum logic [1:0] {
	C_EVEN_Write,
	C_Decay_Check, // in this state we use the dule port to do both read and write.
	C_ODD_Read
} check_state;

logic BIST_start_buf;
logic [15:0] BIST_expected_data[2:0];

// write the 16 least significant bits of the address bus in each memory location
// 
// NOTE: this particular BACKGROUND pattern is specific to this BIST implementation
assign BIST_write_data[15:0] = BIST_address[15:0];

// based on the way how this particular BIST engine is implemented,
// the BIST expected data can be computed on-the-fly by
// decrementing the 16 least significant bits of the address 
//
// NOTE: the expected data must change if the memory is traversed in a different way
assign BIST_expected_data[0] = BIST_address[15:0] ;

// shift register
always_ff @ (posedge Clock or negedge Resetn) begin
	if (Resetn == 1'b0) begin
		BIST_expected_data[1] <= 16'd0;
		BIST_expected_data[2] <= 16'd0;
	end else begin
		BIST_expected_data[2] <= BIST_expected_data[1];
		BIST_expected_data[1] <= BIST_expected_data[0];
	end
end


// this specific BIST engine for this reference implementation works as follows
// write location 0 -> read location 0 -> 
// write location 1 -> read location 1 + compare location 0 ->
// write location 2 -> read location 2 + compare location 1 ->
// ... go through the entire address range
always_ff @ (posedge Clock or negedge Resetn) begin
	if (Resetn == 1'b0) begin
		BIST_state <= S_IDLE;
		BIST_mismatch <= 1'b0;
		BIST_finish <= 1'b0;
		BIST_address <= 18'd0;
		BIST_we_n <= 1'b1;		
		BIST_start_buf <= 1'b0;
	end else begin
		BIST_start_buf <= BIST_start;
		
		case (BIST_state)
		S_IDLE: begin
			if (BIST_start & ~BIST_start_buf) begin
				// start the BIST engine
				BIST_address <= 18'd0;
				BIST_we_n <= 1'b0; // initiate first WRITE
				BIST_mismatch <= 1'b0;
				BIST_finish <= 1'b0;
				BIST_state <= S_EVEN_WRITE;
			end else begin
				BIST_address <= 18'd0;
				BIST_we_n <= 1'b1;
				BIST_finish <= 1'b1;				
			end
		end
		// a couple of delay states to initiate the first WRITE and first READ
		S_ODD_CHECK: begin
			BIST_address <= BIST_address + 18'd2;
			// check the next state
			if(BIST_address < 18'h3FFFF) begin
				BIST_state <= S_ODD_CHECK;
			end else begin
				//start to check in decay order
				BIST_address <= 18'h3FFFF;
				BIST_state <= S_DELAY_4;
			end
		end
		S_EVEN_WRITE: begin
			BIST_address <= BIST_address + 18'd2;
			// check the next state
			if(BIST_address < 18'h3FFFF-18'd1) begin
				BIST_state <= S_EVEN_WRITE;
			end else begin
				//start to check in decay order
				BIST_address <= 18'h3FFFF;
				BIST_state <= S_WRITE_CYCLE;
			end
		end
		S_WRITE_CYCLE: begin
			// initiate a new READ
			BIST_we_n <= 1'b1;
			BIST_address <= BIST_address - 18'd1;
			BIST_state <= S_READ_CYCLE;
		end
		S_READ_CYCLE: begin
			// complete the READ initiated two clock cycles earlier and perform comparison
			if (BIST_read_data != BIST_expected_data[2]) 
				BIST_mismatch <= 1'b1;
			
			if (BIST_address > 18'h0) begin
				// increment address and continue by initiating a new WRITE 
				BIST_address <= BIST_address - 18'd1;
				BIST_we_n <= 1'b0; // init ned write
				BIST_state <= S_WRITE_CYCLE;
			end else begin
				BIST_address <= BIST_address + 18'd1;
				// delay for checking the last address
				BIST_state <= S_ODD_CHECK;
			end
		end
		S_DELAY_4: begin
			// check for data mismatch
			if (BIST_read_data != BIST_expected_data[2]) 
				BIST_mismatch <= 1'b1;
			
			// finish the whole SRAM
			BIST_state <= S_IDLE;
			BIST_finish <= 1'b1;	
		end
		default: BIST_state <= S_IDLE;
		endcase
	end
end

endmodule
