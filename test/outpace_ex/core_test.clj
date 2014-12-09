(ns outpace-ex.core-test
  (:require [clojure.test :refer :all]
            [outpace-ex.core :refer :all]
            [outpace-ex.story1-data :refer [story1-string-data]]))

; Iterate over test examples from the story1-string-data 
(deftest verify-ocr
  (testing "parse-digits on all ocr examples"
    (doseq [{:keys [input result]} story1-string-data]
      (is (= (parse-digits input) result)))))


(deftest pre-condition-checksum
  (testing "valid-checksum pre-conditions"
    (is (thrown? java.lang.AssertionError 
                 (valid-checksum? [0 1 3 4 5])) "pre-condition on 9 digits")
    (is (thrown? java.lang.AssertionError 
                 (valid-checksum? [4 5 7 \? 0 8 0 0 0])) "all 9 must be digits")
    (is (thrown? java.lang.AssertionError 
                 (valid-checksum? [4 5 7 11 0 8 0 0 0])) "all 9 must be single digits")))


(deftest valid-checksum
  (testing "A valid and invalid account numbers"
    (let [valid-accounts [[7 1 1 1 1 1 1 1 1]
                          [1 2 3 4 5 6 7 8 9]
                          [4 9 0 8 6 7 7 1 5]]
          invalid-accounts [[8 8 8 8 8 8 8 8 8]
                            [4 9 0 0 6 7 7 1 5]
                            [0 1 2 3 4 5 6 7 8]]]
      (doseq [good-acc valid-accounts]
        (is (valid-checksum? good-acc) "Should be valid"))
      (doseq [bad-acc invalid-accounts]
        (is (not (valid-checksum? bad-acc)) "Should be invalid")))))

(let 
    [res-file (fn [f] (-> f 
                          (clojure.java.io/resource)
                          (clojure.java.io/file)))
     good-file (res-file "valid_data.txt")
     bad-rec-size-file (res-file "invalid_data_recsize.txt")
     bad-line-ln-file (res-file "invalid_data_line_ln.txt")
     bad-char-in-file (res-file "invalid_data_bad_char.txt")]
  (deftest valid-data
    (let [good-records (extract-records (read-input-data good-file))]
      (is (validate-records good-records)))
    (let [bad-rec-size (extract-records (read-input-data bad-rec-size-file))]
      (is (thrown-with-msg? java.lang.AssertionError #"not three lines long"
                   (validate-records bad-rec-size))))
    (let [bad-char-rec (extract-records (read-input-data bad-char-in-file))]
      (is (thrown-with-msg? java.lang.AssertionError #"has other than | _ or \\space"
                   (validate-records bad-char-rec))))
    (let [bad-line-length (extract-records (read-input-data bad-line-ln-file))]
      (is (thrown-with-msg? java.lang.AssertionError #"not 27 chars long"
                   (validate-records bad-line-length))))))

  
(let [good-acc [4 5 7 5 0 8 0 0 0]
      err-acc  [6 6 4 3 7 1 4 9 5]
      ill-acc  [8 6 1 1 0 \? \? 3 6]]
  (deftest categorize-acc-num
    (is (= (categorize-account-num good-acc)
           [:good good-acc]))
    (is (= (categorize-account-num err-acc)
           [:err err-acc]))
    (is (= (categorize-account-num ill-acc)
           [:ill ill-acc])))
  (deftest formating-account-num
    (is (= "457508000" (format-categorized-account-num 
                        (categorize-account-num good-acc))))
    (is (= "664371495\tERR" (format-categorized-account-num
                            (categorize-account-num err-acc))))
    (is (= "86110??36\tILL" (format-categorized-account-num
                             (categorize-account-num ill-acc))))))


(let [res-file (fn [f] (-> f
                           (clojure.java.io/resource)
                           (clojure.java.io/file)))
      good-file (res-file "valid_data.txt")
      expected-file (res-file "expected_output.txt")
      tmp-outfile (java.io.File/createTempFile "test_output" ".txt")]
  (deftest integration-test
    (do (main-processing good-file tmp-outfile)
        (let [output (slurp tmp-outfile)
              expected (slurp expected-file)]
          (is (= output expected)))))
  (.delete tmp-outfile))
