;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

;;; Packages gorilla as a leiningen plugin.

(ns leiningen.gorilla
  (:require [gorilla-repl.core :as g]
            [leiningen.core.eval :as eval]
            [clojure.pprint :as pp]))

;; The version of Gorilla that we will use
(def gorilla-version "0.3.1-SNAPSHOT")

;; This is the leiningen task. It needs no arguments, and can run outside a project (assuming you've got the plugin
;; installed in your profile).
(defn gorilla
  [project & opts]
  (let [opts-map (apply hash-map opts)
        port (read-string (or (get opts-map ":port") "8990"))
        nrepl-port (read-string (or (get opts-map ":nrepl-port") "0"))
        ;; inject the gorilla-repl dependency into the target project
        curr-deps (or (:dependencies project) [])
        new-deps (conj curr-deps ['gorilla-repl/gorilla-repl gorilla-version])
        prj (assoc project :dependencies new-deps)
        project-name (:name project)]
    (eval/eval-in-project
      prj
      `(g/run-gorilla-server {:port ~port
                              :nrepl-port ~nrepl-port
                              :version ~gorilla-version
                              :project ~project-name})
      '(require 'gorilla-repl.core))))
