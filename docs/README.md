Lilisp Programming Language
===========================
An overview

By Mhyst


Introduction
-------------

Lilisp is a new Lisp-based programming language writen in Java. The
initial reason for a new language was the need of a scripting language
for W3S Personal Web Spider that would allow users to create their own
crawling algorithms using W3S engine. But as the language evolved, it
turned out as a candidate for a general purpose language.

Lilisp parser was going to be simple enough to code it by myself instead
of using a parser generator. Also the same was valid for the lexer. I
didn't want a complex language. I tried to make it as much uniform
as possible. And I think I got it.

Lisp programmers may understand Lilisp more easily at first, but keep
in mind that Lilisp is not Lisp. Lilisp is not as complex as Lisp, and
many commands are different. From Lisp I borrowed the syntax (those
damn parenthesis... lol), lists, macros and other things. As Lisp,
Lilisp is autoextensible using macros.


Lists
-----

A list is a set of elements. To signal a list you must enclose it with
parenthesis and separate the elements using spaces. Elements can be of
any type: numbers, strings, objects, etc. You can mix elements of
different types in the same list. List examples:

	(2 6 1 3)
	("hello" "world")
	(2 "hello friend" 4 "testing")

The first list has four numeric elements, the second has two string
elements and the third has four mixed elements. 

Also elements can be a list:

	(2 6 (3 0 2) 1 (3 4))

In this list the third and fifth elements are lists.

Lists have two access functions: "head" and "tail". Head retrieves the
first element of the list, and tail retrieves a list containing all
the elements but the first (head). For instance, if we apply "head" to
the list: (2 6 1 3) we'll obtain "2". If we apply "tail" to the same
list, we'll obtain the list (6 1 3). These two functions are enough to
navigate throughout the list.


Instructions
------------

In Lilisp, the instructions are lists of arguments. A Lilisp program
is a list itself, a list of instructions. The Lilisp interpreter uses
the very functions "head" and "tail" to read instructions. 

In instructions, the first element of the list must be the action
name. An action can be: a function, a macro, or a "bit". Primitives
in Lilisp are known as "bits". Each bit has its name and
the action that will be taken when the bit is found in the code. If the term
bit confuses you, just consider it like what other languages call a primitive.
Instruction example:

	(print "Hello world!")

Last instruction is a two elements list. The first element is the
action name (in this case a bit). The second element is an argument
to be passed to the "bit"'s execution code. This instruction will write
"Hello world!" in the console.


The first program
-----------------

Once we have some knowledge of Lilisp program structure, let's take a
look to the "Hello world!" program. It looks as follows:

	(
	   (print "Hello world!")
	)

Like I said, a Lilisp program is a list of instructions. In this
case, our program have just one instruction. You can copy this program to a new file and give it a try. The interpreter will load the entire code and will consider it as
a list. It will call the function "head" and "tail" on that list until
the return value will be "null". In such case the interpreter will
finish. Calling "head" once, will return: "(print "Hello world!")",
and calling "tail" it will return an empty list "()". The next call to
head will return "null", causing the interpreter to end working.


Variables
---------

Variables in Lilisp must be declarated prior to use. The name of the
variables must start with "$". The bit you need to create a variable is "set".
There are two ways to do it: using a literal, or
using a type. Using a literal may not render the correct
selection of the type. The safer way to perform it is to use type primitives. Here
are examples of both situations:

	(set $s "hello")    ;Using a literal
	(set $n (numeric))  ;Using the type numeric. Parentesis are mandatory here.

Using type bits, if you don't specify the initial value, Lilisp will try to set one for you (in case of numbers it will be zero; in case of strings, an empty
string). You may specify a value manually.

	(set $l (numeric 10))

If you are familiar with pointers in C or references in java, this is similar. "(numeric 10) will instantiate a numeric variable and will return a reference of it. This reference will be associated to the variable name specified in the set instruction (in previous case $l). If you use a literal as set second argument, Lilisp will try to determine the most suitable data type and then will undergo a similar process.

Bear in mind that Lilisp takes advantage of a deeply recursive design, so you can place any instruction returning a reference in place for the second set argument and it will work.

Once declarated, variables are ready to be used.

