(ns outpace-ex.cmd
  (:require [outpace-ex.core :refer [main-processing]]
            [clojure.string :as s]
            [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
   "Specification of command line options. 
Just an optional -o or --output for the output file.
The input file will be taken as the one and only 
non-optional argument."
   [["-o" "--output OUTPUT" "File for output, STDOUT by default"]
    ["-h" "--help"]])

(def description
  (->> [""
        "A commandline implementation of the Outpace 'OCR' technical exercise."
        "Output will be written to the STDOUT by default."
        "Use the -o option to specify an output file."
        "One and only one input file must be given."
        ""]
       (s/join \newline)))
        

(defn usage [options-summary]
  (->> [""
        "Usage: program-name [options] input_file"
        ""
        "Options:"
        options-summary
        ""]
        (s/join \newline)))

(defn error-msg [errors]
  (str "The following errors occured parsing the command invocation:\n\n"
       (s/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))


(defn -main
  "Main execution entry fn."
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; handle help and error conditions
    (cond 
     (:help options) (do (println description)
                         (exit 0 (usage summary)))
     ;; must be 1 and only 1 inputfile
     (not= 1 (count arguments))
       (do (println "\nPlease specify one and only one inputfile.")
           (exit 1 (usage summary)))
     errors (exit 1 (error-msg errors)))
    ;; Execute with stdout or outputfile
    (let [infile (first arguments)]
      (if-let [outfile (:output options)]
        (main-processing infile outfile)
        (main-processing infile)))))
                 
