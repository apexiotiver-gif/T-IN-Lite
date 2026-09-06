package ai.tinlite;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * T-IN-Lite Model Loader
 * Loads .tflite model file from assets or file path
 * When model is trained on Google GPU, load it here
 */
public class ModelLoader {
    private String modelPath;
    private boolean modelLoaded = false;
    private ByteBuffer modelBuffer = null;
    private String modelVersion = "T-IN-Lite-v1.0";
    private int inputSize = 128;
    private int outputSize = 128;

    public ModelLoader(String path) { this.modelPath = path; }

    public boolean loadModel() {
        try {
            File f = new File(modelPath);
            if (!f.exists()) { modelLoaded = false; return false; }
            FileInputStream fis = new FileInputStream(f);
            FileChannel channel = fis.getChannel();
            modelBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            channel.close();
            fis.close();
            modelLoaded = true;
            return true;
        } catch (Exception e) { modelLoaded = false; return false; }
    }

    public boolean isModelLoaded() { return modelLoaded; }
    public String getModelVersion() { return modelVersion; }
    public String getModelPath() { return modelPath; }
    public int getInputSize() { return inputSize; }
    public int getOutputSize() { return outputSize; }
    public ByteBuffer getModelBuffer() { return modelBuffer; }
    public void setModelVersion(String v) { this.modelVersion = v; }
    public void setInputSize(int s) { this.inputSize = s; }
    public void setOutputSize(int s) { this.outputSize = s; }
    public String getModelInfo() {
        return "Model: " + modelVersion + "\nPath: " + modelPath + "\nLoaded: " + modelLoaded + "\nInput: " + inputSize + " | Output: " + outputSize;
    }
}
