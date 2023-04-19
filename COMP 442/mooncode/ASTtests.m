           entry
           addi r14,r0,topaddr
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t1 := 0
           addi r1,r0,0
           sw t1(r0),r1
           % processing: i := t1
           lw r1,t1(r0)
           sw i(r0),r1
           % processing: t2 := 1
           addi r1,r0,1
           sw t2(r0),r1
           % processing: t3 := j + t2
           lw r2,j(r0)
           lw r3,t2(r0)
           add r1,r2,r3
           sw t3(r0),r1
           % processing: j := t3
           lw r1,t3(r0)
           sw j(r0),r1
           % processing: t4 := 6
           addi r1,r0,6
           sw t4(r0),r1
           % processing: barr3 := t4
           lw r1,t4(r0)
           sw barr3(r0),r1
           hlt

           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for constant 0
t1         res 4
           % space for constant 1
t2         res 4
           % space for j + t2
t3         res 4
           % space for variable ab
ab res 4
           % space for variable ac
ac res 4
           % space for variable barr1
barr1 res 4
           % space for variable barr2
barr2 res 4
           % space for variable barr3
barr3 res 4
           % space for constant 6
t4         res 4
           % space for variable barr1
barr1 res 4
           % buffer space used for console output
buf        res 20

