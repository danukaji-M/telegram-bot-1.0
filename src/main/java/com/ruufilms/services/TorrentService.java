package com.ruufilms.services;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.alerts.AddTorrentAlert;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.BlockFinishedAlert;
import com.ruufilms.bot.FilmBot;

import java.io.File;
import java.sql.SQLOutput;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class TorrentService {
    public TorrentService(String filepath,String destination) throws Throwable{
        File file = new File(filepath);
        System.out.println("Using libtorrent version: "+ LibTorrent.version());
        final SessionManager s = new SessionManager();
        final CountDownLatch signal = new CountDownLatch(1);

        s.addListener(new AlertListener() {
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();
                switch (type){
                    case ADD_TORRENT:
                        System.out.println("Torrent Added");
                        ((AddTorrentAlert)alert).handle().resume();
                        break;
                    case BLOCK_FINISHED:
                        BlockFinishedAlert a = (BlockFinishedAlert) alert;
                        int p = (int) (a.handle().status().progress() * 100);
                        System.out.println("Progress "+p+"for torrent name: "+ a.torrentName());
                        System.out.println(s.stats().totalDownload());
                        break;
                    case TORRENT_FINISHED:
                        System.out.println("Torrent Finished");
                        signal.countDown();
                        break;
                }
            }
        });
        System.out.println("Starting torrent download...");
        s.start();
        TorrentInfo ti = new TorrentInfo(file);
        File des = new File(destination);
        System.out.println("Starting download...");
        s.download(ti, des);  // start download
        System.out.println("Waiting for completion...");
        signal.await(120, TimeUnit.SECONDS);
        System.out.println("Download completed.");
        s.stop();
    }
}
