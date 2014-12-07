(ns outpace-ex.core
  (:require [clojure.string :as s]
            [outpace-ex.digits :as digits])
  (:import [java.io BufferedReader StringReader])
  (:gen-class))

(defn cut-line
  "Break a string containing one line
of input data into a seq of strings of length three."
  [line]
  (for [triple (partition 3 line)]
    (s/join triple)))

(defn parse-digit-strings
  "Takes three lines of input for digits data.
Returns a vector of vectors representing the 
parsed digits."
  [line1 line2 line3]
  (let [line1-triples (cut-line line1)
        line2-triples (cut-line line2)
        line3-triples (cut-line line3)]
    (mapv vector line1-triples line2-triples line3-triples)))


(defn ocr-to-int
  "Look up the vector of strings in the ocr-map and return
the resulting integer. Alternatively a \\? character for an 
unknown, i.e. illegible ocr character."
  [vec-of-ocr-strings]
  (get digits/ocr-map vec-of-ocr-strings \?))

(defn parse-digits
  "Takes a collection of three lines of input data.
Returns a vector of parsed digits. Illegible numbers 
are replaced with a \\? character in the vector.

Return a vector of digits and not a multidigit integer
for easier checksum processing."
  [ocr-lines]
  (let [[line1 line2 line3] ocr-lines]
    (->> (parse-digit-strings line1 line2 line3)
         (mapv ocr-to-int))))

(defn valid-checksum?
  "Calculate the mod11 checksum. 

When account-num is [d9 d8 d7 d6 d5 d4 d3 d2 d1]

Calculate ((1 x d1) + (2 x d2) + ... + (9 x d9)) mod 11

Valid when checksum == 0.

Return true when valid, returns false otherwise."
 [account-num]
 ;; this fn should only be called with potentially
 ;; valid account numbers comprised of 9 single digit
 ;; integers. Ensure this with preconditions.
 ;; The alternative of just returning false 
 ;; on nonsensical account numbers is not desired with 
 ;; the requirement to distinguish illegible from non-valid
 ;; account numbers.
 {:pre [(sequential? account-num)
        (every? integer? account-num)
        (= 9 (count account-num))
        (every? #(<= 0 % 9) account-num)]}
 (let [multipliers (range 9 0 -1)]
   (->>
    (map vector account-num multipliers) ; build pairs like [9 d9] 
    (map (partial apply *))              ; do multiplication of pairs
    (reduce +)                           ; sum them
    ((fn [sum] 
      (= 0 (mod sum 11)))))))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
