package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * this class will extend OutputStream class.
 * the class will help us to compress the maze and send it to output file
 */
public class MyCompressorOutputStream extends OutputStream {
    private OutputStream out;

    /**
     * constructor
     * @param outputStream
     */
    public MyCompressorOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    /**
     * this function will write into the output stream that we get in the constructor
     * @param b - bytes array
     * @throws IOException
     */
    @Override
    public void write(byte[] b) throws IOException {
        byte[] newB = this.compressMazeByteArray(b);
        out.write(newB);
    }

    @Override
    public void write(int b) throws IOException {

    }

    /**
     * this function will compress the maze data.
     * we skip the first 24 bytes.
     * take each 32 bytes that represent binary number and convert it to decimal number(int (4 bytes))
     * @param b - byte array
     * @return - compressed byte array
     */
    private byte[] compressMazeByteArray(byte[] b){
        // to check if the length of the maze data in the array divide in 32 without remind
        int modb = (b.length - 24) % 32;
        // check how many ints the maze data contains
        int numOfInts = (b.length - 24) / 32;
        // create new bytes array
        byte[] newB;

        //build the byte array in the proper size
        if (modb == 0) {
            newB = new byte[24 + 4*numOfInts];
        } else {
            newB = new byte[28 + 4*numOfInts];
        }
        // copy the first 24 bytes to the new compressed byte array
        for (int i = 0; i < 24; i++) {
            newB[i] = b[i];
        }

        int nextFreeIndex = 24;
        // foreach 32 bytes, we convert them to integer and represent it with 4 bytes
        for (int i = 24; i < b.length; i = i + 32) {
            int num = 0;
            // check how much bytes left to the end of the maze data
            int howMuchLeft = b.length - i;
            // if we are in the last bytes and it is smaller then 32
            if (howMuchLeft < 32)
            {
                String binNum = "";
                for (int j = 0; j <howMuchLeft; j++){
                    binNum += ((int)b[i + j]);
                }
                num = new BigInteger(binNum,2).intValue();
            }
            else {
                // calculate the integer
                String binNum = "";
                for (int j = 0; j <= 31; j++) {
                    binNum += ((int)b[i + j]);
                }
                //num = Integer.parseInt(binNum,2);
                num = new BigInteger(binNum,2).intValue();
            }
            // convert the integer to byte array (size 4) and insert it to newB
            ByteBuffer newNum = ByteBuffer.allocate(4);
            newNum.putInt(num);
            for (int k = 0; k < 4; k++) {
                newB[nextFreeIndex] = newNum.array()[k];
                nextFreeIndex++;
            }
        }
        return newB;
    }
}
