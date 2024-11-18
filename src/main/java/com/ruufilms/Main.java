package com.ruufilms;

import com.ruufilms.migration.Migration;

public class Main {
    public static void main(String[] args) {


        Runnable migration = Migration::new;
        Thread thread = new Thread(migration);
        thread.start();

    }
}