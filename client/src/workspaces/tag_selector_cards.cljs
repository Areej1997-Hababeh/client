(ns workspaces.tag-selector-cards
  (:require [nubank.workspaces.card-types.react :as ct.react]
            [nubank.workspaces.core :as ws]
            [reagent.core :as r]
            [wh.components.tag-selector.tag-selector :as tag-selector]))

(def tags [{:weight  0.029,
            :subtype "software",
            :type    "tech",
            :slug    "scala",
            :id      "22684f3a-c3cd-4566-b86b-01c28dde6803",
            :label   "Scala"}
           {:weight  0.028,
            :subtype "software",
            :type    "tech",
            :slug    "clojure",
            :id      "c25968be-79aa-4c2f-baea-75ad42ee057f",
            :label   "Clojure"}
           {:weight  0.027,
            :subtype "software",
            :type    "tech",
            :slug    "haskell",
            :id      "84f1319e-0a15-4f47-a3b2-f0d38554cc10",
            :label   "Haskell"}
           {:weight  0.026,
            :subtype "software",
            :type    "tech",
            :slug    "erlang",
            :id      "d95d2b05-ba08-4e3a-a032-b2e410fab166",
            :label   "ERLANG"}
           {:weight  0.025,
            :subtype "software",
            :type    "tech",
            :slug    "ocaml",
            :id      "63d18de9-099e-4565-a6cb-aa466089f289",
            :label   "OCAml"}
           {:weight  0.024,
            :subtype "software",
            :type    "tech",
            :slug    "clojurescript",
            :id      "940057c8-8d25-4ec5-af72-b7ec0ba77464",
            :label   "ClojureScript"}
           {:weight  0.023,
            :subtype "software",
            :type    "tech",
            :slug    "elixir",
            :id      "cfd0b5c5-cff6-4817-9653-9b4b695f1784",
            :label   "ELIXIR"}
           {:weight  0.022,
            :subtype "software",
            :type    "tech",
            :slug    "elm",
            :id      "1aebf8fd-7539-4f1d-802b-f20df0cf968f",
            :label   "ELM"}
           {:weight  0.021,
            :subtype "software",
            :type    "tech",
            :slug    "rust",
            :id      "63ae5f37-849c-4283-87c6-1c47f27e0595",
            :label   "RUST"}
           {:weight  0.02,
            :subtype "software",
            :type    "tech",
            :slug    "fsharp",
            :id      "286f3aae-701f-4efc-9094-6f2367207c50",
            :label   "F#"}
           {:weight  0.019,
            :subtype "software",
            :type    "tech",
            :slug    "python",
            :id      "2d754939-37e7-41bd-8ed0-1cc783c9f7e7",
            :label   "PYTHON"}
           {:weight  0.018,
            :subtype "software",
            :type    "tech",
            :slug    "java",
            :id      "251d64f6-ebec-4cd2-8de4-3c210a221925",
            :label   "JAVA"}
           {:weight  0.017,
            :subtype "software",
            :type    "tech",
            :slug    "csharp",
            :id      "eeac47a6-0ce9-45b9-9659-d171c626a1f2",
            :label   "C#"}
           {:weight  0.016,
            :subtype "software",
            :type    "tech",
            :slug    "ruby",
            :id      "28788292-f387-4f51-931b-7c260546c65b",
            :label   "RUBY"}
           {:weight  0.015,
            :subtype "software",
            :type    "tech",
            :slug    "node-js",
            :id      "ebe14bbd-509e-4ae0-bd66-809c9ea6509a",
            :label   "Node.JS"}
           {:weight  0.014,
            :subtype "software",
            :type    "tech",
            :slug    "cplusplus",
            :id      "0b640f00-667d-4090-aef4-f318a2a49750",
            :label   "C++"}
           {:weight  0.013,
            :subtype "software",
            :type    "tech",
            :slug    "javascript",
            :id      "8b1a463a-d21e-4900-b8a6-88c2efc491f4",
            :label   "JAVASCRIPT"}])

(defn card []
  [:div {:style {:background "mistyrose"
                 :padding    "20px"
                 :width      "320px"}}
   [tag-selector/card-with-selector tags false {} :default]])

(ws/defcard tag-selector
  (ct.react/react-card
    (r/as-element (card))))

(defonce init (ws/mount))

