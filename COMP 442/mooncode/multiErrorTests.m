           entry
           addi r14,r0,topaddr
           % processing: t1 := 0.0
           addi r1,r0,0.0
           sw t1(r0),r1
           % processing: i := t1
           lw r1,t1(r0)
           sw i(r0),r1
           % processing: idEx := abc_1abc
           lw r1,abc_1abc(r0)
           sw idEx(r0),r1
           % processing: t2 := 2
           addi r1,r0,2
           sw t2(r0),r1
           % processing: t3 := i + t2
           lw r2,i(r0)
           lw r3,t2(r0)
           add r1,r2,r3
           sw t3(r0),r1
           % processing: intEx := t3
           lw r1,t3(r0)
           sw intEx(r0),r1
           % processing:  := 1
           addi r1,r0,1
           sw (r0),r1
           % processing:  :=  + 
           lw r3,(r0)
           lw r2,(r0)
           add r1,r3,r2
           sw (r0),r1
           % processing: arr := 
           lw r1,(r0)
           sw arr(r0),r1
           hlt

           % space for variable intEx
intEx res 4
           % space for variable i
i res 4
           % space for constant 0.0
t1         res 4
           % space for constant 2
t2         res 4
           % space for i + t2
t3         res 4
           % space for constant 1
           res 4
           % space for  + 
           res 4
           % buffer space used for console output
buf        res 20

