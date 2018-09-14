(ns hal.test-runner
  (:require [doo.runner :as doo :include-macros true]
            [hal.core-test]))

(enable-console-print!)

(doo/doo-tests 'hal.core-test)
