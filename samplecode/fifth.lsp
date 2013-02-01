(defun factorial (f) (
	(= res 1)
	(for (= i 1) (<= i f) (++ i) (
		(*= res i))
	)
	(return res))
)

(factorial 3)

