(= res 0)
(for (= i 0) (< i 5) (++ i) (
	(for (= j 0) (< j 5) (++ j) (
		(+= res (+ i j)))
	))
)
(print "Resultado de los for anidados: " res)
(= i 0)
(do (
	(++ i)
) (< i 10))
(print "Resultado del do: " i)
(= i 10)
(while (> i 0) (
	(-- i))
)
(print "Resultado del while: " i)
(return res)
