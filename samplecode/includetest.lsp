;================================================================
; File: includetest.lsp
; Author: Julio César Serrano Ortuno
; Creation date: 27/01/2013
; Update: 
;----------------------------------------------------------------
; Purpose: Lilisp coding introduction
; Desc: Shows the usage of the include primitive.
;       
; Loads a function definition from lib.lsp and uses it to
; sum two numbers. Then shows the result.
;
; Usage: java -jar Lilisp.jar includetest.lsp
;================================================================

(
	(include lib.lsp)


	(set $s (sumar 2 3))
	(println "2 + 3 = " $s) 

)
