(ns komponentit.autosize
  (:require [reagent.core :as r]))

;; Idea: https://github.com/JedWatson/react-input-autosize/blob/master/src/AutosizeInput.js

(def sizer-style {:position "absolute"
                  :top 0
                  :left 0
                  :visiblity "hidden"
                  :height 0
                  :overflow "scroll"
                  :white-space "pre"})

(defn autosize [{:keys [min-width placeholder-is-min-width?]
                 :or {min-width 1}}]
  (let [width (r/atom min-width)
        ;; ref callback function should not change between renders
        placeholder-sizer-el (atom nil)
        placeholder-sizer-el-ref #(reset! placeholder-sizer-el %)
        sizer-el (atom nil)
        sizer-el-ref #(reset! sizer-el %)]
    (r/create-class
      {:display-name "komponentit.autosize.autosize"
       :component-did-mount
       (fn [this]
         (let [{:keys [value]} (r/props this)
               sizer-width (.-scrollWidth @sizer-el)
               placeholder-sizer-width (if (or placeholder-is-min-width? (empty? value))
                                         (if-let [el @placeholder-sizer-el]
                                           (.-scrollWidth el)))]
           (reset! width (max (+ (max sizer-width placeholder-sizer-width) 2) min-width))))
       :component-did-update
       (fn [this]
         (let [{:keys [value]} (r/props this)
               sizer-width (.-scrollWidth @sizer-el)
               placeholder-sizer-width (if (or placeholder-is-min-width? (empty? value))
                                         (if-let [el @placeholder-sizer-el]
                                           (.-scrollWidth el)))]
           (reset! width (max (+ (max sizer-width placeholder-sizer-width) 2) min-width))))
       :reagent-render
       (fn [{:keys [class style input-class input-style value placeholder] :as props}]
         [:div
          {:class class
           :style (merge {:display "inline-block"}
                         style)}
          [:input
           (-> props
               (assoc :style input-style :class input-class)
               (dissoc :min-width :placeholder-is-min-width? :input-class)
               ;; Because padding is not applied to sizers,
               ;; width shouldn't take padding into account
               (update :style #(merge {:box-sizing "content-box"} %))
               (assoc-in [:style :width] @width))]
          [:span
           {:style sizer-style
            :ref sizer-el-ref}
           value]
          (if placeholder
            [:span
             {:style sizer-style
              :ref placeholder-sizer-el-ref}
             placeholder])])})))
