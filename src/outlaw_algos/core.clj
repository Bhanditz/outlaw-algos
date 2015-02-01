(ns outlaw-algos.core
  (:require [clojure.string :as string :only [join]]
            [clojure.java.io :as io])
  (:gen-class
   :name outlawalgos.MultiarmedBandit
   :init init
   :state state
   :constructors {[double int] []}
   :methods [[selectArm [] int]
             [updateReward [int double] void]]))

(defn calc-reward
  [reward-val new-reward num-arms]
  (+ (* (/ (dec num-arms) (float num-arms)) reward-val)
     (* (/ 1 (float num-arms)) new-reward)))

(defprotocol BanditAlgorithm ; interface with two functions 
  (select-arm [this])
  (update [this arm reward]))

(defrecord EpsilonGreedy ; exampl: {:epsilon 0.1 :arms-played: [0 0 0]..}
    [epsilon arms-played rewards-received]
  BanditAlgorithm ; implements protocol ("interface")
  (select-arm [this] ; implements select-arm
    (if (> (rand) (:epsilon this)) ; shall we exploit or explore?
      (let [highest-reward (apply max (:rewards-received this))]
        (rand-nth ; exploit -> random choice...
         (remove nil? ; ... of the arms with the highest reward
                 (map-indexed (fn [idx val]
                                (when (= val highest-reward)
                                  idx)) (:rewards-received this)))))
      (rand-int (count (:arms-played this))))); explore-> random arm choice
  (update [this arm reward] ; implements update
    (let [n (inc (count (:arms-played this)))]
      (-> this
          (update-in [:arms-played arm] inc) ; increment arm
          (update-in [:rewards-received arm] ; recalc arm reward
                     calc-reward reward n)))))

(defn make-epsilon-greedy
  [epsilon num-arms]
  (EpsilonGreedy. epsilon
                   (vec (repeat num-arms 0))
                   (vec (repeat num-arms 0))))

;;; API for thread-safe Java access and side effects

(defn -init
  [epsilon num-arms]
  [[] (agent (make-epsilon-greedy epsilon num-arms))])

(defn -selectArm
  [this]
  (select-arm @(.state this)))

(defn -updateReward
  [this arm reward]
  (send (.state this) update arm reward))

(defn -toString
  [this]
  (let [eg @(.state this)]
    (str "epsilon: "(:epsilon eg)
         "arms-played: " (:arms-played eg)
         "rewards-received: "(:rewards-received eg))))

