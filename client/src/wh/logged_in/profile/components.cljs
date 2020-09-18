(ns wh.logged-in.profile.components
  (:require [clojure.string :as str]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [wh.components.signin-buttons :as signin-buttons]
            [wh.common.text :refer [pluralize]]
            [wh.components.activities.components :as activities-components]
            [wh.components.common :refer [link]]
            [wh.components.error.views :refer [error-box]]
            [wh.components.forms.views :refer [field-container
                                               labelled-checkbox
                                               multi-edit multiple-buttons
                                               select-field select-input
                                               text-field text-input
                                               avatar-field]]
            [wh.components.icons :as icons]
            [wh.components.issue :as issue-components]
            [wh.components.tag :as tag]
            [wh.logged-in.profile.subs :as subs]
            [wh.routes :as routes]
            [wh.subs :refer [<sub]]
            [wh.styles.profile :as styles]
            [wh.util :as util]))

(defn container [& elms]
  (into [:div {:class styles/container}] elms))

(defn meta-row [{:keys [text icon href new-tab? social-provider]}]
  [:a (cond-> {:class (util/mc styles/meta-row
                               [(= social-provider :stackoverflow) styles/meta-row--stackoverflow]
                               [(= social-provider :twitter) styles/meta-row--twitter])
               :href href}
              new-tab? (merge {:target "_blank"
                               :rel    "noopener"}))
   [icons/icon icon :class styles/meta-row__icon]
   [:span {:class styles/meta-row__description} text]])

(defn social-row [social-provider {:keys [display url] :as social} type]
  (let [public? (= type :public)]
    (if social
      [meta-row {:text     display
                 :icon     (name social-provider)
                 :href     url
                 :new-tab? true}]
      (when-not public?
        [meta-row {:text (str "Connect " (str/capitalize (name social-provider)))
                   :icon (name social-provider)
                   :href (signin-buttons/type->href social-provider)
                   :social-provider social-provider}]))))

(defn title [text]
  [:div {:class styles/title} text])

(defn small-link [{:keys [text href]}]
  [:a
   {:class (util/mc styles/button styles/button--small) :href href}
   text])

(defn underline-link [{:keys [text href]}]
  [:a
   {:class styles/underline-link :href href}
   text])

(defn small-button [opts text]
  [:button (merge (util/smc styles/button styles/button--small) opts) text])

(defn upload-button [{:keys [document uploading? on-change data-test]}]
  (if uploading?
    [small-button {:disabled true} "Uploading..."]
    [:label {:class (util/mc styles/button styles/button--small)
             :data-test data-test}
     [:input.visually-hidden {:type "file"
                              :name "avatar"
                              :on-change on-change}]
     [:span (str "Upload " document)]]))

(defn edit-link [{:keys [href text data-test type]
                  :or {text "Edit"}}]
  (let [small? (= type :small)]
    [:a {:href      href
         :data-test data-test
         :class     (util/mc styles/edit [small? styles/edit--small])}
     [icons/icon "edit" :class (when small? styles/edit__small-icon)] [:span.visually-hidden text]]))

(defn edit-profile [{:keys [type]}]
  (let [[user-route admin-route] (if (= type :private)
                                   [:profile-edit-private :candidate-edit-private]
                                   [:profile-edit-header :candidate-edit-header])
        href (if (= (<sub [:wh.pages.core/page]) :profile)
               (routes/path user-route)
               (routes/path admin-route))]
    [edit-link {:href href
                :data-test (if (= type :private) "edit-profile-private" "edit-profile")
                :text "Edit"}]))

(defn profile [user {:keys [github stackoverflow twitter] :as _socials} type]
  (let [public? (= type :public)
        {:keys [name image-url summary]} (util/strip-ns-from-map-keys user)]
    [:div (util/smc styles/section styles/section--profile)
     (when-not public? [edit-profile {:type :default}])
     [:div {:class styles/username} name]
     [:img {:src image-url
            :class styles/avatar}]
     (when summary [:div {:class styles/summary
                          :title summary} summary])
     [:hr {:class styles/separator}]
     [:div {:class styles/meta-rows}
      [social-row :github github type]
      [social-row :stackoverflow stackoverflow type]
      [social-row :twitter twitter type]]
     (when-not public?
       [:<>
        [:hr {:class styles/separator}]
        [:a {:data-pushy-ignore "true"
             :class styles/button
             :href (routes/path :logout)}
         "Logout"]])]))

(defn view-field
  ([label content]
   (view-field nil label content))
  ([data-test label content]
   [:div {:class styles/view-field
          :data-test data-test}
    [:label {:class styles/view-field__label} label]
    [:div {:class styles/view-field__content} content]]))

(defn section [& children]
  (into [:div {:class styles/section}] children))

(defn section-highlighted [& children]
  (into [:div (util/smc styles/section styles/section--highlighted)] children))