If you want to free resources, you can do it with "unset" primitive:

	(unset $l)

This primitive will remove the variable and will set free all the
memory assigned to it.


###Data types


Lilisp data types constitue a simplification of Java data types. Types
can be easily extended, but I don't see the need of more types for
now. Here is the correspondence between Lilisp and Java types:

* numeric   - int
* decimal   - double
* logic     - boolean
* character - char
* string    - java.lang.String
* array     - Object[]
* object    - Object

There are some types that aren't defined in Java but on W3S or the
language itself (W3S Personal Crawler is included with Lilisp package):

* list    - List (represents a list. Allows list handling (head, tail))
* file	  - File (represents a file. Allows file handling)
* link	  - Link (represent a web link)
* links	  - LinksManager (represents a set of web links)

Using the type "object" you can reference any instance of a Java
class. In the future I'll add some more types and primitives to help
handling W3S engine, but for now, you can do anything using the primitives for
objects handling (create, method, staticmethod, getfield,
setfield). This is also applicable to handle link and links variables.


Operators
---------

The operators have a C++/Java style. Though not all Java operators
are available. By now, offset and bit level operators aren't in the
language. The most probable is that these operators will appear in a
future version of Lilisp. Operators here can be overloaded. That means
that every operator works with one specific type of values. Some work
with numbers (numeric and decimal) and some (very few) work with
strings. So every type should have its own set of operators.

###numeric, decimal:

These operators return a number (numeric, decimal)

* +
* -
* *
* /
* sqrt
* ++
* --
* +=
* -=
* *=
* /=

###logic:

These operators return a logic value (true or false)

* ==
* >
* <
* >=
* <=
* !=
* !
* &&
* ||

###string:
* equals
* equalsignorecase
* contains
* replace
* cat

###strings conversion:
* tonumericarray
* tochararray

###character conversion:
* tochar
* tonumber

Operators in lilisp act as any other primitive because they are
defined in a Bit, exactly the same as with the rest of primitives. To
use it set it as the first element of a list. Most operators expect
two arguments. But you'll understand better with some examples:

	(+ 1 2)     ;The result will be 3
	(set $i (numeric))
	(++ $i)     ;The variable $i will be incremented by one.
	(+= $i 2)   ;The variable $i will be incremented by two.
	(< 3 4)     ;true
	(> 3 4)     ;false
	(! true)    ;false
	(! (< 3 4)) ;false

Think of the following instruction as an expression where arguments given to "or" operator are lists. These lists will be
evaluated before evaluating the "or" itself, and the return values
will be given to the "or" instruction.

	(|| (< 3 4) (> 3 4))     ;In this case, 

	(|| argument1 argument2) ;The "or" operator requires two or more logic
		                 ;arguments
	(|| true false)          ;true

	(equals "holA" "hola")  ;false
	(equalsignorecase "holA" "hola") ;true
	(contains "this is a test" "test") ;true
	(replace "hello world" "world" "dolly") ;"hello dolly"
	(cat "hello " "world")  ;returns "hello world"

That's enough to know about operators. If you're a Java or C
programmer you know all you need about this stuff.

Arrays
------

Lilisp can deal with arrays rather well. Apart from the type primitive
that creates an array, Lilisp has two primitives to handle them: aref
and sref. The "aref" primitive gets an element from the array, while
"sref" sets an element on the array. Example:

	#Note: sharps and semicolons mark comments.
	 sharp marks a multiline comment
	 semicolon marks a single line comment#

	(set $a (array 4)) ;creating a four elements array
	(sref $a 0 "hello")
	(sref $a 1 "dolly")
	(sref $a 2 "good")
	(sref $a 3 "bye")  ;We've created a four strings array

	(print (aref $a 2)) ;here we print the $a[2] element

