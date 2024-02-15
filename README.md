البتا
<img src='https://github.com/samadpls/ALEPH/blob/main/Single%20Cycle%20RISC-V%20Core.png' height=600 width=100%>
<br>
First of all get started by cloning this repository on your machine.

```ruby
git clone https://github.com/syedowaisalishah/Al-Battar-.git
```

Create a .txt file and place the ***hexadecimal*** code of your instructions simulated on ***Venus*** (RISC-V Simulator)\
Each instruction's hexadecimal code must be on seperate line as following. This program consists of 9 instructions.

```ruby
00500113
00500193
014000EF
00120293
00502023
00002303
00628663
00310233
00008067
```
Then perform the following step
```
cd RISCV-single-cycle\main\scala\riscv
```
Open **InstructionMem.scala** with this command. You can also manually go into the above path and open the file in your favorite text editor.
```ruby
open InstructionMem.scala
```
Find the following line
``` python
loadMemoryFromFile(mem, "/home/owais/Pictures/Hassan-CHISEL-SingleCycle-RV32I-main/src/main/scala/SingleCycle/Assemble.txt")
```
Change the .txt file path to match your file that you created above storing your own program instructions. or you can also use this file\
After setting up the InstructionMem.scala file, go inside the RV32i folder.
```ruby
cd Single-Cycle-CPU/RV32i
```


And enter
```ruby
sbt
```
When the terminal changes to this type
```ruby
sbt:RISCV-single-cycle>
```
Enter this command
```ruby
sbt:RISCV-single-cycle> testOnly SingleCycle.Topfile_Test -- -DwriteVcd=1
```

After success you will get a folder ***test_run_dir*** on root of your folder. Go into the examples folder inside.\
There you will find the folder named Top. Enter in it and you can find the Top.vcd file which you visualise on **gtkwave** to\
see your program running.
