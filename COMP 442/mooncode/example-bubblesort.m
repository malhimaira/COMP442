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
           % processing: t2 := 0
           addi r1,r0,0
           sw t2(r0),r1
           % processing: j := t2
           lw r1,t2(r0)
           sw j(r0),r1
           % processing: t3 := 0
           addi r1,r0,0
           sw t3(r0),r1
           % processing: temp := t3
           lw r1,t3(r0)
           sw temp(r0),r1
           % processing: n := size
           lw r1,size(r0)
           sw n(r0),r1
           % processing: t4 := 0
           addi r1,r0,0
           sw t4(r0),r1
           % processing: i := t4
           lw r1,t4(r0)
           sw i(r0),r1
           % processing:  := 0
           addi r1,r0,0
           sw (r0),r1
           % processing: t5 := 64
           addi r1,r0,64
           sw t5(r0),r1
           % processing: arr := t5
           lw r1,t5(r0)
           sw arr(r0),r1
           % processing:  := 1
           addi r1,r0,1
           sw (r0),r1
           % processing: t6 := 34
           addi r1,r0,34
           sw t6(r0),r1
           % processing: arr := t6
           lw r1,t6(r0)
           sw arr(r0),r1
           % processing:  := 2
           addi r1,r0,2
           sw (r0),r1
           % processing: t7 := 25
           addi r1,r0,25
           sw t7(r0),r1
           % processing: arr := t7
           lw r1,t7(r0)
           sw arr(r0),r1
           % processing:  := 3
           addi r1,r0,3
           sw (r0),r1
           % processing: t8 := 12
           addi r1,r0,12
           sw t8(r0),r1
           % processing: arr := t8
           lw r1,t8(r0)
           sw arr(r0),r1
           % processing:  := 4
           addi r1,r0,4
           sw (r0),r1
           % processing: t9 := 22
           addi r1,r0,22
           sw t9(r0),r1
           % processing: arr := t9
           lw r1,t9(r0)
           sw arr(r0),r1
           % processing:  := 5
           addi r1,r0,5
           sw (r0),r1
           % processing: t10 := 11
           addi r1,r0,11
           sw t10(r0),r1
           % processing: arr := t10
           lw r1,t10(r0)
           sw arr(r0),r1
           % processing:  := 6
           addi r1,r0,6
           sw (r0),r1
           % processing: t11 := 90
           addi r1,r0,90
           sw t11(r0),r1
           % processing: arr := t11
           lw r1,t11(r0)
           sw arr(r0),r1
           % processing: t12 := 7
           addi r1,r0,7
           sw t12(r0),r1
           % processing: t13 := 7
           addi r1,r0,7
           sw t13(r0),r1
           % processing: t14 := 7
           addi r1,r0,7
           sw t14(r0),r1
           hlt

           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for variable j
j res 4
           % space for variable temp
temp res 4
           % space for constant 0
t1         res 4
           % space for constant 0
t2         res 4
           % space for constant 0
t3         res 4
           % space for variable n
n res 4
           % space for variable i
i res 4
           % space for constant 0
t4         res 4
           % space for variable arr
arr res 4
           % space for constant 0
           res 4
           % space for constant 64
t5         res 4
           % space for constant 1
           res 4
           % space for constant 34
t6         res 4
           % space for constant 2
           res 4
           % space for constant 25
t7         res 4
           % space for constant 3
           res 4
           % space for constant 12
t8         res 4
           % space for constant 4
           res 4
           % space for constant 22
t9         res 4
           % space for constant 5
           res 4
           % space for constant 11
t10        res 4
           % space for constant 6
           res 4
           % space for constant 90
t11        res 4
           % space for constant 7
t12        res 4
           % space for constant 7
t13        res 4
           % space for constant 7
t14        res 4
           % buffer space used for console output
buf        res 20

