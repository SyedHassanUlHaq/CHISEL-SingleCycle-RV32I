package SingleCycle

import chisel3._
import chisel3.util._

class top extends Module{
    val io = IO(new Bundle{
        val instruction = Output(UInt(32.W))
        val out = Output(SInt(32.W))
    })
    io.out := 0.S
    val pC_mod = Module(new pC)
    val dataMem_mod = Module(new dataMemory)
    val instrMem_mod = Module(new instrMem)
    val control_mod = Module(new control)
    val immdGen_mod = Module(new immdValGen)
    val aluControl_mod = Module(new aluControl)
    val alu_mod = Module(new ALU)
    val regFile_mod = Module(new regFile)
    val brControl_mod = Module(new branchControl)
    val jalr_mod = Module(new jalr)
    
    // PC AND INSTR MEMORY
    instrMem_mod.io.addr := pC_mod.io.out(21, 2).asUInt()
    io.instruction := instrMem_mod.io.instr
    control_mod.io.op_code := io.instruction(6, 0)
    
    // IMM GEN
    immdGen_mod.io.instr := io.instruction
    immdGen_mod.io.pc := pC_mod.io.out
    val imm_mux = MuxLookup(control_mod.io.extend_Sel, 0.S, Array(
    ("b00".U) -> immdGen_mod.io.i_imm,
    ("b01".U) -> immdGen_mod.io.s_imm,
    ("b10".U) -> immdGen_mod.io.u_imm,
    ("b11".U) -> 0.S
    ))
    
    // ALU CONTROL
    aluControl_mod.io.alu_Operation := control_mod.io.alu_Operation
    val func3 = io.instruction(14, 12)
    val func7 = io.instruction(31, 25)
    aluControl_mod.io.func3 := func3
    aluControl_mod.io.func7 := func7(5)
    
    // REG FILE
    regFile_mod.io.rs1_addr := io.instruction(19, 15)
    regFile_mod.io.rs2_addr := io.instruction(24, 20)
    regFile_mod.io.rd_addr := io.instruction(11, 7)
    
    // REG FILE MUX
    val readData1Mux = MuxLookup(control_mod.io.operand_A, 0.S, Array(
    ("b00".U) -> regFile_mod.io.rs1,
    ("b01".U) -> pC_mod.io.pc4,
    ("b10".U) -> pC_mod.io.out,
    ("b11".U) -> regFile_mod.io.rs1
    ))
    val readData2Mux = Mux(control_mod.io.operand_B.asBool(), imm_mux, regFile_mod.io.rs2)
    
    // ALU AND BRANCH
    alu_mod.io.in_A := readData1Mux
    alu_mod.io.in_B := readData2Mux
    alu_mod.io.alu_Op := aluControl_mod.io.out
    brControl_mod.io.in_A := readData1Mux
    brControl_mod.io.in_B := readData2Mux
    brControl_mod.io.br_Op := aluControl_mod.io.out
    
    // DATA MEMORY           
    dataMem_mod.io.rdAddr := alu_mod.io.out(11, 2)
    dataMem_mod.io.dataIn := regFile_mod.io.rs2.asSInt()
    dataMem_mod.io.writeData := control_mod.io.memWrite
    dataMem_mod.io.readData := control_mod.io.memRead

    val dataMem_mux = Mux(control_mod.io.memToReg.asBool(), dataMem_mod.io.dataOut, alu_mod.io.out.asSInt())
    regFile_mod.io.writeData := dataMem_mux

    val branch_and = control_mod.io.branch & brControl_mod.io.branch_out.asBool()
    jalr_mod.io.rs1 := regFile_mod.io.rs1.asUInt()
    jalr_mod.io.imm := imm_mux.asUInt()
    val branchMux = Mux(branch_and.asBool(), immdGen_mod.io.sb_imm, pC_mod.io.pc4)
    val pc_mux = MuxLookup(control_mod.io.nextPc_Sel, 0.S, Array(
    ("b00".U) -> pC_mod.io.pc4,
    ("b01".U) -> branchMux,
    ("b10".U) -> immdGen_mod.io.uj_imm,
    ("b11".U) -> jalr_mod.io.pcVal.asSInt()
    ))
    pC_mod.io.in := pc_mux
    io.out := regFile_mod.io.rs1
}