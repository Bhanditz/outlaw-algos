;;;; Reward calculation
(defn calc-reward
  [reward-val new-reward num-arms]
  (+ (* (/ (dec num-arms) (float num-arms)) reward-val)
     (* (/ 1 (float num-arms)) new-reward)))

;;;; First version
;;; Interface to calc-reward
(defn inc-reward
  [algo arm reward]
  (calc-reward ((algo 2) arm)
               reward
               (count (algo 2))))

(defn make-epsilon-greedy ; create three element vector
  [epsilon num-arms] ; ex.: [0.1 [0 0 0] [0.0 0.0 0.0]]
  [epsilon ; idx=0 -> probability to explore
   (vec (repeat num-arms 0)) ; idx=1 -> arms played
   (vec (repeat num-arms 0.0))]) ; idx=2 -> rewards received

(defn select-arm
  [algo]
  (if (> (rand) (algo 0));epsilon
    (.indexOf (algo 2) (apply max (algo 2))) ; most-rewarded arm
    (rand-int (count (algo 1))))) ; random arm

(defn update ; return new vector with updated values
  [algo arm reward] 
  [(algo 0) ; [epsilon, arm[x]+=1, reward[x]=recalc(reward,x)]
   (assoc (algo 1) arm (inc ((algo 1) arm)))
   (assoc (algo 2) arm (calc-reward algo arm reward))])

