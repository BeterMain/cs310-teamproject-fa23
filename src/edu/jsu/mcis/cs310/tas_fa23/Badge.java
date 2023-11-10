package edu.jsu.mcis.cs310.tas_fa23;
import java.util.zip.CRC32;
public class Badge {

    private final String id, description;

    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    public Badge(String description){
        this.description = description;
        
        CRC32 crc32 = new CRC32();
        
        crc32.update(description.getBytes());
        
        String id2 = Long.toHexString(crc32.getValue()).toUpperCase();
        
        if (id2.length() < 8){
            
            for (int i = id2.length(); i < 8; i++){
                
                id2 = "0" + id2; 
            }
        }
        
        this.id = id2;
    
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');

        return s.toString();

    }

}
