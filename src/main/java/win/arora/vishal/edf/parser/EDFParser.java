package win.arora.vishal.edf.parser;

import win.arora.vishal.edf.exception.EDFException;
import win.arora.vishal.edf.model.EDFDataRecord;
import win.arora.vishal.edf.model.EDFDocument;
import win.arora.vishal.edf.model.EDFHeader;
import win.arora.vishal.edf.model.TAL;
import win.arora.vishal.edf.utils.EDFInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static win.arora.vishal.edf.utils.Constants.*;

public class EDFParser {
    public static EDFDocument parseEDF(InputStream is) throws EDFException {
        EDFInputStream edfIS = new EDFInputStream(is);

        EDFHeader header = parseHeader(edfIS);
        List<EDFDataRecord> dataRecord = parseDataRecords(edfIS, header);
        return EDFDocument.builder()
                .header(header)
                .dataRecords(dataRecord.stream().filter(Predicate.not(EDFDataRecord::isAnnotation)).collect(Collectors.toList()))
                .annotations(dataRecord.stream().filter(EDFDataRecord::isAnnotation).map(EDFDataRecord::getAnnotation).collect(Collectors.toList()))
                .build();
    }

    public static EDFHeader parseHeader(EDFInputStream is) throws EDFException {
        try {
            String idCode = is.readASCII(SIZE_IDENTIFICATION_CODE);
            if (!idCode.equals("0")) {
                throw new EDFException("Invalid EDF File");
            }
            EDFHeader.EDFHeaderBuilder headerBuilder = EDFHeader.builder();
            headerBuilder
                    .idCode(idCode)
                    .localPatientID(is.readASCII(SIZE_LOCAL_PATIENT_IDENTIFICATION))
                    .localRecordingID(is.readASCII(SIZE_LOCAL_RECORDING_IDENTIFICATION))
                    .startDate(is.readASCII(SIZE_START_DATE))
                    .startTime(is.readASCII(SIZE_START_TIME))
                    .bytesInHeader(is.readInt(SIZE_HEADER))
                    .formatVersion(is.readASCII(SIZE_FORMAT_VERSION))
                    .numberOfRecords(is.readInt(SIZE_NUMBER_OF_DATA_RECORDS))
                    .durationOfRecords(is.readDouble(SIZE_DURATION_DATA_RECORDS));
            Integer numberOfSignals = is.readInt(SIZE_NUMBER_OF_SIGNALS);

            return headerBuilder
                    .numberOfSignals(numberOfSignals)

                    //parse channel information
                    .signalLabels(is.readMultiASCII(SIZE_SIGNAL_LABELS, numberOfSignals))
                    .transducerTypes(is.readMultiASCII(SIZE_TRANSDUCER_TYPE, numberOfSignals))
                    .physicalDimensions(is.readMultiASCII(SIZE_PHYSICAL_DIMENSION, numberOfSignals))
                    .physicalMin(is.readMultiDouble(SIZE_PHYSICAL_MIN_IN_UNITS, numberOfSignals))
                    .physicalMax(is.readMultiDouble(SIZE_PHYSICAL_MAX_IN_UNITS, numberOfSignals))
                    .digitalMin(is.readMultiInt(SIZE_DIGITAL_MIN, numberOfSignals))
                    .digitalMax(is.readMultiInt(SIZE_DIGITAL_MAX, numberOfSignals))
                    .preFilterings(is.readMultiASCII(SIZE_PRE_FILTERING, numberOfSignals))
                    .sampleCounts(is.readMultiInt(SIZE_NUMBER_OF_SAMPLES, numberOfSignals))
                    .reserves(is.readMultiByteArray(SIZE_CHANNEL_RESERVED, numberOfSignals))
                    .build();
        } catch (IOException e) {
            throw new EDFException(e);
        }
    }

    private static boolean isAnnotation(EDFHeader header, int index) {
        return header.getSignalLabels().get(index).equals(ANNOTATIONS_LABEL);
    }

    private static List<EDFDataRecord> parseDataRecords(EDFInputStream is, EDFHeader header) throws EDFException {
        try {
            List<EDFDataRecord> result = new ArrayList<>();
            for (int i = 0; i < header.getNumberOfRecords(); i++) {
                for (int j = 0; j < header.getNumberOfSignals(); j++) {
                    EDFDataRecord record = new EDFDataRecord();
                    record.setLabel(header.getSignalLabels().get(j));
                    if (isAnnotation(header, j)) {
                        //each sample is 2 bytes, so read bytes for sample as sample count * 2
                        byte[] data = is.readNBytes(header.getSampleCounts().get(j) * 2);
                        record.setAnnotation(parseAnnotation(data));
                    } else {
                        List<Short> values = new ArrayList<>();
                        //List<Double> tempValuesInUnit = new ArrayList<>();
                        for (int k = 0; k < header.getSampleCounts().get(j); k++) {
                            values.add(is.readShort());
                            //tempValuesInUnit.add(tempValue * unitsInDigit.get(j));
                        }
                        record.setValues(values);
                    }
                    result.add(record);
                }
            }
            return result;
        } catch (IOException e) {
            throw new EDFException(e);
        }
    }

    private static TAL parseAnnotation(byte[] b) {
        int onSetIndex = 0;
        int durationIndex = -1;
        int annotationIndex = -2;
        int endIndex = -3;
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == 21) {
                durationIndex = i + 1;
                continue;
            }
            if (b[i] == 0 && b[i + 1] != 0) {
                onSetIndex = i + 1;
                continue;
            }
            if (b[i] == 20 && b[i + 1] != 0 && b[i + 1] != 20) {
                annotationIndex = i + 1;
                continue;
            }
            if (b[i] == 20 && b[i + 1] == 0) {
                endIndex = i;
                continue;
            }
            if (b[i] == 0 && onSetIndex < endIndex) {
                String onSet;
                String duration;
                if (durationIndex > onSetIndex) {
                    onSet = new String(b, onSetIndex, durationIndex - onSetIndex - 1);
                    duration = new String(b, durationIndex, annotationIndex - durationIndex - 1);
                } else {
                    onSet = new String(b, onSetIndex, annotationIndex - onSetIndex - 1);
                    duration = "";
                }
                String annotation = new String(b, annotationIndex, endIndex - annotationIndex);
                return TAL.builder()
                        .annotations(annotation)
                        .duration(duration)
                        .onSet(onSet)
                        .build();
            }
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException, EDFException {
        EDFDocument document = parseEDF(new FileInputStream(new File("/Volumes/CPAP/DATALOG/20240814/20240815_004931_PLD.edf")));
        System.out.println(document);
    }
}