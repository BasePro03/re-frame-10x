(ns mranderson047.reagent.v0v6v0.reagent.debug
  (:refer-clojure :exclude [prn println time]))

(defmacro log
  "Print with console.log, if it exists."
  [& forms]
  `(when mranderson047.reagent.v0v6v0.reagent.debug.has-console
     (.log js/console ~@forms)))

(defmacro warn
  "Print with console.warn."
  [& forms]
  (when *assert*
    `(when mranderson047.reagent.v0v6v0.reagent.debug.has-console
       (.warn (if mranderson047.reagent.v0v6v0.reagent.debug.tracking
                mranderson047.reagent.v0v6v0.reagent.debug.track-console js/console)
              (str "Warning: " ~@forms)))))

(defmacro warn-unless
  [cond & forms]
  (when *assert*
    `(when (not ~cond)
       (warn ~@forms))))

(defmacro error
  "Print with console.error."
  [& forms]
  (when *assert*
    `(when mranderson047.reagent.v0v6v0.reagent.debug.has-console
       (.error (if mranderson047.reagent.v0v6v0.reagent.debug.tracking
                 mranderson047.reagent.v0v6v0.reagent.debug.track-console js/console)
               (str ~@forms)))))

(defmacro println
  "Print string with console.log"
  [& forms]
  `(log (str ~@forms)))

(defmacro prn
  "Like standard prn, but prints using console.log (so that we get
nice clickable links to source in modern browsers)."
  [& forms]
  `(log (pr-str ~@forms)))

(defmacro dbg
  "Useful debugging macro that prints the source and value of x,
as well as package name and line number. Returns x."
  [x]
  (let [ns (str cljs.analyzer/*cljs-ns*)]
    `(let [x# ~x]
       (println (str "dbg "
                     ~ns ":"
                     ~(:line (meta &form))
                     ": "
                     ~(pr-str x)
                     ": "
                     (pr-str x#)))
       x#)))

(defmacro dev?
  "True if assertions are enabled."
  []
  (if *assert* true false))

(defmacro time [& forms]
  (let [ns (str cljs.analyzer/*cljs-ns*)
        label (str ns ":" (:line (meta &form)))]
    `(let [label# ~label
           res# (do
                  (js/console.time label#)
                  ~@forms)]
       (js/console.timeEnd label#)
       res#)))