(defn section-buttons [& children]
  (into [:div {:class styles/section__buttons}] children))

(defn resource [{:keys [href text]}]
  [:a {:class styles/resource :href href :target "_blank" :rel "noopener"} text])

(defn content [& children]
  (into [:div {:class styles/content}] children))

(defn subtitle [text]
  [:div {:class styles/subtitle} text])

(defn top-tech [tag]
  (let [name (str/lower-case (:name tag))]
    [:div {:class styles/top-tech
           :title name}
     [icons/icon (str name "-tag") :class styles/top-tech__icon]
     [:div {:class styles/top-tech__label} name]]))

(defn skills-container [tags]
  (into [:div {:class styles/skills}] tags))

(defn meta-separator []
  [:span {:class styles/meta-separator} "•"])

(defn article-card [{:keys [id title formatted-creation-date reading-time upvote-count published editable?] :as article-data}]
  [:div {:class styles/article}
   (when editable? [edit-link {:href (routes/path :contribute-edit :params {:id id})
                               :type :small}])
   [:a {:class styles/article__title
        :href (routes/path :blog :params {:id id})} title]
   [:div {:class styles/article__meta}
    (when-not published [:<> [:span "not published"]
                             [meta-separator]])
    formatted-creation-date
    [meta-separator]
    reading-time " min read"
    [meta-separator]
    upvote-count " " (pluralize upvote-count "boost")]])

(defn issue-creator [{:keys [company]}]
  (let [img-src (:logo company)
        name (:name company)]
    [:div {:class styles/issue__creator}
     [:img {:src img-src
            :class styles/issue__creator-avatar}]
     [:span {:class styles/issue__creator-name} name]]))

(defn issue-meta-elm [{:keys [icon text]}]
  [:div {:class styles/issue__meta-elm}
   [icons/icon icon :class styles/issue__meta-icon] text])

(defn issue-meta [{:keys [repo compensation level] :as issue}]
  [:div {:class styles/issue__meta}
   [issue-meta-elm {:icon (str "issue-level-" (name level))
                    :text (-> level name str/capitalize)}]
   [:div {:class styles/issue__additional-meta}
    [activities-components/compensation-amount compensation]
    [activities-components/primary-language repo]]])

(defn issue-card [{:keys [id title company] :as issue}]
  [:div {:class styles/issue}
   [issue-creator {:company company}]
   [:a {:class styles/issue__title
        :href (routes/path :issue :params {:id id})} title]
   [issue-meta issue]])

;; skills ----------------------------------------------------------

(defn add-skills-cta []
  [:<>
   [:p "You haven't specified your skills yet"]
   [section-buttons
    [small-link {:href (if (= (<sub [:wh.pages.core/page]) :profile)
                         (routes/path :profile-edit-header)
                         (routes/path :candidate-edit-header))
                 :text "Specify skills"}]]])

(defn display-skills [user-skills]
  (let [top-skill (take 5 user-skills)
        other-skills (drop 5 user-skills)]
    [:<>
     [skills-container (for [skill top-skill]
                         [top-tech skill])]
     (when (seq other-skills)
       [:<>
        [subtitle "also likes to work with"]
        [tag/strs->tag-list :li (map :name other-skills) nil]])]))

(defn section-skills [user-skills type]
  (let [public? (= type :public)
        skills? (seq user-skills)]
    [section
     (when (and skills? (not public?))
       [edit-profile {:type :default}])
     [title "Top Skills"]
     (cond
       skills? [display-skills user-skills]
       (and (not skills?) public?) [:p "User haven't specified his skills yet. "]
       :else [add-skills-cta])]))

;; articles ----------------------------------------------------------

(defn section-articles [articles type]
  (let [public? (= type :public)
        message (if public? "User haven't written any articles yet. "
                            "You haven't written any articles yet.")]
    [section
     [title "Articles"]
     (if (seq articles)
       (for [article articles]
         ^{:key (:id article)}
         [article-card (assoc article :editable? (not public?))])
       [:p message
        (when public? [underline-link {:text "Browse all articles" :href (routes/path :learn)}])])
     (when-not public?
       [section-buttons
        [small-link {:text "Write article"
                     :href (routes/path :contribute)}]])]))

;; issues ----------------------------------------------------------

(defn section-issues [issues type]
  (let [public? (= type :public)
        message (if public? "User haven't started working on any issue yet. "
                            "You haven't started working on any issue yet")]
    [section
     [title "Open Source Issues"]
     (if (seq issues)
       (for [issue issues]
         ^{:key (:id issue)}
         [issue-card issue])
       [:p message
        (when public? [underline-link {:text "Browse all issues" :href (routes/path :issues)}])])
     (when-not public?
       [section-buttons
        [small-link {:text "Explore issues"
                     :href (routes/path :issues)}]])]))