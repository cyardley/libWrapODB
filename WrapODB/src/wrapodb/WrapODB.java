package wrapodb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class WrapODB {

	private String dbname;
	private String path;
	private String sqldir;
	
	
	public WrapODB(String p, String n, String s){
		new File(p).mkdir();
		new File(p+"/"+s).mkdir();
		path = p;
		dbname = n;
		sqldir = s;
	}
	
	
	public boolean UnwrapDB(){
		if(isDBLocked()){
			System.out.println("Can't expose a locked Database!");
			return false;
		}
		//Unzip the DB
		ZipFile zip = null;
		try {
			zip = new ZipFile(path + "/" + dbname);
		} catch (IOException e) {
			return false;
		}
		//Extract DB files
		extractEntry(zip, "data");
		extractEntry(zip, "backup");
		extractEntry(zip, "script");
		extractEntry(zip, "properties");
		try {
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		lockDB();
		return true;
		
	}
	
	public boolean WrapDB(){
		ZipFile zip = null;
		try {
			zip = new ZipFile(path + "/" + dbname);
		} catch (IOException e) {
			return false;
		}
		ZipOutputStream out = null;
        try {
			out = new ZipOutputStream(new FileOutputStream(path + "/out.odb"));
		} catch (FileNotFoundException e1) {
			try {zip.close();} catch (IOException e2) {e2.printStackTrace();}
			return false;
		}
        Enumeration<? extends ZipEntry> e = zip.entries();
        while(e.hasMoreElements()){
			try {
				ZipEntry ze = (ZipEntry) e.nextElement();
				byte[] b = null;
				if(ze.getName().equals("database/data") || ze.getName().equals("database/backup") || 
				   ze.getName().equals("database/properties") || ze.getName().equals("database/script")){
					File f = null;
					if(ze.getName().equals("database/data")) f = new File(path + "/" + sqldir + "/db.data");
					if(ze.getName().equals("database/backup")) f = new File(path + "/" + sqldir + "/db.backup");
					if(ze.getName().equals("database/properties")) f = new File(path + "/" + sqldir + "/db.properties");
					if(ze.getName().equals("database/script")) f = new File(path + "/" + sqldir + "/db.script");

					FileInputStream rf = new FileInputStream(f);
					b = new byte[(int)f.length()];
					rf.read(b, 0, (int)(int)f.length());
					rf.close();
				}else{
				  InputStream rf = zip.getInputStream(ze);
				  b = new byte[(int)(ze.getSize())];
				  rf.read(b, 0, (int)(ze.getSize()));
				  rf.close();
				}
				ZipEntry r = new ZipEntry(ze.getName());
				out.putNextEntry(r);
				out.write(b);
				out.closeEntry();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
        try {
			out.close();
		} catch (IOException e1) {
			try {zip.close();} catch (IOException e2) {e2.printStackTrace();}
			return false;
		}
		try {zip.close();} catch (IOException e2) {e2.printStackTrace();}

		ZipFile zo = null;
		try {
			zo = new ZipFile(path + "/out.odb");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {zo.close();} catch (IOException e2) {e2.printStackTrace();}
		unlockDB();
		return true;
	}
	
	private void lockDB(){
		File file = new File(path + "/" + dbname + ".lck");
		try {
		    file.getParentFile().mkdirs();
		    file.createNewFile();
		}catch(Exception e){ }
	}
	
	private void unlockDB(){
		File file = new File(path + "/" + dbname + ".lck");
		file.delete();
	}
	
	private boolean isDBLocked(){
		File file = new File(path + "/" + dbname + ".lck");
		return file.exists();
	}
	
	private void extractEntry(ZipFile zip, String d){
		ZipEntry e = zip.getEntry("database/" + d);
		try {
			copyInputStream(zip.getInputStream(e),
			           new BufferedOutputStream(new FileOutputStream(path + "/"+ sqldir + "/db." + d)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	private static final void copyInputStream(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int len;
		while((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
		in.close();
		out.close();
	}
		
}
