/**
 * Copyright (C) 2013 Premium Minds.
 *
 * This file is part of billy portugal (PT Pack).
 *
 * billy portugal (PT Pack) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * billy portugal (PT Pack) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy portugal (PT Pack). If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.portugal.migration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostgresBackup {

	public PostgresBackup() {
	}

	private void backup() {

		final List<String> baseCmds = new ArrayList<String>();
		baseCmds.add("/usr/bin/pg_dump");
		baseCmds.add("-h");
		baseCmds.add("localhost");
		baseCmds.add("-p");
		baseCmds.add("5432");
		baseCmds.add("-U");
		baseCmds.add("billy");
		baseCmds.add("-b");
		baseCmds.add("-v");
		baseCmds.add("-o");
		baseCmds.add("-f");
		baseCmds.add("/tmp/backup.sql");
		baseCmds.add("billy");
		final ProcessBuilder pb = new ProcessBuilder(baseCmds);

		final Map<String, String> env = pb.environment();
		env.put("PGPASSWORD", "billy");

		try {
			final Process process = pb.start();

			final BufferedReader r = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			String line = r.readLine();
			while (line != null) {
				System.err.println(line);
				line = r.readLine();
			}
			r.close();

			process.waitFor();
			System.out.println("Backup Terminated");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void restore() {

		final List<String> baseCmds = new ArrayList<String>();
		baseCmds.add("/usr/bin/psql");
		baseCmds.add("-h");
		baseCmds.add("localhost");
		baseCmds.add("-p");
		baseCmds.add("5432");
		baseCmds.add("-U");
		baseCmds.add("billy");
		baseCmds.add("-f");
		baseCmds.add("/tmp/backup.sql");
		baseCmds.add("billy");
		final ProcessBuilder pb = new ProcessBuilder(baseCmds);

		final Map<String, String> env = pb.environment();
		env.put("PGPASSWORD", "billy");

		try {
			final Process process = pb.start();

			final BufferedReader r = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			String line = r.readLine();
			while (line != null) {
				System.err.println(line);
				line = r.readLine();
			}
			r.close();

			process.waitFor();
			System.out.println("Restore Terminated");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		Backup b = new Backup();
//		 b.backup();
//		b.restore();
//	}

}
