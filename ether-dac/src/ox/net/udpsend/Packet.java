package ox.net.udpsend;

public class Packet {
    public byte sequence_hi; /* msb */
    public byte sequence_lo; /* lsb */
    public byte val[];
    public byte padding[];
    public static final int SEQUENCE_LEN = 2;
    /** unmber of 24-bit values in packet */
    public static final int VAL24_COUNT = 32;
    public static final int VAL_LEN = VAL24_COUNT * 3;
    public static final int PADDING_LEN = 205;
    public static final int TOTAL_PACKET_LEN = SEQUENCE_LEN + VAL_LEN + PADDING_LEN;

    public int getLength(){
        return TOTAL_PACKET_LEN;
    }
    public byte[] getBytes(){
        byte[] res = new byte[TOTAL_PACKET_LEN];
        res[ 0] = sequence_hi;
        res[ 1] = sequence_lo;
        System.arraycopy(val, 0, res, SEQUENCE_LEN, VAL_LEN);
        System.arraycopy(padding, 0, res, SEQUENCE_LEN + VAL_LEN, PADDING_LEN);
        return res;
    }

    public void setValue24(int index, long value) throws IndexOutOfBoundsException {
        if ( (index < 0) || (index >= VAL24_COUNT) )
            throw new IndexOutOfBoundsException("Invalid 24-bit value index");
        //long a = (value & 0xff000000)     4;
        long a = (value & 0x00ff0000) >> 16    ;
        long b = (value & 0x0000ff00) >> 8    ;
        long c = value & 0x000000ff;

        int pos = index * 3;
        val[pos] = (byte) a; //hi byte
        val[pos+1] = (byte) b; //mid
        val[pos+2] = (byte) c; //low byte
    }


    public long getValue24(int index) throws IndexOutOfBoundsException {
        if ( (index < 0) || (index >= VAL24_COUNT) )
            throw new IndexOutOfBoundsException("Invalid 24-bit value index");
        int pos_lo = index * 3 + 2;
        int pos_mi = index * 3 + 1;
        int pos_hi = index * 3;
        long v;
        long v1 = val[pos_hi] >= 0 ? val[pos_hi] : val[pos_hi] + 256;
        long v2 = val[pos_mi] >= 0 ? val[pos_mi] : val[pos_mi] + 256;
        long v3 = val[pos_lo] >= 0 ? val[pos_lo] : val[pos_lo] + 256;
        v = (v1 << 16) + (v2 << 8) + v3;
        return v;
    }

    public Packet(){
        sequence_hi = 0;
        sequence_lo = 0;
        val = new byte[VAL_LEN];
        for (int i = 0; i < VAL24_COUNT; i++)
        {
            setValue24(i, 0);
        }
        padding = new byte[PADDING_LEN];
    }
    public Packet(byte[] data){
          sequence_hi /*8 bits */ = data[ 0];
          sequence_lo            = data[ 1];
          val = new byte[VAL_LEN];
          padding = new byte[PADDING_LEN];
          System.arraycopy(data, SEQUENCE_LEN, val, 0, VAL_LEN);
    }

    /**
     * increase the sequence number
     * @return
     */
    public long next(){
        long v = getSequence();
        v++;
        long x1 = (v & 0x0000FF00)     ;
        long x2 = v & 0x000000FF;
        sequence_hi = (byte) (x1 & 0x000000FF);
        sequence_lo = (byte) (x2 & 0x000000FF);
        return v;
    }
    public long getSequence()
    {
        long v1 = sequence_hi > 0  ? sequence_hi : sequence_hi + 256;
        long v2 = sequence_lo > 0  ? sequence_lo : sequence_lo + 256;
        long v = (v1 << 8) + v2;
        return v;
    }
    private static final long MAX24 =  16777215;
    private static final long MAX16 =    65535;
    private boolean vPatternUp = true;

    public void v_pattern(int index,int len){
        if (len <= 0)
            len = 30;
        // 24 bit value

        double slope24 = MAX24 / len * 2;
        if (!vPatternUp) slope24 = -slope24;
        long v24 = getValue24(index) + (long) slope24;
        if (v24 > MAX24) { v24 = MAX24; vPatternUp = false; }
        if (v24 < 0) { v24 = 0;         vPatternUp = true; }
        setValue24(index, v24);
    }


    public float getCurrent24Sample(int index,float max){
        // 24 bit value
        float scaledVal = (float) getValue24(index) / (float) MAX24 * max;
        return scaledVal;
    }

    public static void main(String[] args) {
        Packet p = new Packet();
        p.setValue24(0,1);
        System.out.println(p.getValue24(0));
        p.setValue24(0,65530);
        System.out.println(p.getValue24(0));
        p.setValue24(0,65530);
        System.out.println(p.getValue24(0));
        p.setValue24(0,16777215);
        System.out.println(p.getValue24(0));
        p.setValue24(0,16777216);
        System.out.println(p.getValue24(0));
    }

}

