(defun fac ($n)
   (if (== $n 0)
      (return 1)
      (return (* $n (fac (- $n 1)))))
)

(print (fac (numeric 3)))

#(defun testbreak (n) (
	(int i n)
	(int cont 0)
	(while true (
		(++ i)
		(++ cont)
		(if (>= i 10) break))
	)
	(return cont))
)

((defun lambda (x) (+ x 2)) 3)

(testbreak 3)

(boolean a true)

(if true a false)

(int i)
(while true (
	(++ i)
	(if (> i 10) break))
)
(print i)

(defun condition () (return true))
(while (condition) break)
	

(if a (return 10) (return 11))#