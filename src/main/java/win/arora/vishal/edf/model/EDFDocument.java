package win.arora.vishal.edf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EDFDocument {
    EDFHeader header;
    List<EDFDataRecord> dataRecords;
    List<TAL> annotations;
}
