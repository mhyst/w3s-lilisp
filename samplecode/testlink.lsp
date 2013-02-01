;================================================================
; File: testlink.lsp
; Author: Julio César Serrano Ortuno
; Creation date: First half of 2008 
; Update: 29/01/2013
;----------------------------------------------------------------
; Purpose: Lilisp coding introduction
; Desc: Java/lilisp interface
;       
;
; Usage: java -jar Lilisp.jar testlink.lsp
;================================================================
(
    ;Class instantiation
    (set $vect (create java.util.Vector))

    ;method calling
    (method $vect addElement (numeric 3))
    (method $vect trimToSize)
    (println "The vector size is " (method $vect size))
    
    ;Creating a new link
    ;The constructor may need some arguments
    (set $lnk (create julk.net.w3s.Link "title" "http://jcastle.info" (nil)))
    
    ;Getting link's depth
    (println "The link's depth is " (method $lnk getDepth))

    ;You may need to call a static method
    ;This may take some time because it will visit a web page and will
    ;extract all the links
    (set $lnks (staticmethod julk.net.w3s.AbstractAlgorithm getAllLinksFromPage $lnk))
    
    ;The past instruction returns a links object that can be covered
    ;This loop will show every found link
    (while (isnotnull (set $l (method $lnks next)))
      (println (method $l getLink))
    )

    ;Object fields access
    (println "The minimun allowed priority for links is " (getfield $lnks MINPRIORITY))
    
    ;Modifying object fields
    ;We need a class with public fields. Reference has two: address and value
    (set $ref (create julk.net.w3s.language.Reference))

    ;Let's try with value
    (setfield $ref value 141)
    ;Now we retrieve it
    (println "$ref.value = " (getfield $ref value))
)
