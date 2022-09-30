package SingleCycle

import chisel3._
import chisel3.util._

class aluControl extends Module{
    val io = IO(new Bundle{
        val alu_Operation = Input(UInt(3.W))
        val func3 = Input(UInt(3.W))
        val func7 = Input(UInt(1.W))
        val out = Output(UInt(5.W))
    })
  // io.func7 := io.func7(5)
    when(io.alu_Operation === "b000".U){//R-Type
        io.out := Cat("b0".U, io.func7, io.func3)
    }.elsewhen(io.alu_Operation === "b001".U){//I-Type
        io.out := Cat("b0".U, "b0000000".U, io.func3)
    }.elsewhen(io.alu_Operation === "b101".U){//S-Type
        io.out := "b00000000000".U
    }.elsewhen(io.alu_Operation === "b010".U){//SB-Type
        io.out := Cat("b10000000".U, io.func3)
    }.elsewhen(io.alu_Operation === "b110".U){//LUI-Type
        io.out := "b00000000000".U
    }.elsewhen(io.alu_Operation === "b011".U){//UJ-Type
        io.out := "b11111111111".U
    }.elsewhen(io.alu_Operation === "b100".U){//Load
        io.out := "b00000000000".U
    }.elsewhen(io.alu_Operation === "b111".U){//AUIPC
        io.out := "b00000000000".U
    }.otherwise{
        io.out := DontCare
    }
}