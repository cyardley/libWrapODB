libWrapODB
==========
(version ALPHA - not tested)

Casey Yardley

Java library to query the HSQLDB database in an Open Document Base (odb) file.

Included dependencies: hsqldb.jar

ODB files are save files used by the OpenOffice.org or LibreOffice Base programs. The default database is a Hyper SQL database (hsqldb), which is an all-java SQL database.

The purpose of this library is to allow java applications to query the HSQLDB inside of an ODB file. It accomplishes this by extracting the database files into a working folder, executing queries, and then updating the ODB file.

Currently any changes to the datbase are saved into "out.odb". Copy this file to replace the origional file to acheive final behaviour. This will be changed when testing is complete.

See TestWrapper.java for an example. If you want to run the test wrapper, update the path and database query in the file.

PUBLIC INTERFACE
==========

WrapODB.java
----------
wrapodb.WrapODB;

public WrapODB(String p, String n, String s)

	Creates a new WrapODB object.
	
	String p is the path to the working directory
	
	String n is the odb name (e.g. "myFile.odb")
	
	String s is the working directory for the hsqldb files (e.g. "hsqldb")

public boolean UnwrapDB()

	Extracts the database from the ODB file

public boolean WrapDB()

	Updates the database to the ODB file

WrapDatabase.java
----------
wrapodb.WrapDatabase;

public WrapDatabase(String p)

	Creates a new WrapDatabase object.
	
	String p is teh path to the database

public boolean OpenDB()

	Opens the database in the working directory

public void CloseDB()

	Closes the database in the working directory.

public ResultSet QueryDB(String sql)

	Executes the SQL query "String sql" and returns the results as a java.sql.ResultSet
