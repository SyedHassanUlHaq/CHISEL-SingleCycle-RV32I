package pr

import chisel3._
import chisel3.util._

object ALUOP {
    val ALU_ADD = "b00000".U(5.W)
    val ALU_SUB = "b01000".U(5.W)
    val ALU_AND = "b00111".U(5.W)
    val ALU_OR = "b00110".U(5.W)
    val ALU_XOR = "b00100".U(5.W)
    val ALU_SLT = "b00010".U(5.W)
    val ALU_SLL = "b00001".U(5.W)
    val ALU_SLTU = "b00011".U(5.W)
    val ALU_SRL = "b00101".U(5.W)
    val ALU_SRA = "b01101".U(5.W)
    val ALU_beq = "b10000".U(5.W) 
    val ALU_bne = "b10001".U(5.W)
    val ALU_blt = "b10100".U(5.W)
    val ALU_bge = "b10101".U(5.W)
}

trait Config {
    val WLEN = 32
    val ALUOP_SIG_LEN = 5
}

import ALUOP._
class ALU extends Module with Config {
    val io = IO(new Bundle{
        val in_A = Input(SInt(WLEN.W))
        val in_B = Input(SInt(WLEN.W))
        val alu_Op = Input(UInt(ALUOP_SIG_LEN.W))
        var out = Output(SInt(WLEN.W))
        var branch_out = Output(Bool())
    })

    io.out := 0.S
    io.branch_out := 0.B
    switch(io.alu_Op){
        is(ALU_ADD){
            io.out := io.in_A + io.in_B
        }
        is(ALU_SUB){
            io.out := io.in_A - io.in_B
        }
        is(ALU_SLT){
            when(io.in_A < io.in_B) {
                io.out := 1.S
            }.otherwise{
                io.out := 0.S
            } 
        }
        is(ALU_SLTU){
            when(io.in_A.asUInt < io.in_B.asUInt) {
                io.out := 1.S
            }.otherwise {
                io.out := 0.S
            }
        }
        is(ALU_SRA){
            io.out := io.in_A >> io.in_B(4, 0)
        }
        is(ALU_SRL){
            io.out := io.in_A >> io.in_B(4, 0)
        }
        is(ALU_SLL){
            val sr = io.in_B(4, 0)
            io.out := io.in_A << sr
        }
        is(ALU_AND){
            io.out := io.in_A & io.in_B
        }
        is(ALU_OR){
            io.out:=io.in_A | io.in_B
        }
        is(ALU_XOR){
        io.out := (io.in_A ^ io.in_B)
        }
        is(ALU_beq){
            when (io.in_A === io.in_B){
                io.branch_out := 1.B
            }.otherwise{
                io.branch_out := 0.B
            }
        }
        is(ALU_bne){
            when (io.in_A =/= io.in_B){
                io.branch_out := 1.B
            }.otherwise{
                io.branch_out := 0.B
            }
        }
        is(ALU_blt){
            when (io.in_A < io.in_B){
                io.branch_out := 1.B
            }.otherwise{
                io.branch_out := 0.B
            }
        }
        is(ALU_bge){
            when (io.in_A >= io.in_B){
                io.branch_out := 1.B
            }.otherwise{
                io.branch_out := 0.B
            }
        }
    }
}
