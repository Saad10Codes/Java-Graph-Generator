/*
 * Galaxy
 * Copyright (C) 2012 Parallel Universe Software Co.
 * 
 * This file is part of Galaxy.
 *
 * Galaxy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * Galaxy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with Galaxy. If not, see <http://www.gnu.org/licenses/>.
 */
package co.paralleluniverse.galaxy.core;

import co.paralleluniverse.common.monitoring.PeriodicMonitor;
import co.paralleluniverse.galaxy.monitoring.BackupMXBean;
import co.paralleluniverse.galaxy.monitoring.Counter;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author pron
 */
public class JMXBackupMonitor extends PeriodicMonitor implements BackupMonitor, BackupMXBean {
    private final Counter replicationBackupsCounter = new Counter();
    private final Counter backupsCounter = new Counter();
    private final Counter backupPacketsCounter = new Counter();
    private final Counter slavesAckTimeCounter = new Counter();
    private final Counter serverAckTimeCounter = new Counter();
    private int replicationBackups;
    private int backups;
    private int backupPackets;
    private long avgSlavesAckTimeMicros;
    private long avgServerAckTimeMicros;

    public JMXBackupMonitor(String name) {
        super(BackupMXBean.class, "co.paralleluniverse.galaxy.core:type=Backup");
    }

    @Override
    protected void collectAndResetCounters() {
        replicationBackups = (int) replicationBackupsCounter.get();
        backupPackets = (int) backupPacketsCounter.get();
        backups = (int) backupsCounter.get();

        final long microsSinceLastCollect = TimeUnit.MICROSECONDS.convert(getMillisSinceLastCollect(), TimeUnit.MILLISECONDS);
        avgSlavesAckTimeMicros = slavesAckTimeCounter.get() / microsSinceLastCollect;
        avgServerAckTimeMicros = serverAckTimeCounter.get() / microsSinceLastCollect;
        resetCounters();
    }

    @Override
    protected void resetCounters() {
        replicationBackupsCounter.reset();
        backupsCounter.reset();
        backupPacketsCounter.reset();
        slavesAckTimeCounter.reset();
        serverAckTimeCounter.reset();
    }

    @Override
    public void addReplicationBackup(int num) {
        replicationBackupsCounter.add(num);
    }

    @Override
    public void addBackups(int num) {
        backupsCounter.add(num);
    }

    @Override
    public void addBackupPacket() {
        backupPacketsCounter.inc();
    }

    @Override
    public void addSlaveAckTime(long nanos) {
        slavesAckTimeCounter.add(TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS));
    }

    @Override
    public void addServerAckTime(long nanos) {
        serverAckTimeCounter.add(TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS));
    }

    @Override
    public long getAvgServerAckTimeMicros() {
        return avgServerAckTimeMicros;
    }

    @Override
    public long getAvgSlavesAckTimeMicros() {
        return avgSlavesAckTimeMicros;
    }

    @Override
    public int getBackups() {
        return backups;
    }

    @Override
    public int getBackupPackets() {
        return backupPackets;
    }

    @Override
    public int getReplicationBackups() {
        return replicationBackups;
    }
}
