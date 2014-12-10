(defproject outpace-ex "0.1.0"
  :description "Outpace technical exercise"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.cli "0.3.1"]]
  :profiles {:dev {:resource-paths ["test_files"]}}
  :main outpace-ex.cmd
  :aot :all)
