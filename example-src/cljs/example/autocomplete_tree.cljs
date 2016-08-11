(ns example.autocomplete-tree
  (:require [komponentit.autocomplete :as autocomplete]
            [reagent.core :as r]
            [devcards.core :as dc :include-macros true]
            [clojure.string :as str]))

(dc/defcard-rg tree-autocomplete
  (fn [value _]
    [autocomplete/autocomplete
     {:value @value
      :cb (fn [item] (reset! value (:id item)))
      :search-fields [:name]
      :item->key :id
      :item->text (fn [item]
                    (str (::autocomplete/i item) " " (:name item)))
      :value->text (fn [_ x] (str x))
      ;; Enable tree
      :item->items :items
      :items [{:id 1
               :name "Foo"
               :items [{:id 2
                        :name "Bar"
                        :items [{:id 3
                                 :name "System Module"
                                 :price 1380}
                                {:id 4
                                 :name "ABC"
                                 :price 1340}]}
                       {:id 5
                        :name "Ooooo"
                        :items [{:id 6
                                 :name "asdasd"
                                 :price 9000}]}]}
              {:id 7
               :name "Bar"
               :items [{:id 8
                        :name "A"
                        :items [{:id 9
                                 :name "Foo"
                                 :price 50000}]}]}
              {:id 10
               :name "Lorem ipsum"
               :price 90}]
      :clearable? true}])
  (r/atom 5)
  {:inspect-data true})
