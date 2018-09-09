package com.clbrain.kirizhik.epidetect;

public class User {
    String uid,name,num,parnum,dob,email,sex;

        public User() {
        }

        public User(String uid, String name, String num, String parnum, String dob, String email,String sex) {
            this.uid = uid;
            this.name = name;
            this.num = num;
            this.parnum = parnum;
            this.dob = dob;
            this.email = email;
            this.sex = sex;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getParnum() {
            return parnum;
        }

        public void setParnum(String parnum) {
            this.parnum = parnum;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }}

