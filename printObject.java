import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class printObject {
		// Print dash
	 	// +------+------+------+------+-----+
	    static void print_dashes (ResultSetMetaData rsmeta) throws SQLException
		{
			int i,j;
			int colsize;
			int numberOfColumns = rsmeta.getColumnCount();

			System.out.print("+");

			for (i =1;i <= numberOfColumns;i++){
				colsize =rsmeta.getColumnDisplaySize(i);
				for (j =0;j <colsize +4;j++)
						System.out.print('-');
				System.out.print('+');
			}
			System.out.println();
		}
	    
	 // Print headers using metadata
	 	static void print_headers (ResultSetMetaData rsmeta) throws SQLException
	 	{
	 		int i,j;
	 		int colsize;
	 		// How many cloumns
	 		int numberOfColumns = rsmeta.getColumnCount();
	 		String colName = null;

	 		// Print the column header
	 		// | Name1  | Name2  | ...  | NameN |
	 		// Metadata maintains the size and name of the columns
	 		System.out.print("|");
	 		for (i =1;i <= numberOfColumns;i++){
	 			colsize =rsmeta.getColumnDisplaySize(i);
	 			colName =rsmeta.getColumnName(i);
	 			System.out.print(colName);
	 			// If data is maller than column, we will add some spaces
	 			for(j=1;j<=colsize+4- colName.length();j++)
	 				System.out.print(" ");;
	 			System.out.print("|");

	 			//System.out.print('+');
	 		}
	 		System.out.println();
	 	}
	    
	 	// Print each row in the resultset
	 	// | data11  | data12 | data13 | .... | data1n
	 	// | data21  | data22 | data22 | .... | data2n
	 	static void print_datarows (ResultSetMetaData rsmeta, ResultSet rs) throws SQLException
	 	{
	 		int i,j;
	 		int colsize;
	 		int numberOfColumns = rsmeta.getColumnCount(); 		 	
	 		String strData = null;	 		
	 		
	 		while(rs.next()){  // Move the cursor	 		
	 			System.out.print("|");
	 			for (i =1;i <= numberOfColumns;i++)
	 			{
	 				colsize =rsmeta.getColumnDisplaySize(i);
	 				strData = rs.getString(i);
	 				if (rs.wasNull())  // NULL value check
	 				{
	 					strData = "NULL";
	 				}
	 				if(strData.length()> colsize+4) // Data is larger than the column size
	 					System.out.print(strData.substring(0, colsize+4));
	 				else
	 					System.out.print(strData);
	 				// If data is smaller than column, we will add some spaces
	 				for(j=1;j<=colsize+4- strData.length();j++)
	 					System.out.print(" ");
	 				System.out.print("|");
	 			}
	 			System.out.println();
	 		}
	 	}
	 	
}
