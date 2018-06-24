/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpe.core.database;

import br.jpe.core.utils.JsonX;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Json Connection
 *
 * @author joaovperin
 */
public class JsonConnection implements Connection {

    /** Instantiated control */
    private static AtomicBoolean instantiated;

    /** StringBuffer Initial Size */
    private static final int SB_SIZE = 8096;

    /** Database Name */
    private String databaseName;
    /** Database */
    private Object databaseModel;

    /**
     * Constructor
     */
    public JsonConnection() {
        // If it's already instantiated, throws an exception
        if (instantiated != null || instantiated.get()) {
            throw new UnsupportedOperationException("Cannot be instantiated twice! Must be a Singleton");
        }
        instantiated = new AtomicBoolean(true);
    }

    /**
     * Returns the database model
     *
     * @return DataBaseModel
     * @throws DBException Failed to open Database
     */
    public Object get() throws DBException {
        if (databaseModel == null) {
            System.out.println("*** Openning DATABAS!!!");
            open();
        }
        return databaseModel;
    }

    /**
     * Set the database name
     *
     * @param databaseName
     */
    public final void setDatabaseName(String databaseName) {
        if (databaseModel != null) {
            close();
        }
        this.databaseName = databaseName;
    }

    /**
     * Flush all the data to the database
     *
     * @throws br.jpe.core.database.DBException
     */
    @Override
    public final void commit() throws DBException {
        // Nothing to flush
        if (databaseModel == null) {
            System.out.println("*** Nothing to commit");
            return;
        }
        // Renames the Database to a temporary file as a backup
        File newDatabase = new File(this.databaseName);
        File tmpFile = new File(this.databaseName + ".tmp");
        newDatabase.renameTo(tmpFile);
        // Tries to store the In-Memory Database with the old name
        String data = JsonX.toFormattedJson(databaseModel);
        try (OutputStream ou = new FileOutputStream(newDatabase)) {
            // Create a new file and writes to it
            ou.write(data.getBytes());
            ou.flush();
            // If success, deletes the TMP file
            tmpFile.delete();
        } catch (IOException e) {
            throw new DBException("*** Failed to flush data to database file " + this.databaseName, e);
        }
    }

    /**
     * Rollsback all the changes
     *
     * @throws DBException
     */
    @Override
    public void rollback() throws DBException {
        System.out.println("*** Rolling back database " + this.databaseName);
        open();
    }

    /**
     * Closes the database
     */
    @Override
    public final void close() {
        System.out.println("*** Closing database " + this.databaseName);
        this.databaseModel = null;
    }

    /**
     * Opens the database
     *
     * @throws DBException Failed to open Database
     */
    private void open() throws DBException {
        if (this.databaseModel != null) {
            throw new DBException("Database Already Openned!");
        }
        if (this.databaseName == null || this.databaseName.trim().isEmpty()) {
            throw new DBException("Invalid Database Name: " + this.databaseName);
        }
        // Opens the Database
        StringBuilder sb = new StringBuilder(SB_SIZE);
        try (BufferedReader bf = new BufferedReader(new FileReader(this.databaseName))) {
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new DBException("Failed to Open Database!", e);
        }
        // NPE protection
        String db = sb.toString();
        if (db == null || db.trim().isEmpty()) {
            db = "{}";
        }
        // Casts the string read to the class
        this.databaseModel = JsonX.castTo(db, databaseModel.getClass());
    }

}
