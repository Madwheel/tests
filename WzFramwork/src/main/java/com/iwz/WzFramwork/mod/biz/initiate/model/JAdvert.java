package com.iwz.WzFramwork.mod.biz.initiate.model;


import com.iwz.WzFramwork.base.JBase;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/11/03
 */
public class JAdvert extends JBase {
    private AdData list;

    public JAdvert() {
        this.list = new AdData();
    }

    public AdData getList() {
        return list;
    }

    public void setList(AdData list) {
        this.list = list;
    }

    public class AdData {
        private String id;
        private String title;
        private String url;
        private String img_1080_1920;
        private String img_1080_1600;
        private String img_1125_2436;

        public AdData() {
            this.id = "";
            this.title = "";
            this.url = "";
            this.img_1080_1920 = "";
            this.img_1080_1600 = "";
            this.img_1125_2436 = "";
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url == null ? "" : url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg_1080_1920() {
            return img_1080_1920 == null ? "" : img_1080_1920;
        }

        public void setImg_1080_1920(String img_1080_1920) {
            this.img_1080_1920 = img_1080_1920;
        }

        public String getImg_1080_1600() {
            return img_1080_1600;
        }

        public void setImg_1080_1600(String img_1080_1600) {
            this.img_1080_1600 = img_1080_1600;
        }

        public String getImg_1125_2436() {
            return img_1125_2436;
        }

        public void setImg_1125_2436(String img_1125_2436) {
            this.img_1125_2436 = img_1125_2436;
        }
    }
}
