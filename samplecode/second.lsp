;================================================================
; File: second.lsp
; Author: Julio César Serrano Ortuno
; Creation date: First half of 2008 
; Update: 27/01/2013
;----------------------------------------------------------------
; Purpose: Lilisp coding introduction
; Desc: Function definitions, arithmetic and logical operators, for loop
;       
;
; Usage: java -jar Lilisp.jar second.lsp
;================================================================


(

	(defun sumar ($N1 $N2) (
	   (return (+ $N1 $N2)))
	)

	#This function calculates the speed based in space and time#
	(defun velocidad ($espacio $tiempo) (
		#Comments can be put everywhere#
		     (return (/ $espacio $tiempo)))
	)

	(defun cuadriplicar ($N1) (
		(set $var1 (* $N1 2))
		(set $var2 (* $N1 2))
		(return (* $var1 $var2)))
	)

	(set $num3 (* 4 2))
	(println "$num3: " $num3)
	(set $num1 $num3)
	(println "$num1: " $num1)

	(sumar 2 2)

	(println "Suma: " (sumar $num1 (sumar $num3 2)))
	(set $vel (velocidad 230 2))
	(cuadriplicar $vel)
	(++ $vel)
	(-- $vel)
	(+= $vel 3)
	(-= $vel 1)
	(== 2 3)
	(== 3 3)
	(== $vel 117)
	(>= $vel $num1)
	(!= $vel 118)
	(! (> $vel 200))
	(&& (> $vel 200) (< 3 4))
	(for (set $i 0) (< $i 3) (++ $i) (
		(+= $vel 1))
	)	
)
