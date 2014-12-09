(ns outpace-ex.core
  (:require [clojure.string :as s]
            [outpace-ex.digits :as digits]
            [clojure.java.io :as io])
  (:import [java.io File BufferedReader StringReader]))

(defn read-input-data
  "Reads in the 'ocr' input data from a file.

Based on the spec of ~500 lines all the data will
be slurped into memory rather than streamed.

Returns a line-seq of all lines from the file."
  [^File infile]
  (assert (< (.length infile) 10e6)) ;; take exception to a > 10Mb input file
  (-> (slurp infile)
      (StringReader.)
      (BufferedReader.)
      (line-seq)))

(defn extract-records
  "Take all the input lines, partition on blank lines,
keep all the even partitions, i.e. the account number records.
This approach is tolerant of zero or multiple trailing  blank lines
on the last record."
 [input-lines]
 (->> input-lines
      (partition-by (fn [^String ln]
                      (-> ln (.trim) (.isEmpty)))) ; partition on blank lines
      (keep-indexed (fn [idx entry]                
                      (when (even? idx) ; (even? 0) is true here
                        entry)))))   ; keep only the non-blanks 

(defn validate-records
  "As per the spec ensure all the records are three lines long,
each line is 27 characters, and contains only the ocr characters.
Throws an AssertionError on first invalid record.
Returns true if all records are valid."
  [seq-of-records]
  (doseq [r seq-of-records]
    (assert (= 3 (count r)) (str "Bad record found, not three lines long:\n" r))
    (doseq [ln r] 
      (assert (= 27 (count ln)) 
              (str "Bad line in record, not 27 chars long:\n" ln))
      (assert (every? #{\space \_ \|} ln)
              (str "Bad line in record, has other than | _ or \\space:\n" ln))))
  true) ; return true if all assertions pass

(defn read-records
  "Returns a seq of records read from infile.
Records are validated via validate-records fn."
  [^File infile]
  (let [recs (->> infile
                  read-input-data
                  extract-records)]
    (when (validate-records recs)
      recs)))


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

(defn categorize-account-num
  "Takes an account-num vector as parsed by parse-digits.
Returns a two element vector, the first element being
a kw tag :ill, :err or :good, the second being the account-num vector."
  [acc-num-vec]
  (cond (some #{\?} acc-num-vec) [:ill acc-num-vec]
        (valid-checksum? acc-num-vec) [:good acc-num-vec]
        :else [:err acc-num-vec]))
        
(defn format-categorized-account-num
  "Takes a tagged account number from categorize-account-num
 and returns the string to output."
  [tagged-acc-num]
  (let [[tag acc-num] tagged-acc-num
        acc-str (s/join acc-num)]
    (cond (= tag :ill) (s/join [acc-str \tab "ILL"])
          (= tag :err) (s/join [acc-str \tab "ERR"])
          (= tag :good) acc-str
          :else (assert false (str "Unknown categorize tag: " tag)))))


(defn process-records
  "Takes a sequence of valid records (each a collection of three lines)
and maps them into the output strings"
 [records]
 (for [rec records]
   (->> rec
        (parse-digits)
        (categorize-account-num)
        (format-categorized-account-num))))
 

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
