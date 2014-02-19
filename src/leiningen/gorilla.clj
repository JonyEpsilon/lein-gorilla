;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

;;; Packages gorilla as a leiningen plugin.

(ns leiningen.gorilla
  (:require [gorilla-repl.core :as g]
            [leiningen.core.eval :as eval]
            [clojure.pprint :as pp]))

;; This is the leiningen task. It needs no arguments, and can run outside a project (assuming you've got the plugin
;; installed in your profile). You can pass the arguments:
;; - :worksheet path/to/worksheet  --  loads the indicated worksheet in a new gorilla instance
(defn ^:no-project-needed gorilla
  [project & opts]
  (let [opts-map (apply hash-map opts)
        port (read-string (or (get opts-map ":port") "8990"))
        curr-deps (or (:dependencies project) [])
        new-deps (conj curr-deps '[gorilla-repl/gorilla-repl "0.1.0-SNAPSHOT"])
        prj (assoc project :dependencies new-deps)]
    (when-let [w (get opts-map ":worksheet")] ())
    (eval/eval-in-project
      prj
      `(g/run-gorilla-server {:port ~port})
      '(require 'gorilla-repl.core))))
