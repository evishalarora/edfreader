package win.arora.vishal.edf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnusedEDFDataRecord {
    List<Double> unitsInDigit;
    List<List<Short>> digitalValues;
    List<List<Double>> valuesInUnits;
}