This program should show us "good". Primitives "aref" and "sref" come
from Lisp, but note that Lisp primitive for array creation is
"make-array". Another difference from Lisp is that Lisp can create
multidimensional arrays while Lilisp only can do it indirectly:

	(set $a (array 4))
	(sref $a 0 (array(2))
	(sref $a 1 2)

That code creates a four elements array where the first element is
a two elements array. Note that only the first element is an
array. Accessing these arrays is quite tricky:

	(set $a0 (aref $a 0)) ;$a0 is now an array of two elements.
	(sref $a0 0 "foo")
	(sref $a0 1 "bar")
	(print (aref $a0 1))

That code should print "bar". This feature is one of the worst things
of Lilisp. Though, it's possible to improve it with a macro. We'll see
how in the macros section.


Files
-----

Being able to handle files is an important issue for a programming
language. Lilisp has a File type and some primitives:

* openread     - Opens the file in read mode
* openwrite    - Opens the file in write mode
* openappend   - Opens the file in append mode
* readline     - Reads a line of the file
* read         - Reads a single byte of the file
* writeline    - Writes a line to the file
* write        - Writes a single byte into the file
* skip         - Moves the file pointer
* mark         - Saves the position of the file pointer
* marksupported- To be sure that mark is supported
* reset        - Returns the file pointer to the last saved position
* close        - Closes the file

Lilisp files management uses BufferedReader and BufferedWriter Java
classes. These classes are rather fast and have most of operations
with files. Have into account that openwrite overwrites the file. If
you want to append data to the file, use openappend.

The best way to learn is to study an example program:

	(
	    ;File variable declaration
	    ;From here $f references the file filename.ext
	    (set $f (file "filename.ext"))
	   
	    ;Now let's open the file
	    ;openread need a file variable
	    (openread $f)

	    ;Get the first line from the file
	    ;readline returns a string that is used by set primitive 
	    ;to assign it to the variable $line
	    (set $line (readline $f))

	    ;Here's very few to tell
	    ;$line is a string variable that contains the first
	    ;line of the file.
	    ;The primitive print makes use of it to show it on the screen
	    (print $line))

	    ;If you take into account that this language likes recursivity,
	    ;the primitive set wouldn't be needed:
	    ;
	    ;(print (readline $f))
	    ;
	    ;This version is also possible
	    ;
	    ;(print (readline (openread (file "filename.ext"))))
	    ;But this way we cannot close the file, because no reference
	    ;is kept.
	    ;
	    ;(print (readline (set $f (openread (file "filename.ext")))))
	    ;Here is it a closeable version (yes, it works)

	    
	    ;Closing the file
	    (close $f)
	)



Flow control
------------

Lilisp control structures are simple instructions that use control
primitives. Each primitive requires a different number of arguments
and, with some exceptions, allow one list of instructions as the last
argument. Control primitives are: if, for, foreach, while, do...while, try,
break, continue and return. There are two more primitives in the
Lilisp FlowControl Bit that help to debug your program: debug_on and
debug_off. But let's start from the beginning:

###if
Is the only conditional structure available in the language. Yet
is quite powerfull and, as in most other languages, it allows
nesting. The first argument for if must be a "logic" value (or any
expression that returns a logic value). Example code:

	;simple alternative
	(if (< 2 3)
	   (print "2 is less that 3"))

Second argument to "if" must be the code. If the code involves more
than one instructions it should have a pair of extra parenthesis to be
a list of instructions. Example:

	;simple alternative
	(if (< 2 3)
	   ((print "2 is less than 3")
	    (print "aditional instruction")))

As you can see, if has still two arguments: two lists. The parenthesis
may be placed as the user likes:

	(if (< 2 3)  ;if instruction starts

	   ((print "testing parenthesis")   ;Don't forget the additional brackets
	    (print "testing multiple instructions")
	    (print "one more line"))

	)  ;if isntruction ends

###if...else
The double conditional structure allows three arguments instead of the
two of the simple one. Bear in mind that there's no "else" keyword in lilisp. The double conditional structure just uses an extra argument. If simple alternative needs two arguments, double conditional needs three.

	;double alternative
	(if (== 2 3)
	    (print "this will be printed if the if expression is true")
	    (print "and this on the contrary"))

If you need more instructions per case, just put it inside of a pair
of brackets.

	(if (< 3 2)
	    ((print "This won't be executed since 3 isn't less than 2")
	     (print "This won't be printed either"))
	    ((print "Oh, yeah, this will be executed")
	     (print "The else part")))

All control structures work that way.

