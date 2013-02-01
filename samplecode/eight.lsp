;Instanciación simple
(int a 3)
(int b 4)
(+ a b)

;Instanciación implícita
(+ (int c 3) (int d 4))

;Suma de cadena con numero
(. "cadena " 2)

;Declaración de cadenas
(string cad "Estamos tan agustito ")
(string cad2 "que ni tenemos calor, ")

;Suma de variables con literales
(. cad cad2 "ni frío, ni resquemor")

(print cad)

(if (equals cad cad2) (
	(print "cad y cad2 son iguales")) (
	(print "cad y cad2 no son iguales"))
)
(return "Aquí acaba el programa eight.lsp")