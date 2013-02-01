#(link prueba "Search lore" "http://www.searchlore.org")
(method prueba setPriority 10)
(method prueba getPriority)
(method prueba getNumBars "http://www.google.es/name/do/")
(method prueba getLink)
(method prueba getTitle)
(method prueba getFather)
(link prueba2 "Google lore" "http://www.searchlores.org/google.htm")
(method prueba2 setFather prueba)
(link prueba3 (method prueba2 getFather))
(set prueba prueba2)
(set test (new julk.net.w3s.Link "title" "http://www.mhyst.es" prueba3))

(set cfg (new julk.net.w3s.Config))
(set lm (new julk.net.w3s.LinksManager cfg))

(link lnk "Search lore" "http://www.searchlores.org")
(set htf (new julk.net.w3s.HttpToFile lnk "temp.txt"))

;(set aa (new julk.net.w3s.AbstractAlgorithm))
(smethod julk.net.w3s.LinksExplorer searchForLinksExt htf lm)
(set nlnk (method lm next))#

(link lnk "Search Lores" "http://www.searchlores.org")
(set lm (smethod julk.net.w3s.AbstractAlgorithm getLinksFromPage lnk true))
(set lnk (method lm next))

(print "Primer enlace: " (method lnk getLink))
(set lnk (method lm next))
(print "Segundo enlace: " (method lnk getLink))
(print "Tercer enlace: " (method (method lm next) getLink))
(set lnk (method lm next))
(while (notisnull lnk) (
	(print "Enlace: " (method lnk getLink))
	(set lnk (method lm next)))
)