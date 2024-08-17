package win.arora.vishal.edf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class TAL {
    private String duration;
    private String onSet;
    private String annotations;
}
