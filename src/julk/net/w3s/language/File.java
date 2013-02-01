/*  This file is part of Lilisp.
 *  Copyright (C) 2008  Julio Cesar Serrano Ortuno
 *  Copyright (C) 2008  Julio Cesar Serrano Ortuno
 * 
 *  Lilisp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Lilisp is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lilisp.  If not, see <http://www.gnu.org/licenses/>.
 */

package julk.net.w3s.language;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

public class File implements Iterable<Integer>, Iterator<Integer> {
	public String name;
	public enum Status {CLOSED, OPENREAD, OPENWRITE}
	public Status status;
	public BufferedReader bin;
	public BufferedWriter bout;
	public static final int[] crlf = {0x0d, 0x0a};
	
	public File(String filename) {
		name = filename;
		status = Status.CLOSED;
	}
	
	public void nop() {
		//This method is intended to test the validness of a object
	}
	
	public boolean exists() {
		java.io.File f = new java.io.File(name);
		return f.exists();
	}
	
	public BufferedReader openRead() throws Exception {
		bin = new BufferedReader(new FileReader(name),4096);
		status = Status.OPENREAD;
		return bin;
	}
	
	public BufferedWriter openWrite() throws Exception {
		bout = new BufferedWriter(new FileWriter(name),4096);
		status = Status.OPENWRITE;
		return bout;
	}
	
	public BufferedWriter openAppend() throws Exception {
		bout = new BufferedWriter(new FileWriter(name,true));
		status = Status.OPENWRITE;
		return bout;
	}

	public String readLine() throws Exception {
		if (status != Status.OPENREAD || bin == null)
			throw new Exception("File not ready to read");
		return bin.readLine();
	}

	public void writeLine(String line) throws Exception {
		if (status != Status.OPENWRITE || bout == null)
			throw new Exception("File not ready to write");
		bout.write(line+"\r\n");
		//bout.write(crlf[0]);
		//bout.write(crlf[1]);
	}

	public int read() throws Exception {
		if (status != Status.OPENREAD || bin == null)
			throw new Exception("File not ready to read");
		return bin.read();
	}

	public void write(int c) throws Exception {
		if (status != Status.OPENWRITE || bout == null)
			throw new Exception("File not ready to write");
		bout.write(c);
	}

	/*public int readBuff() throws Exception {
		if (status != Status.OPENREAD || bin == null)
			throw new Exception("File not ready to read");
		bin.r
		return bin.read();
	}

	public void writeBuff(int c) throws Exception {
		if (status != Status.OPENWRITE || bout == null)
			throw new Exception("File not ready to write");
		bout.write(c);
	}*/

	public void close() throws Exception {
		switch (status) {
		case OPENREAD:
			if (bin != null) bin.close();
			break;
		case OPENWRITE:
			if (bout != null) bout.close();
			break;
		case CLOSED:
			break;
		default:
			break;
		}
		status = Status.CLOSED;
	}
	
	public long skip(long n) throws Exception {
		if (status != Status.OPENREAD)
			throw new Exception("File not ready to skip");
		return bin.skip(n);
	}
	
	public boolean markSupported() throws Exception {
		if (status != Status.OPENREAD)
			throw new Exception("File not ready to skip");
		return bin.markSupported();
	}

	public void mark(int readAheadLimit) throws Exception {
		if (status != Status.OPENREAD)
			throw new Exception("File not ready to skip");
		bin.mark(readAheadLimit);
	}

	public void reset() throws Exception {
		if (status != Status.OPENREAD)
			throw new Exception("File not ready to skip");
		bin.reset();
	}

	public boolean hasNext() {
		if (status != Status.OPENREAD) {
			return false;
		}
		boolean rdy = false;
		try {
			rdy = bin.ready();
		} catch (Throwable t) {}
		return rdy;
	}

	public Integer next() {
		if (status != Status.OPENREAD) {
			return null;
		}
		int c = -1;
		try {
			c = bin.read();
		} catch (Throwable t) {}
		return c;
	}

	public void remove() {
	}

	public Iterator<Integer> iterator() {
		// TODO Auto-generated method stub
		return this;
	}
}
