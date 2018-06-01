/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.dao.testing;

import java.util.Objects;

/**
 * A Bean classe created just for testing purposes.
 *
 * @author joaovperin
 */
public class TestBean {

    /**
     * Default constructor
     */
    public TestBean() {
    }

    /**
     * Key/Value pair constructor
     *
     * @param keyy
     * @param valuee
     */
    public TestBean(int keyy, String valuee) {
        this.keyy = keyy;
        this.valuee = valuee;
    }

    /** Key */
    public int keyy;
    /** Value */
    public String valuee;

    /**
     * Returns keyy
     *
     * @return int
     */
    public int getKeyy() {
        return keyy;
    }

    /**
     * Sets keyy
     *
     * @param keyy
     */
    public void setKeyy(int keyy) {
        this.keyy = keyy;
    }

    /**
     * Gets valuee
     *
     * @return String
     */
    public String getValuee() {
        return valuee;
    }

    /**
     * Sets valuee
     *
     * @param valuee
     */
    public void setValuee(String valuee) {
        this.valuee = valuee;
    }

    /**
     * Converts to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Test{" + "chave=" + keyy + ", valor=" + valuee + '}';
    }

    /**
     * Hashes this object
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.keyy;
        hash = 61 * hash + Objects.hashCode(this.valuee);
        return hash;
    }

    /**
     * Returns true if the 2 objects are equal
     *
     * @param anotherObject
     * @return boolean
     */
    @Override
    public boolean equals(Object anotherObject) {
        if (this == anotherObject) {
            return true;
        }
        if (anotherObject == null) {
            return false;
        }
        if (getClass() != anotherObject.getClass()) {
            return false;
        }
        final TestBean other = (TestBean) anotherObject;
        if (this.keyy != other.keyy) {
            return false;
        }
        return Objects.equals(this.valuee, other.valuee);
    }

}
