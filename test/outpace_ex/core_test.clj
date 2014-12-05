(ns outpace-ex.core-test
  (:require [clojure.test :refer :all]
            [outpace-ex.core :refer :all]
            [outpace-ex.story1-data :refer [story1-string-data]]))

; Iterate over test examples from the story1-string-data 
(deftest verify-ocr
  (testing "parse-digits on all ocr examples"
    (doseq [{:keys [input result]} story1-string-data]
      (is (= (parse-digits input) result)))))
