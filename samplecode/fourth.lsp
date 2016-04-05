; Código obsoleto
; Las variables deben llamarse como '$nombre'
; El programa debe considerarse como una lista de instrucciones. Debe tener paréntesis inicial y final
; para contener la lista
(include lib.lsp)
#Factorial calc#
(defun factorial (f) (
	(= res 1) ;this is initialization

	(for (= i 2) (<= i f) (++ i) (
		(*= res i))
	)
	(return res))
)

;
; Here it comes the function call
;
(print "El factorial de 5 es " (factorial 5))
