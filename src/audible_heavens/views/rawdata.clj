(ns audible_heavens.views.rawdata
  (:require [hiccup.core :refer :all]
            [audible_heavens.global :as global]
            [audible_heavens.views :as view]
            [audible_heavens.data :as data]))

(defn sort-buttons []
  [:h4 "Sort By"]
  [:div#sorts.button-group
    [:button.btn.btn-default.btn-primary {:data-sort-by "original-order"} "original order"]
    [:button.btn.btn-default {:data-sort-by "name"} "name"]
    ; [:button.btn.btn-default {:data-sort-by "coords"} "Coords"]
    [:button.btn.btn-default {:data-sort-by "distance"} "distance"]
    [:button.btn.btn-default {:data-sort-by "luminosity"} "luminosity"]
    [:button.btn.btn-default {:data-sort-by "color"} "color"]
    [:button.btn.btn-default {:data-sort-by "speed"} "speed"]
    [:button.btn.btn-default {:data-sort-by "absmag"} "abs mag"]
    [:button.btn.btn-default {:data-sort-by "appmag"} "app mag"]])

(defn star-item [value label norm]
  [:div {:data-sound norm} 
    [:i.clickable.glyphicon.glyphicon-music] 
    "&nbsp;&nbsp;" 
    [:span.text-info (str label ": ")] 
    [:span.text-success value]])

(defn star-row [star thresholds]
  [:div.isotope-star-item.well
    [:h4.name.text-primary.strong (get star :label)]
    [:small.coords.text-warning (str "(" (get star :x) ", " (get star :y) ", " (get star :z) ")" )]
    [:hr]
    (for [attrib data/star-attributes
      :let [k (key attrib)
            v (get star k)
            lbl (val attrib)
            thres (get thresholds k)
            max (get thres :max)
            min (get thres :min)
            norm (data/normalize-value v max min)]]
        (star-item v lbl norm))])

(defn display-data [stars thresholds]
  (map #(star-row % thresholds) (sort-by :id stars)))

(defn index [stars thresholds]
  (html
    [:html
      (view/common-head)
      [:body
        (view/nav-bar)
        [:div.container
          (view/breadcrumbs [["home" "/"] ["all stars" "/allstars"]])
          (view/page-header "All Stars" (str "showing data for " (count stars) " stars"))
          (sort-buttons)
          [:br]
          [:div#star-data.isotope-stars
            (display-data stars thresholds)]]
        (view/common-footer)]]))