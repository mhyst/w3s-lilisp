;================================================================
; File: first.lsp
; Author: Julio César Serrano Ortuno
; Creation date: First half of 2008 
; Update: 27/01/2013
;----------------------------------------------------------------
; Purpose: Lilisp coding introduction
; Desc: Makes some simple arithmetic calculations.
;       
;
; Usage: java -jar Lilisp.jar first.lsp
;================================================================

(
  (set $num1 30)
	(set $num2 (+ $num1 3))

	(println "$num1 = " $num1 " $num2 = " $num2)

	(Set $num3 (* (+ 2 15) $num2))
	(println "$num3 = " $num3)
	(set $res (+ (sqrt 9) (sqrt 9) (sqrt 9) (sqrt 9) $num1 $num2))
	(set $num4 (/ $res (decimal 2.0)))
	(println "$num4 = " $num4)

	#speed calculation#
	(set $space 16)
	(set $time 0.25)
	(set $speed (/ $space $time))
	(println "Speed: " $speed)
)
