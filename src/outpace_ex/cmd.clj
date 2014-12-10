(ns outpace-ex.cmd
  (:require [outpace-ex.core :only [main-processing]]
            [clojure.tools.cli :as cli]))

(defn -main
  "Main execution entry fn."
  [& args]
  (doseq [a args]
    (println a)))
