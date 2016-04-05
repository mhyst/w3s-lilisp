#Código obsoleto. Las variables necesitan indicarse con $ y el código del
programa debe estar entre paréntesis a su vez.#
#Probando estructuras if anidadas#
(defun doble (num) (
	(return (* num 2)))
)
(= num1 10)
(= num2 20)
(if (< (doble num1) (doble num2)) (
	(if (> (doble num2) num1) (
		(= num3 (- num2 num1))
		(+= num3 (doble num2))
	))
))
(= res num3)
(for (= i 0) (< i 10) (++ i) (
	(+= res i)
))
(= res i)
