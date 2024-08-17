package win.arora.vishal.edf.model;

import lombok.Data;

import java.util.List;

@Data
public class EDFDataRecord {
    String label;
    List<Short> values;
    TAL annotation;

    public boolean isAnnotation() {
        return annotation != null && values == null;
    }
}
