(ns outlaw-algos.core-test
  (:require [clojure.string :as string :only [join]]
            [clojure.java.io :as io])
  (:use clojure.test
        outlaw-algos.core))

(defn bernoulli-arm
  [reward-probability]
  (fn []
    (if (> (rand) reward-probability)
      0.0
      1.0)))

(defn arm-generator
  [reward-probabilities]
  (map bernoulli-arm reward-probabilities))

(defn test-algorithm
  [algorithm reward-probs num-sims horizon]
  (let [arms (arm-generator reward-probs)
        draw-arm (fn [i]
                   ((nth arms i)))]
    (loop [algo algorithm
           n num-sims
           t horizon
           last-reward 0
           states []]
      (if (> n 0)
        (if (> t 0)
          (let [selected-arm (select-arm algo)
                arm-reward (draw-arm selected-arm)]
            (recur (update algo selected-arm arm-reward)
                   n
                   (dec t)
                   (+ last-reward arm-reward)
                   (conj states algo)))
          (recur algorithm
                 (dec n)
                 horizon
                 last-reward
                 states))
        states))))


(deftest test-make-epsilon-greedy
  (let [algo (make-epsilon-greedy 0.1 5)]
    (testing "Returns record with the correct elements"
      (is (= (:epsilon algo) 0.1))
      (is (= (count (:arms-played algo)) 5))
      (is (= (count (:rewards-received algo)) 5)))))

(deftest test-epsilon-greedy
  (testing "Runs epsilon-greedy"
    (let [result (test-algorithm
                  (make-epsilon-greedy 1.0 2) [0.0 1.0] 100 250)]
      (is (= 25000 (count result))))))

;; (deftest test-concurrent-epsilon-greedy
;;   (testing "Runs concurrent-epsilon-greedy"
;;     (let [result (test-algorithm
;;                   (make-concurrent-epsilon-greedy 1.0 2 "foo.log") [0.0 1.0] 1 5)]
;;       (is (= 5 (count result))))))