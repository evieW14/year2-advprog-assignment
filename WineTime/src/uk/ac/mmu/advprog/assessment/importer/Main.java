package uk.ac.mmu.advprog.assessment.importer;

import java.io.File;
import java.sql.SQLException;

import uk.ac.mmu.advprog.assessment.shared.DBConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Importer starting...");
        DBConnection dbConnection = new DBConnection();
        DatabaseManager dbManager = new DatabaseManager(dbConnection);
        
		CsvImporter importer = new CsvImporter();
		try {
			importer.run(dbConnection.getConnection(), dbManager);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//                
//    	String line = """
//    			100001,Espumante Moscatel,Sparkling,Varietal/100%,['Muscat/Moscato'],"['Pork', 'Rich Fish', 'Shellfish']",7.5,Medium-bodied,High,BR,Brazil,1001,Serra Gaúcha,10001,Casa Perini,http://www.vinicolaperini.com.br,"[2020, 2019, 2018, 2017, 2016, 2015, 2014, 2013, 2012, 2011, 2010, 2009, 2008, 2007, 2006, 2005, 2003, 2002, 2001, 2000, 1999, 1998, 1997, 1996, 1995, 1993, 1992, 1991, 1990, 1989, 1988, 1985, 1982, 1980, 1970, 1966, 'N.V.']"
//    			""";
//    	
//    	
//    	String[] fields = CsvParser.parseCsvLine(line);
//		for (String field : fields) {
//			System.out.println(field);
//		}
//		
//		System.out.println(CsvParser.parseList(fields[5]));
//		
//		System.out.println(CsvParser.parseVintage(fields[16]));
//		
		System.out.println("Importer DB path = " + new File("data/winetime.db").getAbsolutePath());
				
    }
}
