package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class Catogry_Model implements Serializable {
    private List<Categories> categories;

    public List<Categories> getCategories() {
        return categories;
    }

    public class Categories {
        private String main_category_fk;
        private String main_category_title;
        private List<sub> subs;

        public String getMain_category_fk() {
            return main_category_fk;
        }

        public String getMain_category_title() {
            return main_category_title;
        }

        public List<sub> getSubs() {
            return subs;
        }

        public class sub  implements Serializable{
            private String sub_category_fk;
            private String sub_category_title;
            private List<Sub> subs;

            public String getSub_category_fk() {
                return sub_category_fk;
            }

            public String getSub_category_title() {
                return sub_category_title;
            }

            public List<Sub> getSubs() {
                return subs;
            }

            public class Sub implements Serializable{
                private String model_id_fk;
                private String model_title;

                public String getModel_id_fk() {
                    return model_id_fk;
                }

                public String getModel_title() {
                    return model_title;
                }
            }


        }

    }
}