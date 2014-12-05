(ns outpace-ex.core-test
  (:require [clojure.test :refer :all]
            [outpace-ex.core :refer :all]
            [outpace-ex.story1-data :refer [story1-string-data]]))

; Iterate over test examples from the story1-string-data 
(deftest verify-ocr
  (testing "parse-digits on all ocr examples"
    (doseq [{:keys [input result]} story1-string-data]
      (is (= (parse-digits input) result)))))


(deftest valid-checksum
  (testing "A valid account number"
    (is (valid-checksum? [4 5 7 5 0 8 0 0 0]) "Should be valid")
    (is (not (valid-checksum? [6 6 4 3 7 1 4 9 5])) "Should be invalid")))
