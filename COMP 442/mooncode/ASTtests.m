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
           hlt

           % space for variable n
n res 4
           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for constant 0
t1         res 4
           % space for variable ax
ax res 4
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
           % buffer space used for console output
buf        res 20

