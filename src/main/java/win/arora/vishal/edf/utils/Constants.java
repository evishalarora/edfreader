package win.arora.vishal.edf.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Constants {

    Charset CHARSET = StandardCharsets.US_ASCII;

    int SIZE_IDENTIFICATION_CODE = 8;
    int SIZE_LOCAL_PATIENT_IDENTIFICATION = 80;
    int SIZE_LOCAL_RECORDING_IDENTIFICATION = 80;
    int SIZE_START_DATE = 8;
    int SIZE_START_TIME = 8;
    int SIZE_HEADER = 8;
    int SIZE_FORMAT_VERSION = 44;
    int SIZE_DURATION_DATA_RECORDS = 8;
    int SIZE_NUMBER_OF_DATA_RECORDS = 8;
    int SIZE_NUMBER_OF_SIGNALS = 4;

    int SIZE_SIGNAL_LABELS = 16;
    int SIZE_TRANSDUCER_TYPE = 80;
    int SIZE_PHYSICAL_DIMENSION = 8;
    int SIZE_PHYSICAL_MIN_IN_UNITS = 8;
    int SIZE_PHYSICAL_MAX_IN_UNITS = 8;
    int SIZE_DIGITAL_MIN = 8;
    int SIZE_DIGITAL_MAX = 8;
    int SIZE_PRE_FILTERING = 80;
    int SIZE_NUMBER_OF_SAMPLES = 8;
    int SIZE_CHANNEL_RESERVED = 32;

    String ANNOTATIONS_LABEL = "EDF Annotations";
}
