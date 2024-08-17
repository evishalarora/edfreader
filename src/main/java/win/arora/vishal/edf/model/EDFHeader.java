package win.arora.vishal.edf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EDFHeader {
    String formatVersion = null;
    String idCode = null;
    String localPatientID = null;
    String localRecordingID = null;
    String startDate = null;
    String startTime = null;
    Integer bytesInHeader = 0;

    Integer numberOfRecords = 0;
    Double durationOfRecords = 0.0;
    Integer numberOfSignals = 0;
    List<String> signalLabels = null;
    List<String> transducerTypes = null;
    List<String> physicalDimensions = null;
    List<Double> physicalMin = null;
    List<Double> physicalMax = null;
    List<Integer> digitalMin = null;
    List<Integer> digitalMax = null;
    List<String> preFilterings = null;
    List<Integer> sampleCounts = null;
    List<byte[]> reserves = null;
}
