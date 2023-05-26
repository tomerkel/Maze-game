package IO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * this class will extend InputStream class.
 * the class will help us to read the maze byte array and decompress the maze from the input file.
 */
public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    /**
     * constructor
     * @param inputStream
     */
    public MyDecompressorInputStream(InputStream inputStream) {
        this.in = inputStream;
    }

    /**
     * this function read from the input file the compressed byte array maze and decompress it into b.
     * @param b - empty byte array in the proper length
     * @return -1 if b null. 0 if all good.
     * @throws IOException
     */
    @Override
    public int read(byte[] b) throws IOException {
        //check the input
        if (b==null){
            return -1;
        }
        byte[] parametersMazeArray = new byte[24];
        // reading the first 24 bytes from the input file and check if the reading succeed
        if (in.read(parametersMazeArray)==-1){
            return -1;
        }
        // get the maze size
        int row = ByteBuffer.wrap(parametersMazeArray,0,4).getInt();
        int col = ByteBuffer.wrap(parametersMazeArray,4,4).getInt();

        int sizeOfIntMaze = (2*row -1)*(2*col -1);
        // to check if the length of the maze data in the array divide in 32 without remind
        int modb = (sizeOfIntMaze) % 32;
        // check how many ints the maze data contains
        int numOfInts = (sizeOfIntMaze) / 32;

        byte[] inputByteArray;
        //build the byte array in the proper size
        if (modb == 0) {
            inputByteArray = new byte[24 + 4*numOfInts];
        } else {
            inputByteArray = new byte[28 + 4*numOfInts];
        }
        // copy the first 24 bytes to inputByteArray
        for(int i=0 ;i<24 ;i++){
            inputByteArray[i]= parametersMazeArray[i];
        }
        // read the compressed maze data and check if succeed
        if (in.read(inputByteArray,24,inputByteArray.length-24)==-1){
            return -1;
        }
        // decompress the inputByteArray
        byte[] decompressByteArray = decompressMazeByteArray(inputByteArray,sizeOfIntMaze);
        // copy the decompressed array to the parameter b
        for(int j =0 ; j<decompressByteArray.length;j++){
            b[j] = decompressByteArray[j];
        }
        return 0;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    /**
     * this function will decompress the B array
     * @param B - the maze as compressed byte array
     * @param size - the size of the decompressed maze data (not include size and start/goal positions)
     * @return new decompressed byte array
     */
    private byte[] decompressMazeByteArray (byte[] B,int size) {
        // open new byte array in proper size
        byte[] deCompressedB = new byte[24+size];

        // copy the first 24 bytes.
        for(int i=0 ;i<24 ;i++){
            deCompressedB[i]= B[i];
        }

        int nextIndexToInsert =24;
        // i jumps every loop by 4 because every 4 bytes represent int.
        for(int i =24; i< B.length ; i= i+4){
            // get the int from next 4 bytes.
            int num = ByteBuffer.wrap(B,i,4).getInt();

            //convert the int to binary number (as string) and reverse it
            String s1 = Integer.toBinaryString(num);
            StringBuilder str1 = new StringBuilder(s1);
            String s2 = str1.reverse().toString();

            int howMuchLeft = deCompressedB.length - nextIndexToInsert;
            // check if the next int will be represented by 32 bits or less
            if(howMuchLeft < 32){
                for(int j = 0; j<s2.length();j++){
                    // copy the binary number to the decompressed B array
                    deCompressedB[nextIndexToInsert+howMuchLeft -j-1] =(byte)((int)Integer.valueOf(s2.substring(j,j+1)));
                }
            }
            else{
                for(int j = 0 ; j<s2.length();j++){
                    // copy the binary number to the decompressed B array
                    deCompressedB[nextIndexToInsert+31-j] =(byte)((int)Integer.valueOf(s2.substring(j,j+1)));
                }
            }
            // raise the nextIndexToInsert by 4 bytes
            nextIndexToInsert+=32;
        }
        return deCompressedB;
    }

}
