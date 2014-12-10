(ns outpace-ex.digits)

(def ocr-map
  "A map for use as the main parsing function.
Each digit is a composite of three lines of input text.
Likewise the map keys are each a vector of three strings.

Immutable value semantics allow for composite data as map keys."
  {[" _ "
    "| |"
    "|_|"] 0

   ["   "
    "  |"
    "  |"] 1

   [" _ "
    " _|"
    "|_ "] 2
    
   [" _ "
    " _|"
    " _|"] 3

   ["   "
    "|_|"
    "  |"] 4

   [" _ "
    "|_ "
    " _|"] 5

   [" _ "
    "|_ "
    "|_|"] 6

   [" _ "
    "  |"
    "  |"] 7

   [" _ "
    "|_|"
    "|_|"] 8

   [" _ "
    "|_|"
    " _|"] 9})
