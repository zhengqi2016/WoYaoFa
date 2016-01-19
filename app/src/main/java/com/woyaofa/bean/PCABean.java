package com.woyaofa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LoaR on 15/11/26.
 */
public class PCABean implements Serializable {

    public List<P> ps;

    public List<P> getPs() {
        return ps;
    }

    public void setPs(List<P> ps) {
        this.ps = ps;
    }

    public static class P implements Serializable{
        public String name;
        public List<C> cs;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<C> getCs() {
            return cs;
        }

        public void setCs(List<C> cs) {
            this.cs = cs;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class C implements Serializable{
        public String name;
        public List<A> as;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<A> getAs() {
            return as;
        }

        public void setAs(List<A> as) {
            this.as = as;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class A implements Serializable{
        public String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