###for
Now it's time to see the "for" primitive. The "for" primitive needs
four arguments to be passed: initialization, condition, increment and
code. Condition has to be a logic expression. The rest are there for
you to use the way you like. Initialization is executed in first
place. Then the condition is tested. If the condition is true, the
code will be executed. After the code is executed, the increment is
processed, and again the condition. Remember that initialization is
only processed one time.

	(for (set $i 0) (< $i 10) (++ $i)
	     (print $i))

###foreach
The "foreach" primitive is a different flavor of "for" that can be
applies to arrays and lists. This will help a lot, above all, to
handle arrays. "foreach" needs three arguments: the second is the
array variable, the first is a variable name that will be assigned an
element of the array (or list), the third is the code. In the
following example we'll create a ten elements array and will
initialize it with the numbers from 0 to 9:

	;As ever, we have to declare the array
	(set $a (array 10))

	;The $e variable doesn't not exists previously and is instantiated
	;automatically to the next element in the array. 
	(foreach $e $a
	     (sref $a $e.id $e.id))

Note that the variable $e.id is also created automatically by
"foreach" to keep track of the possition the current element keeps in our
array. 

	;This example prints all the elements of the above array $a
	(foreach $e $a
		 (print $e))

Easy, isn't it?

	;This example uses foreach on a list
	(set $l (list '(10 20 30 40 50 60 70 80 100)))

	;This time we show the possition and the element: 0> 10   1> 20....
	(foreach $e $l
		 (print $e.id "> " $e))

List are very efficient and easy to store static sequential data, while
arrays allow random access to their elements for reading and modification.


###while, do... while
The control structures "while" and "do" are equivalents to "while" and
"do... while" of C/C++/Java. Are while-like instructions where the
only difference is when the condition is tested:

	;With while, the condition is tested before executing the code
	(set $i 10)
	(while (> $i 0)
	       ((print $i)
		(-- $i)))

	;With do, the condition is tested after executing the code.
	(set $i 10)
	(do 
	    ((print $i)
	     (++ $i))
	(< $i 10))       ; Here is the condition

###break, continue
The primitives "break" and "continue" can be used to alter the normal
behavior of loops:

	;This example shows a continuous loop (while true)
	;The exit of the loop is marked by break
	(set $i 0)
	(while true
	  ((print $i)
	   (++ $i)
	   (if (> $i 10) (break))))

	;Here is a normal loop where we're skipping the number 2
	(set $i -1)
	(while (< $i 10)
	  ((++ $i)
	   (if (== $i 2) (continue))
	   (print $i)))



###try, return
There are still two control primitives we have to study: try and
return. Return is the way a Lilisp function returns a value, so
we'll cover it later, in the functions section. The primitive "try" is
a way to execute code prone to produce errors. An error in a Lilisp
program causes the interpreter to abort it. Using "try", will
allow the program to continue. By now, the program doesn't have error
control, so your Lilisp programs won't know the nature of the
error. Though it's possible to know if try code worked well or resulted
in an error. In case of error, the "try" primitive will hold the
throwed exceptions, will notify the errors and will return a false
logic value. If there weren't errors, then a true logic value will be
issued:

	;This set creates a invalid link. 
	;The url (second parameter to link) isn't a valid URL
	(set $l (link "here goes the title" "http:/www"))

	;Here the call to the getAllLinksFromPage static method of the class
	;AbstractAlgorithm (part of W3S core) will fail because we're
	;passing to it the invalid link.
	;"try" will avoid program abnormal termination and will return a false
	;value that we can test with "if".
	(if (try  (staticmethod julk.net.w3s.AbstractAlgorithm getAllLinksFromPage $l))
	   (print "All was ok")            ;if
	   (print "Something was wrong"))  ;else
	;So, "Something was wrong" will be showed to the user.

I recommend not using "try" until you've tested your program with some
test values. This last example contains the first reference to
handling W3S. The primitive "staticmethod" belongs to the objects
handler bit, an interface that allows instantiating Java classes,
calling its methods and accessing its properties. We'll study it in
the apropriate section of this document.

Some primitives need an existing variable to work. The primitive "try"
can be used very efficiently to control such cases:

	(if (try (++ $i))   ;++ will fail if $i is not an existing variable
	    (print "The value of i is " $i)      ;if
	    ((set $i 0)                          ;else
	     (print "The value of i is " $i)))

In that example, if $i variable was not previously declared, it will
declare it, assigning it a zero value and then it will print "The
value is 0".

With "try" you can try to execute a block of instructions. In case of
error, the block will be terminated. This would be good if you plan to
launch a transaction. In that case, if something fails, you don't want
the rest of the transaction being executed. 

If you feel the need, "try" can be nested. Each nested "try" can be
tested on: if, for, while and do instructions. To better control a
chain of nested "try's" you can use "raise". The primitive "raise"
will cause the automatic cancelation of the current "try". Also it may
pass the error to the higher levels of "try's". It can be used alone or
with a numeric argument, being that argument the number of nested
"try's" to be affected.

	(try                  ;level 1
	     ...
	     (try             ;level 2 
		...
		(try          ;level 3
			...
			(raise) ;only current try block will be affected
			...
		)
	     )
	)

In that example "raise" would only affect to the level three "try". No
more instructions on that block will be executed after the "raise".

	(try                  ;level 1
	     ...
	     (try             ;level 2 
		...
		(try          ;level 3
			...
			(raise 1)
			...
		)
	     )
	)

In the previous example "raise 1" will affect the levels 3 and 2. If
it were "raise 2", level 1 would be also affected.

The intelliget use of "try" can be a powerful ally, but if you don't manage
it well, it can turn into your worst nightmare, so be cautious and use
it wisely.


Include system
--------------

The include system allows the programmer to write parts of the same
program in separated files. These separated files can later be added
to the main source file with the "include" primitive. That's pretty
similar as C include system. The main difference is that C has a
preprocessor that reads files and prepares the definitive source file
to be compiled. In Lilisp, if the interpreter reads the "include"
primitive, it immediatly will load and execute that file. You can
place includes everywhere in your source files. If an included file
doesn't exists it will cause the program to terminate. Though, you can
control it with try.

	;In this example we try to include (execute) the file "first.lsp"
	;We're using "try" to control the possible error.
	(if (try (include "first.lsp"))
	    (print "All was ok, the file exists and was included")
	    (print "The file ifile.lsp couldn't be included"))

Remember that "include" is a normal primitive. There's no such a thing
as a preprocessor in Lilisp.


Functions
---------

In Lilisp you can create functions to do part of your program
tasks. Functions are helpful because you define it once but you can
call it many times, so it saves you time and effort. 

Each function have a new field for new variables. These
variables are called local variables and you still can access program
variables called global variables. This isn't very different of what
you'd espect. Functions can receive arguments or not, at your choice,
and can return a value by means of "return" primitive. In Lilisp, you
don't have to define the arguments type nor the return type, but be
careful, if your function works with numbers and you use non numeric
values, it will result in errors. You can test the actual argument
types using the $<var>.type construction from inside the function. 

To declare a function, you have to use the "defun" primitive. That
primitive acts as any other one, but when finished you'll have a new
callable function. The "defun" primitive accepts tree arguments:
function name, function arguments, and function code. Function
arguments and function code have to be lists containing the arguments
and the code, respectively. But we have enough blavering for now,
let's see some example functions:


	;our first function declaration
	(defun multiply ($n1 $n2) (return (* $n1 $n2)))

	;Now we'll call it
	(multiply 2 3)

	;the function will return 6, but there's no container to receive the
	;value, the next example will print the result:

	(print "2 X 3 = " (multiply 2 3)) ;2 x 3 = 6

The "multiply" primitive doesn't exitst until we execute the "defun
multiply..." instruction. But after it, it acts exactly as other
primitives. 

Though functions can access global variables, it is not recommended to
write programs that way. Side effects can be dangerous and hard to get
rid of. 

Lilisp functions arguments are passed by value. That means that you
cannot modify the variables involved in the calling arguments. That's
good because it provides isolation, reducing the possibility of side
effects. 

Functions may be recursive. Recursivity is a constant in Lilisp. An stack
overflow error can occur if you don't plan recursive functions
carefully. But that's also an issue on most languages.

	;factorial 
	(defun fac ($n)
	       (if (== $n 0)
	       	   (return 1)                     ;if
		   (return (* $n (fac (- $n 1)))) ;else
	       )
	)


Macros
------

Lilisp macros idea came from Lisp, though Lilisp macros aren't that
complex. Macros definition is exactly equal to functions'. What is
different is that macros work as code replacement tools. Macros aren't
called, when a macro is detected, the macro code is placed instead of
the macro's instruction. To help understand macros, we are going to
create two macros that will serve for multi-dimensional arrays:

	;Look this program
	;Read arrays section again if you have any doubt
	(
	    ;macros in shape are like functions
	    ;we have to use "defmacro" primitive to declare a macro

	    ;here we have two macros
	    ;Variables case doesn't matter, that's just my preference
	    (defmacro d2aref ($A $I0 $I1) (aref (aref $A $I0) $I1))
	    (defmacro d2sref ($A $I0 $I1 $VAL) (sref (aref $A $I0) $I1 $VAL))

	    ;May be $a a two elements array
	    (set $a (array 2))

	    ;each element of $a is another two elements array
	    (sref $a 0 (array 2))
	    (sref $a 1 (array 2))

	    ;Our macros make the handling of bi-dimensional arrays rather easy
	    ;macro  array row col value
	    (d2sref  $a    0   0    1)
	    (d2sref  $a    0   1    2)
	    (d2sref  $a    1   0    3)
	    (d2sref  $a    1   1    4)

	    ;Every d2ref line is replaced with the macro code this way
	    ;This is the result of the last d2sref instruction:
	    ;
	    ;(sref (aref $a 1) 1  4)
	    ;
	    ;Notice as argument values are placed according 
	    ;macro's definition arguments

	    (print (d2aref $a 0 0))   ;1
	    (print (d2aref $a 0 1))   ;2
	    (print (d2aref $a 1 0))   ;3
	    (print (d2aref $a 1 1))   ;4
	)

Macros can be simple or as complex as you fancy.


Java objects handling
---------------------

Every new language needs time to grow. I didn't want to have Lilisp
limited because its youth. To solve it, I designed an interface
against Java classes. That interface is called Objects Handler Bit
(OHB) and helps you to instantiate Java objects, calling its methods
and accesing its properties. The primary use of this bit was to deal
with W3S crawling engine. Some W3S objects are addressed by Lilisp
primitives. For instance: link and links data types, both referencing
julk.net.w3s.Link and julk.net.w3s.LinksManager. In the near future 
the rest of W3S objects will count with its own primitives, but for
now, we can use the Objects Handler Bit. Again, there's a lot of
things uncovered by Lilisp that you can do via OHB.

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

Until now, the unique limitation I've observed is that you need an
instance before access a static field. Another implicit limitation is
that you need to know some bits of Java before using OHB. For Java
programmers will be very easy though.


How to get user input in Lilisp
-------------------------------

Sometimes in your programs you might need the user input. There are
two primitives to do that: input and inputw.

###input
Uses the console to interact with the user. It needs two
arguments: type and message. It will show the message, get the input
and return a value of the specified type. If the value doesn't fit the
type, it will make the user know and will request the data again.

	;getting a string
	(input string "Tell me your name: ")

You can retrieve the return value with "set" or use the as argument
for another primitive call:

	;retrieving a name
	(set $name (input "Tell me your name: "))

	;retrieving a number
	(println "The circunference is " (* 2 (* 3.1416 (input decimal "Radius: "))))

	;retrieving a list. For instance enter: '(1 2 3 4)
	(set $l (input list "Enter a number list: "))
	(foreach $e $l
	  (println $e))

###inputw
If you're executing your Lilisp program from the W3S
environment you're not going to interact with the user via the
console, you'd like to do that with an input window. That's exactly
the way inputw works. Notwithstanding, you can use inputw also from a
Lilisp console program. Using "inputw" is a bit more difficult than
using "input". The "inputw" requires five arguments: the windows
title, the input message, the button caption, the textfield width and
the desired type.

	(inputw "Testing" "Tell me your name: " "Accept" 20 string)

As for the rest, "inputw" acts exactly the same as "input".

	(println "Your name is: " (inputw "Testing" "Name: " "Accept" 20
	string))


