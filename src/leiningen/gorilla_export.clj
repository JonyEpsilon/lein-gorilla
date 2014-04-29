;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.


(ns leiningen.gorilla-export
  (:require [gorilla-repl.core :as g]
            [leiningen.core.eval :as eval]
            [clojure.pprint :as pp]))

(defn ^:no-project-needed gorilla-export
  [project & opts]
  (let [opts-map (apply hash-map opts)
        file (get opts-map ":file")]
    (g/run-gorilla-server {})
    (println file)))