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


(defn parse-digits
  "Takes a collection of three lines of input data.
Returns a vector of parsed digits. Illegible numbers 
are replaced with a ? character in the vector."
  [ocr-lines]
  (let [[line1 line2 line3] ocr-lines]
    (->> (parse-digit-strings line1 line2 line3)
         (mapv #(get digits/ocr-map % \?)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
