public class Main {
    static final int ARR_SIZE = 1000000;
    static final int ARR_HALF = ARR_SIZE / 2;

    static class CalcArray implements Runnable {
        float[] arr = new float[ARR_HALF];
        int startPosition;

        public CalcArray(int startPosition){
            this.startPosition = startPosition;
        }

        public void setArray(float arr[]){
            this.arr = arr;
        }

        public float[] getArray(){
            return arr;
        }

        @Override
        public void run() {
            int j;
            for (int i = 0; i < ARR_HALF; i++) {
                j = i + startPosition;
                arr[i] = (float)(arr[i] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
            }
        }
    }

    public static void main(String[] args) {
        float[] arr1 = new float[ARR_SIZE];
        float[] arr2 = new float[ARR_SIZE];

        singleThread(arr1);
        System.out.println();
        multipleThreads(arr2);
    }

    private static float[] fillOne(float[] arr){
        for (int i = 0; i < ARR_SIZE; i++) {
            arr[i] = 1;
        }
        return arr;
    }

    private static String getTimeDelta(long time){
        return String.valueOf(time) + " miliseconds";
    }

    private static void singleThread(float[] arr){
        long timeStart;
        long timeEnd;
        arr = fillOne(arr);
        timeStart = System.currentTimeMillis();
        for (int i = 0; i < ARR_SIZE; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        timeEnd = System.currentTimeMillis();
        System.out.println("Single thread. Used time = " + getTimeDelta(timeEnd - timeStart));
    }

    private static void multipleThreads(float[] arr){
        float[] arr1 = new float[ARR_HALF];
        float[] arr2 = new float[ARR_HALF];
        CalcArray calcArray1 = new CalcArray(0);
        CalcArray calcArray2 = new CalcArray(ARR_HALF);
        Thread threadCalc1 = new Thread(calcArray1);
        Thread threadCalc2 = new Thread(calcArray2);
        long timeStartCopy;
        long timeEndCopy;
        long timeStartFill;
        long timeEndFill;
        long timeStartCopy2;
        long timeEndCopy2;

        arr = fillOne(arr);

        timeStartCopy = System.currentTimeMillis();
        System.arraycopy(arr, 0, arr1, 0, ARR_HALF);
        System.arraycopy(arr, ARR_HALF, arr2, 0, ARR_HALF);
        calcArray1.setArray(arr1);
        calcArray2.setArray(arr2);
        timeEndCopy = System.currentTimeMillis();

        timeStartFill = System.currentTimeMillis();
        threadCalc1.start();
        threadCalc2.start();
        try {
            threadCalc1.join();
            threadCalc2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeEndFill = System.currentTimeMillis();

        timeStartCopy2 = System.currentTimeMillis();
        System.arraycopy(calcArray1.getArray(), 0, arr, 0, ARR_HALF);
        System.arraycopy(calcArray2.getArray(), 0, arr, ARR_HALF, ARR_HALF);
        timeEndCopy2 = System.currentTimeMillis();

        System.out.println("Multiple threads. Copying time = " + getTimeDelta(timeEndCopy - timeStartCopy));
        System.out.println("Multiple threads. Copying time = " + getTimeDelta(timeEndFill - timeStartFill));
        System.out.println("Multiple threads. Copying time = " + getTimeDelta(timeEndCopy2 - timeStartCopy2));
        System.out.println("Multiple threads. Total time = " + getTimeDelta(timeEndCopy2 - timeStartCopy));
    }
}
