;Instanciaci�n simple
(int a 3)
(int b 4)
(+ a b)

;Instanciaci�n impl�cita
(+ (int c 3) (int d 4))

;Suma de cadena con numero
(. "cadena " 2)

;Declaraci�n de cadenas
(string cad "Estamos tan agustito ")
(string cad2 "que ni tenemos calor, ")

;Suma de variables con literales
(. cad cad2 "ni fr�o, ni resquemor")

(print cad)

(if (equals cad cad2) (
	(print "cad y cad2 son iguales")) (
	(print "cad y cad2 no son iguales"))
)
(return "Aqu� acaba el programa eight.lsp")