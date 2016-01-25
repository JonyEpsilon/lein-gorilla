;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

;;; Packages gorilla as a leiningen plugin.

(ns leiningen.gorilla
  (:require [gorilla-repl.core :as g]
            [leiningen.core.eval :as eval]
            [clojure.pprint :as pp]))

;; The version of Gorilla that we will use
(def gorilla-version "0.3.15")

;; This is the leiningen task. It needs no arguments, and can run outside a project (assuming you've got the plugin
;; installed in your profile).
(defn gorilla
  [project & opts]
  (let [opts-map (apply hash-map opts)
        port (read-string (or (get opts-map ":port") "0"))
        ip (or (get opts-map ":ip") "127.0.0.1")
        nrepl-port (read-string (or (get opts-map ":nrepl-port") "0"))
        ;; inject the gorilla-repl dependency into the target project
        curr-deps (or (:dependencies project) [])
        new-deps (conj curr-deps ['org.clojars.benfb/gorilla-repl gorilla-version])
        prj (assoc project :dependencies new-deps)
        project-name (:name project)
        gorilla-options (:gorilla-options project)]
    (eval/eval-in-project
      prj
      `(g/run-gorilla-server {:port ~port
                              :ip ~ip
                              :nrepl-port ~nrepl-port
                              :version ~gorilla-version
                              :project ~project-name
                              :gorilla-options ~gorilla-options})
      '(require 'gorilla-repl.core))))
